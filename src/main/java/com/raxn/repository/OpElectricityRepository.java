package com.raxn.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.raxn.entity.OpElectricity;

public interface OpElectricityRepository extends JpaRepository<OpElectricity, Integer>{
	
	OpElectricity findByOpKey(String opKey);
	
	OpElectricity findByStatus(String status);
	

}
