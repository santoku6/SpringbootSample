package com.raxn.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.raxn.entity.UserActiveRecurringCoupons;

@Repository
public interface UserActiveRecurringCouponsRepository extends JpaRepository<UserActiveRecurringCoupons, Integer> {

	UserActiveRecurringCoupons findByUserid(String userid);

	UserActiveRecurringCoupons findByUseridAndDate01(String userid, String date01);

	UserActiveRecurringCoupons findByUseridAndDate02(String userid, String date02);

	UserActiveRecurringCoupons findByUseridAndDate03(String userid, String date03);

	UserActiveRecurringCoupons findByUseridAndDate04(String userid, String date04);

	UserActiveRecurringCoupons findByUseridAndDate05(String userid, String date05);

	UserActiveRecurringCoupons findByUseridAndDate06(String userid, String date06);

	UserActiveRecurringCoupons findByUseridAndDate07(String userid, String date07);

	UserActiveRecurringCoupons findByUseridAndDate08(String userid, String date08);

	UserActiveRecurringCoupons findByUseridAndDate09(String userid, String date09);

	UserActiveRecurringCoupons findByUseridAndDate10(String userid, String date10);

	UserActiveRecurringCoupons findByUseridAndDate11(String userid, String date11);

	UserActiveRecurringCoupons findByUseridAndDate12(String userid, String date12);

	UserActiveRecurringCoupons findByUseridAndDate13(String userid, String date13);

	UserActiveRecurringCoupons findByUseridAndDate14(String userid, String date14);

	UserActiveRecurringCoupons findByUseridAndDate15(String userid, String date15);

	UserActiveRecurringCoupons findByUseridAndDate16(String userid, String date16);

	UserActiveRecurringCoupons findByUseridAndDate17(String userid, String date17);

	UserActiveRecurringCoupons findByUseridAndDate18(String userid, String date18);

	UserActiveRecurringCoupons findByUseridAndDate19(String userid, String date19);

	UserActiveRecurringCoupons findByUseridAndDate20(String userid, String date20);

	UserActiveRecurringCoupons findByUseridAndDate21(String userid, String date21);

	UserActiveRecurringCoupons findByUseridAndDate22(String userid, String date22);

	UserActiveRecurringCoupons findByUseridAndDate23(String userid, String date23);

	UserActiveRecurringCoupons findByUseridAndDate24(String userid, String date24);

	UserActiveRecurringCoupons findByUseridAndDate25(String userid, String date25);

	UserActiveRecurringCoupons findByUseridAndDate26(String userid, String date26);

	UserActiveRecurringCoupons findByUseridAndDate27(String userid, String date27);

	UserActiveRecurringCoupons findByUseridAndDate28(String userid, String date28);

	UserActiveRecurringCoupons findByUseridAndDate29(String userid, String date29);

	UserActiveRecurringCoupons findByUseridAndDate30(String userid, String date30);
	
	UserActiveRecurringCoupons findByUseridAndDate31(String userid, String date31);

}
