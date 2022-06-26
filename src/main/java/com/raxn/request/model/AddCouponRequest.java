package com.raxn.request.model;

import java.time.LocalDate;

public class AddCouponRequest {

	private String code; // coupon code
	private String type; // instant, recurring, percentage
	private String category;// wallet,recharge/dth,bills,giftcards
	private String description;// coupon description/title
	private String usetime;// number of times a user can apply any coupon
	private String termsAndConditions;// coupon terms & condition
	private String eligibleAmount;// amount for which coupon(instant/recurring) can be applied
	private LocalDate startDate;// coupon start date
	private LocalDate endDate;// coupon end date
	private String status;// coupon status(active,inactive)
	private String useremail;// user email for which a coupon can be applied(special case)
	private String couponmode;// web or app mode or both

	// instant coupon
	private int instantCashbackWebMode;// instant cashback(amount) for web mode after coupon apply
	private int instantCashbackAppMode;// instant cashback(amount) for app mode after coupon apply

	// recurring coupon
	private String recurringCashbackWebMode;// cashback(in web mode) breakup for entire duration
	private String recurringCashbackAppMode;// cashback(in app mode) breakup for entire duration
	private String recurringInterval;// cashback interval i.e month/weeks/days
	private int recurringDuration;// number of recurring cashback i.e 3,6,9,12,24
	private int minBalance;// min amount to keep in account to get cashback(only for recurring type)

	// percentage coupon
	//private int eligibleMinAmountPercent; // amount for which coupon(percent) can be applied
	private int percentCashbackWebMode;// cashback(web mode) in percent when coupon type is percent
	private int percentCashbackAppMode;// cashback(app mode) in percent when coupon type is percent
	private int maxCashbackAmountWebMode;// max cashback(in web mode) if coupon is percent type
	private int maxCashbackAmountAppMode;// max cashback(in app mode) if coupon is percent type

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getUsetime() {
		return usetime;
	}

	public void setUsetime(String usetime) {
		this.usetime = usetime;
	}

	public String getTermsAndConditions() {
		return termsAndConditions;
	}

	public void setTermsAndConditions(String termsAndConditions) {
		this.termsAndConditions = termsAndConditions;
	}

	public String getEligibleAmount() {
		return eligibleAmount;
	}

	public void setEligibleAmount(String eligibleAmount) {
		this.eligibleAmount = eligibleAmount;
	}

	public LocalDate getStartDate() {
		return startDate;
	}

	public void setStartDate(LocalDate startDate) {
		this.startDate = startDate;
	}

	public LocalDate getEndDate() {
		return endDate;
	}

	public void setEndDate(LocalDate endDate) {
		this.endDate = endDate;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getUseremail() {
		return useremail;
	}

	public void setUseremail(String useremail) {
		this.useremail = useremail;
	}

	public String getCouponmode() {
		return couponmode;
	}

	public void setCouponmode(String couponmode) {
		this.couponmode = couponmode;
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

	public String getRecurringInterval() {
		return recurringInterval;
	}

	public void setRecurringInterval(String recurringInterval) {
		this.recurringInterval = recurringInterval;
	}

	public int getRecurringDuration() {
		return recurringDuration;
	}

	public void setRecurringDuration(int recurringDuration) {
		this.recurringDuration = recurringDuration;
	}

	public int getMinBalance() {
		return minBalance;
	}

	public void setMinBalance(int minBalance) {
		this.minBalance = minBalance;
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
