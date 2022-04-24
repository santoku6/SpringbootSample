package com.raxn.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.raxn.entity.OpGiftcard;

public interface OpGiftcardRepository extends JpaRepository<OpGiftcard, Integer>{
	
	OpGiftcard findByOpKey(String opKey);
	
	OpGiftcard findByStatus(String status);
	

}
