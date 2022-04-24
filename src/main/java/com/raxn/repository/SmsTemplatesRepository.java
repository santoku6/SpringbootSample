package com.raxn.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.raxn.entity.SmsTemplates;

public interface SmsTemplatesRepository extends JpaRepository<SmsTemplates, Integer>{
	
	SmsTemplates findByPurpose(String purpose);
	
	SmsTemplates findByTemplateName(String templateName);

}
