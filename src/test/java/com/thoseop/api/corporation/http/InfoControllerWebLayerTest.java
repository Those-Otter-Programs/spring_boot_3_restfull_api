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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@DisplayName("Testing InfoController")
@WebMvcTest(controllers = InfoControllerImpl.class, 
	excludeAutoConfiguration = { SecurityAutoConfiguration.class })
class InfoControllerWebLayerTest {

    @Autowired
    private MockMvc mockMvc;
    
    @Value("${spring.application.name}")
    private String appName;

    private String baseRoute = "/api/corporation/v1";

    @BeforeEach
    void setUp() throws Exception {}

    @DisplayName("test Info_when Do Request_then Return HTTP 200")
    @ParameterizedTest
    @ValueSource(strings = { "", "/info" })
    void testInfo_whenDoRequest_thenReturnHTTP200(String route) throws Exception {
	// g
	RequestBuilder requestBuilder = MockMvcRequestBuilders
		.get(baseRoute + route)
		.accept(MediaType.APPLICATION_JSON_VALUE);
	// w
	ResultActions response = mockMvc.perform(requestBuilder);
	// t
	response.andDo(print())
		.andExpect(status().isOk());
    }

    @DisplayName("test Info_when Do Request_then Return Expected Data")
    @ParameterizedTest
    @ValueSource(strings = { "", "/info" })
    void testInfo_whenDoRequest_thenReturnExpectedData(String route) throws Exception {
	// g
	String appDescription = "A Spring Boot 3 RESTfull API sample";

	RequestBuilder requestBuilder = MockMvcRequestBuilders
		.get(baseRoute + route)
		.accept(MediaType.APPLICATION_JSON_VALUE);
	// w
	ResultActions response = mockMvc.perform(requestBuilder);
	// t
	response.andDo(print())
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.Application", is(this.appName)))
		.andExpect(jsonPath("$.Description", is(appDescription)));
    }

    @DisplayName("test Info_when Do Request Allowed CORS Origin_then Return HTTP 200")
    @ParameterizedTest
    @ValueSource(strings = { "", "/info" })
    void testInfo_whenDoRequestAllowedCORSOrigin_thenReturnHTTP200(String route) throws Exception {
	// g
	String appDescription = "A Spring Boot 3 RESTfull API sample";

	RequestBuilder requestBuilder = MockMvcRequestBuilders
		.get(baseRoute + route)
		.header("Origin", "http://localhost:3000")
		.accept(MediaType.APPLICATION_JSON_VALUE);
	// w
	ResultActions response = mockMvc.perform(requestBuilder);
	// t
	response.andDo(print())
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.Application", is(this.appName)))
		.andExpect(jsonPath("$.Description", is(appDescription)));
    }
    
    @DisplayName("test Info_when Do Request Invalid CORS Origin_then Return HTTP 403")
    @ParameterizedTest
    @ValueSource(strings = { "", "/info" })
    void testInfo_whenDoRequestInvalidCORSOrigin_thenReturnHTTP403(String route) throws Exception {
	// g
	RequestBuilder requestBuilder = MockMvcRequestBuilders
		.get(baseRoute + route)
		.header("Origin", "http://localhost:3001")
		.accept(MediaType.APPLICATION_JSON_VALUE);
	// w
	ResultActions response = mockMvc.perform(requestBuilder);
	// t
	response.andDo(print())
		.andExpect(status().isForbidden());
    }
    
    @DisplayName("test Info_when Do Request Accepting XML Media Type_then Return HTTP 200")
    @ParameterizedTest
    @ValueSource(strings = { "", "/info" })
    void testInfo_whenDoRequestAcceptingXMLMediaType_thenReturnHTTP200(String route) throws Exception {
	// g
	RequestBuilder requestBuilder = MockMvcRequestBuilders
		.get(baseRoute + route)
		.header("Origin", "http://localhost:3000")
		.header("Accept", "application/xml");
	// w
	ResultActions response = mockMvc.perform(requestBuilder);
	// t
	response.andDo(print())
		.andExpect(status().isOk());
    }
    
    @DisplayName("test Info_when Do Request Accepting YML Media Type_then Return HTTP 200")
    @ParameterizedTest
    @ValueSource(strings = { "", "/info" })
    void testInfo_whenDoRequestAcceptingYMLMediaType_thenReturnHTTP200(String route) throws Exception {
	// g
	RequestBuilder requestBuilder = MockMvcRequestBuilders
		.get(baseRoute + route)
		.header("Origin", "http://localhost:3000")
		.header("Accept", "application/x-yaml");
	// w
	ResultActions response = mockMvc.perform(requestBuilder);
	// t
	response.andDo(print())
		.andExpect(status().isOk());
    }

    @DisplayName("test Info Corp_when Do Request_then Return HTTP 200")
    @ParameterizedTest
    @ValueSource(strings = { "/info-corp" })
    void testInfoCorp_whenDoRequest_thenReturnHTTP200(String route) throws Exception {
	// g
	RequestBuilder requestBuilder = MockMvcRequestBuilders
		.get(baseRoute + route)
		.accept(MediaType.APPLICATION_JSON_VALUE);
	// w
	ResultActions response = mockMvc.perform(requestBuilder);
	// t
	response.andDo(print())
		.andExpect(status().isOk());
    }

    @DisplayName("test Info Corp_when Do Request_then Return Expected Data")
    @ParameterizedTest
    @ValueSource(strings = { "/info-corp" })
    void testInfoCorp_whenDoRequest_thenReturnExpectedData(String route) throws Exception {
	// g
	String reqMessage = "All facilities operating";
	String reqLocation = "Toronto, Canada";

	RequestBuilder requestBuilder = MockMvcRequestBuilders
		.get(baseRoute + route)
		.accept(MediaType.APPLICATION_JSON_VALUE);
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
    @ValueSource(strings = { "/info-corp" })
    void testInfoCorp_whenDoRequestAllowedCORSOrigin_thenReturnHTTP200(String route) throws Exception {
	// g
	String reqMessage = "All facilities operating";
	String reqLocation = "Toronto, Canada";

	RequestBuilder requestBuilder = MockMvcRequestBuilders
		.get(baseRoute + route)
		.header("Origin", "http://localhost:3000")
		.accept(MediaType.APPLICATION_JSON_VALUE);
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
    @ValueSource(strings = { "/info-corp" })
    void testInfoCorp_whenDoRequestInvalidCORSOrigin_thenReturnHTTP403(String route) throws Exception {
	// g
	RequestBuilder requestBuilder = MockMvcRequestBuilders
		.get(baseRoute + route)
		.header("Origin", "http://localhost:3001")
		.accept(MediaType.APPLICATION_JSON_VALUE);
	// w
	ResultActions response = mockMvc.perform(requestBuilder);
	// t
	response.andDo(print())
		.andExpect(status().isForbidden());
    }
    
    @DisplayName("test Info Corp_when Do Request Accepting XML Media Type_then Return HTTP 200")
    @ParameterizedTest
    @ValueSource(strings = { "/info-corp" })
    void testInfoCorp_whenDoRequestAcceptingXMLMediaType_thenReturnHTTP200(String route) throws Exception {
	// g
	RequestBuilder requestBuilder = MockMvcRequestBuilders
		.get(baseRoute + route)
		.header("Origin", "http://localhost:3000")
		.header("Accept", "application/xml");
	// w
	ResultActions response = mockMvc.perform(requestBuilder);
	// t
	response.andDo(print())
		.andExpect(status().isOk());
    }
    
    @DisplayName("test Info Corp_when Do Request Accepting YML Media Type_then Return HTTP 200")
    @ParameterizedTest
    @ValueSource(strings = { "/info-corp" })
    void testInfoCorp_whenDoRequestAcceptingYMLMediaType_thenReturnHTTP200(String route) throws Exception {
	// g
	RequestBuilder requestBuilder = MockMvcRequestBuilders
		.get(baseRoute + route)
		.header("Origin", "http://localhost:3000")
		.header("Accept", "application/x-yaml");
	// w
	ResultActions response = mockMvc.perform(requestBuilder);
	// t
	response.andDo(print())
		.andExpect(status().isOk());
    }
}
