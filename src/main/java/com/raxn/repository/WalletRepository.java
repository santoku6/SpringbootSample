package com.raxn.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.raxn.entity.Wallet;

public interface WalletRepository extends JpaRepository<Wallet, Integer>{
	
	
	Wallet findById(String id);	
	List<Wallet> findByUserid(String userid);
	
	
	
	
}
