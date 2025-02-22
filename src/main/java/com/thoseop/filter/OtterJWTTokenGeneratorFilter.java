package com.thoseop.filter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.stream.Collectors;

import javax.crypto.SecretKey;

import org.springframework.core.env.Environment;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class OtterJWTTokenGeneratorFilter extends OncePerRequestFilter {

    /**
     * @param request
     * @param response
     * @param filterChain
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
	    throws ServletException, IOException {

	Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	
	log.info("Otter - Generating the JWT token");
	
	if (null != authentication) {
	    Environment env = getEnvironment();
            if (null != env) {
                String secret = env.getProperty("jwt.secret.key", "nxgMZXng3egORujUQQvCwpeA4LCInz6or9Q45CPn6oE3Eb2xXquG");
                SecretKey secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));

                String jwt = Jwts.builder()
                	.issuer(env.getProperty(
                		"spring.application.name", "spring_boot_3_restfull_api"))
                        .subject("JWT Token")
                        .claim("username", authentication.getName())
                        .claim("authorities", authentication.getAuthorities()
                        	.stream().map(GrantedAuthority::getAuthority)
                        	         .collect(Collectors.joining(",")))
                        .issuedAt(new Date())
                        .expiration(new Date((new Date()).getTime() + 60000)) // token valid for 60 seconds 60000 ms
            		.signWith(secretKey).compact();
                
                response.setHeader("Authorization", jwt);
            }
	}
	filterChain.doFilter(request, response);
    }

    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
	return !request.getServletPath().equals("/api/member/v1/token");
    }
}
