package com.smcontactm.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.smcontactm.entities.Contact;
import com.smcontactm.entities.User;

public class UserModel implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int id;
	
	@NotBlank(message = "Name cannot be blank !!")
	@Size(min = 2,max = 15, message = "Name must be 2 to 15 Charecters!!")
	private String name;
	
	@Pattern(regexp = "^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+$",message = "Invalid email !!")
	private String email;
	
	private String password;
	
	private String role;
	
	private boolean enabled; //user active or deactive
	
	private String imageUrl;
	
	@NotBlank(message = "Description cannot be blank !!")
	@Size(min = 2,max = 100, message = "Description must be 10 to 100 character !!")
	private String aboutUser;
	
	private List<Contact> contacts =  new ArrayList<Contact> ();
	
	private String city;
	
	private String state;
	
	private String phone;
	
	private String work;

	public UserModel() {
		super();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public String getAboutUser() {
		return aboutUser;
	}

	public void setAboutUser(String aboutUser) {
		this.aboutUser = aboutUser;
	}

	public List<Contact> getContacts() {
		return contacts;
	}

	public void setContacts(List<Contact> contacts) {
		this.contacts = contacts;
	}
	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	
	
	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getWork() {
		return work;
	}

	public void setWork(String work) {
		this.work = work;
	}



	@Override
	public String toString() {
		return "UserModel [id=" + id + ", name=" + name + ", email=" + email + ", password=" + password + ", role="
				+ role + ", enabled=" + enabled + ", imageUrl=" + imageUrl + ", aboutUser=" + aboutUser + ", contacts="
				+ contacts + ", city=" + city + ", state=" + state + ", phone=" + phone + ", work=" + work + "]";
	}

	public User toUserEntity(UserModel model) {
		User userEntity = new User();
		
		userEntity.setName(model.getName());
		userEntity.setEnabled(model.isEnabled());
		userEntity.setAboutUser(model.getAboutUser());
		userEntity.setImageUrl(model.getImageUrl());
		userEntity.setEmail(model.getEmail());
		userEntity.setRole(model.getRole());
		userEntity.setPassword(model.getPassword());
		userEntity.setCity(model.getCity());
		userEntity.setState(model.getState());
		userEntity.setPhone(model.getPhone());
		userEntity.setWork(model.getWork());
		
		return userEntity;
	}
	

}
