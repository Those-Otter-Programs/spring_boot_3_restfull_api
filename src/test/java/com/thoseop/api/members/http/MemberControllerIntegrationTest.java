package com.thoseop.api.members.http;

import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
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

    // base route for the tested controller
    private final String baseRoute = "/api/member/v1"; 
    
    @Autowired
    public MemberControllerIntegrationTest(JdbcTemplate jdbcTemplate) {
	jdbcTemplate.execute("DELETE FROM authorities WHERE member_id > 50");
	jdbcTemplate.execute("DELETE FROM members WHERE id > 50");
    }

    @DisplayName("test Create Member_when Member Authorized_then Returns HTTP 201")
    @Test
    @Order(1)
    void testCreateMember_whenMemberAuthorized_thenReturnsHTTP201() {
	// g
	String route = "%s/member-create".formatted(this.baseRoute);
	
	MemberCreateRequest memberCreateRequest = 
		new MemberCreateRequest("Rocco Lampone", "rocco.lamponne@test.com",
			"(865) 1234-5678", "roccos_pass", Set.of("ROLE_ASSOCIATE"));

	// creating the headers for the request
	HttpHeaders headers = new HttpHeaders();
	headers.setContentType(MediaType.APPLICATION_JSON);
	headers.setAccept(List.of(MediaType.APPLICATION_JSON));

	HttpEntity<MemberCreateRequest> request = new HttpEntity<>(
		memberCreateRequest, headers);
	// w
	ResponseEntity<MemberResponse> response = testRestTemplate
		.withBasicAuth("ayrton.senna@bravo.com", 
				"ayrton_pass")
		.exchange(route, HttpMethod.POST, request, MemberResponse.class);
	
	// t
	Assertions.assertEquals(HttpStatus.CREATED, 
		response.getStatusCode());
	Assertions.assertEquals(memberCreateRequest.getMemberName(), 
		response.getBody().getMemberName());
	Assertions.assertEquals(memberCreateRequest.getMemberEmail(), 
		response.getBody().getMemberEmail());
	Assertions.assertEquals(memberCreateRequest.getMemberMobileNumber(), 
		response.getBody().getMemberMobileNumber());
    }

    @DisplayName("test Get Members_when Member Authorized_then Returns HTTP 200")
    @Test
    @Order(2)
    void testGetMembers_whenMemberAuthorized_thenReturnsHTTP200() {
	// g
	String route = "%s/list".formatted(this.baseRoute);

	// creating the headers for the request
	HttpHeaders headers = new HttpHeaders();
	headers.setContentType(MediaType.APPLICATION_JSON);
	headers.setAccept(List.of(MediaType.APPLICATION_JSON));

	HttpEntity<?> request = new HttpEntity<>(
		null, headers);
	// w
	ResponseEntity<PagedModel<EntityModel<MemberResponse>>> response = testRestTemplate
		.withBasicAuth("ayrton.senna@bravo.com", "ayrton_pass")
		.exchange(route, HttpMethod.GET, request, new ParameterizedTypeReference<PagedModel<EntityModel<MemberResponse>>>() {
		});

//	Collection<EntityModel<MemberResponse>> members = response.getBody().getContent();
	// t
	Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @DisplayName("test Get Member By Username_when Member Authorized_then Returns HTTP 200")
    @Test
    @Order(3)
    void testGetMemberByUsername_whenMemberAuthorized_thenReturnsHTTP200() {
	// g
	String route = "%s/member-details/ayrton.senna@bravo.com".formatted(this.baseRoute);

	// creating the headers for the requestString
	HttpHeaders headers = new HttpHeaders();
	headers.setContentType(MediaType.APPLICATION_JSON);
	headers.setAccept(List.of(MediaType.APPLICATION_JSON));

	HttpEntity<?> request = new HttpEntity<>(
		null, headers);
	// w
	ResponseEntity<MemberResponse> response = testRestTemplate
		.withBasicAuth("ayrton.senna@bravo.com", 
				"ayrton_pass")
		.exchange(route, HttpMethod.GET, request, MemberResponse.class);
	// t
	Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
	Assertions.assertEquals("Ayrton Senna", 
		response.getBody().getMemberName());
	Assertions.assertEquals("ayrton.senna@bravo.com", 
		response.getBody().getMemberEmail());
	Assertions.assertEquals("(11) 98765-4321", 
		response.getBody().getMemberMobileNumber());
    }
    
    @DisplayName("test Update Member Password_when Authenticated_then Returns HTTP 200")
    @Test
    @Order(4)
    void testUpdateMemberPassword_whenAuthenticated_thenReturnsHTTP200() {
	// g
	String route = "%s/member-password".formatted(this.baseRoute);
	
	MemberUpdatePasswordRequest memberUpdPassRequest = 
		new MemberUpdatePasswordRequest("new_mick_pass"); 

	// creating the headers for the requestString
	HttpHeaders headers = new HttpHeaders();
	headers.setContentType(MediaType.APPLICATION_JSON);
	headers.setAccept(List.of(MediaType.APPLICATION_JSON));

	HttpEntity<MemberUpdatePasswordRequest> updPassHTTPrequest = new HttpEntity<>(
		memberUpdPassRequest, headers);
	// w
	// ...changing the members password...
	ResponseEntity<MemberResponse> responsePassChange = testRestTemplate
		.withBasicAuth("mfredson2@amazon.com", "lQEXBPL")
		.exchange(route, HttpMethod.PATCH, updPassHTTPrequest, MemberResponse.class);
	// t
	Assertions.assertEquals(HttpStatus.OK, 
		responsePassChange.getStatusCode());

	// ------------------ REVERTING PASSWORD CHANGE ------------------
	// g
	memberUpdPassRequest = 
		new MemberUpdatePasswordRequest("lQEXBPL"); 
	updPassHTTPrequest = new HttpEntity<>(memberUpdPassRequest, headers);
	// w
	responsePassChange = testRestTemplate
		.withBasicAuth("mfredson2@amazon.com", "new_mick_pass")
		.exchange(route, HttpMethod.PATCH, updPassHTTPrequest, MemberResponse.class);
	// t
	Assertions.assertEquals(HttpStatus.OK, 
		responsePassChange.getStatusCode());
    }
    
    @DisplayName("test Manage Member Password_when Authenticated_then Returns HTTP 200")
    @Test
    @Order(5)
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

	HttpEntity<MemberManagePasswordRequest> updPassHTTPrequest = new HttpEntity<>(
		memberMngPassRequest, headers);
	// w
	// ...changing the members password...
	ResponseEntity<MemberResponse> responsePassChange = testRestTemplate
		.withBasicAuth("ayrton.senna@bravo.com", "ayrton_pass")
		.exchange(route, HttpMethod.PATCH, updPassHTTPrequest, MemberResponse.class);
	// t
	Assertions.assertEquals(HttpStatus.OK, 
		responsePassChange.getStatusCode());

	// ------------------ REVERTING PASSWORD CHANGE ------------------
	// g
	memberMngPassRequest = 
		new MemberManagePasswordRequest("mfredson2@amazon.com", "lQEXBPL"); 
	updPassHTTPrequest = new HttpEntity<>(memberMngPassRequest, headers);
	// w
	responsePassChange = testRestTemplate
		.withBasicAuth("ayrton.senna@bravo.com", "ayrton_pass")
		.exchange(route, HttpMethod.PATCH, updPassHTTPrequest, MemberResponse.class);
	// t
	Assertions.assertEquals(HttpStatus.OK, 
		responsePassChange.getStatusCode());
    }

    @DisplayName("test Inactivate Member_when Right Privileges_then Returns HTTP 200")
    @Test
    @Order(6)
    void testInactivateMember_whenRightPrivileges_thenReturnsHTTP200() {
	// g
	String route = "%s/member-disable/3".formatted(this.baseRoute);

	// creating the headers for the requestString
	HttpHeaders headers = new HttpHeaders();
	headers.setContentType(MediaType.APPLICATION_JSON);
	headers.setAccept(List.of(MediaType.APPLICATION_JSON));

	HttpEntity<?> request = new HttpEntity<>(
		null, headers);
	// w
	ResponseEntity<MemberResponse> response = testRestTemplate
		.withBasicAuth("ayrton.senna@bravo.com", 
				"ayrton_pass")
		.exchange(route, HttpMethod.PATCH, request, MemberResponse.class);
	// t
	Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
	Assertions.assertEquals(false, 
		response.getBody().getMemberEnabled());
    }

    @DisplayName("test Activate Member_when Right Privileges_then Returns HTTP 200")
    @Test
    @Order(7)
    void testActivateMember_whenRightPrivileges_thenReturnsHTTP200() {
	// g
	String route = "%s/member-enable/3".formatted(this.baseRoute);

	// creating the headers for the requestString
	HttpHeaders headers = new HttpHeaders();
	headers.setContentType(MediaType.APPLICATION_JSON);
	headers.setAccept(List.of(MediaType.APPLICATION_JSON));

	HttpEntity<?> request = new HttpEntity<>(
		null, headers);
	// w
	ResponseEntity<MemberResponse> response = testRestTemplate
		.withBasicAuth("ayrton.senna@bravo.com", 
				"ayrton_pass")
		.exchange(route, HttpMethod.PATCH, request, MemberResponse.class);
	// t
	Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
	Assertions.assertEquals(true, 
		response.getBody().getMemberEnabled());
    }
}
