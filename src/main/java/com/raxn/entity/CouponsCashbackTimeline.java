package com.raxn.entity;

import java.io.Serializable;
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
public class CouponsCashbackTimeline implements Serializable {

	private static final long serialVersionUID = 5396441864605937480L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Lob
	private String date01;// comma separated codes
	@Lob
	private String date02;
	@Lob
	private String date03;
	@Lob
	private String date04;
	@Lob
	private String date05;
	@Lob
	private String date06;
	@Lob
	private String date07;
	@Lob
	private String date08;
	@Lob
	private String date09;
	@Lob
	private String date10;
	@Lob
	private String date11;
	@Lob
	private String date12;
	@Lob
	private String date13;
	@Lob
	private String date14;
	@Lob
	private String date15;
	@Lob
	private String date16;
	@Lob
	private String date17;
	@Lob
	private String date18;
	@Lob
	private String date19;
	@Lob
	private String date20;
	@Lob
	private String date21;
	@Lob
	private String date22;
	@Lob
	private String date23;
	@Lob
	private String date24;
	@Lob
	private String date25;
	@Lob
	private String date26;
	@Lob
	private String date27;
	@Lob
	private String date28;
	@Lob
	private String date29;
	@Lob
	private String date30;
	@Lob
	private String date31;

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

	public String getDate01() {
		return date01;
	}

	public void setDate01(String date01) {
		this.date01 = date01;
	}

	public String getDate02() {
		return date02;
	}

	public void setDate02(String date02) {
		this.date02 = date02;
	}

	public String getDate03() {
		return date03;
	}

	public void setDate03(String date03) {
		this.date03 = date03;
	}

	public String getDate04() {
		return date04;
	}

	public void setDate04(String date04) {
		this.date04 = date04;
	}

	public String getDate05() {
		return date05;
	}

	public void setDate05(String date05) {
		this.date05 = date05;
	}

	public String getDate06() {
		return date06;
	}

	public void setDate06(String date06) {
		this.date06 = date06;
	}

	public String getDate07() {
		return date07;
	}

	public void setDate07(String date07) {
		this.date07 = date07;
	}

	public String getDate08() {
		return date08;
	}

	public void setDate08(String date08) {
		this.date08 = date08;
	}

	public String getDate09() {
		return date09;
	}

	public void setDate09(String date09) {
		this.date09 = date09;
	}

	public String getDate10() {
		return date10;
	}

	public void setDate10(String date10) {
		this.date10 = date10;
	}

	public String getDate11() {
		return date11;
	}

	public void setDate11(String date11) {
		this.date11 = date11;
	}

	public String getDate12() {
		return date12;
	}

	public void setDate12(String date12) {
		this.date12 = date12;
	}

	public String getDate13() {
		return date13;
	}

	public void setDate13(String date13) {
		this.date13 = date13;
	}

	public String getDate14() {
		return date14;
	}

	public void setDate14(String date14) {
		this.date14 = date14;
	}

	public String getDate15() {
		return date15;
	}

	public void setDate15(String date15) {
		this.date15 = date15;
	}

	public String getDate16() {
		return date16;
	}

	public void setDate16(String date16) {
		this.date16 = date16;
	}

	public String getDate17() {
		return date17;
	}

	public void setDate17(String date17) {
		this.date17 = date17;
	}

	public String getDate18() {
		return date18;
	}

	public void setDate18(String date18) {
		this.date18 = date18;
	}

	public String getDate19() {
		return date19;
	}

	public void setDate19(String date19) {
		this.date19 = date19;
	}

	public String getDate20() {
		return date20;
	}

	public void setDate20(String date20) {
		this.date20 = date20;
	}

	public String getDate21() {
		return date21;
	}

	public void setDate21(String date21) {
		this.date21 = date21;
	}

	public String getDate22() {
		return date22;
	}

	public void setDate22(String date22) {
		this.date22 = date22;
	}

	public String getDate23() {
		return date23;
	}

	public void setDate23(String date23) {
		this.date23 = date23;
	}

	public String getDate24() {
		return date24;
	}

	public void setDate24(String date24) {
		this.date24 = date24;
	}

	public String getDate25() {
		return date25;
	}

	public void setDate25(String date25) {
		this.date25 = date25;
	}

	public String getDate26() {
		return date26;
	}

	public void setDate26(String date26) {
		this.date26 = date26;
	}

	public String getDate27() {
		return date27;
	}

	public void setDate27(String date27) {
		this.date27 = date27;
	}

	public String getDate28() {
		return date28;
	}

	public void setDate28(String date28) {
		this.date28 = date28;
	}

	public String getDate29() {
		return date29;
	}

	public void setDate29(String date29) {
		this.date29 = date29;
	}

	public String getDate30() {
		return date30;
	}

	public void setDate30(String date30) {
		this.date30 = date30;
	}

	public String getDate31() {
		return date31;
	}

	public void setDate31(String date31) {
		this.date31 = date31;
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
