package com.thoseop.api.corporation.http;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@DisplayName("Testing InfoController")
@WebMvcTest(controllers = InfoControllerImpl.class) 
@TestPropertySource(properties = { 
	"spring.main.allow-bean-definition-overriding=true",
})
@ContextConfiguration(classes = {SecurityTestConfig.class})
@AutoConfigureMockMvc
class InfoControllerAuthWebLayerTest {

    @Autowired
    private MockMvc mockMvc;
    
    private String baseRoute = "/api/corporation/v1";

    @BeforeEach
    void setUp() throws Exception {}

    @DisplayName("test Info Corp_when Do Request With Auth_then Return HTTP 200")
    @ParameterizedTest
    @ValueSource(strings = { "/info-corp", "/info-corp/" })
    void testInfoCorp_whenDoRequestWithAuth_thenReturnHTTP200(String route) throws Exception {
	// g
	HttpHeaders headers = new HttpHeaders();
	headers.set("Accept", "application/json");
        headers.setBasicAuth("johnwart@corp.com","johns_pass");
        
	RequestBuilder requestBuilder = MockMvcRequestBuilders
		.get(this.baseRoute + route)
		.headers(headers);
	// w
	ResultActions response = mockMvc.perform(requestBuilder);
	// t
	response.andDo(print())
		.andExpect(status().isOk());
    }

    @DisplayName("test Info Corp_when Do Request With Auth_then Return Expected Data")
    @ParameterizedTest
    @ValueSource(strings = { "/info-corp", "/info-corp/" })
    void testInfoCorp_whenDoRequestWithAuth_thenReturnExpectedData(String route) throws Exception {
	// g
	String reqMessage = "All facilities operating";
	String reqLocation = "Toronto, Canada";
	
	HttpHeaders headers = new HttpHeaders();
	headers.set("Accept", "application/json");
        headers.setBasicAuth("johnwart@corp.com","johns_pass");

	RequestBuilder requestBuilder = MockMvcRequestBuilders
		.get(this.baseRoute + route)
		.headers(headers);
	// w
	ResultActions response = mockMvc.perform(requestBuilder);
	// t
	response.andDo(print())
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.message", is(reqMessage)))
		.andExpect(jsonPath("$.location", is(reqLocation)));
    }

    @DisplayName("test Info Corp_when Do Request Allowed CORS Origin_then Return HTTP 200")
    @ParameterizedTest
    @ValueSource(strings = { "/info-corp", "/info-corp/" })
    void testInfoCorp_whenDoRequestWithAuthAllowedCORSOrigin_thenReturnHTTP200(String route) throws Exception {
	// g
	String reqMessage = "All facilities operating";
	String reqLocation = "Toronto, Canada";
	
	HttpHeaders headers = new HttpHeaders();
	headers.set("Accept", "application/json");
        headers.setBasicAuth("johnwart@corp.com","johns_pass");

	RequestBuilder requestBuilder = MockMvcRequestBuilders
		.get(this.baseRoute + route)
		.headers(headers);
	// w
	ResultActions response = mockMvc.perform(requestBuilder);
	// t
	response.andDo(print())
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.message", is(reqMessage)))
		.andExpect(jsonPath("$.location", is(reqLocation)));
    }
    
    @DisplayName("test Info Corp_when Do Request Invalid CORS Origin_then Return HTTP 403")
    @ParameterizedTest
    @ValueSource(strings = { "/info-corp", "/info-corp/" })
    void testInfoCorp_whenDoRequestWithAuthInvalidCORSOrigin_thenReturnHTTP403(String route) throws Exception {
	// g
	HttpHeaders headers = new HttpHeaders();
	headers.set("Accept", "application/json");
        headers.setBasicAuth("johnwart@corp.com","johns_pass");

	RequestBuilder requestBuilder = MockMvcRequestBuilders
		.get(this.baseRoute + route)
		.header("Origin", "http://localhost:3001")
		.headers(headers);
	// w
	ResultActions response = mockMvc.perform(requestBuilder);
	// t
	response.andDo(print())
		.andExpect(status().isForbidden());
    }
    
    @DisplayName("test Info Corp_when Do Request Accepting XML Media Type_then Return HTTP 200")
    @ParameterizedTest
    @ValueSource(strings = { "/info-corp", "/info-corp/" })
    void testInfoCorp_whenDoRequestWithAuthAcceptingXMLMediaType_thenReturnHTTP200(String route) throws Exception {
	// g
	HttpHeaders headers = new HttpHeaders();
	headers.set("Accept", "application/xml");
        headers.setBasicAuth("johnwart@corp.com","johns_pass");

	RequestBuilder requestBuilder = MockMvcRequestBuilders
		.get(this.baseRoute + route)
		.header("Origin", "http://localhost:3000")
		.headers(headers);
	// w
	ResultActions response = mockMvc.perform(requestBuilder);
	// t
	response.andDo(print())
		.andExpect(status().isOk());
    }
    
    @DisplayName("test Info Corp_when Do Request Accepting YML Media Type_then Return HTTP 200")
    @ParameterizedTest
    @ValueSource(strings = { "/info-corp", "/info-corp/" })
    void testInfoCorp_whenDoRequestWithAuthAcceptingYMLMediaType_thenReturnHTTP200(String route) throws Exception {
	// g
	HttpHeaders headers = new HttpHeaders();
	headers.set("Accept", "application/x-yaml");
        headers.setBasicAuth("johnwart@corp.com","johns_pass");

	RequestBuilder requestBuilder = MockMvcRequestBuilders
		.get(this.baseRoute + route)
		.header("Origin", "http://localhost:3000")
		.headers(headers);
	// w
	ResultActions response = mockMvc.perform(requestBuilder);
	// t
	response.andDo(print())
		.andExpect(status().isOk());
    }
}
