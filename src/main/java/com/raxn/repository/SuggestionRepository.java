package com.raxn.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.raxn.entity.Suggestion;

public interface SuggestionRepository extends JpaRepository<Suggestion, Integer>{
	
	Suggestion findByMobile(String mobile);
	Suggestion findByName(String name);
	List<Suggestion> findByStatus(String status);
	

}
