package com.thoseop.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class OtterWebMvcConfig implements WebMvcConfigurer {

    @Value("${corsAllowedOrigins:http://localhost:8080}")
    private String corsAllowedOrigins;

    @Override
    public void addCorsMappings(CorsRegistry corsRegistry)
    {
        String[] allowedOrigins = this.corsAllowedOrigins.split(" ");

        corsRegistry.addMapping("/**")
                //		.allowedMethods("*") // allow all HTTP methods
                .allowedMethods("GET", "POST", "PUT", "PATCH") // allow only the specified HTTP methods
                .allowedOrigins(allowedOrigins)
                .maxAge(600) // how long in seconds the response from a pre-flight request can be cached by clients.
                .allowCredentials(true);
    }
}
