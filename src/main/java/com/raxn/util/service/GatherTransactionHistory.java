package com.raxn.util.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.raxn.entity.RchDth;
import com.raxn.entity.RchMobile;
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
