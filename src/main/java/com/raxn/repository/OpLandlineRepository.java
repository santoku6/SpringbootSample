package com.raxn.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.raxn.entity.OpLandline;

public interface OpLandlineRepository extends JpaRepository<OpLandline, Integer>{
	
	OpLandline findByOpKey(String opKey);
	
	OpLandline findByStatus(String status);
	

}
