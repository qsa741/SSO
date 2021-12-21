package com.jyPage.network;

import java.util.Date;
import java.util.Objects;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.jyPage.kafka.KafkaProducer;
import com.jyPage.sso.config.Action;
import com.jyPage.sso.config.SAVE;
import com.jyPage.sso.entity.Users;
import com.jyPage.sso.sql.SsoSQL;

@Service
public class NetworkServiceImpl implements NetworkService {

	@Value("${save.agent.id}")
	private String saveAgentId;

	@Value("${save.agent.network}")
	private String saveAgentNetwork;

	@Value("${action.agent.id}")
	private String actionAgentId;

	@Value("${sso.properties.network}")
	private String ssoPropertiesNetwork;

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
		if (Objects.equals(ssoPropertiesNetwork,agentNetwork)) {
			result = true;
		}

		return result;
	}

	// Save Agent에 데이터 채우고 DB/KAFKA 명령어 전송
	@Override
	public void saveUser(Users user, String action) throws Exception {
		JSONObject json = new JSONObject();
		json.put("id", saveAgentId);
		// 망이 같으면 DB, 다르면 KAFKA로 타입 전송
		if (networkCheck(saveAgentNetwork)) {
			json.put("type", SAVE.DB.name());
		} else {
			json.put("type", SAVE.KAFKA.name());
		}
		json.put("time", new Date().toString());
		json.put("data", user.toString());

		ssoSQL.saveUserAction(action, user.getId());
		producer.sendUserSave(json);
	}

	// UserAction 테이블을 action 별로 정리 후 json으로 전송
	// 같은망일때 DB로, 다른망일때 KAFKA로 type 세팅
	@Override
	public void settingUserAction(JSONObject data) throws Exception {
		JSONObject create = ssoSQL.getUserAction(Action.CREATE.name());
		JSONObject read = ssoSQL.getUserAction(Action.READ.name());
		JSONObject update = ssoSQL.getUserAction(Action.UPDATE.name());
		JSONObject delete = ssoSQL.getUserAction(Action.DELETE.name());

		JSONObject json = new JSONObject();
		json.put(Action.CREATE.name(), create);
		json.put(Action.READ.name(), read);
		json.put(Action.UPDATE.name(), update);
		json.put(Action.DELETE.name(), delete);

		if (Objects.equals(data.get("type"),SAVE.DB.name())) {
			ssoSQL.actionSchedulerSave(json.toString());
		} else if (Objects.equals(data.get("type"),SAVE.KAFKA.name())) {
			JSONObject kafkaData = new JSONObject();
			kafkaData.put("id", actionAgentId + "-02");
			kafkaData.put("data", json.toString());
			kafkaData.put("time", new Date().toString());

			this.producer.sendUserAction(kafkaData);
		}

	}

}
