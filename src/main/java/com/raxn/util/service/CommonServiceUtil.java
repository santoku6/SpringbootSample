package com.raxn.util.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import org.apache.commons.lang3.RandomStringUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class CommonServiceUtil {

	// Generate Reference No for suggestion/complaint
	public static String genReferenceNo() {
		String refNo = "RCN";
		refNo += RandomStringUtils.random(4, AppConstant.RANDOM_NUMS);
		refNo += '-' + RandomStringUtils.random(4, AppConstant.RANDOM_NUMS);
		refNo += '-' + RandomStringUtils.random(4, AppConstant.RANDOM_NUMS);
		refNo += '-' + RandomStringUtils.random(4, AppConstant.RANDOM_NUMS);
		return refNo;
	}

	public static String genUsername() {
		String tempUsername = "";
		tempUsername += RandomStringUtils.random(4, AppConstant.RANDOM_CHARS);
		tempUsername += '-' + RandomStringUtils.random(4, AppConstant.RANDOM_CHARS);
		tempUsername += '-' + RandomStringUtils.random(4, AppConstant.RANDOM_CHARS);
		tempUsername += '-' + RandomStringUtils.random(4, AppConstant.RANDOM_CHARS);
		return tempUsername;
	}

	public static String genTempname() {
		String tempname = "";
		tempname += RandomStringUtils.random(20, AppConstant.RANDOM_CHARS);
		return tempname;
	}
	
	public static String genAddMoneyOrderId() {
		String orderid = "AM";
		orderid += RandomStringUtils.random(16, AppConstant.RANDOM_CHARS);
		return orderid;
	}

	// Generate OTP
	public static String genOTP() {
		String otp = "";
		otp += RandomStringUtils.random(6, AppConstant.RANDOM_NUMS);
		return otp;
	}

	public static String genOrderId() {
		String orderid = "";
		orderid += RandomStringUtils.random(12, AppConstant.RANDOM_NUMS);
		return orderid;
	}

	public static boolean checkMode(String mode) {
		boolean modestatus = false;
		if (mode.equalsIgnoreCase("web") || mode.equalsIgnoreCase("APP")) {
			modestatus = true;
		}
		return modestatus;
	}

	public static boolean checkCouponMode(String mode) {
		boolean modestatus = false;
		if (mode.equalsIgnoreCase("web") || mode.equalsIgnoreCase("APP") || mode.equalsIgnoreCase("both")) {
			modestatus = true;
		}
		return modestatus;
	}

	public static boolean checkSMSVendor(String vendor) {
		boolean vendorstatus = false;
		if (vendor.equalsIgnoreCase(AppConstant.VENDOR_BULKSMS)
				|| vendor.equalsIgnoreCase(AppConstant.VENDOR_NATIONALSMS)) {
			vendorstatus = true;
		}
		return vendorstatus;
	}

	public static boolean checkCouponCategory(String category) {
		boolean couponstatus = false;
		if (category.equalsIgnoreCase(AppConstant.WALLET) || category.equalsIgnoreCase(AppConstant.RECHARGE)
				|| category.equalsIgnoreCase(AppConstant.UTILITY) || category.equalsIgnoreCase(AppConstant.GIFTCARD)) {
			couponstatus = true;
		}
		return couponstatus;
	}

	public static boolean checkCouponInterval(String interval) {
		boolean couponstatus = false;
		if (interval.equalsIgnoreCase(AppConstant.REC_MONTHLY) || interval.equalsIgnoreCase(AppConstant.REC_WEEKLY)
				|| interval.equalsIgnoreCase(AppConstant.REC_DAILY)) {
			couponstatus = true;
		}
		return couponstatus;
	}

	public static boolean checkStatusOneZero(String status) {
		boolean statusActive = false;
		if (status.equalsIgnoreCase(AppConstant.STATUS_ZERO) || status.equalsIgnoreCase(AppConstant.STATUS_ONE)) {
			statusActive = true;
		}
		return statusActive;
	}

	public static String encodePassword(String password) {
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		String encodedPassword = encoder.encode(password);
		return encodedPassword;
	}

	// Get IP Address
	public static String getIp() throws Exception {
		URL whatismyip = new URL("http://checkip.amazonaws.com");
		BufferedReader in = null;
		try {
			in = new BufferedReader(new InputStreamReader(whatismyip.openStream()));
			String ip = in.readLine();
			return ip;
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public static boolean mobileChecker(String mobile) {
		boolean validNumber = false;
		// String s1 = "6123456789";
		String regex = "^[6-9]\\d{9}$";
		if (mobile.length() == 10 && mobile.matches(regex)) {
			validNumber = true;
		}

		// System.out.println(validNumber);
		return validNumber;
	}

	// AB-ambika, PA-payone, MT-mitra
	public static String getOurRefNo() {
		String refNo = "";
		refNo += RandomStringUtils.random(8, AppConstant.RANDOM_NUMS);

		return refNo;
	}

	public static boolean matchAmountInPlans(String mplantext, int amount) {
		boolean matchAmount = false;
		amount = 111;

		JSONObject obj = new JSONObject(mplantext);
		JSONArray categories = obj.getJSONArray("categories");

		int catlength = categories.length();
		// System.out.println("catlength=" + catlength);

		outerloop: for (int i = 0; i < catlength; i++) {
			JSONObject obj2 = categories.getJSONObject(i);
			JSONArray plans = obj2.getJSONArray("plans");

			for (int j = 0; j < plans.length(); j++) {
				JSONObject obj3 = plans.getJSONObject(j);
				int amount1 = obj3.getInt("amount");
				// System.out.println("amount1=" + amount1);
				if (amount1 == amount) {
					matchAmount = true;
					break outerloop;
				}
			}
		}
		// System.out.println("matchAmount=" + matchAmount);
		return matchAmount;
	}
	/*
	public static void main(String[] args) {
		String tempname = RandomStringUtils.random(16, AppConstant.RANDOM_CHARS);
		System.out.println("tempname="+tempname);
	}*/

}
