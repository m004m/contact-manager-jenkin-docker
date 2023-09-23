package com.smcontactm.helper;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.stereotype.Component;


@Component
public class EmailSenderClass {
	
	
	//method to send the email
	public static boolean sendEmail(String toEmail) {
		
		final String message = "Hello your email is registered with Conacta Manager Please check";
		final String subject = "REGISTRATION FOR CONTACT MAAGER";
		final String from = "mahir.m004@gmail.com";
		String to = toEmail;  //"mahir.m004@gmail.com";
		
		boolean isMsgSent = false;
		String host = "smtp.gmail.com"; // this is  gmail host address from which we are sending mail
		
		
		Properties properties = System.getProperties();
		//setting info to the property object
		properties.put("mail.smtp.host", host);
		properties.put("mail.smtp.port", "465");
		properties.put("mail.smtp.ssl.enable", "true");
		properties.put("mail.smtp.auth", "true");
		
		//step 1 get the session object
		Session session = Session.getInstance(properties, new Authenticator() {
			
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				// TODO Auto-generated method stub
				return new PasswordAuthentication("mahir.m004@gmail.com", "Mahi@123654");
			}
			
		});
		
		
		session.setDebug(true); // to uneable the debugging
		
		//step2 compose the message[text, multimedia]
		MimeMessage mimeMessage = new MimeMessage(session);
		
		try {
			
			// from mail
			mimeMessage.setFrom(from);
			
			//reciepient (to)  can send to one address or array of address
			
			mimeMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
			
			//addiing subject to messgae
			mimeMessage.setSubject(subject);
			
			//Adding text to message  it can be multi part anything set accordingcolly
			mimeMessage.setText(message);
			
			
			//Step 3 send message using Transport class message;
			Transport.send(mimeMessage);
			
			isMsgSent = true;
			
			
		}catch (Exception e) {
			isMsgSent = false;
			e.printStackTrace();
		}
		
		return isMsgSent;
	}

}
