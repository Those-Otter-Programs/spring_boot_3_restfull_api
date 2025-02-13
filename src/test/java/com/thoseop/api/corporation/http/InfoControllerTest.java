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

@DisplayName("Testing CorporationController")
@WebMvcTest(controllers = InfoController.class, 
	excludeAutoConfiguration = { SecurityAutoConfiguration.class })
class InfoControllerTest {

    @Autowired
    private MockMvc mockMvc;
    
    @Value("${spring.application.name}")
    private String appName;
    
    private String baseRoute = "/api/corporation/v1";

    @BeforeEach
    void setUp() throws Exception {}

    @ParameterizedTest
    @ValueSource(strings = { "", "/", "/info", "/info/" })
    void testInfo_whenDoRequest_thenReturnHTTP200(String route) throws Exception {
	// g
	RequestBuilder requestBuilder = MockMvcRequestBuilders.get(baseRoute + route)
		.accept(MediaType.APPLICATION_JSON_VALUE);
	// w
	ResultActions response = mockMvc.perform(requestBuilder);
	// t
	response.andDo(print())
		.andExpect(status().isOk());
    }

    @ParameterizedTest
    @ValueSource(strings = { "", "/", "/info", "/info/" })
    void testInfo_whenDoRequest_thenReturnExpectedData(String route) throws Exception {
	// g
	String appDescription = "A Spring Boot 3 RESTfull API sample";

	RequestBuilder requestBuilder = MockMvcRequestBuilders.get(baseRoute + route)
		.accept(MediaType.APPLICATION_JSON_VALUE);
	// w
	ResultActions response = mockMvc.perform(requestBuilder);
	// t
	response.andDo(print())
		.andExpect(jsonPath("$.Application", is(this.appName)))
		.andExpect(jsonPath("$.Description", is(appDescription)));
    }
}
