package com.thoseop.api.members.http;

import static com.thoseop.config.OtterWebMvcConfig._APPLICATION_YAML_VALUE;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoseop.api.members.entity.enums.MemberEnabledStatus;
import com.thoseop.api.members.entity.enums.MemberLockedStatus;
import com.thoseop.api.members.http.request.MemberCreateRequest;
import com.thoseop.api.members.http.request.MemberManagePasswordRequest;
import com.thoseop.api.members.http.request.MemberUpdatePasswordRequest;
import com.thoseop.api.members.http.response.MemberAccountLockedResponse;
import com.thoseop.api.members.http.response.MemberDetailsResponse;
import com.thoseop.api.members.http.response.MemberEnabledResponse;
import com.thoseop.api.members.http.response.MemberResponse;
import com.thoseop.exception.response.OtterAPIErrorResponse;

import net.minidev.json.JSONObject;

@DisplayName("Testing MemberController")
@TestPropertySource(locations="classpath:application_test.properties")
@ActiveProfiles(value = {"test"})
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class) // execute test methods based on the position set on @Order
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class MemberControllerIntegrationTest {

    @LocalServerPort // retrieves the random server port being used to run these tests
    private int localServerPort;
    
    @Autowired
    private TestRestTemplate testRestTemplate;
    
    private String jwtAuthToken;

    // base route for the tested controller
    private final String baseRoute = "/api/member/v1"; 
    
    @Autowired
    public MemberControllerIntegrationTest(JdbcTemplate jdbcTemplate) {
	jdbcTemplate.execute("DELETE FROM authorities WHERE member_id > 50");
	jdbcTemplate.execute("DELETE FROM members WHERE id > 50");
    }
    
    @DisplayName("test Member Token_when Member Authorized_then Return Token")
    @Test
    @Order(1)
    void testMemberToken_whenMemberAuthorized_thenReturnToken() {
	// g
	String route = "%s/token".formatted(this.baseRoute);

	// creating the headers for the request
	HttpHeaders headers = new HttpHeaders();
	headers.setContentType(MediaType.APPLICATION_JSON);
	headers.setAccept(List.of(MediaType.APPLICATION_JSON));

	HttpEntity<?> request = new HttpEntity<>(null, headers);
	// w
	ResponseEntity<JSONObject> response = testRestTemplate
		.withBasicAuth("ayrton.senna@bravo.com", "ayrton_pass")
		.exchange(route, HttpMethod.GET, request, JSONObject.class);

	// getting the JWT token and setting it to a property to use on the other
	// authenticated tests.
	this.jwtAuthToken = response.getHeaders().getValuesAsList("Authorization").get(0);
//	this.jwtAuthToken = response.getBody().get("token").toString();

	// t
	Assertions.assertEquals(HttpStatus.OK, response.getStatusCode(), 
		() -> "The returned http status code was not the expected.");
	Assertions.assertNotNull(this.jwtAuthToken, 
		() -> "Response should contain the JWT token in the Authorization header field");
	Assertions.assertEquals(response.getBody().get("token"), this.jwtAuthToken);
    }
    
    @DisplayName("test Member Token_when Search Member Doest Exists_then Returns Error Response")
    @Test
    @Order(2)
    void testMemberToken_whenSearchMemberDoestExists_thenReturnsErrorResponse() {
	// g
	String route = "%s/token".formatted(this.baseRoute);

	// creating the headers for the request
	HttpHeaders headers = new HttpHeaders();
	headers.setContentType(MediaType.APPLICATION_JSON);
	headers.setAccept(List.of(MediaType.APPLICATION_JSON));

	HttpEntity<?> request = new HttpEntity<>(null, headers);
	// w
	ResponseEntity<OtterAPIErrorResponse> response = testRestTemplate
		.withBasicAuth("unknown@none.com", "rumpelstiltskin")
		.exchange(route, HttpMethod.GET, request, OtterAPIErrorResponse.class);
	// t
	Assertions.assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode(),
		() -> "The returned http status code was not the expected.");	
	Assertions.assertTrue(response.getBody().getError()
		.contains("UsernameNotFoundException"), 
		() -> "The error message was not the expected.");
	Assertions.assertEquals("User details not found for the user: unknown@none.com", 
		response.getBody().getMessage(), 
		() -> "The message was not the expected.");
	Assertions.assertEquals("/token", response.getBody().getPath(), 
		() -> "The path was not the expected.");
    }

    static Stream<Arguments> memberSeed() {
	return Stream.of(
		Arguments.of(new MemberCreateRequest("Santos Dumont", "santos.dumont@test.com",
			"(11) 94567-8901", "dumont_pass", Set.of("ROLE_ADMIN", "ROLE_INVENTOR"))),
		Arguments.of(new MemberCreateRequest("Tom Jobim", "tom.jobim@test.com",
			"(11) 95678-9012", "jobim_pass", Set.of("ROLE_ADMIN", "ROLE_MUSICIAN"))),
		Arguments.of(new MemberCreateRequest("Pele", "pele@test.com",
			"(11) 92345-6789", "pele_pass", Set.of("ROLE_ADMIN", "ROLE_ATHLETE"))),
		Arguments.of(new MemberCreateRequest("Vitor Belfort", "vitor.belfort@test.com",
			"(11) 91234-5678", "belfort_pass", Set.of("ROLE_MANAGER", "ROLE_WARRIOR"))),
		Arguments.of(new MemberCreateRequest("Cláudia Abreu", "claudia.abreu@test.com",
			"(11) 93456-7890", "abreu_pass", Set.of("ROLE_MANAGER", "ROLE_HOTEST")))
		);
    }

    @DisplayName("test Create Member_when Member Authorized_then Returns HTTP 201")
    @ParameterizedTest
    @MethodSource("memberSeed")
    @Order(3)
    void testCreateMember_whenMemberAuthorized_thenReturnsHTTP201(MemberCreateRequest memberCreateRequest) {
	// g
	String route = "%s/member-create".formatted(this.baseRoute);

	// creating the headers for the request
	HttpHeaders headers = new HttpHeaders();
	headers.setContentType(MediaType.APPLICATION_JSON);
	headers.setAccept(List.of(MediaType.APPLICATION_JSON));
	headers.set("Authorization", this.jwtAuthToken);

	HttpEntity<MemberCreateRequest> request = new HttpEntity<>(
		memberCreateRequest, headers);
	// w
	ResponseEntity<MemberResponse> response = testRestTemplate
		.exchange(route, HttpMethod.POST, request, MemberResponse.class);
	// t
	Assertions.assertEquals(HttpStatus.CREATED, 
		response.getStatusCode(), 
		() -> "The returned http status code was not the expected.");
	Assertions.assertEquals(memberCreateRequest.getMemberName(), 
		response.getBody().getMemberName(), 
		() -> "The returned member name was not the expected.");
	Assertions.assertEquals(memberCreateRequest.getMemberEmail(), 
		response.getBody().getMemberEmail(), 
		() -> "The returned member email was not the expected.");
	Assertions.assertEquals(memberCreateRequest.getMemberMobileNumber(), 
		response.getBody().getMemberMobileNumber(), 
		() -> "The retruned member mobile number was not the expected");
    }

    @DisplayName("test Get Members_when Member Authorized_then Returns HTTP 200")
    @Test
    @Order(4)
    void testGetMembers_whenMemberAuthorized_thenReturnsHTTP200() throws JsonMappingException, JsonProcessingException {
	// g
	String route = "%s/list".formatted(this.baseRoute);

	// creating the headers for the request
	HttpHeaders headers = new HttpHeaders();
	headers.setContentType(MediaType.APPLICATION_JSON);
	headers.setAccept(List.of(MediaType.APPLICATION_JSON));
	headers.set("Authorization", this.jwtAuthToken);

	HttpEntity<?> request = new HttpEntity<>(null, headers);
	// w
	ResponseEntity<PagedModel<EntityModel<MemberResponse>>> response = testRestTemplate
		.exchange(route, HttpMethod.GET, request,
			new ParameterizedTypeReference<PagedModel<EntityModel<MemberResponse>>>() {
		});
	// t
	Assertions.assertEquals(HttpStatus.OK, response.getStatusCode(), 
		() -> "The returned http status code returned was not the expected.");
    }

    @DisplayName("test Get Members_when Member Authorized_then Returns Paginated List Of Members")
    @Test
    @Order(5)
    void testGetMembers_whenMemberAuthorized_thenReturnsPaginatedListOfMembers() throws JsonMappingException, JsonProcessingException {
	// g
	String route = "%s/list?page=2&size=10&sort=desc".formatted(this.baseRoute);

	// creating the headers for the request
	HttpHeaders headers = new HttpHeaders();
	headers.setContentType(MediaType.APPLICATION_JSON);
	headers.setAccept(List.of(MediaType.APPLICATION_JSON));
	headers.set("Authorization", this.jwtAuthToken);

	HttpEntity<?> request = new HttpEntity<>(null, headers);
	// w
	ResponseEntity<PagedModel<EntityModel<MemberResponse>>> response = testRestTemplate
		.exchange(route, HttpMethod.GET, request,
			new ParameterizedTypeReference<PagedModel<EntityModel<MemberResponse>>>() {
		});
	
        PagedModel<EntityModel<MemberResponse>> pagedMembers = response.getBody();        
        
        List<EntityModel<MemberResponse>> membersEM = pagedMembers.getContent().stream().collect(Collectors.toList());
        MemberResponse m1 = membersEM.get(0).getContent();
	
	// t
	Assertions.assertEquals(HttpStatus.OK, response.getStatusCode(), 
		() -> "The returned http status code returned was not the expected.");

	Assertions.assertEquals(10, pagedMembers.getMetadata().getSize(), 
		() -> "The page size was not the expected");
	Assertions.assertTrue(pagedMembers.getMetadata().getTotalElements() >= 50, 
		() -> "The total number of elements was not the expected");
	Assertions.assertTrue(pagedMembers.getMetadata().getTotalPages() >= 5, 
		() -> "The total number of pages was not the expected");
	Assertions.assertEquals(2, pagedMembers.getMetadata().getNumber(), 
		() -> "The page number was not the expected");
	
	Assertions.assertEquals("Mick Fredson", m1.getMemberName(), 
		() -> "The member name was not the expected");
	Assertions.assertEquals("mfredson2@amazon.com", m1.getMemberEmail(), 
		() -> "The member email was not the expected");
	Assertions.assertEquals("+996 (177) 963-3057", m1.getMemberMobileNumber(), 
		() -> "The member phone number was not the expected");
    }

     // NOT SO GOOD APPROACH...
    @DisplayName("test Get Members_when Member Authorized_then Returns Paginated List Of Members - JSON Way")
    @Test
    @Order(6)
    void testGetMembers_whenMemberAuthorized_thenReturnsPaginatedListOfMembersJSONWay() throws JsonMappingException, JsonProcessingException {
	// g
	String route = "%s/list?page=2&size=10&sort=desc".formatted(this.baseRoute);

	// creating the headers for the request
	HttpHeaders headers = new HttpHeaders();
	headers.setContentType(MediaType.APPLICATION_JSON);
	headers.setAccept(List.of(MediaType.APPLICATION_JSON));
	headers.set("Authorization", this.jwtAuthToken);

	HttpEntity<?> request = new HttpEntity<>(null, headers);
	// w
	ResponseEntity<PagedModel<EntityModel<MemberResponse>>> response = testRestTemplate
		.exchange(route, HttpMethod.GET, request,
			new ParameterizedTypeReference<PagedModel<EntityModel<MemberResponse>>>() {
		});
        
	// getting the body as a JSON 
	ObjectMapper mapper = new ObjectMapper();
	
	String responseStr = mapper.writeValueAsString(response.getBody());
	JsonNode jsonObjectBody = new ObjectMapper().readTree(responseStr);
//	System.out.println(jsonObjectBody.toPrettyString());
//	System.out.println(jsonObjectBody.get("page").toPrettyString());
//	System.out.println(jsonObjectBody.get("page").get("size").toPrettyString());
	
	String pageSize = jsonObjectBody.get("page").get("size").toString();
	String totalElements = jsonObjectBody.get("page").get("totalElements").toString();
	String totalPages = jsonObjectBody.get("page").get("totalPages").toString();
	String pageNumber = jsonObjectBody.get("page").get("number").toString();
	// t  
	Assertions.assertEquals(10, Integer.valueOf(pageSize), 
		() -> "The page size was not the expected");
	Assertions.assertTrue(Integer.valueOf(totalElements) >= 50, 
		() -> "The total number of elements was not the expected");
	Assertions.assertTrue(Integer.valueOf(totalPages) >= 5, 
		() -> "The total number of pages was not the expected");
	Assertions.assertEquals(2, Integer.valueOf(pageNumber), 
		() -> "The page number was not the expected");
    }

    @DisplayName("test Get Member By Username_when Member Authorized_then Returns HTTP 200")
    @Test
    @Order(7)
    void testGetMemberByUsername_whenMemberAuthorized_thenReturnsHTTP200() {
	// g
	String route = "%s/member-details/ayrton.senna@bravo.com".formatted(this.baseRoute);

	// creating the headers for the requestString
	HttpHeaders headers = new HttpHeaders();
	headers.setContentType(MediaType.APPLICATION_JSON);
	headers.setAccept(List.of(MediaType.APPLICATION_JSON));
	headers.set("Authorization", this.jwtAuthToken);

	HttpEntity<?> request = new HttpEntity<>(
		null, headers);
	// w
	ResponseEntity<MemberResponse> response = testRestTemplate
		.exchange(route, HttpMethod.GET, request, MemberResponse.class);
	// t
	Assertions.assertEquals(HttpStatus.OK, response.getStatusCode(),
		() -> "The returned http status code was not the expected.");
	Assertions.assertEquals("Ayrton Senna", response.getBody().getMemberName(), 
		() -> "The returned member name was not the expected.");
	Assertions.assertEquals("ayrton.senna@bravo.com", response.getBody().getMemberEmail(), 
		() -> "The returned member email was not the expected.");
	Assertions.assertEquals("(11) 98765-4321", response.getBody().getMemberMobileNumber(),
		() -> "The returned member mobile number was not expected.");
    }

    @DisplayName("test Get Member By Username_when Search Member Doest Exists_then Throws Member Not Found Exception")
    @Test
    @Order(8)
    void testGetMemberByUsername_whenSearchMemberDoestExists_thenReturnsErrorResponse() {
	// g
	String route = "%s/member-details/unknown@none.com".formatted(this.baseRoute);

	// creating the headers for the requestString
	HttpHeaders headers = new HttpHeaders();
	headers.setContentType(MediaType.APPLICATION_JSON);
	headers.setAccept(List.of(MediaType.APPLICATION_JSON));
	headers.set("Authorization", this.jwtAuthToken);

	HttpEntity<?> request = new HttpEntity<>(
		null, headers);
	// w
	ResponseEntity<OtterAPIErrorResponse> response = testRestTemplate
		.exchange(route, HttpMethod.GET, request, OtterAPIErrorResponse.class);
	// t
	Assertions.assertEquals(HttpStatus.OK, response.getStatusCode(),
		() -> "The returned http status code was not the expected.");	
	Assertions.assertTrue(response.getBody().getError()
		.contains("MemberNotFoundException"), 
		() -> "The error message was not the expected.");
	Assertions.assertEquals("Member not found", response.getBody().getMessage(), 
		() -> "The message was not the expected.");
	Assertions.assertEquals("Member not found", response.getBody().getPath(), 
		() -> "The path was not the expected.");
    }

    @DisplayName("test Get Member By Username_when Request With Accepted Media Types_then Return HTTP 200")
    @ParameterizedTest
    @ValueSource(strings = {MediaType.APPLICATION_JSON_VALUE, 
	    MediaType.APPLICATION_XML_VALUE, _APPLICATION_YAML_VALUE})
    @Order(9)
    void testGetMemberByUsername_whenRequestWithAcceptedMediaTypes_thenReturnHTTP200(String mediaTypeAccepted) {
	// g
	String route = "%s/member-details/ayrton.senna@bravo.com".formatted(this.baseRoute);

	// creating the headers for the requestString
	HttpHeaders headers = new HttpHeaders();
//	headers.setContentType(MediaType.APPLICATION_JSON);
	headers.set("Content-Type", mediaTypeAccepted);
	headers.setAccept(List.of(MediaType.APPLICATION_JSON));
	headers.set("Authorization", this.jwtAuthToken);

	HttpEntity<?> request = new HttpEntity<>(
		null, headers);
	// w
	ResponseEntity<MemberResponse> response = testRestTemplate
		.exchange(route, HttpMethod.GET, request, MemberResponse.class);
	// t
	Assertions.assertEquals(HttpStatus.OK, response.getStatusCode(),
		() -> "The returned http status code was not the expected.");
    }

    @DisplayName("test Get Member Full Details By Username_when Member Authorized_then Returns HTTP 200")
    @Test
    @Order(10)
    void testGetMemberFullDetailsByUsername_whenMemberAuthorized_thenReturnsHTTP200() {
	// g
	String route = "%s/member-full-details/ayrton.senna@bravo.com".formatted(this.baseRoute);

	// creating the headers for the requestString
	HttpHeaders headers = new HttpHeaders();
	headers.setContentType(MediaType.APPLICATION_JSON);
	headers.setAccept(List.of(MediaType.APPLICATION_JSON));
	headers.set("Authorization", this.jwtAuthToken);

	HttpEntity<?> request = new HttpEntity<>(
		null, headers);
	// w
	ResponseEntity<MemberDetailsResponse> response = testRestTemplate
		.exchange(route, HttpMethod.GET, request, MemberDetailsResponse.class);
	// t
	Assertions.assertEquals(HttpStatus.OK, response.getStatusCode(),
		() -> "The returned http status code was not the expected.");
	Assertions.assertEquals("Ayrton Senna", response.getBody().getMemberName(), 
		() -> "The returned member name was not the expected.");
	Assertions.assertEquals("ayrton.senna@bravo.com", response.getBody().getMemberEmail(), 
		() -> "The returned member email was not the expected.");
	Assertions.assertEquals("(11) 98765-4321", response.getBody().getMemberMobileNumber(),
		() -> "The returned member mobile number was not expected.");
    }

    @DisplayName("test Get Member Full Details By Username_when Search Member Doest Exists_then Returns Error Response")
    @Test
    @Order(11)
    void testGetMemberFullDetailsByUsername_whenSearchMemberDoestExists_thenReturnsErrorResponse() {
	// g
	String route = "%s/member-full-details/unknown@none.com".formatted(this.baseRoute);

	// creating the headers for the requestString
	HttpHeaders headers = new HttpHeaders();
	headers.setContentType(MediaType.APPLICATION_JSON);
	headers.setAccept(List.of(MediaType.APPLICATION_JSON));
	headers.set("Authorization", this.jwtAuthToken);

	HttpEntity<?> request = new HttpEntity<>(
		null, headers);
	// w
	ResponseEntity<OtterAPIErrorResponse> response = testRestTemplate
		.exchange(route, HttpMethod.GET, request, OtterAPIErrorResponse.class);
	// t
	Assertions.assertEquals(HttpStatus.OK, response.getStatusCode(),
		() -> "The returned http status code was not the expected.");	
	Assertions.assertTrue(response.getBody().getError()
		.contains("MemberNotFoundException"), 
		() -> "The error message was not the expected.");
	Assertions.assertEquals("Member not found", response.getBody().getMessage(), 
		() -> "The message was not the expected.");
	Assertions.assertEquals("Member not found", response.getBody().getPath(), 
		() -> "The path was not the expected.");
    }

    @DisplayName("test Get Member Full Details By Username_when Request With Accepted Media Types_then Return HTTP 200")
    @ParameterizedTest
    @ValueSource(strings = {MediaType.APPLICATION_JSON_VALUE, 
	    MediaType.APPLICATION_XML_VALUE, _APPLICATION_YAML_VALUE})
    @Order(12)
    void testGetMemberFullDetailsByUsername_whenRequestWithAcceptedMediaTypes_thenReturnHTTP200(String mediaTypeAccepted) {
	// g
	String route = "%s/member-full-details/ayrton.senna@bravo.com".formatted(this.baseRoute);

	// creating the headers for the requestString
	HttpHeaders headers = new HttpHeaders();
//	headers.setContentType(MediaType.APPLICATION_JSON);
	headers.set("Content-Type", mediaTypeAccepted);
	headers.setAccept(List.of(MediaType.APPLICATION_JSON));
	headers.set("Authorization", this.jwtAuthToken);

	HttpEntity<?> request = new HttpEntity<>(
		null, headers);
	// w
	ResponseEntity<MemberDetailsResponse> response = testRestTemplate
		.exchange(route, HttpMethod.GET, request, MemberDetailsResponse.class);
	// t
	Assertions.assertEquals(HttpStatus.OK, response.getStatusCode(),
		() -> "The returned http status code was not the expected.");
    }

    @DisplayName("test Get Me_when Member Authenticated_then Returns HTTP 200")
    @Test
    @Order(13)
    void testMe_whenMemberAuthenticated_thenReturnsHTTP200() {
	// g
	String route = "%s/me".formatted(this.baseRoute);

	// creating the headers for the requestString
	HttpHeaders headers = new HttpHeaders();
	headers.setContentType(MediaType.APPLICATION_JSON);
	headers.setAccept(List.of(MediaType.APPLICATION_JSON));
	headers.set("Authorization", this.jwtAuthToken);

	HttpEntity<?> request = new HttpEntity<>(
		null, headers);
	// w
	ResponseEntity<MemberResponse> response = testRestTemplate
		.exchange(route, HttpMethod.GET, request, MemberResponse.class);
	// t
	Assertions.assertEquals(HttpStatus.OK, response.getStatusCode(),
		() -> "The returned http status code was not the expected.");
	Assertions.assertEquals("Ayrton Senna", response.getBody().getMemberName(), 
		() -> "The returned member name was not the expected.");
	Assertions.assertEquals("ayrton.senna@bravo.com", response.getBody().getMemberEmail(), 
		() -> "The returned member email was not the expected.");
	Assertions.assertEquals("(11) 98765-4321", response.getBody().getMemberMobileNumber(),
		() -> "The returned member mobile number was not expected.");
    }

    @DisplayName("test Me_when Request With Accepted Media Types_then Return HTTP 200")
    @ParameterizedTest
    @ValueSource(strings = {MediaType.APPLICATION_JSON_VALUE, 
	    MediaType.APPLICATION_XML_VALUE, _APPLICATION_YAML_VALUE})
    @Order(14)
    void testMe_whenRequestWithAcceptedMediaTypes_thenReturnHTTP200(String mediaTypeAccepted) {
	// g
	String route = "%s/me".formatted(this.baseRoute);

	// creating the headers for the requestString
	HttpHeaders headers = new HttpHeaders();
//	headers.setContentType(MediaType.APPLICATION_JSON);
	headers.set("Content-Type", mediaTypeAccepted);
	headers.setAccept(List.of(MediaType.APPLICATION_JSON));
	headers.set("Authorization", this.jwtAuthToken);

	HttpEntity<?> request = new HttpEntity<>(
		null, headers);
	// w
	ResponseEntity<MemberResponse> response = testRestTemplate
		.exchange(route, HttpMethod.GET, request, MemberResponse.class);
	// t
	Assertions.assertEquals(HttpStatus.OK, response.getStatusCode(),
		() -> "The returned http status code was not the expected.");
	Assertions.assertEquals("Ayrton Senna", response.getBody().getMemberName(), 
		() -> "The returned member name was not the expected.");
	Assertions.assertEquals("ayrton.senna@bravo.com", response.getBody().getMemberEmail(), 
		() -> "The returned member email was not the expected.");
	Assertions.assertEquals("(11) 98765-4321", response.getBody().getMemberMobileNumber(),
		() -> "The returned member mobile number was not expected.");
    }
    
    @DisplayName("test Update Member Password_when Authenticated_then Returns HTTP 200")
    @Test
    @Order(15)
    void testUpdateMemberPassword_whenAuthenticated_thenReturnsHTTP200() {
	// g
	String route = "%s/member-password".formatted(this.baseRoute);
	
	MemberUpdatePasswordRequest memberUpdPassRequest = 
		new MemberUpdatePasswordRequest("new_ayrton_pass"); 

	// creating the headers for the requestString
	HttpHeaders headers = new HttpHeaders();
	headers.setContentType(MediaType.APPLICATION_JSON);
	headers.setAccept(List.of(MediaType.APPLICATION_JSON));
	headers.set("Authorization", this.jwtAuthToken);

	HttpEntity<MemberUpdatePasswordRequest> updPassHTTPrequest = new HttpEntity<>(
		memberUpdPassRequest, headers);
	// w
	// ...changing the members password...
	ResponseEntity<MemberResponse> responsePassChange = testRestTemplate
		.exchange(route, HttpMethod.PATCH, updPassHTTPrequest, MemberResponse.class);
	// t
	Assertions.assertEquals(HttpStatus.OK, responsePassChange.getStatusCode(),
		() -> "The returned http status code was not the expected.");

	// ------------------ REVERTING PASSWORD CHANGE ------------------
	// g
	memberUpdPassRequest = 
		new MemberUpdatePasswordRequest("ayrton_pass"); 
	updPassHTTPrequest = new HttpEntity<>(memberUpdPassRequest, headers);
	// w
	responsePassChange = testRestTemplate
		.exchange(route, HttpMethod.PATCH, updPassHTTPrequest, MemberResponse.class);
	// t
	Assertions.assertEquals(HttpStatus.OK, responsePassChange.getStatusCode(),
		() -> "The returned http status code was not the expected.");
    }
    
    @DisplayName("test Manage Member Password_when Authenticated_then Returns HTTP 200")
    @Test
    @Order(16)
    void testManageMemberPassword_whenAuthenticated_thenReturnsHTTP200() {
	// g
	String route = "%s/manage-member-password".formatted(this.baseRoute);
	
	MemberManagePasswordRequest memberMngPassRequest = 
		new MemberManagePasswordRequest("mfredson2@amazon.com", 
			"new_mick_pass"); 

	// creating the headers for the requestString
	HttpHeaders headers = new HttpHeaders();
	headers.setContentType(MediaType.APPLICATION_JSON);
	headers.setAccept(List.of(MediaType.APPLICATION_JSON));
	headers.set("Authorization", this.jwtAuthToken);

	HttpEntity<MemberManagePasswordRequest> updPassHTTPrequest = new HttpEntity<>(
		memberMngPassRequest, headers);
	// w
	// ...changing the members password...
	ResponseEntity<MemberResponse> responsePassChange = testRestTemplate
		.exchange(route, HttpMethod.PATCH, updPassHTTPrequest, MemberResponse.class);
	// t
	Assertions.assertEquals(HttpStatus.OK, responsePassChange.getStatusCode(),
		() -> "The returned http status code was not the expected.");

	// ------------------ REVERTING PASSWORD CHANGE ------------------
	// g
	memberMngPassRequest = 
		new MemberManagePasswordRequest("mfredson2@amazon.com", "lQEXBPL"); 
	updPassHTTPrequest = new HttpEntity<>(memberMngPassRequest, headers);
	// w
	responsePassChange = testRestTemplate
		.exchange(route, HttpMethod.PATCH, updPassHTTPrequest, MemberResponse.class);
	// t
	Assertions.assertEquals(HttpStatus.OK, responsePassChange.getStatusCode(),
		() -> "The returned http status code was not the expected.");
    }
    
    @DisplayName("test Manage Member Password_when Searched Member Doest Exists_then Returns Error Response")
    @Test
    @Order(17)
    void testManageMemberPassword_whenSearchedMemberDoestExists_thenReturnsErrorResponse() {
	// g
	String route = "%s/manage-member-password".formatted(this.baseRoute);
	
	MemberManagePasswordRequest request = 
		new MemberManagePasswordRequest("unknow@test.com", "something"); 

	// creating the headers for the requestString
	HttpHeaders headers = new HttpHeaders();
	headers.setContentType(MediaType.APPLICATION_JSON);
	headers.setAccept(List.of(MediaType.APPLICATION_JSON));
	headers.set("Authorization", this.jwtAuthToken);

	HttpEntity<MemberManagePasswordRequest> rrequest = new HttpEntity<>(
		request, headers);
	// w
	// ...changing the members password...
	ResponseEntity<OtterAPIErrorResponse> response = testRestTemplate
		.exchange(route, HttpMethod.PATCH, rrequest, OtterAPIErrorResponse.class);
	// t
	Assertions.assertEquals(HttpStatus.OK, response.getStatusCode(),
		() -> "The returned http status code was not the expected.");
	Assertions.assertTrue(response.getBody().getError()
		.contains("MemberNotFoundException"), 
		() -> "The error message was not the expected.");
	Assertions.assertEquals("Member not found", response.getBody().getMessage(), 
		() -> "The message was not the expected.");
	Assertions.assertEquals("Member not found", response.getBody().getPath(), 
		() -> "The path was not the expected.");
    }

    @DisplayName("test Inactivate Member_when Inactivating Member_then Returns HTTP 200")
    @Test
    @Order(18)
    void testInactivateMember_whenInactivatingMember_thenReturnsHTTP200() {
	// g
	String route = "%s/member-disable/3".formatted(this.baseRoute);

	// creating the headers for the requestString
	HttpHeaders headers = new HttpHeaders();
	headers.setContentType(MediaType.APPLICATION_JSON);
	headers.setAccept(List.of(MediaType.APPLICATION_JSON));
	headers.set("Authorization", this.jwtAuthToken);

	HttpEntity<?> request = new HttpEntity<>(null, headers);
	// w
	ResponseEntity<MemberEnabledResponse> response = testRestTemplate
		.exchange(route, HttpMethod.PATCH, request, MemberEnabledResponse.class);
	// t
	Assertions.assertEquals(HttpStatus.OK, response.getStatusCode(),
		() -> "The returned http status code was not the expected.");
	Assertions.assertEquals(MemberEnabledStatus.DISABLED.getStatus(), response.getBody().getMemberEnabled(),
		() -> "The member should be disabled.");
    }

    @DisplayName("test Inactivate Member_when Searched Member Doest Exists_then Returns Error Response")
    @Test
    @Order(19)
    void testInactivateMember_whenSearchedMemberDoestExists_thenReturnsErrorResponse() {
	// g
	String route = "%s/member-disable/1000".formatted(this.baseRoute);

	// creating the headers for the requestString
	HttpHeaders headers = new HttpHeaders();
	headers.setContentType(MediaType.APPLICATION_JSON);
	headers.setAccept(List.of(MediaType.APPLICATION_JSON));
	headers.set("Authorization", this.jwtAuthToken);

	HttpEntity<?> request = new HttpEntity<>(
		null, headers);
	// w
	ResponseEntity<OtterAPIErrorResponse> response = testRestTemplate
		.exchange(route, HttpMethod.PATCH, request, OtterAPIErrorResponse.class);
	// t
	Assertions.assertEquals(HttpStatus.OK, response.getStatusCode(),
		() -> "The returned http status code was not the expected.");
	Assertions.assertTrue(response.getBody().getError()
		.contains("MemberNotFoundException"), 
		() -> "The error message was not the expected.");
	Assertions.assertEquals("Member not found", response.getBody().getMessage(), 
		() -> "The message was not the expected.");
	Assertions.assertEquals("Member not found", response.getBody().getPath(), 
		() -> "The path was not the expected.");
    }

    @DisplayName("test Activate Member_when Activating Member_then Returns HTTP 200")
    @Test
    @Order(20)
    void testActivateMember_whenActivatingMember_thenReturnsHTTP200() {
	// g
	String route = "%s/member-enable/3".formatted(this.baseRoute);

	// creating the headers for the requestString
	HttpHeaders headers = new HttpHeaders();
	headers.setContentType(MediaType.APPLICATION_JSON);
	headers.setAccept(List.of(MediaType.APPLICATION_JSON));
	headers.set("Authorization", this.jwtAuthToken);

	HttpEntity<?> request = new HttpEntity<>(
		null, headers);
	// w
	ResponseEntity<MemberEnabledResponse> response = testRestTemplate
		.exchange(route, HttpMethod.PATCH, request, MemberEnabledResponse.class);
	// t
	Assertions.assertEquals(HttpStatus.OK, response.getStatusCode(),
		() -> "The returned http status code was not the expected.");
	Assertions.assertEquals(MemberEnabledStatus.ENABLED.getStatus(), response.getBody().getMemberEnabled(),
		() -> "The member enabled should be enabled.");
    }

    @DisplayName("test Activate Member_when Searched Member Doest Exists_then Returns Error Response")
    @Test
    @Order(21)
    void testActivateMember_whenSearchedMemberDoestExists_thenReturnsErrorResponse() {
	// g
	String route = "%s/member-enable/1000".formatted(this.baseRoute);

	// creating the headers for the requestString
	HttpHeaders headers = new HttpHeaders();
	headers.setContentType(MediaType.APPLICATION_JSON);
	headers.setAccept(List.of(MediaType.APPLICATION_JSON));
	headers.set("Authorization", this.jwtAuthToken);

	HttpEntity<?> request = new HttpEntity<>(
		null, headers);
	// w
	ResponseEntity<OtterAPIErrorResponse> response = testRestTemplate
		.exchange(route, HttpMethod.PATCH, request, OtterAPIErrorResponse.class);
	// t
	Assertions.assertEquals(HttpStatus.OK, response.getStatusCode(),
		() -> "The returned http status code was not the expected.");
	Assertions.assertTrue(response.getBody().getError()
		.contains("MemberNotFoundException"), 
		() -> "The error message was not the expected.");
	Assertions.assertEquals("Member not found", response.getBody().getMessage(), 
		() -> "The message was not the expected.");
	Assertions.assertEquals("Member not found", response.getBody().getPath(), 
		() -> "The path was not the expected.");
    }

    @DisplayName("testLockMemberAccount_whenAccountLocking_thenReturnsHTTP200")
    @Test
    @Order(22)
    void testLockMemberAccount_whenAccountLocking_thenReturnsHTTP200() {
	// g
	String route = "%s/member-lock/3".formatted(this.baseRoute);

	// creating the headers for the requestString
	HttpHeaders headers = new HttpHeaders();
	headers.setContentType(MediaType.APPLICATION_JSON);
	headers.setAccept(List.of(MediaType.APPLICATION_JSON));
	headers.set("Authorization", this.jwtAuthToken);

	HttpEntity<?> request = new HttpEntity<>(
		null, headers);
	// w
	ResponseEntity<MemberAccountLockedResponse> response = testRestTemplate
		.exchange(route, HttpMethod.PATCH, request, MemberAccountLockedResponse.class);
	// t
	Assertions.assertEquals(HttpStatus.OK, response.getStatusCode(),
		() -> "The returned http status code was not the expected.");
	Assertions.assertEquals(MemberLockedStatus.LOCKED.getStatus(), response.getBody().getMemberAccountNotLocked(),
		() -> "The member's account should be locked.");
    }

    @DisplayName("test Lock Member Account_when Searched Member Doest Exists_then Returns Error Response")
    @Test
    @Order(23)
    void testLockMemberAccount_whenSearchedhMemberDoestExists_thenReturnsErrorResponse() {
	// g
	String route = "%s/member-lock/1000".formatted(this.baseRoute);

	// creating the headers for the requestString
	HttpHeaders headers = new HttpHeaders();
	headers.setContentType(MediaType.APPLICATION_JSON);
	headers.setAccept(List.of(MediaType.APPLICATION_JSON));
	headers.set("Authorization", this.jwtAuthToken);

	HttpEntity<?> request = new HttpEntity<>(
		null, headers);
	// w
	ResponseEntity<OtterAPIErrorResponse> response = testRestTemplate
		.exchange(route, HttpMethod.PATCH, request, OtterAPIErrorResponse.class);
	// t
	Assertions.assertEquals(HttpStatus.OK, response.getStatusCode(),
		() -> "The returned http status code was not the expected.");
	Assertions.assertTrue(response.getBody().getError()
		.contains("MemberNotFoundException"), 
		() -> "The error message was not the expected.");
	Assertions.assertEquals("Member not found", response.getBody().getMessage(), 
		() -> "The message was not the expected.");
	Assertions.assertEquals("Member not found", response.getBody().getPath(), 
		() -> "The path was not the expected.");
    }

    @DisplayName("test Unlock Member Account_when Account Locking_then Returns HTTP 200")
    @Test
    @Order(24)
    void testUnlockMemberAccount_whenAccountLocking_thenReturnsHTTP200() {
	// g
	String route = "%s/member-unlock/3".formatted(this.baseRoute);

	// creating the headers for the requestString
	HttpHeaders headers = new HttpHeaders();
	headers.setContentType(MediaType.APPLICATION_JSON);
	headers.setAccept(List.of(MediaType.APPLICATION_JSON));
	headers.set("Authorization", this.jwtAuthToken);

	HttpEntity<?> request = new HttpEntity<>(
		null, headers);
	// w
	ResponseEntity<MemberAccountLockedResponse> response = testRestTemplate
		.exchange(route, HttpMethod.PATCH, request, MemberAccountLockedResponse.class);
	// t
	Assertions.assertEquals(HttpStatus.OK, response.getStatusCode(),
		() -> "The returned http status code was not the expected.");
	Assertions.assertEquals(MemberLockedStatus.UNLOCKED.getStatus(), response.getBody().getMemberAccountNotLocked(),
		() -> "The member's account should be unlocked");
    }

    @DisplayName("test Unlock Member Account_when Search Member Doest Exists_then Returns Error Response")
    @Test
    @Order(25)
    void testUnlockMemberAccount_whenSearchMemberDoestExists_thenReturnsErrorResponse() {
	// g
	String route = "%s/member-unlock/1000".formatted(this.baseRoute);

	// creating the headers for the requestString
	HttpHeaders headers = new HttpHeaders();
	headers.setContentType(MediaType.APPLICATION_JSON);
	headers.setAccept(List.of(MediaType.APPLICATION_JSON));
	headers.set("Authorization", this.jwtAuthToken);

	HttpEntity<?> request = new HttpEntity<>(
		null, headers);
	// w
	ResponseEntity<OtterAPIErrorResponse> response = testRestTemplate
		.exchange(route, HttpMethod.PATCH, request, OtterAPIErrorResponse.class);
	// t
	Assertions.assertEquals(HttpStatus.OK, response.getStatusCode(),
		() -> "The returned http status code was not the expected.");
	Assertions.assertTrue(response.getBody().getError()
		.contains("MemberNotFoundException"), 
		() -> "The error message was not the expected.");
	Assertions.assertEquals("Member not found", response.getBody().getMessage(), 
		() -> "The message was not the expected.");
	Assertions.assertEquals("Member not found", response.getBody().getPath(), 
		() -> "The path was not the expected.");
    }
}
