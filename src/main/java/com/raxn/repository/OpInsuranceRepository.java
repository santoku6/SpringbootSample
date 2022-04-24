package com.raxn.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.raxn.entity.OpInsurance;

public interface OpInsuranceRepository extends JpaRepository<OpInsurance, Integer>{
	
	OpInsurance findByOpKey(String opKey);
	
	OpInsurance findByStatus(String status);
	

}
