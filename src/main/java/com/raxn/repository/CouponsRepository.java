package com.raxn.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.raxn.entity.Coupons;

@Repository
public interface CouponsRepository extends JpaRepository<Coupons, Integer> {

	List<Coupons> findByCode(String code);
	
	@Query("FROM Coupons WHERE email is NULL AND status='active' AND ?1 between startDate And endDate")
	List<Coupons> findAllOffers(Date currentDate);
	
	
	
	
	
	

}
