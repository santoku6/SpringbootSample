package com.raxn.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.raxn.entity.Service;

public interface ServiceRepository extends JpaRepository<Service, Integer>{
	
	
}
