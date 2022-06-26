package com.raxn.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.raxn.entity.Coupons;

@Repository
public interface CouponsRepository extends JpaRepository<Coupons, Integer> {

	List<Coupons> findByCode(String code);
	
	@Query("FROM Coupons WHERE email is NULL AND status='active' AND ?1 between startDate And endDate")
	List<Coupons> findAllOffers(LocalDate currentDate);
	
	@Query("FROM Coupons WHERE startDate >= ?1 ORDER BY startDate DESC")
	List<Coupons> findAllCoupons4Display(LocalDate date);
	
	
	@Query("SELECT code FROM Coupons WHERE LOWER(type) LIKE LOWER('recurring') AND status='active' AND ?1 between startDate And endDate")
	List<String> findAllRecurringCodes(LocalDate currentDate);
	
	@Query("SELECT code FROM Coupons coupons")
	List<String> findCodes();
	
	@Query("FROM Coupons WHERE code= ?1")
	Coupons findByCodeSingle(String code);
	

}
