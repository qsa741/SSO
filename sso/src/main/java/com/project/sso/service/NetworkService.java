package com.project.sso.service;

import org.json.JSONObject;

import com.project.sso.entity.Users;

public interface NetworkService {
	
	public void saveUser(Users user, String action) throws Exception;
	
	public boolean networkCheck(String agentNetwork);
	
	public void settingUserAction(JSONObject data) throws Exception;
}
