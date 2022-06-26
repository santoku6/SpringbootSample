package com.raxn.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.raxn.entity.OrdertableRecharge;

public interface OrdertableRechargeRepository extends JpaRepository<OrdertableRecharge, Integer>{
	
	OrdertableRecharge findByOrderid(String orderid);
	
	OrdertableRecharge findByUsername(String username);
	

}
