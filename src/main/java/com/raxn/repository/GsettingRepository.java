package com.raxn.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.raxn.entity.Gsetting;

public interface GsettingRepository extends JpaRepository<Gsetting, Integer>{
	
	Gsetting findById(int id);
	

}
