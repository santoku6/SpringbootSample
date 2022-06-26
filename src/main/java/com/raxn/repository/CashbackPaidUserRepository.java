package com.raxn.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.raxn.entity.CashbackPaidUser;

@Repository
public interface CashbackPaidUserRepository extends JpaRepository<CashbackPaidUser, Integer> {
	
	
	CashbackPaidUser findByUsername(String username);

	

}
