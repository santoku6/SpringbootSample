package com.raxn.util.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class PlansInfoService {

	Logger LOGGER = LoggerFactory.getLogger(PlansInfoService.class);

	@Value("${plansinfo.operator.url}")
	private String PLANSINFO_OPERATOR_URL;

	@Value("${plansinfo.plans.url}")
	private String PLANSINFO_MPLANS_URL;

	public String findOperator(String mobile) {
		LOGGER.info("Entered into findOperator()");
		LOGGER.info("mobile = " + mobile);

		String json_response = "";
		try {
			String requestUrl = PLANSINFO_OPERATOR_URL + URLEncoder.encode(mobile, "UTF-8");
			URL url = new URL(requestUrl);
			HttpURLConnection uc = (HttpURLConnection) url.openConnection();
			LOGGER.info("response code=" + uc.getResponseCode());// 200

			InputStreamReader in = new InputStreamReader(uc.getInputStream());
			BufferedReader br = new BufferedReader(in);
			String text = "";
			while ((text = br.readLine()) != null) {
				json_response += text;
			}

			LOGGER.info("json_response msg = " + json_response);
			uc.disconnect();
		} catch (IOException e) {
			LOGGER.error("IOException :: -> " + e.getMessage());
		}
		LOGGER.info("Exiting findOperator()");
		return json_response;
	}

	public String mobilePlans(String operator, String circle) {
		LOGGER.info("Entered into mobilePlans()");
		LOGGER.info("operator = " + operator + " ,circle=" + circle);

		String json_response = "";
		try {
			String encodedToken = URLEncoder.encode("zJcgbVlZCbMfglmsPCv3cxxfzCDtnY2t", "utf-8");

			String requestUrl = PLANSINFO_MPLANS_URL + encodedToken + "&operator=" + operator + "&circle=" + circle;

			URL url = new URL(requestUrl);
			HttpURLConnection uc = (HttpURLConnection) url.openConnection();
			LOGGER.info("response code=" + uc.getResponseCode());// 200

			InputStreamReader in = new InputStreamReader(uc.getInputStream());
			BufferedReader br = new BufferedReader(in);
			String text = "";
			while ((text = br.readLine()) != null) {
				json_response += text;
			}

			//LOGGER.info("json_response msg = " + json_response);
			uc.disconnect();
		} catch (IOException e) {
			LOGGER.error("IOException :: -> " + e.getMessage());
		}
		LOGGER.info("Exiting mobilePlans()");
		return json_response;
	}

}
