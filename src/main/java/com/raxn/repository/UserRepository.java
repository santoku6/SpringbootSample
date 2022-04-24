package com.raxn.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.raxn.entity.User;

public interface UserRepository extends JpaRepository<User, Integer>{
	
	User findByMobile(String mobile);
	User findByEmail(String email);
	User findById(int id);
	
	@Query("FROM User WHERE email = ?1 OR mobile = ?1")
	User findByEmailOrMobile(String emailOrMobile);
	
	@Query("FROM User WHERE email = ?1 OR mobile = ?1")
	User findByUsername(String username);
	
	
}
