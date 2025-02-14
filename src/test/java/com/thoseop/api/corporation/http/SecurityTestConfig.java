package com.thoseop.api.corporation.http;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@TestConfiguration
class SecurityTestConfig {

    @Bean
    @Primary
    InMemoryUserDetailsManager userDetailsManager() {

	UserDetails associate = User.builder()
		.username("johnwart@corp.com")
		.password("{noop}johns_pass")
		.roles("ASSOCIATE")
		.authorities("VIEWINFOCORP")
		.build();

	return new InMemoryUserDetailsManager(associate);
    }
}
