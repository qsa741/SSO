package com.project.sso.service;

import org.json.JSONObject;

public interface KafkaService {

	public void saveUser(JSONObject data) throws Exception;
	
}
