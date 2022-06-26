package com.raxn.service;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import com.raxn.request.model.AddCouponRequest;
import com.raxn.request.model.AddSMSTemplateRequest;
import com.raxn.request.model.EditServiceRequest;
import com.raxn.request.model.FaqRequest;

public interface AddUpdateService {

	ResponseEntity<String> addCoupon(AddCouponRequest addCouponRequest);

	ResponseEntity<String> editcoupon(AddCouponRequest addCouponRequest);

	ResponseEntity<String> editcouponstatus(String code);

	ResponseEntity<String> checkCodeInDB(String code);

	ResponseEntity<String> viewcoupons();

	ResponseEntity<String> viewservices();

	ResponseEntity<String> editservice(EditServiceRequest editServiceRequest);

	ResponseEntity<String> editservicestatus(String id);

	ResponseEntity<String> addservice(EditServiceRequest editServiceRequest);

	ResponseEntity<String> delservice(String id);

	ResponseEntity<String> viewsliders();

	ResponseEntity<String> editslider(MultipartFile mpfile, String imagename, String id, String status);

	ResponseEntity<String> editsliderstatus(String id);

	ResponseEntity<String> addslider(MultipartFile mpfile, String name, String status);

	ResponseEntity<String> viewsmsvendor();

	ResponseEntity<String> editsmsvendor(String id, String vendor);

	ResponseEntity<String> addsmsvendor(AddSMSTemplateRequest smsTemplateRequest);

	ResponseEntity<String> delsmsvendor(String id);

	ResponseEntity<String> viewfaq();

	ResponseEntity<String> editfaq(FaqRequest faq);

	ResponseEntity<String> addfaq(FaqRequest faq);

	ResponseEntity<String> deletefaq(String id);

	ResponseEntity<String> editfooter(String header, String aboutus, String chooseus, String mission, String vision,
			String address, String email, String mobile, String whatsapp, String noticebar, String ppolicy,
			String refpolicy, String tnc);
	
	//void scheduleDailyActiveReCodeSystem();

}
