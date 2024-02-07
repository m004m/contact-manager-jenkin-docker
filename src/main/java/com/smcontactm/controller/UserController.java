package com.smcontactm.controller;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.time.Clock;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import com.smcontactm.service.CloudinaryImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.smcontactm.entities.Contact;
import com.smcontactm.entities.User;
import com.smcontactm.helper.MessageClass;
import com.smcontactm.model.UserModel;
import com.smcontactm.repository.ContactRepository;
import com.smcontactm.repository.UserRepository;

@Controller
@RequestMapping("/user")
public class UserController {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private ContactRepository contactRepository;

	@Autowired
	private CloudinaryImageService cloudinaryImageService;
	
	//@Autowired
	//private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	/*
	 * after adding the model attribute annotaion 
	 * this method get called every time wehn request comes 
	 * for example /index, /add-contact 
	 * */
	@ModelAttribute 
	public void getCommenData(Model model,Principal principal,HttpSession session) {
		
		//User usersession = SessionHelperClass.getUserFromSession(session);
		//System.out.println("usersession" + usersession);
		
		User user = userRepository.getUserByUserName(principal.getName());
		//creating dummy conatct to show in user dashboard
		List<Contact> contacts =  new ArrayList<>();
		//contacts.add(new Contact(1, "rahul", "grag", "Bank Employee", "rahul.grag@gmail.com", "9123678954", "slider1.jpg", "I am associte manager at SBI bank", user));
		//contacts.add(new Contact(2, "vinaya", "patil", "Advocate", "patil.vinayak@gmail.com", "9823678920", "slider2.jpg", "I am Advocate in dharwad jurdition", user));
		user.setContacts(contacts);
		
		model.addAttribute("user", user);
	}

	@GetMapping("/index")
	public String getDashBoard(Model model,Principal principal,HttpSession session) {
		
		System.out.println("principle value setted :"+principal.getName());
		
		model.addAttribute("title","User - Dashboard Page");
		model.addAttribute("pageName","User-Dashboard");
		
	//	boolean sendEmail = EmailSenderClass.sendEmail("mahi.m004@gmail.com");
		
	//	System.out.println("Email Sent -->"+sendEmail);
		
		return "normal/user_dashboard";
	}
	
	
	@GetMapping("/add-conact")
	public String openAddConatctForm(Model model) {
		
		model.addAttribute("title","User - Add-Contact Page");
		model.addAttribute("pageName","Add-Contact");
		model.addAttribute("pageHeading","Add Contact");
		model.addAttribute("updateCurPageNo", 0); // to solve  bug add contact updateCurPageNo coming as null
		
		model.addAttribute("contact", new Contact());
		return "normal/add_conact_form";
	}
	
	//process-addconact save and update contact form
	@PostMapping("/process-addconact")
	public String doRegister(@Valid @ModelAttribute  Contact contactModel,BindingResult result,
							 @RequestParam("currentPageNo") String currentPageNo,
							 @RequestParam("cId") Integer cId,
							 @RequestParam("image") MultipartFile file,Principal principal,Model model,HttpSession session) throws IOException {


		try (Reader reader = new InputStreamReader(file.getInputStream())){

			//final String path_dic = "C:\\Users\\91889\\Documents\\STS-NEW\\smart-contactmanger\\src\\main\\resources\\static\\db-images"+File.separator;
			//String orginalFileNameWithTime ="";
			User user = this.userRepository.getUserByUserName(principal.getName());
			Contact oldContact = null;

			// for update case
			if(cId != null && cId != 0) {
				Optional<Contact> optionalContact = this.contactRepository.findById(cId);
				if(optionalContact.isPresent()) {
					oldContact = optionalContact.get();
				}
				contactModel.setcId(cId);
			}


			//file processing code first save file then save user details
			if(!file.isEmpty()) {
				//		Clock clock = Clock.systemDefaultZone();
				//		long milliSeconds=clock.millis();
				//		System.out.println(milliSeconds);
				//		orginalFileNameWithTime = milliSeconds+"-SCM"+user.getId()+"-"+file.getOriginalFilename();
				//		contactModel.setImage(orginalFileNameWithTime);

				//new Way
				//     File saveFileAbsPath = new ClassPathResource("static/images").getFile(); //gives abs path till static/db-images
				//     if(oldContact != null && !oldContact.getImage().isEmpty() && !oldContact.getImage().equalsIgnoreCase("contact.png")) {// for update case delete old file
				//			File deleteFile = new File(saveFileAbsPath, oldContact.getImage());
				//			deleteFile.delete();
				//		}
				//       Path path = Paths.get(saveFileAbsPath.getAbsolutePath()+File.separator+orginalFileNameWithTime);//creating path abspath+filename to write file to with Oeg img name adding miisec for unique
				//       Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);//file will copy specied directory with orginalnane+milsec

				if(oldContact != null && !oldContact.getImage().isEmpty() && !oldContact.getImage().equalsIgnoreCase("contact.png")
						&& oldContact.getCloudinaryImgToken() != null) {// for update case delete old file
					this.cloudinaryImageService.delete(oldContact.getCloudinaryImgToken());

				}
				Map<?, ?> map = this.cloudinaryImageService.upload(file);
				if(!map.isEmpty() && map.get("secure_url") != null) {
					contactModel.setImage((String) map.get("secure_url"));
					contactModel.setCloudinaryImgToken((String) map.get("public_id"));
				}


			}else {
				System.out.println("File is empty please select the file");
				contactModel.setImage("contact.png");
				if(oldContact != null && !oldContact.getImage().isEmpty()) {
					contactModel.setImage(oldContact.getImage());
				}
				//session.setAttribute("message", new MessageClass("File is empty please select the file !!", "alert-danger"));
				//return "normal/add_conact_form";
			}

			contactModel.setUser(user);

			user.getContacts().add(contactModel);
			User userResult = this.userRepository.save(user);
			System.out.println(contactModel);
			if(userResult != null) {
				session.setAttribute("message", new MessageClass("Contact saved successfully !!", "alert-success"));
			}

			if(cId != null && cId != 0) {
				return "redirect:/user/show-conact/contact/"+cId+"/"+currentPageNo;
			}else {
				model.addAttribute("pageHeading","Add Contact");
				model.addAttribute("contact", new Contact());
			}

		} catch (Exception e) {
			e.printStackTrace();
			session.setAttribute("message", new MessageClass("Something went Wrong !!", "alert-danger"));
		}

		return "normal/add_conact_form";

	}
	
	//get contacts list with pagination
	//perpage rows or data
	//current page - pageNo of current page
	@GetMapping("/show-conact/{currentPageNo}")
	public String showConatcs(@PathVariable("currentPageNo") Integer currentPageNo, Model model,Principal principal) {
		
		try {
			
			model.addAttribute("title","User - Show-Contact Page");
			model.addAttribute("pageName","Show-Contact");
			
			User user = userRepository.getUserByUserName(principal.getName());
			
			Integer perPage = 2;
			
			if(user != null) {
				//List<Contact> listContacts = this.contactRepository.findContactsByUser(user.getId());
				
				//conatains curent page and perpage data
				Pageable pageable = PageRequest.of(currentPageNo, perPage);
				
				int totlContact = this.contactRepository.getCountContactsByUser(user.getId());
				
				Page<Contact> listContacts =  this.contactRepository.findContactsByUserPagination(user.getId(), pageable);
				
				
				
				model.addAttribute("listContacts", listContacts);
				
				
				model.addAttribute("currentPage", currentPageNo);
				model.addAttribute("totalPages", listContacts.getTotalPages());
				model.addAttribute("perPage", perPage);
				
				if(listContacts.getContent().isEmpty() && totlContact !=0) {
					return "redirect:/user/show-conact/0";
				}
				 
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return "normal/show_conatcs_form";
	}
	
	//get single contacts
	@GetMapping("/show-conact/contact/{cId}/{currentPageNo}")
	public String getSingleContact(@PathVariable("cId") Integer cId,@PathVariable("currentPageNo") Integer currentPageNo,
			Model model,Principal principal, HttpSession session) {
		
		try {
			
			model.addAttribute("title","Single Contact - Show-Contact Page");
			model.addAttribute("pageName","Get-Contact");
			
			
			if(cId != null) {
				
				User user = userRepository.getUserByUserName(principal.getName());
				
				Optional<Contact> optionalContact = this.contactRepository.findById(cId);
				
				if(optionalContact.isPresent() && optionalContact.get() != null) {
					
					Contact contact = optionalContact.get();
					
					if(contact.getUser().getId() == user.getId()) {
						model.addAttribute("singleContact", contact);
						model.addAttribute("curPageNo", currentPageNo);
					}else {
						session.setAttribute("message", new MessageClass("Your not authorized to view this page !!", "alert-danger"));
					}
				}else {
					session.setAttribute("message", new MessageClass("user not found !!", "alert-danger"));
				}
				
				
				
				
			}
			
		} catch (Exception e) {
			session.setAttribute("message", new MessageClass("something went wrong !!", "alert-danger"));
		}
		
		return "normal/show_single_conatcs_form";
	}
	
	
	@GetMapping("/show-conact/delete-contact/{cId}/{currentPageNo}")
	public String deleteContact(@PathVariable("cId") Integer cId,@PathVariable("currentPageNo") Integer currentPageNo, Model model,Principal principal,HttpSession session) {
		
		try {
			
			model.addAttribute("title","Show Contact - Show-Contact Page");
			model.addAttribute("pageName","Get-Contact");
			
			
			if(cId != null) {
				
				User user = userRepository.getUserByUserName(principal.getName());
				
				Optional<Contact> optionalContact = this.contactRepository.findById(cId);
				
				Contact contact = optionalContact.get();
				
				if(contact.getUser().getId() == user.getId()) {
					
					contact.setUser(null);
					
					this.contactRepository.delete(contact);
					
					session.setAttribute("message", new MessageClass("Contact deleted successfully !!", "alert-success"));
					
					return "redirect:/user/show-conact/"+currentPageNo; 
				}
				
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return "redirect:/user/show-conact/"+currentPageNo; 
	}
	
	
	// to get update form for updation
	@GetMapping("/contact/update-contact/{cId}/{currentPageNo}")
	public String getUpdateContactForm(@PathVariable("cId") Integer cId,
			@PathVariable("currentPageNo") Integer currentPageNo, 
			@RequestParam ("pageName") String pageName,
			Model model,Principal principal,
			HttpSession session) {
		
		String pageView = "normal/show_single_conatcs_form";
		
		try {
			
			model.addAttribute("title","Update Contact - Update-Contact Page");
			model.addAttribute("pageName","Update-Contact");
			
			
			if(cId != null) {
				
				User user = userRepository.getUserByUserName(principal.getName());
				
				Optional<Contact> optionalContact = this.contactRepository.findById(cId);
				
				Contact contact = optionalContact.get();
				
				if(contact.getUser().getId() == user.getId()) {
					if("profileEdit".equalsIgnoreCase(pageName)) {
						model.addAttribute("contact", contact);
						model.addAttribute("pageHeading","Update Contact");
						pageView = "normal/add_conact_form";
					}else {
						model.addAttribute("singleContact", contact);
					}
					model.addAttribute("updateCurPageNo", currentPageNo);
				}
				else {
					session.setAttribute("message", new MessageClass("Your not authorized to view/edit this page !!", "alert-danger"));
					return "normal/show_single_conatcs_form";
				}
				
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return pageView;
	}
	
	
	// to get user profile page
	@GetMapping("/user-profile")
	public String getUserProfile(Model model,Principal principal,HttpSession session) {
		
		try {
			
			model.addAttribute("title","profile - User-Profile Page");
			model.addAttribute("pageName","User Profile");
			
			User user = userRepository.getUserByUserName(principal.getName());
				
			model.addAttribute("user", user);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return "normal/user_profile.html";
	}
	
	// to get user profile setting page
		@GetMapping("/user-setting")
		public String getUserSetting(Model model,Principal principal,HttpSession session) {
			
			try {
				
				model.addAttribute("title","profile - User-Setting Page");
				model.addAttribute("pageName","User Setting");
				
				User user = userRepository.getUserByUserName(principal.getName());
					
				model.addAttribute("user", user);
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			return "normal/user-setting";
		}
		
		
		//update user
		@PostMapping("/updateUserProfile")
		public String updateUserProfile(@Valid @ModelAttribute("usermodel") UserModel userModel,
				BindingResult result,
				@RequestParam("imageUrl") MultipartFile file,
				Principal principal,
				Model model,HttpSession session) {



			try {
				System.out.println(userModel);
				model.addAttribute("title","Update -User Page");
				model.addAttribute("pageName","Update-User");

				User savedUser = userRepository.getUserByUserName(principal.getName());


				if(userModel.getCity() != null && !userModel.getCity().isEmpty()) {
					savedUser.setCity(userModel.getCity());
				}
				if(userModel.getState() != null && !userModel.getState().isEmpty() ) {
					savedUser.setState(userModel.getState());
				}
				if(userModel.getPhone() != null && !userModel.getPhone().isEmpty()) {
					savedUser.setPhone(userModel.getPhone());
				}
				if(userModel.getWork() != null && !userModel.getWork().isEmpty()) {
					savedUser.setWork(userModel.getWork());
				}

				//userModel.setPassword(bCryptPasswordEncoder.encode(userModel.getPassword()));

				String orginalFileNameWithTime = "";
				if(!file.isEmpty()) {
					//Clock clock = Clock.systemDefaultZone();
					//long milliSeconds=clock.millis();
					//System.out.println(milliSeconds);
					//orginalFileNameWithTime = milliSeconds+"-SCM-U"+userModel.getId()+"-"+file.getOriginalFilename();
					//savedUser.setImageUrl(orginalFileNameWithTime);

					//new Way
					//File saveFileAbsPath = new ClassPathResource("static/images").getFile(); //gives abs path till static/db-images
					//if(!savedUser.getImageUrl().isEmpty() && !savedUser.getImageUrl().equalsIgnoreCase("contact.png")) {// for update case delete old file
					//	File deleteFile = new File(saveFileAbsPath, savedUser.getImageUrl());
					//	deleteFile.delete();
					//}
					//Path path = Paths.get(saveFileAbsPath.getAbsolutePath()+File.separator+orginalFileNameWithTime);//creating path abspath+filename to write file to with Oeg img name adding miisec for unique
					//Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);//file will copy specied directory with orginalnane+milsec

					//saving image file in the cloud couldniary
					Map<?, ?> map = this.cloudinaryImageService.upload(file);
					if(!map.isEmpty() && map.get("secure_url") != null) {
						savedUser.setImageUrl((String) map.get("secure_url"));
					}


				}else {
					System.out.println("File is empty please select the file");

					//session.setAttribute("message", new MessageClass("File is empty please select the file !!", "alert-danger"));
					//return "normal/add_conact_form";
				}

					/*

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
					} */

				User userResult = this.userRepository.save(savedUser);
				if(userResult != null) {
					session.setAttribute("message", new MessageClass("Successfully updated !!", "alert-success"));
				}
				model.addAttribute("user",userResult);
				return "normal/user-setting";

			} catch (Exception e) {
				e.printStackTrace();
				model.addAttribute("user",userModel);
				session.setAttribute("message", new MessageClass("Something went Wrong !!", "alert-danger"));
				return "normal/user-setting";
			}


		}
	
}
