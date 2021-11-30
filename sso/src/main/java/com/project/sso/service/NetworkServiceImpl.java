package com.project.sso.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.sso.entity.Users;
import com.project.sso.kafka.KafkaProducer;
import com.project.sso.properties.Action;
import com.project.sso.properties.Save;
import com.project.sso.properties.Sso;
import com.project.sso.sql.SsoSQL;


@Service
public class NetworkServiceImpl implements NetworkService{

	@Autowired
	private Save save;
	
	@Autowired
	private Action action;
	
	@Autowired
	private Sso sso;
	
	private final KafkaProducer producer;
	
	@Autowired
	NetworkServiceImpl(KafkaProducer producer) {
		this.producer = producer;
	}
	
	@Autowired
	private SsoSQL ssoSQL;
	
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
	public void saveUser(Users user, String action) throws Exception {
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
		
		ssoSQL.saveUserAction(action, user.getId());
		producer.sendDBMS(json);
	}
	
	// UserAction 테이블을 action 별로 정리 후 json으로 전송
	// 같은망일때 DB로, 다른망일때 KAFKA로 type 세팅
	@Override
	public void settingUserAction(JSONObject data) throws Exception {
		JSONObject create = ssoSQL.getUserAction("C");
		JSONObject read = ssoSQL.getUserAction("R");
		JSONObject update = ssoSQL.getUserAction("U");
		JSONObject delete = ssoSQL.getUserAction("D");
		
		JSONObject json = new JSONObject();
		json.put("create", create);
		json.put("read", read);
		json.put("update", update);
		json.put("delete", delete);
		
		if(data.get("type").equals("DB")) {
			ssoSQL.actionSchedulerSave(json.toString());
		} else if(data.get("type").equals("KAFKA")) {
			List<Map<String, String>> agent = action.getAgent();
			JSONObject kafkaData = new JSONObject();
			kafkaData.put("id", agent.get(0).get("id"));
			kafkaData.put("data", json.toString());
			kafkaData.put("time", new Date().toString());
			
			this.producer.sendUserAction(kafkaData);
		}
		
	}
	
}
