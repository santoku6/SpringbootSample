package com.raxn.util.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import org.apache.commons.lang3.RandomStringUtils;
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

	public static boolean checkCouponCategory(String category) {
		boolean couponstatus = false;
		if (category.equalsIgnoreCase("wallet") || category.equalsIgnoreCase("recharge")
				|| category.equalsIgnoreCase("utility") || category.equalsIgnoreCase("giftcard")) {
			couponstatus = true;
		}
		return couponstatus;
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
		//String s1 = "6123456789";
		String regex = "^[6-9]\\d{9}$";
		if(mobile.length()==10 && mobile.matches(regex)) {
			validNumber = true;
		}
		
		System.out.println(validNumber);
		return validNumber;
	}
	
	//AB-ambika, PA-payone, MT-mitra
	public static String getOurRefNo() {
		String refNo = "";
		refNo += RandomStringUtils.random(8, AppConstant.RANDOM_NUMS);
		
		return refNo;
	}
}
