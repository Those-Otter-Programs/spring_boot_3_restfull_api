package com.thoseop.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import com.thoseop.exception.OtterAccessDeniedHandler;
import com.thoseop.exception.OtterBasicAuthenticationEntryPoint;

import lombok.RequiredArgsConstructor;

@Profile("prod")
@RequiredArgsConstructor
@Configuration
public class OtterSpringSecurityConf {

    private final OtterBasicAuthenticationEntryPoint otterBasicAuthenticationEntryPoint;

    /**
     * 
     * @return
     */
    @Bean
    InMemoryUserDetailsManager userDetailsManager() {

	UserDetails associate = User.builder()
		.username("johnwart@corp.com")
		.password("{noop}johns_pass")
		.roles("ASSOCIATE")
		.authorities("VIEWINFOCORP")
		.build();

	return new InMemoryUserDetailsManager(associate);
    }

    /**
     * 
     * @param http
     * @return
     * @throws Exception
     */
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
	
	http.authorizeHttpRequests((requestFilter) -> requestFilter
		// AUTHENTICATED
		.requestMatchers(HttpMethod.GET, 
			"/api/corporation/v1/info-corp","/api/corporation/v1/info-corp/"
			).authenticated()

		// NON-AUTHENTICATED
		.requestMatchers("/swagger-ui/**", "/v3/**").permitAll()
		.requestMatchers(HttpMethod.GET, 
			"/api/corporation/v1","/api/corporation/v1/",
			"/api/corporation/v1/info","/api/corporation/v1/info/"
			).permitAll()
	);

	http.formLogin(formLogin -> formLogin.disable()); // disable default Spring's login form
	http.httpBasic(httpBasic -> httpBasic.authenticationEntryPoint(this.otterBasicAuthenticationEntryPoint));
        
        http.exceptionHandling(excepConf -> {
                excepConf.accessDeniedHandler(new OtterAccessDeniedHandler()); // deal with HTTP 403
        });

	return http.build();
    }
}
