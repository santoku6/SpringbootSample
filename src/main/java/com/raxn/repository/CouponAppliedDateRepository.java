package com.raxn.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.raxn.entity.CouponsAppliedDate;

@Repository
public interface CouponAppliedDateRepository extends JpaRepository<CouponsAppliedDate, Integer> {

	CouponsAppliedDate findByUsername(String username);
	
	/*
	 * @Query("FROM Coupons WHERE ?1 between startDate And endDate") List<Coupons>
	 * findAllOffers(Date startDate);
	 */
	
	
	
	

}
