package com.thoseop.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.password.CompromisedPasswordChecker;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.password.HaveIBeenPwnedRestApiPasswordChecker;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import com.thoseop.exception.OtterAccessDeniedHandler;
import com.thoseop.exception.OtterBasicAuthenticationEntryPoint;
import com.thoseop.filter.AuthenticationLoggingAfterFilter;
import com.thoseop.filter.AuthenticationLoggingAtFilter;
import com.thoseop.filter.AuthenticationLoggingBeforeFilter;
import com.thoseop.filter.OtterJWTTokenGeneratorFilter;
import com.thoseop.filter.OtterJWTTokenValidatorFilter;

import lombok.RequiredArgsConstructor;

@Configuration
@Profile("!prod")
@RequiredArgsConstructor
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
	
	// FILTER 
        http.addFilterBefore(new AuthenticationLoggingBeforeFilter(), BasicAuthenticationFilter.class)
            .addFilterAt(new AuthenticationLoggingAtFilter(), BasicAuthenticationFilter.class)
            .addFilterAfter(new AuthenticationLoggingAfterFilter(), BasicAuthenticationFilter.class)
            // JWT FILTERS
            .addFilterBefore(new OtterJWTTokenValidatorFilter(), BasicAuthenticationFilter.class)
            .addFilterAfter(new OtterJWTTokenGeneratorFilter(), BasicAuthenticationFilter.class);
        
//        http.requiresChannel(rcc -> rcc.anyRequest().requiresSecure()); // HTTPS only

	http.authorizeHttpRequests((requestFilter) -> requestFilter
		// AUTHENTICATED ROUTES
		.requestMatchers(HttpMethod.GET, "/api/member/v1/token").authenticated() 
		.requestMatchers(HttpMethod.GET, 
			"/api/corporation/v1/info-corp","/api/corporation/v1/info-corp/"
			).authenticated()
		.requestMatchers(HttpMethod.PATCH, "/api/member/v1/member-password/**").authenticated()
		.requestMatchers(HttpMethod.GET, "/api/member/v1/me").authenticated()

		// ROLE BASED AUTHENTICATED ROUTES
		.requestMatchers(HttpMethod.POST, "/api/member/v1/member-create/**").hasRole("ADMIN")
		.requestMatchers(HttpMethod.PATCH, "/api/member/v1/member-disable/{id}",
						"/api/member/v1/member-enable/{id}").hasAnyRole("ADMIN", "MANAGER", "SUPERVISOR")
		.requestMatchers(HttpMethod.PATCH, "/api/member/v1/member-lock/{id}",
						"/api/member/v1/member-unlock/{id}").hasAnyRole("ADMIN", "MANAGER", "SUPERVISOR")
		.requestMatchers(HttpMethod.PATCH, "/api/member/v1/manage-member-password/**").hasRole("ADMIN")
		.requestMatchers(HttpMethod.GET, "/api/member/v1/member-full-details/{username}").hasAnyRole("ADMIN", "MANAGER", "SUPERVISOR")
		.requestMatchers(HttpMethod.GET, "/api/member/v1/member-details/{username}").hasAnyRole("ADMIN", "MANAGER", "SUPERVISOR")
		.requestMatchers(HttpMethod.GET, "/api/member/v1/list/**").hasAnyRole("ADMIN", "MANAGER")

		.requestMatchers(HttpMethod.GET, "/api/authentication-failure/v1/member/{username}",
						"/api/authentication-failure/v1/log/**").hasRole("ADMIN")

		// NON-AUTHENTICATED ROUTES
		.requestMatchers("/swagger-ui/**", "/v3/**").permitAll() // OpenAPI
		.requestMatchers("/h2/**").permitAll() // H2

		.requestMatchers(HttpMethod.GET, "/api/corporation/v1", "/api/corporation/v1/",
						"/api/corporation/v1/info", "/api/corporation/v1/info/").permitAll()
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
	// using the delegating password encode approach, which determines the type
	// of hashing by the "prefix" on the password ({noop}, {bcrypt}).
	// By default BCrypt password hashing is used for newly created users.
	return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    /**
     * Spring Security 6.3+
     *
     * @return
     */
    @Bean
    CompromisedPasswordChecker compromisedPasswordChecker() {
        return new HaveIBeenPwnedRestApiPasswordChecker();
    }

    @Bean
    AuthenticationManager authenticationManager(UserDetailsService userDetailsService,
            PasswordEncoder passwordEncoder) {
	OtterEmailPwdAuthenticationProvider authenticationProvider =
                new OtterEmailPwdAuthenticationProvider(userDetailsService, passwordEncoder);
        ProviderManager providerManager = new ProviderManager(authenticationProvider);
        providerManager.setEraseCredentialsAfterAuthentication(false);
        return providerManager;
    }
}

