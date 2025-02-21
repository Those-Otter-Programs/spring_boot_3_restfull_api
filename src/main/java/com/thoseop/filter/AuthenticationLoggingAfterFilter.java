package com.thoseop.filter;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AuthenticationLoggingAfterFilter implements Filter {

    /**
     * @param request the...request... 
     * @param response the...response...
     * @param chain must be called at the end of the method in order to execute the next filter
     *    	in the chain.
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
	    throws IOException, ServletException {

	Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

	if (null != authentication) {
	    log.info("User {} successfully authentication with the following authorities ", 
		    authentication.getName() , authentication.getAuthorities().toString());
	}

	chain.doFilter(request, response);
    }

}
