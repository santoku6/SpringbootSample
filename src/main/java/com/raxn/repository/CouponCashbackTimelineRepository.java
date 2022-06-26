package com.raxn.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.raxn.entity.CouponsCashbackTimeline;

@Repository
public interface CouponCashbackTimelineRepository extends JpaRepository<CouponsCashbackTimeline, Integer> {

	@Query("FROM CouponsCashbackTimeline")
	List<CouponsCashbackTimeline> findAll();
	
	CouponsCashbackTimeline findByDate01(String date01);
	CouponsCashbackTimeline findByDate02(String date02);
	CouponsCashbackTimeline findByDate03(String date03);
	CouponsCashbackTimeline findByDate04(String date04);
	CouponsCashbackTimeline findByDate05(String date05);
	CouponsCashbackTimeline findByDate06(String date06);
	CouponsCashbackTimeline findByDate07(String date07);
	CouponsCashbackTimeline findByDate08(String date08);
	CouponsCashbackTimeline findByDate09(String date09);
	CouponsCashbackTimeline findByDate10(String date10);
	CouponsCashbackTimeline findByDate11(String date11);
	CouponsCashbackTimeline findByDate12(String date12);
	CouponsCashbackTimeline findByDate13(String date13);
	CouponsCashbackTimeline findByDate14(String date14);
	CouponsCashbackTimeline findByDate15(String date15);
	CouponsCashbackTimeline findByDate16(String date16);
	CouponsCashbackTimeline findByDate17(String date17);
	CouponsCashbackTimeline findByDate18(String date18);
	CouponsCashbackTimeline findByDate19(String date19);
	CouponsCashbackTimeline findByDate20(String date20);
	CouponsCashbackTimeline findByDate21(String date21);
	CouponsCashbackTimeline findByDate22(String date22);
	CouponsCashbackTimeline findByDate23(String date23);
	CouponsCashbackTimeline findByDate24(String date24);
	CouponsCashbackTimeline findByDate25(String date25);
	CouponsCashbackTimeline findByDate26(String date26);
	CouponsCashbackTimeline findByDate27(String date27);
	CouponsCashbackTimeline findByDate28(String date28);
	CouponsCashbackTimeline findByDate29(String date29);
	CouponsCashbackTimeline findByDate30(String date30);
	CouponsCashbackTimeline findByDate31(String date31);
	
	
	/*
	 * @Query("FROM Coupons WHERE ?1 between startDate And endDate") List<Coupons>
	 * findAllOffers(Date startDate);
	 */
	
	
	
	

}
