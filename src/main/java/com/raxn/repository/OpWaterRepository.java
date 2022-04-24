package com.raxn.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.raxn.entity.OpWater;

public interface OpWaterRepository extends JpaRepository<OpWater, Integer>{
	
	OpWater findByOpKey(String opKey);
	
	OpWater findByStatus(String status);
	

}
