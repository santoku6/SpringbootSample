package com.raxn.response.model;

public class UserResponse {

	private String name;
	private String mobile;
	private String email;
	private String username;
	private String city;
	private String dob; // date of birth
	private double walletBalance;// always this be updated
	private double rewardPoint;
	private String cashbackEarned;
	private String role;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getDob() {
		return dob;
	}

	public void setDob(String dob) {
		this.dob = dob;
	}

	public double getWalletBalance() {
		return walletBalance;
	}

	public void setWalletBalance(double walletBalance) {
		this.walletBalance = walletBalance;
	}

	public double getRewardPoint() {
		return rewardPoint;
	}

	public void setRewardPoint(double rewardPoint) {
		this.rewardPoint = rewardPoint;
	}

	public String getCashbackEarned() {
		return cashbackEarned;
	}

	public void setCashbackEarned(String cashbackEarned) {
		this.cashbackEarned = cashbackEarned;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

}
