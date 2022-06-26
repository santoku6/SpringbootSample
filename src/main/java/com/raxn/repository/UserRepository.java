package com.raxn.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.raxn.entity.User;

public interface UserRepository extends JpaRepository<User, Integer>{
	
	User findByMobile(String mobile);
	User findByEmail(String email);
	User findById(int id);
	
	@Query("FROM User WHERE email = ?1 OR mobile = ?1")
	User findByEmailOrMobile(String emailOrMobile);
	
	User findByUsername(String username);
	
	@Modifying
	@Transactional
	@Query(value="delete from User where email = ?1 OR mobile = ?1")
	void deleteByEmailOrMobile(String identifier);
	
	
}
