package com.raxn.entity;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
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
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@NotBlank
	@Column(unique = true)
	private String code; // coupon code

	@NotNull
	private int eligibleAmount; // amount for which coupon(instant/recurring) can be applied

	// private int eligibleMinAmountPercent; // amount for which coupon(percent) can
	// be applied

	@NotBlank
	private String category;// list - wallet,recharge/dth,bills,giftcards

	@NotBlank
	private String type;// instant/recurring/percent

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
	private int usetime;// number of times a user can apply any coupon

	private LocalDate startDate;// coupon start date
	private LocalDate endDate;// coupon end date
	private LocalDate cbStartdate;// cashback start date for recurring coupon
	private LocalDate cbEnddate;// cashback end date for recurring coupon
	private String status;// coupon status(0=inactive,1=active,2=completed)
	private String email;// user email for which a coupon can be applied(special case)
	private String description;// coupon description/title

	@Lob
	private String tnc;// coupon terms & condition

	private String couponmode;// web or app mode

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

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
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

	public int getUsetime() {
		return usetime;
	}

	public void setUsetime(int usetime) {
		this.usetime = usetime;
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

	public LocalDate getCbStartdate() {
		return cbStartdate;
	}

	public void setCbStartdate(LocalDate cbStartdate) {
		this.cbStartdate = cbStartdate;
	}

	public LocalDate getCbEnddate() {
		return cbEnddate;
	}

	public void setCbEnddate(LocalDate cbEnddate) {
		this.cbEnddate = cbEnddate;
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

	public String getCouponmode() {
		return couponmode;
	}

	public void setCouponmode(String couponmode) {
		this.couponmode = couponmode;
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

}
