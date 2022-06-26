package com.raxn.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.raxn.entity.DailyCashback;

@Repository
public interface DailyCashbackRepository extends JpaRepository<DailyCashback, Integer> {

	DailyCashback findByCode(String code);
	
	DailyCashback findByUsername(String username);
	
	/*
	@Query("FROM Coupons WHERE email is NULL AND status='active' AND ?1 between startDate And endDate")
	List<Coupons> findAllOffers(Date currentDate);
	
	@Query("SELECT code FROM Coupons coupons")
	List<String> findCodes();
	*/
	
	

}
