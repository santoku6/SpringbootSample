package com.raxn.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.raxn.entity.OpGasAndCylinder;

public interface OpGasAndCylinderRepository extends JpaRepository<OpGasAndCylinder, Integer>{
	
	OpGasAndCylinder findByOpKey(String opKey);
	
	OpGasAndCylinder findByStatus(String status);
	

}
