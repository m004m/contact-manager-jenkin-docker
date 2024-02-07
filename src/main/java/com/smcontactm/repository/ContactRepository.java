package com.smcontactm.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.smcontactm.entities.Contact;

import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;


public interface ContactRepository  extends JpaRepository<Contact, Integer> {
	
	//find list of conats using userID
	@Query("select c from Contact c where c.user.id =:userId")
	public List<Contact> findContactsByUser(@Param("userId") int userId);
	
	/**
	 * added this repository to get pagination also Page is sublist of object returns page with contacts ,
	 * Pageable store pagination information conatains current page and per page contact
	 */
	@Query("select c from Contact c where c.user.id =:userId")
	public Page<Contact> findContactsByUserPagination(@Param("userId") int userId,Pageable pageable);
	
	
	@Query("select count(*) from Contact c where c.user.id =:userId")
	public int getCountContactsByUser(@Param("userId") int userId);


}
