package com.thoseop.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.thoseop.exception.OtterAccessDeniedHandler;
import com.thoseop.exception.OtterBasicAuthenticationEntryPoint;

import lombok.RequiredArgsConstructor;

@Profile("!prod")
@RequiredArgsConstructor
@Configuration
public class OtterDevSpringSecurityConf {

    private final OtterBasicAuthenticationEntryPoint otterBasicAuthenticationEntryPoint;

    /**
     * 
     * @param http
     * @return
     * @throws Exception
     */
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

	http.sessionManagement(smc -> {
	    smc.maximumSessions(2) // ONLY 2 LOGGED USERS WITH THE SAME CREDENTIALS ARE ALLOWED
		.maxSessionsPreventsLogin(false); // If true, prevents a user from authenticating when the {@link #maximumSessions(int)} has been reached.
	    smc.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
	});  

	http.authorizeHttpRequests((requestFilter) -> requestFilter
		// AUTHENTICATED ROUTES
		.requestMatchers(HttpMethod.GET, 
			"/api/corporation/v1/info-corp","/api/corporation/v1/info-corp/"
			).authenticated()

		// ROLE BASED AUTHENTICATED ROUTES
		.requestMatchers(HttpMethod.POST, "/api/member/v1/member-create/**").hasRole("ADMIN")
		.requestMatchers(HttpMethod.PATCH, "/api/member/v1/member-disable/**",
						"/api/member/v1/member-enable/**").hasAnyRole("ADMIN", "MANAGER", "SUPERVISOR")
		.requestMatchers(HttpMethod.GET, "/api/member/v1/member-details/**").hasAnyRole("ADMIN", "MANAGER", "SUPERVISOR")
		.requestMatchers(HttpMethod.GET, "/api/member/v1/list/**").hasAnyRole("ADMIN", "MANAGER")

		// NON-AUTHENTICATED ROUTES
		.requestMatchers("/swagger-ui/**", "/v3/**").permitAll() // OpenAPI
		.requestMatchers("/h2/**").permitAll() // H2

		.requestMatchers(HttpMethod.GET, 
			"/api/corporation/v1", "/api/corporation/v1/",
			"/api/corporation/v1/info", "/api/corporation/v1/info/"
			).permitAll()
	);

	http.formLogin(formLogin -> formLogin.disable()); // disable default Spring's login form
	http.httpBasic(httpBasic -> httpBasic.authenticationEntryPoint(this.otterBasicAuthenticationEntryPoint));
        
        http.exceptionHandling(excepConf -> {
                excepConf.accessDeniedHandler(new OtterAccessDeniedHandler()); // deal with HTTP 403
        });

        http.csrf(csrf -> csrf.disable()); // CSRF doesn't make sense most of the time for REST APIs 

        http.headers(headers -> headers.frameOptions(frameOptions -> frameOptions.disable())); // to enable h2 console access

	return http.build();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
	return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}
