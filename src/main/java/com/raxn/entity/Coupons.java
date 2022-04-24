package com.raxn.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@EntityListeners(AuditingEntityListener.class)
@JsonIgnoreProperties(value = { "createdAt", "updatedAt" }, allowGetters = true)
public class Coupons implements Serializable, Cloneable {

	private static final long serialVersionUID = -9022062908075762940L;

	@Id
	@GeneratedValue
	private int id;

	@NotBlank
	@Column(unique = true)
	private String code; // coupon code

	@NotNull
	private int eligibleAmount; // amount for which coupon(instant/recurring) can be applied

	private int eligibleMinAmountPercent; // amount for which coupon(percent) can be applied

	@NotBlank
	private String couponCategory;// list - wallet,recharge/dth,bills,giftcards

	@NotBlank
	private String couponType;// instant/recurring/percent

	private int instantCashbackWebMode;// instant cashback(amount) for web mode after coupon apply
	private int instantCashbackAppMode;// instant cashback(amount) for app mode after coupon apply
	private int percentCashbackWebMode;// cashback(web mode) in percent when coupon type is percent
	private int percentCashbackAppMode;// cashback(app mode) in percent when coupon type is percent
	private String recurringInterval;// cashback interval i.e month/weeks/days
	private int recurringDuration;// number of recurring cashback i.e 3,6,9,12,24
	private String recurringCashbackWebMode;// cashback(in web mode) breakup for entire duration
	private String recurringCashbackAppMode;// cashback(in app mode) breakup for entire duration
	private int maxCashbackAmountWebMode;// max cashback(in web mode) if coupon is percent type
	private int maxCashbackAmountAppMode;// max cashback(in app mode) if coupon is percent type
	private int minBalance;// min amount to keep in account to get cashback(only for recurring type)

	@NotNull
	private int couponUseTime;// number of times a user can apply any coupon

	private Date startDate;// coupon start date
	private Date endDate;// coupon end date
	private String status;// coupon status(1=active,0=inactive)
	private String email;// user email for which a coupon can be applied(special case)
	private String couponDescription;// coupon description/title
	private String termsAndConditions;// coupon terms & condition
	private String mode;// web or app mode

	@Column(nullable = false, updatable = false)
	@Temporal(TemporalType.TIMESTAMP)
	@CreatedDate
	private Date createdAt;

	@Column(nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	@LastModifiedDate
	private Date updatedAt;

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

	public int getEligibleAmount() {
		return eligibleAmount;
	}

	public void setEligibleAmount(int eligibleAmount) {
		this.eligibleAmount = eligibleAmount;
	}

	public int getEligibleMinAmountPercent() {
		return eligibleMinAmountPercent;
	}

	public void setEligibleMinAmountPercent(int eligibleMinAmountPercent) {
		this.eligibleMinAmountPercent = eligibleMinAmountPercent;
	}

	public String getCouponCategory() {
		return couponCategory;
	}

	public void setCouponCategory(String couponCategory) {
		this.couponCategory = couponCategory;
	}

	public String getCouponType() {
		return couponType;
	}

	public void setCouponType(String couponType) {
		this.couponType = couponType;
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

	public int getMinBalance() {
		return minBalance;
	}

	public void setMinBalance(int minBalance) {
		this.minBalance = minBalance;
	}

	public int getCouponUseTime() {
		return couponUseTime;
	}

	public void setCouponUseTime(int couponUseTime) {
		this.couponUseTime = couponUseTime;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
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

	public String getMode() {
		return mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public Date getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}

	public Coupons clone() throws CloneNotSupportedException {
		return (Coupons) super.clone();
	}
}
