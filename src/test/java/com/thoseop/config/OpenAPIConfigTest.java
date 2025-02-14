package com.thoseop.config;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import io.restassured.RestAssured;

@DisplayName("Testing OpenAPI configuration")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class OpenAPIConfigTest {

    @LocalServerPort // anotação especial que retorna o número da porta "recrutada"         
    private int localServerPort;

    @Test
    @DisplayName("")
    void testGivenOpenAPIConfiguration_whenUIRequested_thenDisplayIt() {
	// g
	// w
	String content = RestAssured.given()
		.basePath("/swagger-ui/index.html")
		.port(localServerPort)
		.when().get()
		.then().statusCode(200)
		.extract()
		.body()
		.asString();
	// t
	Assertions.assertTrue(content.contains("Swagger UI"));
    }
}