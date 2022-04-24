package com.raxn.model;

public class CouponsDisplay {
	
	private int id;	
	private String code;  //coupon code
	private String couponCategory;//wallet,recharge/dth,bills,giftcards
	private int instantCashbackWebMode;//instant cashback(amount) for web mode after coupon apply
	private int instantCashbackAppMode;//instant cashback(amount) for app mode after coupon apply
	private int percentCashbackWebMode;//cashback(web mode) in percent when coupon type is percent
	private int percentCashbackAppMode;//cashback(app mode) in percent when coupon type is percent
	private int couponUseTime;//number of times a user can apply any coupon
	private String couponDescription;//coupon description/title
	private String termsAndConditions;//coupon terms & condition
	private String recurringCashbackWebMode;//cashback(in web mode) breakup for entire duration
	private String recurringCashbackAppMode;//cashback(in app mode) breakup for entire duration
	private int maxCashbackAmountWebMode;//max cashback(in web mode) if coupon is percent type
	private int maxCashbackAmountAppMode;//max cashback(in app mode) if coupon is percent type
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getCouponCategory() {
		return couponCategory;
	}
	public void setCouponCategory(String couponCategory) {
		this.couponCategory = couponCategory;
	}
	public int getInstantCashbackWebMode() {
		return instantCashbackWebMode;
	}
	public void setInstantCashbackWebMode(int instantCashbackWebMode) {
		this.instantCashbackWebMode = instantCashbackWebMode;
	}
	public int getInstantCashbackAppMode() {
		return instantCashbackAppMode;
	}
	public void setInstantCashbackAppMode(int instantCashbackAppMode) {
		this.instantCashbackAppMode = instantCashbackAppMode;
	}
	public int getPercentCashbackWebMode() {
		return percentCashbackWebMode;
	}
	public void setPercentCashbackWebMode(int percentCashbackWebMode) {
		this.percentCashbackWebMode = percentCashbackWebMode;
	}
	public int getPercentCashbackAppMode() {
		return percentCashbackAppMode;
	}
	public void setPercentCashbackAppMode(int percentCashbackAppMode) {
		this.percentCashbackAppMode = percentCashbackAppMode;
	}
	public int getCouponUseTime() {
		return couponUseTime;
	}
	public void setCouponUseTime(int couponUseTime) {
		this.couponUseTime = couponUseTime;
	}
	public String getCouponDescription() {
		return couponDescription;
	}
	public void setCouponDescription(String couponDescription) {
		this.couponDescription = couponDescription;
	}
	public String getTermsAndConditions() {
		return termsAndConditions;
	}
	public void setTermsAndConditions(String termsAndConditions) {
		this.termsAndConditions = termsAndConditions;
	}
	public String getRecurringCashbackWebMode() {
		return recurringCashbackWebMode;
	}
	public void setRecurringCashbackWebMode(String recurringCashbackWebMode) {
		this.recurringCashbackWebMode = recurringCashbackWebMode;
	}
	public String getRecurringCashbackAppMode() {
		return recurringCashbackAppMode;
	}
	public void setRecurringCashbackAppMode(String recurringCashbackAppMode) {
		this.recurringCashbackAppMode = recurringCashbackAppMode;
	}
	public int getMaxCashbackAmountWebMode() {
		return maxCashbackAmountWebMode;
	}
	public void setMaxCashbackAmountWebMode(int maxCashbackAmountWebMode) {
		this.maxCashbackAmountWebMode = maxCashbackAmountWebMode;
	}
	public int getMaxCashbackAmountAppMode() {
		return maxCashbackAmountAppMode;
	}
	public void setMaxCashbackAmountAppMode(int maxCashbackAmountAppMode) {
		this.maxCashbackAmountAppMode = maxCashbackAmountAppMode;
	}

}
