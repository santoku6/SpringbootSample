package com.raxn.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.raxn.entity.UserCouponCashbackTimeline;

@Repository
public interface UserCouponCashbackTimelineRepository extends JpaRepository<UserCouponCashbackTimeline, Integer> {

	UserCouponCashbackTimeline findByUserid(String userid);

	UserCouponCashbackTimeline findByUseridAndDate01(String userid, String date01);

	UserCouponCashbackTimeline findByUseridAndDate02(String userid, String date02);

	UserCouponCashbackTimeline findByUseridAndDate03(String userid, String date03);

	UserCouponCashbackTimeline findByUseridAndDate04(String userid, String date04);

	UserCouponCashbackTimeline findByUseridAndDate05(String userid, String date05);

	UserCouponCashbackTimeline findByUseridAndDate06(String userid, String date06);

	UserCouponCashbackTimeline findByUseridAndDate07(String userid, String date07);

	UserCouponCashbackTimeline findByUseridAndDate08(String userid, String date08);

	UserCouponCashbackTimeline findByUseridAndDate09(String userid, String date09);

	UserCouponCashbackTimeline findByUseridAndDate10(String userid, String date10);

	UserCouponCashbackTimeline findByUseridAndDate11(String userid, String date11);

	UserCouponCashbackTimeline findByUseridAndDate12(String userid, String date12);

	UserCouponCashbackTimeline findByUseridAndDate13(String userid, String date13);

	UserCouponCashbackTimeline findByUseridAndDate14(String userid, String date14);

	UserCouponCashbackTimeline findByUseridAndDate15(String userid, String date15);

	UserCouponCashbackTimeline findByUseridAndDate16(String userid, String date16);

	UserCouponCashbackTimeline findByUseridAndDate17(String userid, String date17);

	UserCouponCashbackTimeline findByUseridAndDate18(String userid, String date18);

	UserCouponCashbackTimeline findByUseridAndDate19(String userid, String date19);

	UserCouponCashbackTimeline findByUseridAndDate20(String userid, String date20);

	UserCouponCashbackTimeline findByUseridAndDate21(String userid, String date21);

	UserCouponCashbackTimeline findByUseridAndDate22(String userid, String date22);

	UserCouponCashbackTimeline findByUseridAndDate23(String userid, String date23);

	UserCouponCashbackTimeline findByUseridAndDate24(String userid, String date24);

	UserCouponCashbackTimeline findByUseridAndDate25(String userid, String date25);

	UserCouponCashbackTimeline findByUseridAndDate26(String userid, String date26);

	UserCouponCashbackTimeline findByUseridAndDate27(String userid, String date27);

	UserCouponCashbackTimeline findByUseridAndDate28(String userid, String date28);

	UserCouponCashbackTimeline findByUseridAndDate29(String userid, String date29);

	UserCouponCashbackTimeline findByUseridAndDate30(String userid, String date30);
	
	UserCouponCashbackTimeline findByUseridAndDate31(String userid, String date31);

}
