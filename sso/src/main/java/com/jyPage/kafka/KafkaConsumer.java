package com.jyPage.kafka;


import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.jyPage.kafka.service.KafkaServiceImpl;
import com.jyPage.network.NetworkServiceImpl;

@Service
public class KafkaConsumer {

	@Value("${save.agent.id}")
	private String saveAgentId;
	
	@Value("${action.agent.id}")
	private String actionAgentId;
	
	@Autowired
	private NetworkServiceImpl networkService;
	
	@Autowired
	private KafkaServiceImpl kafkaService;
	
	@KafkaListener(topics = "userTopic", groupId = "sso")
	public void saveUser(String data) throws Exception {
		JSONObject json = new JSONObject(data);
		
		String id = (String)json.get("id");
		if(id.equals(saveAgentId + "-02")) {
			kafkaService.saveUser(json);
		}
	}
	
	@KafkaListener(topics = "actionTopic", groupId = "sso") 
	public void settingUserAction(String data) throws Exception {
		JSONObject json = new JSONObject(data);
		
		String id = json.getString("id");
		if(id.equals(actionAgentId)) {
			networkService.settingUserAction(json); 
		}
	}
}
