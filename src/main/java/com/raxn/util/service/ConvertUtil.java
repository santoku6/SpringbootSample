package com.raxn.util.service;

import java.util.ArrayList;
import java.util.List;

public class ConvertUtil {

	// get coupon category list of a particular code
	public static List<String> convertCategoryToList(String couponCategory) {
		couponCategory = couponCategory.substring(1, couponCategory.length() - 1);
		String[] couponCategoryArray = couponCategory.split(",");
		List<String> categorylist = new ArrayList<>();

		for (String str1 : couponCategoryArray) {
			categorylist.add(str1.toUpperCase().trim());
		}
		return categorylist;
	}

	// extract user applied code list from user code applied history
	// {MM100=23-04-2022 00:07:38#web#WL634632636, HL100=23-04-2022
	// 00:07:38#app#AP2363276345, PP400=23-04-2022 00:07:38#web#ggP2363276345}
	public static List<String> extractCodesFromAppliedCoupons(String userAppliedSuccessCouponsList) {
		userAppliedSuccessCouponsList = userAppliedSuccessCouponsList.substring(1,
				userAppliedSuccessCouponsList.length() - 1);
		String[] keyCodeValuePairs = userAppliedSuccessCouponsList.split(",");
		List<String> keyCodeList = new ArrayList<String>();

		for (String str1 : keyCodeValuePairs) {
			String[] temp = str1.split("=");
			String aa = temp[0];
			keyCodeList.add(aa.toUpperCase().trim());
		}
		return keyCodeList;
	}

	// "ab01=datetime1,web,orderid1#cd01=datetime2,web,orderid2#xy01=datetime3,app,orderid3"
	public static List<String> extractCodesFromUserAppliedCoupons(String userAppliedSuccessCouponsList) {

		String[] keyCodeValuePairs = userAppliedSuccessCouponsList.split("#");
		List<String> keyCodeList = new ArrayList<String>();

		for (String str1 : keyCodeValuePairs) {
			String temp = str1.split("=")[0];
			// String aa = temp[0];
			keyCodeList.add(temp.toUpperCase().trim());
		}
		return keyCodeList;
	}

	// get count of same code applied
	public static int getAppliedCodeCount(List<String> keyCodeList, String code) {
		int appliedCodeCounter = 0;
		for (String str1 : keyCodeList) {
			if (str1.equalsIgnoreCase(code)) {
				appliedCodeCounter++;
			}
		}
		return appliedCodeCounter;
	}

	// convert comma values to list
	// [10,20,30,40,50]
	public static List<String> getCommasToList(String row) {
		String simplerow = row.substring(1, row.length() - 1);
		List<String> amountList = new ArrayList<String>();
		String[] amountArray = simplerow.split(",");
		for (String str1 : amountArray) {
			amountList.add(str1);
		}
		return amountList;
	}
}
