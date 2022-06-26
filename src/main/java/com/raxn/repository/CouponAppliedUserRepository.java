package com.raxn.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.raxn.entity.CouponAppliedUser;

@Repository
public interface CouponAppliedUserRepository extends JpaRepository<CouponAppliedUser, Integer> {

	CouponAppliedUser findByUsername(String username);
	
	/*
	 * @Query("FROM Coupons WHERE ?1 between startDate And endDate") List<Coupons>
	 * findAllOffers(Date startDate);
	 */
	
	
	
	

}
