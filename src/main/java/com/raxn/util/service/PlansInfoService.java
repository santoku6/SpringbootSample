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

public class PlansInfoService {

	Logger LOGGER = LoggerFactory.getLogger(PlansInfoService.class);

	@Value("${plansinfo.operator.url}")
	private String PLANSINFO_OPERATOR_URL;

	public void findOperator(String mobile) {
		LOGGER.info("Entered into findOperator()");
		LOGGER.info("mobile = " + mobile);
		mobile = "8073280884";
		try {
			String requestUrl = PLANSINFO_OPERATOR_URL + URLEncoder.encode(mobile, "UTF-8");
			URL url = new URL(requestUrl);
			HttpURLConnection uc = (HttpURLConnection) url.openConnection();
			LOGGER.info("response msg = " + uc.getResponseMessage());// ok
			LOGGER.info("response code=" + uc.getResponseCode());// 200

			String json_response = "";
			InputStreamReader in = new InputStreamReader(uc.getInputStream());
			BufferedReader br = new BufferedReader(in);
			String text = "";
			while ((text = br.readLine()) != null) {
				LOGGER.info("text msg = " + text);
				json_response += text;
			}

			LOGGER.info("json_response msg = " + json_response);
			uc.disconnect();
		} catch (IOException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
		LOGGER.info("Exiting findOperator()");
	}
}
