package com.smcontactm.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.smcontactm.config.UserDetailsServiceImpl;
import com.smcontactm.entities.User;
import com.smcontactm.helper.JWTUtilHelper;
import com.smcontactm.model.JWTResponse;

@RestController
@RequestMapping("/api")
public class JwtController {
	
	@Autowired
	private UserDetailsServiceImpl userDetailsServiceImpl;
	
	@Autowired
	private JWTUtilHelper jwtUtilHelper;
	
	@Autowired
	private DaoAuthenticationProvider daoAuthenticationProvider;
//	@Autowired 
//	private AuthenticationManager authenticationManager;
	 
	@PostMapping("/getToken")	
	public ResponseEntity<JWTResponse> generateToken(@RequestBody User user) throws Exception {
		
		try {
			
			this.daoAuthenticationProvider.authenticate(new UsernamePasswordAuthenticationToken(user.getEmail(),user.getPassword()));
			
			//this.authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getEmail(),user.getPassword()));
			
		} catch (UsernameNotFoundException e) {
			e.printStackTrace();
			//throw new Exception("User name not Found");
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new JWTResponse(null, e.getMessage()));
			
		} catch (BadCredentialsException e) {
			e.printStackTrace();
			//throw new Exception("Wrong User Name and password");
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new JWTResponse(null, e.getMessage()));
		}
		
		//if authentication is done
		
		UserDetails userDetails = this.userDetailsServiceImpl.loadUserByUsername(user.getEmail());
		
		//generate token here
		String generateToken = this.jwtUtilHelper.generateToken(userDetails);
		System.out.println(generateToken);
		return ResponseEntity.status(HttpStatus.CREATED).body(new JWTResponse(generateToken,"Successfuly genarated token"));
	}
	
	
 	
	@GetMapping("/welcome/jwt")
	public String welcome() {
		
		return "This page is not for un authorized user";
	}
}
