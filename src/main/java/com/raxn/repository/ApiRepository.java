package com.raxn.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.raxn.entity.Api;

public interface ApiRepository extends JpaRepository<Api, Integer>{
	
	Api findByName(String name);
	

}
