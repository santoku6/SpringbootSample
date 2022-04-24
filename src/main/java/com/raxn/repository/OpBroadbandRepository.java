package com.raxn.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.raxn.entity.OpBroadband;

public interface OpBroadbandRepository extends JpaRepository<OpBroadband, Integer>{
	
	OpBroadband findByOpKey(String opKey);
	
	OpBroadband findByStatus(String status);
	

}
