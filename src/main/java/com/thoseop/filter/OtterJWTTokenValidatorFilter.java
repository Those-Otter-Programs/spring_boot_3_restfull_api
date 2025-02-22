package com.thoseop.filter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import javax.crypto.SecretKey;

import org.springframework.core.env.Environment;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class OtterJWTTokenValidatorFilter extends OncePerRequestFilter {

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

	log.info("Otter - Validating the JWT token");

	String jwt = request.getHeader("Authorization");
	if (null != jwt) {
	    try {
		Environment env = getEnvironment();
		if (null != env) {
                    String secret = env.getProperty("jwt.secret.key", "nxgMZXng3egORujUQQvCwpeA4LCInz6or9Q45CPn6oE3Eb2xXquG");
		    SecretKey secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));

		    Claims claims = Jwts.parser().verifyWith(secretKey)
			    .build().parseSignedClaims(jwt).getPayload();
		    String username = String.valueOf(claims.get("username"));
		    String authorities = String.valueOf(claims.get("authorities"));

		    Authentication authentication = new UsernamePasswordAuthenticationToken(username, null,
			    AuthorityUtils.commaSeparatedStringToAuthorityList(authorities));

		    SecurityContextHolder.getContext().setAuthentication(authentication);
		}
	    } catch (Exception exception) {
		throw new BadCredentialsException("Otter - Invalid JWT Token");
	    }
	}
	filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
	return request.getServletPath().equals("/api/member/v1/token");
    }
}