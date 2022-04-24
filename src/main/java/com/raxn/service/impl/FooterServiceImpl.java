package com.raxn.service.impl;

import java.util.List;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.raxn.entity.Gsetting;
import com.raxn.repository.GsettingRepository;
import com.raxn.service.FooterService;
import com.raxn.util.service.AppConstant;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class FooterServiceImpl implements FooterService {

	private static final Logger LOGGER = LoggerFactory.getLogger(FooterServiceImpl.class);

	@Autowired
	GsettingRepository gsetrepo;

	private static String successStatus = "Success";
	private static String errorStatus = "Error";

	@Override
	public ResponseEntity<String> getFooterSettingByName(String name) throws JsonProcessingException {
		LOGGER.info("Entered getFooterSettingByName() -> Start");
		LOGGER.info("Parameter name -> " + name);
		ObjectMapper objMapper = new ObjectMapper();
		JSONObject response = new JSONObject();

		Gsetting gobj = gsetrepo.findByName(name.trim());
		if (null == gobj) {
			response.put(AppConstant.STATUS, errorStatus);
			response.put(AppConstant.MESSAGE, "Footer setting with name " + name + " is not found in database");
			LOGGER.error("Footer setting with name " + name + " is not found in database");
			return new ResponseEntity<String>(response.toString(), HttpStatus.NOT_FOUND);
		}
		LOGGER.info(name + " values -> " + objMapper.writeValueAsString(gobj));
		return new ResponseEntity<String>(objMapper.writeValueAsString(gobj), HttpStatus.OK);

	}

	@Override
	public ResponseEntity<String> getFooterSettingById(Integer id) throws JsonProcessingException {
		LOGGER.info("Entered getFooterSettingById() -> Start");
		LOGGER.info("Parameter id -> " + id);
		ObjectMapper objMapper = new ObjectMapper();
		JSONObject response = new JSONObject();

		Gsetting gobj = gsetrepo.findById(id).orElse(null);
		if (null == gobj) {
			response.put(AppConstant.STATUS, errorStatus);
			response.put(AppConstant.MESSAGE, "Footer setting with id " + id + " is not found in database");
			LOGGER.error("Footer setting with id " + id + " is not found in database");
			return new ResponseEntity<String>(response.toString(), HttpStatus.NOT_FOUND);
		}
		LOGGER.info(id + " values -> " + objMapper.writeValueAsString(gobj));
		return new ResponseEntity<String>(objMapper.writeValueAsString(gobj), HttpStatus.OK);
	}

	@Override
	public ResponseEntity<String> getAllFooterSettings() throws JsonProcessingException {
		LOGGER.info("Entered getAllFooterSettings() -> Start");
		ObjectMapper objMapper = new ObjectMapper();
		JSONObject response = new JSONObject();

		List<Gsetting> list = gsetrepo.findAll();
		if (null == list) {
			response.put(AppConstant.STATUS, errorStatus);
			response.put(AppConstant.MESSAGE, "None of footer setting is found in database");
			LOGGER.error("None of footer setting is found in database");
			return new ResponseEntity<String>(response.toString(), HttpStatus.NOT_FOUND);
		}
		LOGGER.info("All footer values -> " + objMapper.writeValueAsString(list));
		return new ResponseEntity<String>(objMapper.writeValueAsString(list), HttpStatus.OK);
	}

	@Override
	public ResponseEntity<String> saveFooterSetting(Gsetting gs) throws JsonProcessingException {
		LOGGER.info("Entered saveFooterSetting() -> Start");
		ObjectMapper objMapper = new ObjectMapper();
		LOGGER.info("Parameter GS data to save ->" + objMapper.writeValueAsString(gs));

		JSONObject response = new JSONObject();

		Gsetting gobj = gsetrepo.save(gs);
		if (null == gobj) {
			response.put(AppConstant.STATUS, errorStatus);
			response.put(AppConstant.MESSAGE, "Footer setting not saved in database");
			LOGGER.error("Footer setting not saved in database");
			return new ResponseEntity<String>(response.toString(), HttpStatus.NOT_FOUND);
		}
		LOGGER.info("Saved footer values -> " + objMapper.writeValueAsString(gobj));
		return new ResponseEntity<String>(objMapper.writeValueAsString(gobj), HttpStatus.OK);
	}

	@Override
	public ResponseEntity<String> saveAllFooterSetting(List<Gsetting> gs) throws JsonProcessingException {
		LOGGER.info("Entered saveAllFooterSetting() -> Start");
		ObjectMapper objMapper = new ObjectMapper();
		LOGGER.info("Parameter List of GS data to save ->" + objMapper.writeValueAsString(gs));
		JSONObject response = new JSONObject();

		List<Gsetting> listGobj = gsetrepo.saveAll(gs);
		if (null == listGobj) {
			response.put(AppConstant.STATUS, errorStatus);
			response.put(AppConstant.MESSAGE, "List of Footer setting not saved in database");
			LOGGER.error("List of Footer setting not saved in database");
			return new ResponseEntity<String>(response.toString(), HttpStatus.NOT_FOUND);
		}
		LOGGER.info("Saved footer values -> " + objMapper.writeValueAsString(listGobj));
		return new ResponseEntity<String>(objMapper.writeValueAsString(listGobj), HttpStatus.OK);
	}

	@Override
	public ResponseEntity<String> updateFooterSetting(Gsetting gs) throws JsonProcessingException {
		LOGGER.info("Entered updateFooterSetting() -> Start");
		ObjectMapper objMapper = new ObjectMapper();
		LOGGER.info("Parameter GS data to update ->" + objMapper.writeValueAsString(gs));
		JSONObject response = new JSONObject();

		Gsetting gobj = gsetrepo.findById(gs.getId()).orElse(null);
		if (null == gobj) {
			response.put(AppConstant.STATUS, errorStatus);
			response.put(AppConstant.MESSAGE, "Given footer setting not found in database");
			LOGGER.error("Given footer setting not found in database");
			return new ResponseEntity<String>(response.toString(), HttpStatus.NOT_FOUND);
		}

		gobj.setName(gs.getName());
		gobj.setValue(gs.getValue());
		Gsetting gobj2 = gsetrepo.save(gobj);
		LOGGER.info("Updated footer values -> " + objMapper.writeValueAsString(gobj2));
		return new ResponseEntity<String>(objMapper.writeValueAsString(gobj2), HttpStatus.OK);
	}

	@Override
	public ResponseEntity<String> deleteFooterSetting(int id) {
		LOGGER.info("Entered deleteFooterSetting() -> Start");
		LOGGER.info("Parameter id ->" + id);

		JSONObject response = new JSONObject();
		Gsetting gobj = gsetrepo.findById(id).orElse(null);
		if (null == gobj) {
			response.put(AppConstant.STATUS, errorStatus);
			response.put(AppConstant.MESSAGE, "Footer setting with id " + id + " not found in database");
			LOGGER.error("Footer setting with id " + id + " not found in database");
			return new ResponseEntity<String>(response.toString(), HttpStatus.NOT_FOUND);
		}
		gsetrepo.delete(gobj);
		response.put(AppConstant.STATUS, successStatus);
		response.put(AppConstant.MESSAGE, "Footer setting with id " + id + " deleted from database");
		LOGGER.info("Footer setting with id " + id + " deleted from database");
		// return ResponseEntity.ok().build();
		return new ResponseEntity<String>(response.toString(), HttpStatus.OK);
	}

}
