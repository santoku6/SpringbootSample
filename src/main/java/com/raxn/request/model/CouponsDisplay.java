package com.raxn.request.model;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

public class CouponsDisplay {

	private String code; // coupon code

	private String category;// wallet,recharge/dth,bills,giftcards

	@JsonInclude(Include.NON_NULL)
	private Integer instantCashbackWebMode;// instant cashback(amount) for web mode after coupon apply

	@JsonInclude(Include.NON_NULL)
	private Integer instantCashbackAppMode;// instant cashback(amount) for app mode after coupon apply

	@JsonInclude(Include.NON_NULL)
	private Integer percentCashbackWebMode;// cashback(web mode) in percent when coupon type is percent

	@JsonInclude(Include.NON_NULL)
	private Integer percentCashbackAppMode;// cashback(app mode) in percent when coupon type is percent

	private int usetime;// number of times a user can apply any coupon

	private String description;// coupon description/title

	private String tnc;// coupon terms & condition

	@JsonInclude(Include.NON_NULL)
	private String recurringCashbackWebMode;// cashback(in web mode) breakup for entire duration

	@JsonInclude(Include.NON_NULL)
	private String recurringCashbackAppMode;// cashback(in app mode) breakup for entire duration

	@JsonInclude(Include.NON_NULL)
	private Integer maxCashbackAmountWebMode;// max cashback(in web mode) if coupon is percent type

	@JsonInclude(Include.NON_NULL)
	private Integer maxCashbackAmountAppMode;// max cashback(in app mode) if coupon is percent type

	private LocalDate endDate;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public Integer getInstantCashbackWebMode() {
		return instantCashbackWebMode;
	}

	public void setInstantCashbackWebMode(Integer instantCashbackWebMode) {
		this.instantCashbackWebMode = instantCashbackWebMode;
	}

	public Integer getInstantCashbackAppMode() {
		return instantCashbackAppMode;
	}

	public void setInstantCashbackAppMode(Integer instantCashbackAppMode) {
		this.instantCashbackAppMode = instantCashbackAppMode;
	}

	public Integer getPercentCashbackWebMode() {
		return percentCashbackWebMode;
	}

	public void setPercentCashbackWebMode(Integer percentCashbackWebMode) {
		this.percentCashbackWebMode = percentCashbackWebMode;
	}

	public Integer getPercentCashbackAppMode() {
		return percentCashbackAppMode;
	}

	public void setPercentCashbackAppMode(Integer percentCashbackAppMode) {
		this.percentCashbackAppMode = percentCashbackAppMode;
	}

	public int getUsetime() {
		return usetime;
	}

	public void setUsetime(int usetime) {
		this.usetime = usetime;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getTnc() {
		return tnc;
	}

	public void setTnc(String tnc) {
		this.tnc = tnc;
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

	public Integer getMaxCashbackAmountWebMode() {
		return maxCashbackAmountWebMode;
	}

	public void setMaxCashbackAmountWebMode(Integer maxCashbackAmountWebMode) {
		this.maxCashbackAmountWebMode = maxCashbackAmountWebMode;
	}

	public Integer getMaxCashbackAmountAppMode() {
		return maxCashbackAmountAppMode;
	}

	public void setMaxCashbackAmountAppMode(Integer maxCashbackAmountAppMode) {
		this.maxCashbackAmountAppMode = maxCashbackAmountAppMode;
	}

	public LocalDate getEndDate() {
		return endDate;
	}

	public void setEndDate(LocalDate endDate) {
		this.endDate = endDate;
	}

}
