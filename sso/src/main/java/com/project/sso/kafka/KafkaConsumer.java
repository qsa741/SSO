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
	
	@KafkaListener(topics = "saveUser", groupId = "sso")
	public void consume(String data) throws Exception {
		System.out.println(String.format("Consumed message : %s", data));
		JSONObject json = new JSONObject(data);
		String id = (String)json.get("id");
		if(id.equals("JY-SCRIPT")) {
			networkService.sendScript(json);
		} else if(id.equals("JY-SAVE-02")) {
			kafkaService.saveUser(json);
		}
	}
	
}
