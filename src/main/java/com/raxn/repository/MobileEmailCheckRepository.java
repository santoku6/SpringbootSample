package com.raxn.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.raxn.entity.MobileEmailCheck;

public interface MobileEmailCheckRepository extends JpaRepository<MobileEmailCheck, Integer>{
	
	MobileEmailCheck findByIdentifier(String identifier);
	
	@Query("FROM MobileEmailCheck WHERE identifier = ?1")
	MobileEmailCheck findByMobile(String identifier);
	
	@Query("FROM MobileEmailCheck WHERE identifier = ?1")
	MobileEmailCheck findByEmail(String identifier);
	
	@Transactional
	int deleteByIdentifier(String identifier);
	
	
	

}
