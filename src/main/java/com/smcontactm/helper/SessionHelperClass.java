package com.smcontactm.helper;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.smcontactm.entities.User;

@Component
public class SessionHelperClass {
	
	//Method return loggedin user
	public static User getUserFromSession(HttpSession session) {
		User usersession = (User) session.getAttribute("usersession");
		usersession.setPassword(null);
		return usersession;
	}
	
	
	
	public void removeMessageFromSession() {
		try {
			System.out.println("Removing message from session");
			
			ServletRequestAttributes attribute = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
			HttpSession session = attribute.getRequest().getSession();
			
			session.removeAttribute("message");
		}catch (Exception e) {
			e.printStackTrace();
		}
	}

}
