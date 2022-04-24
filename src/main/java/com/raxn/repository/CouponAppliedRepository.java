package com.raxn.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.raxn.entity.CouponApplied;

@Repository
public interface CouponAppliedRepository extends JpaRepository<CouponApplied, Integer> {

	CouponApplied findByUserid(String userid);
	
	/*
	 * @Query("FROM Coupons WHERE ?1 between startDate And endDate") List<Coupons>
	 * findAllOffers(Date startDate);
	 */
	
	
	
	

}
