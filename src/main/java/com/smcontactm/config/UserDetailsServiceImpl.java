package com.smcontactm.config;

import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.smcontactm.entities.User;
import com.smcontactm.repository.UserRepository;



@Component
@Transactional
public class UserDetailsServiceImpl implements UserDetailsService {
	
	@Autowired
	private UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// fetching user from database
		User user = userRepository.getUserByUserName(username);
		
		//added code get session object and set user details in session
		ServletRequestAttributes attribute = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
		HttpSession session = attribute.getRequest().getSession();
		
		if(user ==  null) {
			throw new UsernameNotFoundException("User name not found please check enter correct username!!!");
		}
		//added code for null check contacts
		if(user.getContacts().isEmpty() || user.getContacts() ==  null) {
			user.setContacts(null);
		}
		session.setAttribute("usersession", user);
		System.out.println("Session user setted");
		
		CustomUserDetails cutomeUserDetails = new CustomUserDetails(user);
		
		return cutomeUserDetails;
	}

}
