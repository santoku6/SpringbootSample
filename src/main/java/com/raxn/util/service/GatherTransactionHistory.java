package com.raxn.util.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.raxn.entity.RchDth;
import com.raxn.entity.RchMobile;
import com.raxn.repository.RchDthRepository;
import com.raxn.repository.RchMobileRepository;

public class GatherTransactionHistory {
	
	Logger LOGGER = LoggerFactory.getLogger(GatherTransactionHistory.class);
	
	@Autowired
	RchMobileRepository rchMobileRepo;
	
	@Autowired
	RchDthRepository rchDthRepo;
	
	public static List<String> listRechargeHistory(List<RchMobile> rechMobileHistory, List<RchDth> rechDthHistory) {
		
		
		
		
		
		return null;
		
	}
	
	public static List<String> listBillsHistory(String typeBills) {
		return null;
		
	}
	
	public static List<String> listWalletHistory(String typeWallet) {
		return null;
		
	}
	
	public static List<String> listGiftcardHistory(String typeGiftcard) {
		return null;
		
	}

}
