package com.smcontactm.config;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

// handles all entry of un auhtorized request n send 401 error
@Component
public class JWTAuthenticationEntryPoint implements AuthenticationEntryPoint {


	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException authException) throws IOException, ServletException {
		response.sendError(401,"unauthorized request please check");
		  //response.sendRedirect("http://localhost:6200/getLoginPage");
		 if (request != null) {
			 String url = ((HttpServletRequest)request).getRequestURL().toString();
			 String queryString = ((HttpServletRequest)request).getQueryString();
			 System.out.println("queryString"+queryString+"-->url"+url);
			}
		
		
	}

}
