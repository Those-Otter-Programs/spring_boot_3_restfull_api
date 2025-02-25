package com.thoseop.config.security;

import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
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

	// CHECK IF USERNAME EXISTS AND THROWS UsernameNotFoundException IF NOT 
	UserDetails userDetails = userDetailsService.loadUserByUsername(username);

	// CHECK IF ACCOUNT EXPIRED
	if (!userDetails.isAccountNonExpired())
	    throw new AccountExpiredException("User account is expired");
	// CHECK IF ACCOUNT NON-LOCKED
	if (!userDetails.isAccountNonLocked()) 
	    throw new LockedException("User account is locked");
	// CHECK IF CREDENTIALS NON-EXPIRED
	if (!userDetails.isCredentialsNonExpired())
	    throw new CredentialsExpiredException("User credentials are expired");
	// CHECK IF ACCOUNT ENABLED
	if (!userDetails.isEnabled())
	    throw new DisabledException("User account is disabled");

	// CHECK IF PASSWORD IS CORRECT
	if (passwordEncoder.matches(pwd, userDetails.getPassword()))
	    return new UsernamePasswordAuthenticationToken(username, pwd, userDetails.getAuthorities());
	else 
	    throw new BadCredentialsException("User provided invalid password.");
    }

    @Override
    public boolean supports(Class<?> authentication) {
	return (UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication));
    }
}