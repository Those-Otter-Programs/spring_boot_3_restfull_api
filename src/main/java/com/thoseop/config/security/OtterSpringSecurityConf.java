package com.thoseop.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Profile("prod")
@Configuration
public class OtterSpringSecurityConf {

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
	http.authorizeHttpRequests((requests) -> requests
		.anyRequest().permitAll());

	http.formLogin(formLogin -> formLogin.disable());

	return http.build();
    }
}
