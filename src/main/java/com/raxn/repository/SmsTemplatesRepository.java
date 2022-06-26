package com.raxn.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.raxn.entity.SmsTemplates;

public interface SmsTemplatesRepository extends JpaRepository<SmsTemplates, Integer>{
	
	SmsTemplates findByPurpose(String purpose);
	
	SmsTemplates findByTemplateName(String templateName);
	
	SmsTemplates findByTemplateId(String templateId);
	
	//@Query("FROM SmsTemplates ORDER BY id")
	List<SmsTemplates> findAllByOrderByIdAsc();
	
	SmsTemplates findById(int id);

}
