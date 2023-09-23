package com.smcontactm.config;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.smcontactm.helper.JWTUtilHelper;

@Component
public class JWTAuthenticationFilter extends OncePerRequestFilter{

	@Autowired
	private UserDetailsServiceImpl userDetailsServiceImpl;
	
	@Autowired
	private JWTUtilHelper jwtUtilHelper;
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		//step 1 get jwt toekn
		//step 2 check it starts with bearer 
		//step 3 validate the token
		
		String requestHeader = request.getHeader("Authorization");
		
		//get username and jwt token from incoming header token 
		String username = null;
		String jwttoken = null;
		
			if(requestHeader != null && requestHeader.startsWith("Bearer")) { //null and token format check
			
			jwttoken = requestHeader.substring(7);
			
			try {
				username = jwtUtilHelper.extractUsername(jwttoken);
			}catch (Exception e) {
				e.printStackTrace();
			}
			
			//getuser details
			UserDetails userDetails = userDetailsServiceImpl.loadUserByUsername(username);
			
			//secuirty
			if(username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
				
				UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
				
				usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				
				SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
			}else {
				System.out.println("token not validated");
			}
			
			
		}
		
		filterChain.doFilter(request, response);
		
		
	}

}
