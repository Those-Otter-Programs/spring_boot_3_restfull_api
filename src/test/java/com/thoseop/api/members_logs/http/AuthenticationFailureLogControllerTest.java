package com.thoseop.api.members_logs.http;

import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.params.ParameterizedTest;
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
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.thoseop.api.members_logs.http.response.AuthenticationFailureLogResponse;

import jakarta.security.auth.message.AuthStatus;
import net.minidev.json.JSONObject;

@DisplayName("Testing AuthenticationFailureLogController")
@TestPropertySource(locations="classpath:application_test.properties")
@ActiveProfiles(value = {"test"})
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class) // execute test methods based on the position set on @Order
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AuthenticationFailureLogControllerTest {

    @LocalServerPort // retrieves the random server port being used to run these tests
    private int localServerPort;
    
    @Autowired
    private TestRestTemplate testRestTemplate;

    private String jwtAuthToken;

    // base route for the tested controller
    private final String baseRoute = "/api/authentication-failure/v1"; 

    @BeforeEach
    void setUp() throws Exception {}
    
    @DisplayName("test Member Token_when Member Authorized_then Return Token")
    @Test
    @Order(1)
    void testMemberToken_whenMemberAuthorized_thenReturnToken() {
	// g
	String route = "/api/member/v1/token".formatted(this.baseRoute);

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
	Assertions.assertEquals(response.getBody().get("token"), this.jwtAuthToken, 
		() -> "Token receive is differente than expected");
    }

    @ParameterizedTest
    @ValueSource(strings = {
	    "/api/authentication-failure/v1/member/ayrton.senna@bravo.com?page=0&size=8&sortDir=asc&sortBy=logAuthTime",
	    "/api/authentication-failure/v1/member/ayrton.senna@bravo.com?page=0&size=8&sortDir=asc",
	    "/api/authentication-failure/v1/member/ayrton.senna@bravo.com?page=0&size=8",
	    "/api/authentication-failure/v1/member/ayrton.senna@bravo.com"
	    })
    @Order(2)
    void testGetMemberAuthenticationFailures(String route) throws JsonMappingException, JsonProcessingException {
	// g
	String username = "ayrton.senna@bravo.com";

	// creating the headers for the request
	HttpHeaders headers = new HttpHeaders();
	headers.setContentType(MediaType.APPLICATION_JSON);
	headers.setAccept(List.of(MediaType.APPLICATION_JSON));
	headers.set("Authorization", this.jwtAuthToken);

	HttpEntity<?> request = new HttpEntity<>(null, headers);
	// w
	ResponseEntity<PagedModel<EntityModel<AuthenticationFailureLogResponse>>> response = testRestTemplate
		.exchange(route, HttpMethod.GET, request,
			new ParameterizedTypeReference<PagedModel<EntityModel<AuthenticationFailureLogResponse>>>() {
		});

	PagedModel<EntityModel<AuthenticationFailureLogResponse>> pagedMembers = response.getBody();        
	
//	System.out.println(pagedMembers.toString());
        
        List<EntityModel<AuthenticationFailureLogResponse>> logEM = pagedMembers.getContent().stream().collect(Collectors.toList());
        AuthenticationFailureLogResponse l1 = logEM.get(0).getContent();
	
	// t
	Assertions.assertEquals(HttpStatus.OK, response.getStatusCode(), 
		() -> "The returned http status code returned was not the expected.");

	Assertions.assertEquals(8, pagedMembers.getMetadata().getSize(), 
		() -> "The page size was not the expected");
	Assertions.assertTrue(pagedMembers.getMetadata().getTotalElements() >= 4, 
		() -> "The total number of elements was not the expected");
	Assertions.assertTrue(pagedMembers.getMetadata().getTotalPages() >= 1, 
		() -> "The total number of pages was not the expected");
	Assertions.assertEquals(0, pagedMembers.getMetadata().getNumber(), 
		() -> "The page number was not the expected");
	
	Assertions.assertEquals(username, l1.getLogUsername(), 
		() -> "The username was not the expected.");
	Assertions.assertEquals(AuthStatus.FAILURE.toString(), l1.getLogEventResult(), 
		() -> "The event result was not the expected.");
	Assertions.assertNotNull(l1.getLogRemoteIpAddress(), 
		() -> "The log's remoteIpAddress data should not be null.");
	Assertions.assertNotNull(l1.getLogMessage(), 
		() -> "The log's logMessage data should not be null.");
	Assertions.assertNotNull(l1.getLogAuthTime(), 
		() -> "The log's logAuthTime data should not be null.");
	Assertions.assertNotNull(l1.getLogCreatedAt(), 
		() -> "The log's logCreatedAt data should not be null.");
    }

    @Test
    @Order(3)
    void testGetLogById() {
	// g
	String username = "ayrton.senna@bravo.com";
	String route = "%s/log/1".formatted(this.baseRoute);

	// creating the headers for the requestString
	HttpHeaders headers = new HttpHeaders();
	headers.setContentType(MediaType.APPLICATION_JSON);
	headers.setAccept(List.of(MediaType.APPLICATION_JSON));
	headers.set("Authorization", this.jwtAuthToken);

	HttpEntity<?> request = new HttpEntity<>(
		null, headers);
	// w
	ResponseEntity<AuthenticationFailureLogResponse> response = testRestTemplate
		.exchange(route, HttpMethod.GET, request, AuthenticationFailureLogResponse.class);
	// t
	Assertions.assertEquals(HttpStatus.OK, response.getStatusCode(),
		() -> "The returned http status code was not the expected.");
	Assertions.assertEquals(username, response.getBody().getLogUsername(), 
		() -> "The username was not the expected.");
	Assertions.assertEquals(AuthStatus.FAILURE.toString(), response.getBody().getLogEventResult(), 
		() -> "The event result was not the expected.");
	Assertions.assertNotNull(response.getBody().getLogRemoteIpAddress(), 
		() -> "The log's remoteIpAddress data should not be null.");
	Assertions.assertNotNull(response.getBody().getLogMessage(), 
		() -> "The log's logMessage data should not be null.");
	Assertions.assertNotNull(response.getBody().getLogAuthTime(), 
		() -> "The log's logAuthTime data should not be null.");
	Assertions.assertNotNull(response.getBody().getLogCreatedAt(), 
		() -> "The log's logCreatedAt data should not be null.");
    }
}
