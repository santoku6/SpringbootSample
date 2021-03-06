package com.raxn.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.raxn.entity.Faq;

public interface FaqRepository extends JpaRepository<Faq, Integer>{
	
	List<Faq> findAllByOrderByIdAsc();
	
	Faq findById(int id);

}
