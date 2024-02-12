package com.smcontactm.controller;



import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import com.smcontactm.model.AuthResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.smcontactm.entities.Contact;
import com.smcontactm.entities.User;
import com.smcontactm.helper.MessageClass;
import com.smcontactm.model.UserModel;
import com.smcontactm.repository.UserRepository;



//@EnableOAuth2Sso
@Controller
public class HomeController {

	private final Logger logger = LoggerFactory.getLogger(HomeController.class);
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@GetMapping("/getHome")
	public String getHomePage1(Model model) {
		
		User user = new User();
		user.setName("Durgesh23");
		user.setEmail("durgesh23@gmail.com");
		
		Contact c = new Contact();
		c.setName("coantact1");
		c.setPhone("3434334");
		
		user.getContacts().add(c);
		
		User save = userRepository.save(user);
		
		
		model.addAttribute("user", save);
		model.addAttribute("name","Durgi name");
		return "home";
	}
	
	@GetMapping("/")
	public String getHomePage(Model model) {
		
		model.addAttribute("title","Home -Conatct Manger Page");
		model.addAttribute("pageName","Home");
		model.addAttribute("homeText","This project is about storing ang getting the contacts in the smarter way !!!");
		return "home";
	}
	
	
	@GetMapping("/getSignupPage")
	public String getSignupPage(Model model) {
		
		model.addAttribute("title","Register -Conatct Manger Page");
		model.addAttribute("pageName","Register");
		model.addAttribute("user", new User());
		//model.addAttribute("homeText","This project is about storing ang getting the contacts in the smarter way !!!");
		return "signup";
	}
	
	
	@GetMapping("/getAboutPage")
	public String getAboutPage(Model model) {
		
		model.addAttribute("title","About -Conatct Manger Page");
		model.addAttribute("name","Durgi About name");
		return "about";
	}
	
	//doRegister
	@PostMapping("/doRegister")
	public String doRegister(@Valid @ModelAttribute("usermodel") UserModel userModel,BindingResult result,@RequestParam(value = "agreement", defaultValue = "false") boolean agreement, 
			Model model,HttpSession session) {
		
		try {
			System.out.println("Agreement "+agreement);
			System.out.println(userModel);
			model.addAttribute("title","Register -Conatct Manger Page");
			model.addAttribute("pageName","Register");
			
			userModel.setRole("ROLE_USER");
			userModel.setEnabled(true);
			userModel.setImageUrl("contact.png");
			if(userModel.getCity() == null || userModel.getCity().isEmpty()) {
				userModel.setCity("UNKNOWN");
			}
			if(userModel.getState() == null || userModel.getState().isEmpty() ) {
				userModel.setState("UNKNOWN");
			}
			userModel.setPassword(bCryptPasswordEncoder.encode(userModel.getPassword()));
			
			if(!agreement) {
				System.out.println("Please do Agree Terms and conditions !!");
				//throw new Exception("Please do Agree Terms and conditions !!");
				model.addAttribute("user",userModel);
				session.setAttribute("message", new MessageClass("Please do Agree Terms and conditions !!", "alert-danger"));
				return "signup";
			}
			
			if(result.hasErrors()) {
				System.out.println("Error CustThymeleaf"+result.toString());
				model.addAttribute("user",userModel);
				String errorMsg = "";
				Map<String, Object> fielderror = new HashMap<>();
				List<FieldError> errorsList= result.getFieldErrors();
				for (FieldError error : errorsList) {
					fielderror.put(error.getField(), error.getDefaultMessage());
					errorMsg = errorMsg + error.getDefaultMessage();
				}
				
				model.addAttribute("user",userModel);
				session.setAttribute("message", new MessageClass(errorMsg, "alert-danger"));
				
				return  "signup";
			}
			
			User userResult = this.userRepository.save(userModel.toUserEntity(userModel));
			if(userResult != null) {
				session.setAttribute("message", new MessageClass("Successfully Registered !!", "alert-success"));
			}
			model.addAttribute("user",new UserModel());
			return "signup";
			
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("user",userModel);
			session.setAttribute("message", new MessageClass("Something went Wrong !!", "alert-danger"));
			return "signup";
		}
		
	}


	@GetMapping("/getLoginPage")
	public String getLoginPage(Model model) {
		
		model.addAttribute("title","Login -Conatct Manger Page");
		model.addAttribute("name","Durgi About name");
		return "login";
	}

	@GetMapping("/login")
	public ResponseEntity<AuthResponse> login(
			@RegisteredOAuth2AuthorizedClient("okta") OAuth2AuthorizedClient client,
			@AuthenticationPrincipal OidcUser user,
			Model model
	){

		logger.info("user email id : {}",user.getEmail());

		AuthResponse authResponseBuilder = AuthResponse.builder()
				.userId(user.getEmail())
				.accessToken(client.getAccessToken().getTokenValue())
				.refreshToken(Objects.requireNonNull(client.getRefreshToken()).getTokenValue())
				.expireAt(Objects.requireNonNull(client.getAccessToken().getExpiresAt()).getEpochSecond())
				.authorities(user.getAuthorities().stream().map(GrantedAuthority::getAuthority
				).collect(Collectors.toList())).build();

		logger.info("Auth Response console: {}",authResponseBuilder);
//		if(authResponseBuilder.getUserId() != null){
//			return "redirect:/user/index";
//		}
		return new ResponseEntity<>(authResponseBuilder, HttpStatus.OK);
	}
	
}
