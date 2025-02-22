package com.thoseop.api.members.http;

import java.util.List;
import java.util.Set;
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

import com.thoseop.api.members.http.request.MemberCreateRequest;
import com.thoseop.api.members.http.request.MemberManagePasswordRequest;
import com.thoseop.api.members.http.request.MemberUpdatePasswordRequest;
import com.thoseop.api.members.http.response.MemberResponse;

import static com.thoseop.config.OtterWebMvcConfig._APPLICATION_YAML_VALUE;

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

	HttpEntity<String> request = new HttpEntity<>(null, headers);
	// w
	ResponseEntity<String> response = testRestTemplate
		.withBasicAuth("ayrton.senna@bravo.com", "ayrton_pass")
		.exchange(route, HttpMethod.GET, request, String.class);

	// getting the JWT token and setting it to a property to use on the other
	// authenticated tests.
	this.jwtAuthToken = response.getHeaders().getValuesAsList("Authorization").get(0);

	// t
	Assertions.assertEquals(HttpStatus.OK, 
		response.getStatusCode(), 
		() -> "The returned http status code was not the expected.");
	Assertions.assertNotNull(this.jwtAuthToken, 
		() -> "Response should contain the JWT token in the Authorization header field");
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
    @Order(2)
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
//		.withBasicAuth("ayrton.senna@bravo.com", "ayrton_pass")
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
    @Order(3)
    void testGetMembers_whenMemberAuthorized_thenReturnsHTTP200() {
	// g
	String route = "%s/list".formatted(this.baseRoute);

	// creating the headers for the request
	HttpHeaders headers = new HttpHeaders();
	headers.setContentType(MediaType.APPLICATION_JSON);
	headers.setAccept(List.of(MediaType.APPLICATION_JSON));
	headers.set("Authorization", this.jwtAuthToken);

	HttpEntity<?> request = new HttpEntity<>(
		null, headers);
	// w
	ResponseEntity<PagedModel<EntityModel<MemberResponse>>> response = testRestTemplate
//		.withBasicAuth("ayrton.senna@bravo.com", "ayrton_pass")
		.exchange(route, HttpMethod.GET, request, new ParameterizedTypeReference<PagedModel<EntityModel<MemberResponse>>>() {
		});

//	Collection<EntityModel<MemberResponse>> members = response.getBody().getContent();
	// t
	Assertions.assertEquals(HttpStatus.OK, response.getStatusCode(), 
		() -> "The returned http status code returned was not the expected.");
    }

    @DisplayName("test Get Member By Username_when Member Authorized_then Returns HTTP 200")
    @Test
    @Order(4)
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
//		.withBasicAuth("ayrton.senna@bravo.com", "ayrton_pass")
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

    @DisplayName("test Get Member By Username_when Request With Accepted Media Types_then Return HTTP 200")
    @ParameterizedTest
    @ValueSource(strings = {MediaType.APPLICATION_JSON_VALUE, 
	    MediaType.APPLICATION_XML_VALUE, _APPLICATION_YAML_VALUE})
    @Order(5)
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
//		.withBasicAuth("ayrton.senna@bravo.com", "ayrton_pass")
		.exchange(route, HttpMethod.GET, request, MemberResponse.class);
	// t
	Assertions.assertEquals(HttpStatus.OK, response.getStatusCode(),
		() -> "The returned http status code was not the expected.");
    }

    @DisplayName("test Get Me_when Member Authenticated_then Returns HTTP 200")
    @Test
    @Order(6)
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
//		.withBasicAuth("ayrton.senna@bravo.com", "ayrton_pass")
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
    @Order(7)
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
//		.withBasicAuth("ayrton.senna@bravo.com", "ayrton_pass")
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
    @Order(8)
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
//		.withBasicAuth("mfredson2@amazon.com", "new_mick_pass")
		.exchange(route, HttpMethod.PATCH, updPassHTTPrequest, MemberResponse.class);
	// t
	Assertions.assertEquals(HttpStatus.OK, responsePassChange.getStatusCode(),
		() -> "The returned http status code was not the expected.");
    }
    
    @DisplayName("test Manage Member Password_when Authenticated_then Returns HTTP 200")
    @Test
    @Order(9)
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
//		.withBasicAuth("ayrton.senna@bravo.com", "ayrton_pass")
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
//		.withBasicAuth("ayrton.senna@bravo.com", "ayrton_pass")
		.exchange(route, HttpMethod.PATCH, updPassHTTPrequest, MemberResponse.class);
	// t
	Assertions.assertEquals(HttpStatus.OK, responsePassChange.getStatusCode(),
		() -> "The returned http status code was not the expected.");
    }

    @DisplayName("test Inactivate Member_when Right Privileges_then Returns HTTP 200")
    @Test
    @Order(10)
    void testInactivateMember_whenRightPrivileges_thenReturnsHTTP200() {
	// g
	String route = "%s/member-disable/3".formatted(this.baseRoute);

	// creating the headers for the requestString
	HttpHeaders headers = new HttpHeaders();
	headers.setContentType(MediaType.APPLICATION_JSON);
	headers.setAccept(List.of(MediaType.APPLICATION_JSON));
	headers.set("Authorization", this.jwtAuthToken);

	HttpEntity<?> request = new HttpEntity<>(
		null, headers);
	// w
	ResponseEntity<MemberResponse> response = testRestTemplate
//		.withBasicAuth("ayrton.senna@bravo.com", "ayrton_pass")
		.exchange(route, HttpMethod.PATCH, request, MemberResponse.class);
	// t
	Assertions.assertEquals(HttpStatus.OK, response.getStatusCode(),
		() -> "The returned http status code was not the expected.");
	Assertions.assertEquals(false, response.getBody().getMemberEnabled(),
		() -> "The returned member status was not the expected.");
    }

    @DisplayName("test Activate Member_when Right Privileges_then Returns HTTP 200")
    @Test
    @Order(11)
    void testActivateMember_whenRightPrivileges_thenReturnsHTTP200() {
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
	ResponseEntity<MemberResponse> response = testRestTemplate
//		.withBasicAuth("ayrton.senna@bravo.com", "ayrton_pass")
		.exchange(route, HttpMethod.PATCH, request, MemberResponse.class);
	// t
	Assertions.assertEquals(HttpStatus.OK, response.getStatusCode(),
		() -> "The returned http status code was not the expected.");
	Assertions.assertEquals(true, response.getBody().getMemberEnabled(),
		() -> "The returned member status was not the expected.");
    }
}
