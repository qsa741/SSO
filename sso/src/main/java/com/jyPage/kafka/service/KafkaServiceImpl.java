package com.jyPage.kafka.service;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jyPage.sso.entity.Users;
import com.jyPage.sso.repository.UsersRepository;

// 명령어가 Kafka로 왔을 경우 실행 함수들
@Service
public class KafkaServiceImpl implements KafkaService {

	@Autowired
	private UsersRepository userRepository;

	// kafka로 온 user 데이터 저장
	@Override
	public void saveUser(JSONObject data) throws Exception {
		JSONObject json = new JSONObject(data.get("data").toString());

		Users user = new Users();
		user.setId((String) json.get("id"));
		user.setPw((String) json.get("pw"));
		user.setEmail((String) json.get("email"));
		user.setPhone((String) json.get("phone"));
		user.setDbId((String) json.get("dbId"));
		user.setDbPw((String) json.get("dbPw"));

		if (json.get("signUpDate").equals("null")) {
			SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String time = fmt.format(new Date());
			user.setSignUpDate(fmt.parse(time));
		} else {
			user.setSignUpDate((Date) json.get("signUpDate"));
		}

		user.setUnSignDate(null);
		user.setState((String) json.get("state"));

		userRepository.save(user);
	}

}
