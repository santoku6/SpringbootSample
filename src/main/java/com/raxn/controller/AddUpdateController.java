package com.raxn.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.raxn.request.model.AddCouponRequest;
import com.raxn.request.model.AddSMSTemplateRequest;
import com.raxn.request.model.EditServiceRequest;
import com.raxn.request.model.FaqRequest;
import com.raxn.service.AddUpdateService;

@CrossOrigin
@RestController
@RequestMapping("/admin")
public class AddUpdateController {

	// private static final Logger LOGGER =
	// LoggerFactory.getLogger(AddUpdateController.class);

	@Autowired
	private AddUpdateService addupdateService;

	// private static String errorStatus = "Error";

	@PostMapping("/addcoupon")
	public ResponseEntity<String> addCoupon(@RequestBody AddCouponRequest addCouponRequest) {
		ResponseEntity<String> response = addupdateService.addCoupon(addCouponRequest);
		return new ResponseEntity<String>(response.getBody(), response.getStatusCode());
	}

	@PostMapping("/editcoupon")
	public ResponseEntity<String> editcoupon(@RequestBody AddCouponRequest addCouponRequest) {
		ResponseEntity<String> response = addupdateService.editcoupon(addCouponRequest);
		return new ResponseEntity<String>(response.getBody(), response.getStatusCode());
	}

	@PostMapping("/editcouponstatus")
	public ResponseEntity<String> editcouponstatus(@RequestParam String code) {
		ResponseEntity<String> response = addupdateService.editcouponstatus(code);
		return new ResponseEntity<String>(response.getBody(), response.getStatusCode());
	}

	@GetMapping("/checkcode/{code}")
	public ResponseEntity<String> checkCodeInDB(@PathVariable String code) {
		ResponseEntity<String> response = addupdateService.checkCodeInDB(code);
		return new ResponseEntity<String>(response.getBody(), response.getStatusCode());
	}

	@GetMapping("/viewcoupons")
	public ResponseEntity<String> viewcoupons() {
		ResponseEntity<String> response = addupdateService.viewcoupons();
		return new ResponseEntity<String>(response.getBody(), response.getStatusCode());
	}

	@GetMapping("/viewservices")
	public ResponseEntity<String> viewservices() {
		ResponseEntity<String> response = addupdateService.viewservices();
		return new ResponseEntity<String>(response.getBody(), response.getStatusCode());
	}

	@PostMapping("/editservice")
	public ResponseEntity<String> editservice(@RequestBody EditServiceRequest editServiceRequest) {
		ResponseEntity<String> response = addupdateService.editservice(editServiceRequest);
		return new ResponseEntity<String>(response.getBody(), response.getStatusCode());
	}

	@PostMapping("/editservicestatus")
	public ResponseEntity<String> editservicestatus(@RequestParam("id") String id) {
		ResponseEntity<String> response = addupdateService.editservicestatus(id);
		return new ResponseEntity<String>(response.getBody(), response.getStatusCode());
	}

	@PostMapping("/addservice")
	public ResponseEntity<String> addservice(@RequestBody EditServiceRequest editServiceRequest) {
		ResponseEntity<String> response = addupdateService.addservice(editServiceRequest);
		return new ResponseEntity<String>(response.getBody(), response.getStatusCode());
	}

	@PostMapping("/delservice")
	public ResponseEntity<String> delservice(@RequestParam("id") String id) {
		ResponseEntity<String> response = addupdateService.delservice(id);
		return new ResponseEntity<String>(response.getBody(), response.getStatusCode());
	}

	@GetMapping("/viewsliders")
	public ResponseEntity<String> viewsliders() {
		ResponseEntity<String> response = addupdateService.viewsliders();
		return new ResponseEntity<String>(response.getBody(), response.getStatusCode());
	}

	@PostMapping("/editslider")
	public ResponseEntity<String> editslider(@RequestParam(required = true, name = "file") MultipartFile mpfile,
			@RequestParam("name") String imagename, @RequestParam("id") String id,
			@RequestParam("status") String status) {
		ResponseEntity<String> response = addupdateService.editslider(mpfile, imagename, id, status);
		return new ResponseEntity<String>(response.getBody(), response.getStatusCode());
	}

	@PostMapping("/editsliderstatus")
	public ResponseEntity<String> editsliderstatus(@RequestParam("id") String id) {
		ResponseEntity<String> response = addupdateService.editsliderstatus(id);
		return new ResponseEntity<String>(response.getBody(), response.getStatusCode());
	}

	@PostMapping("/addslider")
	public ResponseEntity<String> addslider(@RequestParam(required = true, name = "file") MultipartFile mpfile,
			@RequestParam("name") String name, @RequestParam("status") String status) {
		ResponseEntity<String> response = addupdateService.addslider(mpfile, name, status);
		return new ResponseEntity<String>(response.getBody(), response.getStatusCode());
	}

	@GetMapping("/viewsmsvendor")
	public ResponseEntity<String> viewsmsvendor() {
		ResponseEntity<String> response = addupdateService.viewsmsvendor();
		return new ResponseEntity<String>(response.getBody(), response.getStatusCode());
	}

	@PostMapping("/addsmsvendor")
	public ResponseEntity<String> addsmsvendor(@RequestBody AddSMSTemplateRequest smsTemplateRequest) {
		ResponseEntity<String> response = addupdateService.addsmsvendor(smsTemplateRequest);
		return new ResponseEntity<String>(response.getBody(), response.getStatusCode());
	}

	@PostMapping("/delsmsvendor")
	public ResponseEntity<String> delsmsvendor(@RequestParam("id") String id) {
		ResponseEntity<String> response = addupdateService.delsmsvendor(id);
		return new ResponseEntity<String>(response.getBody(), response.getStatusCode());
	}

	@PostMapping("/editsmsvendor")
	public ResponseEntity<String> editsmsvendor(@RequestParam("id") String id, @RequestParam("vendor") String vendor) {
		ResponseEntity<String> response = addupdateService.editsmsvendor(id, vendor);
		return new ResponseEntity<String>(response.getBody(), response.getStatusCode());
	}

	@GetMapping("/viewfaq")
	public ResponseEntity<String> viewfaq() {
		ResponseEntity<String> response = addupdateService.viewfaq();
		return new ResponseEntity<String>(response.getBody(), response.getStatusCode());
	}

	@PostMapping("/editfaq")
	public ResponseEntity<String> editfaq(@RequestBody FaqRequest faq) {
		ResponseEntity<String> response = addupdateService.editfaq(faq);
		return new ResponseEntity<String>(response.getBody(), response.getStatusCode());
	}

	@PostMapping("/addfaq")
	public ResponseEntity<String> addfaq(@RequestBody FaqRequest faq) {
		ResponseEntity<String> response = addupdateService.addfaq(faq);
		return new ResponseEntity<String>(response.getBody(), response.getStatusCode());
	}

	@PostMapping("/delfaq")
	public ResponseEntity<String> deletefaq(@RequestParam("id") String id) {
		ResponseEntity<String> response = addupdateService.deletefaq(id);
		return new ResponseEntity<String>(response.getBody(), response.getStatusCode());
	}

	@PostMapping("/editfooter")
	public ResponseEntity<String> editfooter(@RequestParam(required = false, name = "header") String header,
			@RequestParam(required = false, name = "aboutus") String aboutus,
			@RequestParam(required = false, name = "chooseus") String chooseus,
			@RequestParam(required = false, name = "mission") String mission,
			@RequestParam(required = false, name = "vision") String vision,
			@RequestParam(required = false, name = "address") String address,
			@RequestParam(required = false, name = "email") String email,
			@RequestParam(required = false, name = "mobile") String mobile,
			@RequestParam(required = false, name = "whatsapp") String whatsapp,
			@RequestParam(required = false, name = "noticebar") String noticebar,
			@RequestParam(required = false, name = "ppolicy") String ppolicy,
			@RequestParam(required = false, name = "refpolicy") String refpolicy,
			@RequestParam(required = false, name = "tnc") String tnc) {
		ResponseEntity<String> response = addupdateService.editfooter(header, aboutus, chooseus, mission, vision,
				address, email, mobile, whatsapp, noticebar, ppolicy, refpolicy, tnc);
		return new ResponseEntity<String>(response.getBody(), response.getStatusCode());
	}

	/*
	 * @GetMapping("/dailyCodes") public void scheduleDailyActiveReCodeSystem() {
	 * addupdateService.scheduleDailyActiveReCodeSystem(); //return new
	 * ResponseEntity<String>(response.getBody(), response.getStatusCode()); }
	 */

}
