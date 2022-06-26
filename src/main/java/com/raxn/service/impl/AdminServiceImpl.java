package com.raxn.service.impl;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

import javax.imageio.ImageIO;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.raxn.entity.Admin;
import com.raxn.entity.Slider;
import com.raxn.repository.AdminRepository;
import com.raxn.repository.ServiceRepository;
import com.raxn.repository.SliderRepository;
import com.raxn.service.AdminService;
import com.raxn.util.service.AppConstant;
import com.raxn.util.service.CommonServiceUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class AdminServiceImpl implements AdminService {

	private static final Logger LOGGER = LoggerFactory.getLogger(AdminServiceImpl.class);

	@Autowired
	AdminRepository adminrepo;

	@Autowired
	SliderRepository sliderrepo;

	@Autowired
	ServiceRepository servicerepo;

	@Value("${slider.path}")
	private String SLIDER_PATH;

	private static String successStatus = "Success";
	private static String errorStatus = "Error";

	@Override
	public ResponseEntity<String> getAdminByUserName(String username) throws JsonProcessingException {
		LOGGER.info("Entered getAdminByUserName() -> Start");
		LOGGER.info("Parameter username -> " + username);
		ObjectMapper objMapper = new ObjectMapper();
		JSONObject response = new JSONObject();

		Admin adminobj = adminrepo.findByUserName(username);
		if (null == adminobj) {
			response.put(AppConstant.STATUS, errorStatus);
			response.put(AppConstant.MESSAGE, "No Admin found with username:" + username);
			LOGGER.error("No Admin found with username:" + username);
			return new ResponseEntity<String>(response.toString(), HttpStatus.NOT_FOUND);
		}
		LOGGER.info("Admin found with username -> " + objMapper.writeValueAsString(adminobj));
		return new ResponseEntity<String>(objMapper.writeValueAsString(adminobj), HttpStatus.OK);
	}

	@Override
	public ResponseEntity<String> addAdmin(Admin admin) throws JsonProcessingException {
		LOGGER.info("Entered postAdmin() -> Start");
		ObjectMapper objMapper = new ObjectMapper();
		LOGGER.info("Parameter admin -> " + objMapper.writeValueAsString(admin));

		JSONObject response = new JSONObject();

		Admin adminobj = adminrepo.save(admin);
		if (null == adminobj) {
			response.put(AppConstant.STATUS, errorStatus);
			response.put(AppConstant.MESSAGE, "Admin not saved in database");
			LOGGER.error("Admin not saved in database");
			return new ResponseEntity<String>(response.toString(), HttpStatus.NOT_FOUND);
		}
		LOGGER.info("Admin saved in DB -> " + objMapper.writeValueAsString(adminobj));
		return new ResponseEntity<String>(objMapper.writeValueAsString(adminobj), HttpStatus.OK);
	}

	@Override
	public ResponseEntity<String> postSlider(MultipartFile mpfile, String status) throws IOException {
		LOGGER.info("Entered postSlider() -> Start");
		ObjectMapper objMapper = new ObjectMapper();
		JSONObject response = new JSONObject();
		String[] extnarray = { "png", "jpg", "jpeg" };
		Slider slider = new Slider();
		long bytes = mpfile.getSize();
		if (bytes == 0) {
			response.put(AppConstant.STATUS, errorStatus);
			response.put(AppConstant.MESSAGE, "Banner image is empty");
			LOGGER.error("Banner image is empty");
			return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
		}

		if (null != mpfile && !mpfile.isEmpty()) {
			String filename = mpfile.getOriginalFilename();
			LOGGER.info("filename=" + filename);
			int extnPointer = filename.lastIndexOf(".");
			if (extnPointer == -1) {
				response.put(AppConstant.STATUS, errorStatus);
				response.put(AppConstant.MESSAGE, "File without extn is not allowed");
				LOGGER.error("File without extn is not allowed");
				return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
			}
			String extn = filename.substring(extnPointer + 1);
			boolean checkExtn = Arrays.asList(extnarray).contains(extn);
			if (!checkExtn) {
				response.put(AppConstant.STATUS, errorStatus);
				response.put(AppConstant.MESSAGE, extn + " is not allowed");
				LOGGER.error(extn + " is not allowed");
				return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
			}
			BufferedImage bimg = ImageIO.read(mpfile.getInputStream());
			int width = bimg.getWidth();
			int height = bimg.getHeight();

			LOGGER.info("width=" + width);
			LOGGER.info("height=" + height);
			if (width > 550 || height > 165) {
				LOGGER.error("File dimension should be 550 by 165");
				response.put(AppConstant.STATUS, errorStatus);
				response.put(AppConstant.MESSAGE, "File dimension should be 550 by 165");
				return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
			}

			double kilobytes = (bytes / 1024);
			double megabytes = (kilobytes / 1024);
			if (megabytes > 1.0) {
				LOGGER.error("File size is more than 1MB");
				response.put(AppConstant.STATUS, errorStatus);
				response.put(AppConstant.MESSAGE, "File size is more than 1MB");
				return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
			}

			java.nio.file.Path pathTo = Paths.get(SLIDER_PATH);
			String destFileName = CommonServiceUtil.genTempname() + "." + extn;
			// java.nio.file.Path pathTo = Paths.get("/home/ec2-user/santosh/");
			java.nio.file.Path destination = Paths.get(pathTo.toString() + "\\" + destFileName);
			LOGGER.info("destination = " + destination);
			InputStream in;
			try {
				in = mpfile.getInputStream();
				// Files.deleteIfExists(destination);
				Files.copy(in, destination);
			} catch (IOException e) {
				LOGGER.error(e.getMessage());
			}
			slider.setImage(destFileName);
			if (status.equalsIgnoreCase("inactive")) {
				slider.setStatus("0");
			} else {
				slider.setStatus("1");
			}

			sliderrepo.save(slider);
		}
		response.put(AppConstant.STATUS, successStatus);
		response.put(AppConstant.MESSAGE, "Slider saved successfully");
		LOGGER.info("Slider saved successfully");
		return new ResponseEntity<String>(response.toString(), HttpStatus.OK);
	}

	@Override
	public ResponseEntity<String> updateSlider(MultipartFile mpfile, String status, String id) throws IOException {
		LOGGER.info("Entered updateSlider() -> Start");
		ObjectMapper objMapper = new ObjectMapper();
		JSONObject response = new JSONObject();
		String[] extnarray = { "png", "jpg", "jpeg" };
		Slider slider = new Slider();
		long bytes = mpfile.getSize();
		if (bytes == 0) {
			response.put(AppConstant.STATUS, errorStatus);
			response.put(AppConstant.MESSAGE, "Banner image is empty");
			LOGGER.error("Banner image is empty");
			return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
		}

		if (null != mpfile && !mpfile.isEmpty()) {
			String filename = mpfile.getOriginalFilename();
			LOGGER.info("filename=" + filename);
			int extnPointer = filename.lastIndexOf(".");
			if (extnPointer == -1) {
				response.put(AppConstant.STATUS, errorStatus);
				response.put(AppConstant.MESSAGE, "File without extn is not allowed");
				LOGGER.error("File without extn is not allowed");
				return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
			}
			String extn = filename.substring(extnPointer + 1);
			boolean checkExtn = Arrays.asList(extnarray).contains(extn);
			if (!checkExtn) {
				response.put(AppConstant.STATUS, errorStatus);
				response.put(AppConstant.MESSAGE, extn + " is not allowed");
				LOGGER.error(extn + " is not allowed");
				return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
			}
			BufferedImage bimg = ImageIO.read(mpfile.getInputStream());
			int width = bimg.getWidth();
			int height = bimg.getHeight();

			LOGGER.info("width=" + width);
			LOGGER.info("height=" + height);
			if (width > 550 || height > 165) {
				LOGGER.error("File dimension should be 550 by 165");
				response.put(AppConstant.STATUS, errorStatus);
				response.put(AppConstant.MESSAGE, "File dimension should be 550 by 165");
				return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
			}

			// long bytes = mpfile.getSize();
			double kilobytes = (bytes / 1024);
			double megabytes = (kilobytes / 1024);
			if (megabytes > 1.0) {
				LOGGER.error("File size is more than 1MB");
				response.put(AppConstant.STATUS, errorStatus);
				response.put(AppConstant.MESSAGE, "File size is more than 1MB");
				return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
			}

			java.nio.file.Path pathTo = Paths.get(SLIDER_PATH);
			String destFileName = filename;
			// java.nio.file.Path pathTo = Paths.get("/home/ec2-user/santosh/");
			java.nio.file.Path destination = Paths.get(pathTo.toString() + "\\" + destFileName);
			LOGGER.info("destination = " + destination);
			InputStream in;
			try {
				in = mpfile.getInputStream();
				Files.deleteIfExists(destination);
				Files.copy(in, destination);
			} catch (IOException e) {
				LOGGER.error(e.getMessage());
			}
			slider.setImage(destFileName);

			if (status.equalsIgnoreCase("inactive")) {
				slider.setStatus("0");
			} else {
				slider.setStatus("1");
			}

			slider.setId(Integer.parseInt(id));
			sliderrepo.save(slider);
		}
		response.put(AppConstant.STATUS, successStatus);
		response.put(AppConstant.MESSAGE, "Slider updated successfully");
		LOGGER.info("Slider updated successfully");
		return new ResponseEntity<String>(response.toString(), HttpStatus.OK);
	}

	@Override
	public ResponseEntity<String> postServiceIcon(com.raxn.entity.Service service) throws IOException {
		LOGGER.info("Entered postServiceIcon() -> Start");
		ObjectMapper objMapper = new ObjectMapper();
		JSONObject response = new JSONObject();
		com.raxn.entity.Service servicedata = new com.raxn.entity.Service();

		if (null == service.getName() || service.getName().isEmpty()) {
			response.put(AppConstant.STATUS, errorStatus);
			response.put(AppConstant.MESSAGE, "service name is empty or null");
			LOGGER.error("service name is empty or null");
			return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
		}
		if (service.getStatus().equalsIgnoreCase("inactive")) {
			servicedata.setStatus("0");
		} else {
			servicedata.setStatus("1");
		}

		if (null != service.getFafaicon() && !service.getFafaicon().isEmpty()) {
			servicedata.setFafaicon(service.getFafaicon().trim());
		}
		if (null != service.getUrl() && !service.getUrl().isEmpty()) {
			servicedata.setUrl(service.getUrl().trim());
		}

		String servicename = service.getName().trim();
		servicedata.setName(servicename);

		servicerepo.save(servicedata);

		response.put(AppConstant.STATUS, successStatus);
		response.put(AppConstant.MESSAGE, "service saved successfully");
		LOGGER.info("service saved successfully");
		return new ResponseEntity<String>(response.toString(), HttpStatus.OK);
	}

	@Override
	public ResponseEntity<String> updateServiceIcon(com.raxn.entity.Service service) throws IOException {
		LOGGER.info("Entered updateServiceIcon() -> Start");
		ObjectMapper objMapper = new ObjectMapper();
		JSONObject response = new JSONObject();
		com.raxn.entity.Service servicedata = new com.raxn.entity.Service();

		if (null == service.getName() || service.getName().isEmpty()) {
			response.put(AppConstant.STATUS, errorStatus);
			response.put(AppConstant.MESSAGE, "service name is empty or null");
			LOGGER.error("service name is empty or null");
			return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
		}
		if (service.getId() <= 0) {
			response.put(AppConstant.STATUS, errorStatus);
			response.put(AppConstant.MESSAGE, "service id is wrong");
			LOGGER.error("service name is wrong");
			return new ResponseEntity<String>(response.toString(), HttpStatus.BAD_REQUEST);
		}
		if (service.getStatus().equalsIgnoreCase("inactive")) {
			servicedata.setStatus("0");
		} else {
			servicedata.setStatus("1");
		}

		if (null != service.getFafaicon() && !service.getFafaicon().isEmpty()) {
			servicedata.setFafaicon(service.getFafaicon().trim());
		}
		if (null != service.getUrl() && !service.getUrl().isEmpty()) {
			servicedata.setUrl(service.getUrl().trim());
		}

		String servicename = service.getName().trim();
		servicedata.setName(servicename);
		servicedata.setId(service.getId());

		servicerepo.save(servicedata);

		response.put(AppConstant.STATUS, successStatus);
		response.put(AppConstant.MESSAGE, "service updated successfully");
		LOGGER.info("service updated successfully");
		return new ResponseEntity<String>(response.toString(), HttpStatus.OK);
	}

}
