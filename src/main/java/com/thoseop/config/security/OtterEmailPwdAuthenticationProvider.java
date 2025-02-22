package com.thoseop.config.security;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
//@Profile("prod")
@RequiredArgsConstructor
@Component
public class OtterEmailPwdAuthenticationProvider implements AuthenticationProvider {

    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
	String username = authentication.getName();
	String pwd = authentication.getCredentials().toString();

	UserDetails userDetails = userDetailsService.loadUserByUsername(username);

	if (passwordEncoder.matches(pwd, userDetails.getPassword()) && userDetails.isEnabled())
	    return new UsernamePasswordAuthenticationToken(username, pwd, userDetails.getAuthorities());
	else 
	    throw new BadCredentialsException("Invalid password or inactivated user.");
    }

    @Override
    public boolean supports(Class<?> authentication) {
	return (UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication));
    }
}