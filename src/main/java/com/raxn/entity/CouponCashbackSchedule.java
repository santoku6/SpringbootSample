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
public class CouponCashbackSchedule implements Serializable {

	private static final long serialVersionUID = -3237300787301582094L;
	
	@Id
	@GeneratedValue
	private int id;
	
	@NotBlank
	@Column(unique=true)
	private String couponCode;  
	
	private String cb1Info; //daterange:cbamount (01-07:web(100),app(110))
	private String cb2Info; //daterange:cbamount (01-07:web(100),app(110))
	private String cb3Info; //daterange:cbamount (01-07:web(100),app(110))
	private String cb4Info; //daterange:cbamount (01-07:web(100),app(110))
	private String cb5Info; //daterange:cbamount (01-07:web(100),app(110))
	private String cb6Info; //daterange:cbamount (01-07:web(100),app(110))
	private String cb7Info; //daterange:cbamount (01-07:web(100),app(110))
	private String cb8Info; //daterange:cbamount (01-07:web(100),app(110))
	private String cb9Info; //daterange:cbamount (01-07:web(100),app(110))
	private String cb10Info; //daterange:cbamount (01-07:web(100),app(110))
	private String cb11Info; //daterange:cbamount (01-07:web(100),app(110))
	private String cb12Info; //daterange:cbamount (01-07:web(100),app(110))
	private String cb13Info; //daterange:cbamount (01-07:web(100),app(110))
	private String cb14Info; //daterange:cbamount (01-07:web(100),app(110))
	private String cb15Info; //daterange:cbamount (01-07:web(100),app(110))
	private String cb16Info; //daterange:cbamount (01-07:web(100),app(110))
	private String cb17Info; //daterange:cbamount (01-07:web(100),app(110))
	private String cb18Info; //daterange:cbamount (01-07:web(100),app(110))
	private String cb19Info; //daterange:cbamount (01-07:web(100),app(110))
	private String cb20Info; //daterange:cbamount (01-07:web(100),app(110))
	private String cb21Info; //daterange:cbamount (01-07:web(100),app(110))
	private String cb22Info; //daterange:cbamount (01-07:web(100),app(110))
	private String cb23Info; //daterange:cbamount (01-07:web(100),app(110))
	private String cb24Info; //daterange:cbamount (01-07:web(100),app(110))
	
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
	
	public String getCouponCode() {
		return couponCode;
	}
	public void setCouponCode(String couponCode) {
		this.couponCode = couponCode;
	}
	
	public String getCb1Info() {
		return cb1Info;
	}
	public void setCb1Info(String cb1Info) {
		this.cb1Info = cb1Info;
	}
	public String getCb2Info() {
		return cb2Info;
	}
	public void setCb2Info(String cb2Info) {
		this.cb2Info = cb2Info;
	}
	public String getCb3Info() {
		return cb3Info;
	}
	public void setCb3Info(String cb3Info) {
		this.cb3Info = cb3Info;
	}
	public String getCb4Info() {
		return cb4Info;
	}
	public void setCb4Info(String cb4Info) {
		this.cb4Info = cb4Info;
	}
	public String getCb5Info() {
		return cb5Info;
	}
	public void setCb5Info(String cb5Info) {
		this.cb5Info = cb5Info;
	}
	public String getCb6Info() {
		return cb6Info;
	}
	public void setCb6Info(String cb6Info) {
		this.cb6Info = cb6Info;
	}
	public String getCb7Info() {
		return cb7Info;
	}
	public void setCb7Info(String cb7Info) {
		this.cb7Info = cb7Info;
	}
	public String getCb8Info() {
		return cb8Info;
	}
	public void setCb8Info(String cb8Info) {
		this.cb8Info = cb8Info;
	}
	public String getCb9Info() {
		return cb9Info;
	}
	public void setCb9Info(String cb9Info) {
		this.cb9Info = cb9Info;
	}
	public String getCb10Info() {
		return cb10Info;
	}
	public void setCb10Info(String cb10Info) {
		this.cb10Info = cb10Info;
	}
	public String getCb11Info() {
		return cb11Info;
	}
	public void setCb11Info(String cb11Info) {
		this.cb11Info = cb11Info;
	}
	public String getCb12Info() {
		return cb12Info;
	}
	public void setCb12Info(String cb12Info) {
		this.cb12Info = cb12Info;
	}
	public String getCb13Info() {
		return cb13Info;
	}
	public void setCb13Info(String cb13Info) {
		this.cb13Info = cb13Info;
	}
	public String getCb14Info() {
		return cb14Info;
	}
	public void setCb14Info(String cb14Info) {
		this.cb14Info = cb14Info;
	}
	public String getCb15Info() {
		return cb15Info;
	}
	public void setCb15Info(String cb15Info) {
		this.cb15Info = cb15Info;
	}
	public String getCb16Info() {
		return cb16Info;
	}
	public void setCb16Info(String cb16Info) {
		this.cb16Info = cb16Info;
	}
	public String getCb17Info() {
		return cb17Info;
	}
	public void setCb17Info(String cb17Info) {
		this.cb17Info = cb17Info;
	}
	public String getCb18Info() {
		return cb18Info;
	}
	public void setCb18Info(String cb18Info) {
		this.cb18Info = cb18Info;
	}
	public String getCb19Info() {
		return cb19Info;
	}
	public void setCb19Info(String cb19Info) {
		this.cb19Info = cb19Info;
	}
	public String getCb20Info() {
		return cb20Info;
	}
	public void setCb20Info(String cb20Info) {
		this.cb20Info = cb20Info;
	}
	public String getCb21Info() {
		return cb21Info;
	}
	public void setCb21Info(String cb21Info) {
		this.cb21Info = cb21Info;
	}
	public String getCb22Info() {
		return cb22Info;
	}
	public void setCb22Info(String cb22Info) {
		this.cb22Info = cb22Info;
	}
	public String getCb23Info() {
		return cb23Info;
	}
	public void setCb23Info(String cb23Info) {
		this.cb23Info = cb23Info;
	}
	public String getCb24Info() {
		return cb24Info;
	}
	public void setCb24Info(String cb24Info) {
		this.cb24Info = cb24Info;
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
