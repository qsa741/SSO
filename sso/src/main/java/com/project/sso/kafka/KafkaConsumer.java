package com.project.sso.kafka;


import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.project.sso.service.KafkaServiceImpl;
import com.project.sso.service.NetworkServiceImpl;

@Service
public class KafkaConsumer {

	@Autowired
	private NetworkServiceImpl networkService;
	
	@Autowired
	private KafkaServiceImpl kafkaService;
	
	@KafkaListener(topics = "userTopic", groupId = "sso")
	public void saveUser(String data) throws Exception {
		JSONObject json = new JSONObject(data);
		
		String id = (String)json.get("id");
		if(id.equals("JY-SAVE-02")) {
			kafkaService.saveUser(json);
		}
	}
	
	@KafkaListener(topics = "actionTopic", groupId = "sso") 
	public void settingUserAction(String data) throws Exception {
		JSONObject json = new JSONObject(data);
		
		String id = json.getString("id");
		if(id.equals("JY-ACTION")) {
			networkService.settingUserAction(json); 
		}
		
	}
}
