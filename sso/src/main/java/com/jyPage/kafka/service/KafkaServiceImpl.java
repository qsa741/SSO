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
		SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		JSONObject json = new JSONObject(data.getString("data").toString());
		Users user = new Users();
		
		user.setId(json.getString("id"));
		user.setPw(json.getString("pw"));
		user.setEmail(json.getString("email"));
		user.setPhone(json.getString("phone"));
		user.setDbId(json.getString("dbId"));
		user.setDbPw(json.getString("dbPw"));

		if (json.getString("signUpDate").equals("null")) {
			String time = fmt.format(new Date());
			user.setSignUpDate(fmt.parse(time));
		} else {
			user.setSignUpDate(fmt.parse(json.getString("signUpDate")));
		}

		user.setUnSignDate(null);
		user.setState(json.getString("state"));

		userRepository.save(user);
	}

}
