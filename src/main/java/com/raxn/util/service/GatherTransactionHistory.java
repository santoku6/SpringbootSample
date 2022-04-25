package com.raxn.util.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.raxn.entity.RchDth;
import com.raxn.entity.RchGiftcards;
import com.raxn.entity.RchMobile;
import com.raxn.entity.Wallet;
import com.raxn.repository.RchDthRepository;
import com.raxn.repository.RchMobileRepository;
import com.raxn.response.model.TransHistoryResponse;

public class GatherTransactionHistory {

	static Logger LOGGER = LoggerFactory.getLogger(GatherTransactionHistory.class);

	@Autowired
	RchMobileRepository rchMobileRepo;

	@Autowired
	RchDthRepository rchDthRepo;

	public static List<TransHistoryResponse> listRechargeHistory(List<RchMobile> rechMobileHistory,
			List<RchDth> rechDthHistory) {
		LOGGER.info("Entered into listRechargeHistory()");

		List<TransHistoryResponse> list1 = new ArrayList<TransHistoryResponse>();
		TransHistoryResponse transResponse = new TransHistoryResponse();

		for (RchMobile mobileLine : rechMobileHistory) {
			BeanUtils.copyProperties(mobileLine, transResponse);
			transResponse.setCategory("recharge");
			transResponse.setAmount(mobileLine.getRchAmount());
			list1.add(transResponse);
		}
		System.out.println(rechMobileHistory.size() + "===" + list1.size());
		for (RchDth dthLine : rechDthHistory) {
			BeanUtils.copyProperties(dthLine, transResponse);
			transResponse.setCategory("recharge");
			transResponse.setAmount(dthLine.getRchAmount());
			list1.add(transResponse);
		}
		System.out.println("Total size=" + list1.size());

		list1.sort(Comparator.comparing(TransHistoryResponse::getDatetime).reversed());

		System.out.println("Total size=" + list1.size());

		return list1;

	}

	public static List<TransHistoryResponse> listBillsHistory(String typeBills) {
		return null;

	}

	public static List<TransHistoryResponse> listWalletHistory(List<Wallet> rechWalletHistory) {
		LOGGER.info("Entered into listWalletHistory()");

		List<TransHistoryResponse> list1 = new ArrayList<TransHistoryResponse>();
		TransHistoryResponse transResponse = new TransHistoryResponse();

		for (Wallet walletLine : rechWalletHistory) {
			BeanUtils.copyProperties(walletLine, transResponse);
			transResponse.setCategory("wallet");
			if (walletLine.getCredit() > 0) {
				transResponse.setAmount(walletLine.getCredit());
			}
			if (walletLine.getDebit() > 0) {
				transResponse.setAmount(walletLine.getDebit());
			}
			list1.add(transResponse);
		}
		System.out.println(rechWalletHistory.size() + "===" + list1.size());
		return list1;
	}

	public static List<TransHistoryResponse> listGiftcardHistory(List<RchGiftcards> rechGiftcardHistory) {
		LOGGER.info("Entered into listGiftcardHistory()");

		List<TransHistoryResponse> list1 = new ArrayList<TransHistoryResponse>();
		TransHistoryResponse transResponse = new TransHistoryResponse();

		for (RchGiftcards giftcardLine : rechGiftcardHistory) {
			BeanUtils.copyProperties(giftcardLine, transResponse);
			transResponse.setCategory("giftcard");
			transResponse.setAmount(giftcardLine.getRchAmount());
			list1.add(transResponse);
		}
		System.out.println(rechGiftcardHistory.size() + "===" + list1.size());
		return list1;
	}

}
