package com.raxn.util.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PlansInfoService {

	Logger logger = LoggerFactory.getLogger(PlansInfoService.class);

	public void findOperator(String mobile) {
		logger.info("Entered into findOperator()");
		logger.info("mobile = " + mobile);
		mobile = "8073280884";
		//https://open-api.plansinfo.com/mobile/operator-circle?number=7893758280
		try {
			String requestUrl = "https://open-api.plansinfo.com/mobile/operator-circle?number="+URLEncoder.encode(mobile,"UTF-8");
			URL url = new URL(requestUrl);
		    HttpURLConnection uc = (HttpURLConnection)url.openConnection();
		    logger.info("response msg = "+uc.getResponseMessage());//ok
		    logger.info("response code="+uc.getResponseCode());//200
		    
		    String json_response = "";
		    InputStreamReader in = new InputStreamReader(uc.getInputStream());
		    BufferedReader br = new BufferedReader(in);
		    String text = "";
		    while ((text = br.readLine()) != null) {
		    	logger.info("text msg = "+text);
		      json_response += text;
		    }
		    
		    logger.info("json_response msg = "+json_response);
		    uc.disconnect();
		} catch (IOException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}

		logger.info("plansinfo done");
		logger.info("Exiting findOperator()");
	}
}
