package com.jyPage.network;

import org.json.JSONObject;

import com.jyPage.sso.entity.Users;

public interface NetworkService {
	
	public void saveUser(Users user, String action) throws Exception;
	
	public boolean networkCheck(String agentNetwork);
	
	public void settingUserAction(JSONObject data) throws Exception;
}
