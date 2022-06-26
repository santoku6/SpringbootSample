package com.raxn.util.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.raxn.entity.RchBroadbandLandline;
import com.raxn.entity.RchDth;
import com.raxn.entity.RchElectricity;
import com.raxn.entity.RchFastag;
import com.raxn.entity.RchGasAndCylinder;
import com.raxn.entity.RchGiftcards;
import com.raxn.entity.RchInsurance;
import com.raxn.entity.RchMobile;
import com.raxn.entity.RchPostpaid;
import com.raxn.entity.RchWater;
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

		List<TransHistoryResponse> transListResponse = new ArrayList<TransHistoryResponse>();

		for (RchMobile mobileLine : rechMobileHistory) {
			TransHistoryResponse transResponse = new TransHistoryResponse();
			BeanUtils.copyProperties(mobileLine, transResponse);
			transResponse.setCategory("recharge");
			transResponse.setAmount(mobileLine.getRchAmount());
			transListResponse.add(transResponse);
		}
		//System.out.println(rechMobileHistory.size() + "===" + transListResponse.size());
		for (RchDth dthLine : rechDthHistory) {
			TransHistoryResponse transResponse = new TransHistoryResponse();
			BeanUtils.copyProperties(dthLine, transResponse);
			transResponse.setCategory("recharge");
			transResponse.setAmount(dthLine.getRchAmount());
			transListResponse.add(transResponse);
		}
		//System.out.println("Total size=" + transListResponse.size());

		transListResponse.sort(Comparator.comparing(TransHistoryResponse::getDatetime).reversed());

		LOGGER.info("recharge list count =" + transListResponse.size());

		return transListResponse;

	}

	public static List<TransHistoryResponse> listBillsHistory(List<RchBroadbandLandline> rechBBLLHistory,
			List<RchElectricity> rechElectricityHistory, List<RchFastag> rechFastagHistory,
			List<RchGasAndCylinder> rechGasAndCylinderHistory, List<RchInsurance> rechInsuranceHistory,
			List<RchPostpaid> rechPostpaidHistory, List<RchWater> rechWaterHistory) {
		LOGGER.info("Entered into listBillsHistory()");

		List<TransHistoryResponse> transListResponse = new ArrayList<TransHistoryResponse>();

		for (RchBroadbandLandline bbllLine : rechBBLLHistory) {
			TransHistoryResponse transResponse = new TransHistoryResponse();
			BeanUtils.copyProperties(bbllLine, transResponse);
			transResponse.setCategory("bills");
			transResponse.setAmount(bbllLine.getBillAmount());
			transListResponse.add(transResponse);
		}
		//System.out.println("list size after BBLL=" + transListResponse.size());
		for (RchElectricity electricityLine : rechElectricityHistory) {
			TransHistoryResponse transResponse = new TransHistoryResponse();
			BeanUtils.copyProperties(electricityLine, transResponse);
			transResponse.setCategory("bills");
			transResponse.setAmount(electricityLine.getBillAmount());
			transListResponse.add(transResponse);
		}
		//System.out.println("list size after electricity=" + transListResponse.size());
		for (RchFastag fastagLine : rechFastagHistory) {
			TransHistoryResponse transResponse = new TransHistoryResponse();
			BeanUtils.copyProperties(fastagLine, transResponse);
			transResponse.setCategory("bills");
			transResponse.setAmount(fastagLine.getBillAmount());
			transListResponse.add(transResponse);
		}
		//System.out.println("list size after fastag=" + transListResponse.size());
		for (RchGasAndCylinder gasAndCylinderLine : rechGasAndCylinderHistory) {
			TransHistoryResponse transResponse = new TransHistoryResponse();
			BeanUtils.copyProperties(gasAndCylinderLine, transResponse);
			transResponse.setCategory("bills");
			transResponse.setAmount(gasAndCylinderLine.getBillAmount());
			transListResponse.add(transResponse);
		}
		//System.out.println("list size after GasAndCylinder=" + transListResponse.size());
		for (RchInsurance insuranceLine : rechInsuranceHistory) {
			TransHistoryResponse transResponse = new TransHistoryResponse();
			BeanUtils.copyProperties(insuranceLine, transResponse);
			transResponse.setCategory("bills");
			transResponse.setAmount(insuranceLine.getBillAmount());
			transListResponse.add(transResponse);
		}
		//System.out.println("list size after insurance=" + transListResponse.size());
		for (RchPostpaid postpaidLine : rechPostpaidHistory) {
			TransHistoryResponse transResponse = new TransHistoryResponse();
			BeanUtils.copyProperties(postpaidLine, transResponse);
			transResponse.setCategory("bills");
			transResponse.setAmount(postpaidLine.getBillAmount());
			transListResponse.add(transResponse);
		}
		//System.out.println("list size after postpaidLine=" + transListResponse.size());
		for (RchWater waterLine : rechWaterHistory) {
			TransHistoryResponse transResponse = new TransHistoryResponse();
			BeanUtils.copyProperties(waterLine, transResponse);
			transResponse.setCategory("bills");
			transResponse.setAmount(waterLine.getBillAmount());
			transListResponse.add(transResponse);
		}
		//System.out.println("list size after water=" + transListResponse.size());

		transListResponse.sort(Comparator.comparing(TransHistoryResponse::getDatetime).reversed());
		LOGGER.info("bill list count=" + transListResponse.size());
		return transListResponse;
	}

	public static List<TransHistoryResponse> listWalletHistory(List<Wallet> rechWalletHistory) {
		LOGGER.info("Entered into listWalletHistory()");

		List<TransHistoryResponse> transListResponse = new ArrayList<TransHistoryResponse>();

		for (Wallet walletLine : rechWalletHistory) {
			TransHistoryResponse transResponse = new TransHistoryResponse();
			BeanUtils.copyProperties(walletLine, transResponse);
			transResponse.setCategory("wallet");
			if (walletLine.getCredit() > 0) {
				transResponse.setAmount(walletLine.getCredit());
			}
			if (walletLine.getDebit() > 0) {
				transResponse.setAmount(walletLine.getDebit());
			}
			transListResponse.add(transResponse);
		}
		//System.out.println(rechWalletHistory.size() + "===" + transListResponse.size());
		LOGGER.info("wallet list count =" + transListResponse.size());
		return transListResponse;
	}

	public static List<TransHistoryResponse> listGiftcardHistory(List<RchGiftcards> rechGiftcardHistory) {
		LOGGER.info("Entered into listGiftcardHistory()");

		List<TransHistoryResponse> transListResponse = new ArrayList<TransHistoryResponse>();

		for (RchGiftcards giftcardLine : rechGiftcardHistory) {
			TransHistoryResponse transResponse = new TransHistoryResponse();
			BeanUtils.copyProperties(giftcardLine, transResponse);
			transResponse.setCategory("giftcard");
			transResponse.setAmount(giftcardLine.getRchAmount());
			transListResponse.add(transResponse);
		}
		//System.out.println(rechGiftcardHistory.size() + "===" + transListResponse.size());
		LOGGER.info("giftcard list count =" + transListResponse.size());
		return transListResponse;
	}

}
