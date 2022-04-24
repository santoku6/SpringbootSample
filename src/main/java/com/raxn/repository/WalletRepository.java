package com.raxn.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.raxn.entity.User;
import com.raxn.entity.Wallet;

public interface WalletRepository extends JpaRepository<Wallet, Integer>{
	
	
	User findById(String id);	
	User findByUserid(String userid);
	
	
	
	
}
