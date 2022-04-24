package com.raxn.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.raxn.entity.OpFastag;

public interface OpFastagRepository extends JpaRepository<OpFastag, Integer>{
	
	OpFastag findByOpKey(String opKey);
	
	OpFastag findByStatus(String status);
	

}
