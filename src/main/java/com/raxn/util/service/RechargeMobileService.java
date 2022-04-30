package com.raxn.util.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.raxn.request.model.APIMobileRechargeRequest;

@Service
public class RechargeMobileService {

	Logger LOGGER = LoggerFactory.getLogger(RechargeMobileService.class);

	/*
	 * @Value("${plansinfo.operator.url}") private String PLANSINFO_OPERATOR_URL;
	 * 
	 * @Value("${plansinfo.plans.url}") private String PLANSINFO_MPLANS_URL;
	 */

	public String rechargeMobile(APIMobileRechargeRequest rechargemobilereq) {
		LOGGER.info("Entered into rechargeMobile()");
		ObjectMapper objMapper = new ObjectMapper();

		JSONObject response = new JSONObject();
		String operator = null, circle = null;
		String apiuserid = "14853";
		String apitoken = "b48691e471e0423d6d0743c0f1867d0b";
		String account = rechargemobilereq.getConsumerno().trim();
		String amount = rechargemobilereq.getAmount().trim();
		String opcode = rechargemobilereq.getOperatorcode().trim();
		String reqid = rechargemobilereq.getRequestid().trim();
		String geocode = "23.8530,87.9727";
		String customerid = "8073280884";
		String pincode = "800007";

		// http://api.ambikamultiservices.com/API/TransactionAPI?UserID=14853
		// &Token=b48691e471e0423d6d0743c0f1867d0b&Account=ConsumerNo&Amount=Amount&SPKey=OperatorCode
		// &APIRequestID=UniqueRefNo&Optional1=&Optional2=&Optional3=&Optional4=&GEOCode=Longitude,Latitude
		// &CustomerNumber=Reg.MobileNumber&Pincode=Area
		// Pincode&Format=2

		String aburl = "http://api.ambikamultiservices.com/API/TransactionAPI?UserID=";

		String json_response = "";
		try {
			LOGGER.info("Parameter rechargemobilereq -> " + objMapper.writeValueAsString(rechargemobilereq));
			String requestUrl = aburl + apiuserid + "&Token=" + apitoken + "&Account=" + account + "&Amount=" + amount
					+ "&SPKey=" + opcode + "&APIRequestID=" + reqid + "&GEOCode=" + geocode + "&CustomerNumber="
					+ customerid + "&Pincode=" + pincode + "&Format=1";

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
			LOGGER.error("IOException :: -> " + e.getMessage(), e);
		}
		LOGGER.info("Exiting rechargeMobile()");
		return json_response;
	}

	/*
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

			LOGGER.info("json_response msg = " + json_response);
			uc.disconnect();
		} catch (IOException e) {
			LOGGER.error("IOException :: -> " + e.getMessage(), e);
		}
		LOGGER.info("Exiting mobilePlans()");
		return json_response;
	}
	 */
}
