package com.raxn.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.raxn.entity.OpPostpaidMobile;

public interface OpPostpaidMobileRepository extends JpaRepository<OpPostpaidMobile, Integer>{
	
	OpPostpaidMobile findByOpKey(String opKey);
	
	OpPostpaidMobile findByStatus(String status);
	

}
