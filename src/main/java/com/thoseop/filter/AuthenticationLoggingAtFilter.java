package com.thoseop.filter;

import jakarta.servlet.*;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
public class AuthenticationLoggingAtFilter implements Filter {

    /**
     * @param request  the...request...
     * @param response the...response...
     * @param chain have to be invoked along with doFilter(), in order for the next filter
     *  		in the chain to be executed.
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
	
	// there is no guarantee when this filter (At) will be excecuted, if before or
	// after the member authentication happen.
        log.info("Authentication in progress..");
        
        chain.doFilter(request,response);
    }
}
