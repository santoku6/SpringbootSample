package com.raxn.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

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
@JsonIgnoreProperties(value = { "dateTime", "updatedAt" }, allowGetters = true)
public class RchPostpaid implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	private String orderid;
	private String userid;
	private String mobile;
	private String operator;
	private String category;// mobile
	private String circle;
	private double billAmount;
	private double walletAmountBefore;
	private double walletAmountAfter;
	private String usedCouponcode;
	private String couponBenefit;

	private double payAmountWallet;
	private double payAmountGateway;
	private String rechargeStatus;

	private String txnId;
	private String txnStatus;
	private String isRefund;
	private String callbackResponse;
	private String remark;

	@Column(nullable = false, updatable = false)
	@Temporal(TemporalType.TIMESTAMP)
	@CreatedDate
	private Date dateTime;

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

	public String getOrderid() {
		return orderid;
	}

	public void setOrderid(String orderid) {
		this.orderid = orderid;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public String getCircle() {
		return circle;
	}

	public void setCircle(String circle) {
		this.circle = circle;
	}

	public double getBillAmount() {
		return billAmount;
	}

	public void setBillAmount(double billAmount) {
		this.billAmount = billAmount;
	}

	public double getPayAmountWallet() {
		return payAmountWallet;
	}

	public void setPayAmountWallet(double payAmountWallet) {
		this.payAmountWallet = payAmountWallet;
	}

	public double getPayAmountGateway() {
		return payAmountGateway;
	}

	public void setPayAmountGateway(double payAmountGateway) {
		this.payAmountGateway = payAmountGateway;
	}

	public double getWalletAmountBefore() {
		return walletAmountBefore;
	}

	public void setWalletAmountBefore(double walletAmountBefore) {
		this.walletAmountBefore = walletAmountBefore;
	}

	public double getWalletAmountAfter() {
		return walletAmountAfter;
	}

	public void setWalletAmountAfter(double walletAmountAfter) {
		this.walletAmountAfter = walletAmountAfter;
	}

	public String getRechargeStatus() {
		return rechargeStatus;
	}

	public void setRechargeStatus(String rechargeStatus) {
		this.rechargeStatus = rechargeStatus;
	}

	public String getTxnId() {
		return txnId;
	}

	public void setTxnId(String txnId) {
		this.txnId = txnId;
	}

	public String getTxnStatus() {
		return txnStatus;
	}

	public void setTxnStatus(String txnStatus) {
		this.txnStatus = txnStatus;
	}

	public String getIsRefund() {
		return isRefund;
	}

	public void setIsRefund(String isRefund) {
		this.isRefund = isRefund;
	}

	public String getCallbackResponse() {
		return callbackResponse;
	}

	public void setCallbackResponse(String callbackResponse) {
		this.callbackResponse = callbackResponse;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Date getDateTime() {
		return dateTime;
	}

	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}

	public Date getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}

	public String getUsedCouponcode() {
		return usedCouponcode;
	}

	public void setUsedCouponcode(String usedCouponcode) {
		this.usedCouponcode = usedCouponcode;
	}

	public String getCouponBenefit() {
		return couponBenefit;
	}

	public void setCouponBenefit(String couponBenefit) {
		this.couponBenefit = couponBenefit;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

}
