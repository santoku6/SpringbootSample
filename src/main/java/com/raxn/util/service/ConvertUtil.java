package com.raxn.util.service;

import java.util.ArrayList;
import java.util.List;

public class ConvertUtil {

	//get coupon category list of a particular code
	public static List<String> convertCategoryToList(String couponCategory) {
		couponCategory = couponCategory.substring(1, couponCategory.length() - 1);
		String[] couponCategoryArray = couponCategory.split(",");
		List<String> categorylist = new ArrayList<>();

		for (String str1 : couponCategoryArray) {
			categorylist.add(str1.toUpperCase().trim());
		}
		return categorylist;
	}

	//extract user applied code list from user code applied history
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

	//get count of same code applied
	public static int getAppliedCodeCount(List<String> keyCodeList, String code) {
		int appliedCodeCounter = 0;
		for (String str1 : keyCodeList) {
			if (str1.equalsIgnoreCase(code)) {
				appliedCodeCounter++;
			}
		}
		return appliedCodeCounter;
	}
}
