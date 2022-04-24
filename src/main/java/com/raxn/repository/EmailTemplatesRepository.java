package com.raxn.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.raxn.entity.EmailTemplates;

public interface EmailTemplatesRepository extends JpaRepository<EmailTemplates, Integer>{
	
	EmailTemplates findByPurpose(String purpose);
	

}
