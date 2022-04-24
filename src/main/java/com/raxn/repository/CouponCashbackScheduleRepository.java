package com.raxn.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.raxn.entity.CouponCashbackSchedule;

@Repository
public interface CouponCashbackScheduleRepository extends JpaRepository<CouponCashbackSchedule, Integer> {

	CouponCashbackSchedule findByCouponCode(String couponCode);
	
	/*
	 * @Query("FROM Coupons WHERE ?1 between startDate And endDate") List<Coupons>
	 * findAllOffers(Date startDate);
	 */
	
	
	
	

}
