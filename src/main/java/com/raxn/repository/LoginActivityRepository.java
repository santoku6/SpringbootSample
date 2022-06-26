package com.raxn.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.raxn.entity.LoginActivity;

public interface LoginActivityRepository extends JpaRepository<LoginActivity, Integer>{
	
	LoginActivity findByUsername(String username);
	

}
