package com.raxn.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.raxn.entity.OpDth;

public interface OpDthRepository extends JpaRepository<OpDth, Integer>{
	
	OpDth findByOpKey(String opKey);
	
	OpDth findByStatus(String status);
	

}
