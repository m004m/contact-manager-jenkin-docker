package com.smcontactm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.smcontactm.entities.User;

public interface UserRepository extends JpaRepository<User, Integer> {

	@Query("select u from User u where u.email = :email") //here if give space  = : email error it should be = :email 
	public User getUserByUserName(@Param("email") String email);
	
//	public User findByEmail(String username);
}
