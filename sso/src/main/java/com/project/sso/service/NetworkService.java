package com.project.sso.service;

import org.json.JSONObject;

import com.project.sso.entity.Users;

public interface NetworkService {
	
	public void sendScript(JSONObject json);
	
	public void saveUser(Users user) throws Exception;
	
	public boolean networkCheck(String agentNetwork);
	
}
