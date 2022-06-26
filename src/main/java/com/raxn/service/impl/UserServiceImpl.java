package com.raxn.service.impl;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.raxn.entity.OrdertableMoney;
import com.raxn.repository.OrdertableMoneyRepository;
import com.raxn.request.model.AddMoneyRequest;
import com.raxn.service.UserService;
import com.raxn.util.service.AppConstant;
import com.raxn.util.service.CommonServiceUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {

	Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);
	private static String successStatus = "Success";
	private static String errorStatus = "Error";

	@Autowired
	OrdertableMoneyRepository orderMoneyRepo;

	@Override
	public ResponseEntity<String> addmoney(AddMoneyRequest addmoneyRequest) {
		LOGGER.info("Entered addmoney() -> Start");
		LOGGER.info("addmoneyRequest =" + ReflectionToStringBuilder.toString(addmoneyRequest));
		JSONObject response = new JSONObject();
		String orderId = CommonServiceUtil.genAddMoneyOrderId();
		OrdertableMoney orderMoney = new OrdertableMoney();

		if (null == addmoneyRequest.getUsername() || addmoneyRequest.getUsername().isEmpty()) {
			response.put(AppConstant.STATUS, errorStatus);
			response.put(AppConstant.MESSAGE, "username is empty or null");
			LOGGER.error("username is empty or null");
			return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
		}
		if (null == addmoneyRequest.getCategory() || addmoneyRequest.getCategory().isEmpty()) {
			response.put(AppConstant.STATUS, errorStatus);
			response.put(AppConstant.MESSAGE, "category is empty or null");
			LOGGER.error("category is empty or null");
			return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
		}
		if (null == addmoneyRequest.getAmount() || addmoneyRequest.getAmount().isEmpty()) {
			response.put(AppConstant.STATUS, errorStatus);
			response.put(AppConstant.MESSAGE, "amount is empty or null");
			LOGGER.error("category is empty or null");
			return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
		}
		if (null == addmoneyRequest.getMode() || addmoneyRequest.getMode().isEmpty()) {
			response.put(AppConstant.STATUS, errorStatus);
			response.put(AppConstant.MESSAGE, "mode is empty or null");
			LOGGER.error("category is empty or null");
			return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
		}
		if (null != addmoneyRequest.getMode() && !addmoneyRequest.getMode().isEmpty()) {
			if (!CommonServiceUtil.checkMode(addmoneyRequest.getMode().trim())) {
				response.put(AppConstant.STATUS, errorStatus);
				response.put(AppConstant.MESSAGE, "mode is incorrect");
				LOGGER.error("mode is incorrect");
				return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
			}
		}

		String code=null;
		String username = addmoneyRequest.getUsername().trim();
		String category = addmoneyRequest.getCategory().trim();
		String mode = addmoneyRequest.getMode().trim();
		String amount = addmoneyRequest.getAmount().trim();
		if (null != addmoneyRequest.getCode() && !addmoneyRequest.getCode().isEmpty()) {
			code = addmoneyRequest.getCode().trim();
		}

		try {
			orderMoney.setAmount(Double.parseDouble(amount));
			orderMoney.setUsername(username);
			orderMoney.setOrderid(orderId);
			orderMoney.setCode(code);
			orderMoney.setMode(mode);
			orderMoney.setCategory(category);
			orderMoney.setStatus(AppConstant.STATUS_INCOMPLETE);
			
			orderMoney = orderMoneyRepo.save(orderMoney);

		} catch (Exception e) {
			LOGGER.error("Error in addmoney::"+e.getMessage());
			response.put(AppConstant.STATUS, errorStatus);
			response.put(AppConstant.MESSAGE, e.getMessage());
			return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);

		}

		return null;
	}


}
