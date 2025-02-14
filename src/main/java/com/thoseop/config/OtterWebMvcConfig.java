package com.thoseop.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.thoseop.serialization.converter.OtterYamlJackson2HttpMessageConverter;

@Configuration
public class OtterWebMvcConfig implements WebMvcConfigurer {

    private static final MediaType MEDIA_TYPE_APPLICATION_YAML = MediaType.valueOf("application/x-yaml");

    @Value("${corsAllowedOrigins:http://localhost:8080}")
    private String corsAllowedOrigins;

    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
	converters.add(new OtterYamlJackson2HttpMessageConverter());
    }

    @Override
    public void addCorsMappings(CorsRegistry corsRegistry) {
	String[] allowedOrigins = this.corsAllowedOrigins.split(" ");

	corsRegistry.addMapping("/**")
		//		.allowedMethods("*") // allow all HTTP methods
		.allowedMethods("GET", "POST", "PUT", "PATCH") // allow only the specified HTTP methods
		.allowedOrigins(allowedOrigins).maxAge(600) // how long in seconds the response from a pre-flight request can be cached by clients.
		.allowCredentials(true);
    }

    @Override
    public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
	configurer.favorParameter(false) // not accepting parameters
		.ignoreAcceptHeader(false) // accept header params
		.useRegisteredExtensionsOnly(false).defaultContentType(MediaType.APPLICATION_JSON) // default responses format is JSON
		.mediaType("json", MediaType.APPLICATION_JSON).mediaType("x-yaml", MEDIA_TYPE_APPLICATION_YAML)
		.mediaType("xml", MediaType.APPLICATION_XML); // additional supported media types
    }
}
