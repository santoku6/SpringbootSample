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
import javax.validation.constraints.NotBlank;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@EntityListeners(AuditingEntityListener.class)
@JsonIgnoreProperties(value = { "dateTime" }, allowGetters = true)
public class SocialCareFund implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@NotBlank
	private String userid;

	private String activity;
	private String orderId;
	private int amount;
	private int earnedPoint;
	private String status;
	private String mode;

	@Column(nullable = false, updatable = false)
	@Temporal(TemporalType.TIMESTAMP)
	@CreatedDate
	private Date dateTime;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getActivity() {
		return activity;
	}

	public void setActivity(String activity) {
		this.activity = activity;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public int getEarnedPoint() {
		return earnedPoint;
	}

	public void setEarnedPoint(int earnedPoint) {
		this.earnedPoint = earnedPoint;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getMode() {
		return mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}

	public Date getDateTime() {
		return dateTime;
	}

	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}

}
