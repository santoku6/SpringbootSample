package com.raxn.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.raxn.entity.OrdertableMoney;

public interface OrdertableMoneyRepository extends JpaRepository<OrdertableMoney, Integer>{
	
	OrdertableMoney findByOrderid(String orderid);
	
	OrdertableMoney findByUsername(String username);
	

}
