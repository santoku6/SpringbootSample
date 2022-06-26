package com.raxn.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import com.raxn.entity.JwtTokentable;

public interface jwtTokentableRepository extends JpaRepository<JwtTokentable, Integer>{
	
	JwtTokentable findByUsername(String username);
	
	@Transactional
	int deleteByUsername(String username);
	

}
