package com.project.sso.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.sso.entity.Users;
import com.project.sso.kafka.KafkaProducer;
import com.project.sso.properties.Save;
import com.project.sso.properties.Sso;


@Service
public class NetworkServiceImpl implements NetworkService{

	@Autowired
	private Save save;
	
	@Autowired
	private Sso sso;
	
	private final KafkaProducer producer;
	
	@Autowired
	NetworkServiceImpl(KafkaProducer producer) {
		this.producer = producer;
	}
	
	@Override
	public void sendScript(JSONObject json) {
		this.producer.sendDBMS(json);
	}
	
	// Agent와 같은 망인지 확인
	@Override
	public boolean networkCheck(String agentNetwork) {
		boolean result = false;
		Map<String, String> network = sso.getProperties().get(0);
		if(network.get("network").equals(agentNetwork)) {
			result = true;
		}
		
		return result;
	}
		
	// Save Agent에 데이터 채우고 DB/KAFKA 명령어 전송
	@Override
	public void saveUser(Users user) throws Exception {
		List<Map<String, String>> agent = save.getAgent();
		JSONObject json = new JSONObject();
		json.put("id",agent.get(0).get("id"));
		// 망이 같으면 DB, 다르면 KAFKA로 타입 전송
		if(networkCheck(agent.get(1).get("network"))) {
			json.put("type", "DB");
		} else {
			json.put("type", "KAFKA");
		}
		json.put("time", new Date().toString());
		json.put("data", user.toString());
		
		producer.sendDBMS(json);
	}
}
