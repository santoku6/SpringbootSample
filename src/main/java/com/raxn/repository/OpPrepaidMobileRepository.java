package com.raxn.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.raxn.entity.OpPrepaidMobile;

public interface OpPrepaidMobileRepository extends JpaRepository<OpPrepaidMobile, Integer>{
	
	OpPrepaidMobile findByOpKey(String opKey);
	
	OpPrepaidMobile findByStatus(String status);
	

}
