package com.raxn.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.raxn.entity.Admin;

public interface AdminRepository extends JpaRepository<Admin, Integer>{
	
	Admin findByUserName(String username);
	

}
