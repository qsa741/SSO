package com.jyPage.sso.scheduler;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.jyPage.sso.entity.Users;
import com.jyPage.sso.repository.UsersRepository;
import com.jyPage.sso.sql.ScheduleSQL;

@Service
public class ScheduleServiceImpl implements ScheduleService {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private ScheduleSQL scheduleSQL;

	@Autowired
	private UsersRepository userRepository;

	// 10초 간격으로 유저 가입/수정 메서드 실행
	@Scheduled(cron = "0/10 * * * * *")
	@Override
	public void userScheduler() throws Exception {
		List<Map<String, Object>> list = scheduleSQL.userScheduler();
		Map<String, Object> map;
		Users user;
		SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String time = fmt.format(new Date());
		for (int i = 0; i < list.size(); i++) {

			map = list.get(i);
			// userScheduler 테이블의 DATA Column
			JSONObject json = new JSONObject((String) map.get("DATA"));
			user = new Users();

			user.setId(json.getString("id"));
			user.setPw(json.getString("pw"));
			user.setEmail(json.getString("email"));
			user.setPhone(json.getString("phone"));
			user.setDbId(json.getString("dbId"));
			user.setDbPw(json.getString("dbPw"));
			
			if (Objects.equals(json.getString("signUpDate"),"null")) {
				time = fmt.format(new Date());
				user.setSignUpDate(fmt.parse(time));
			} else {
				user.setSignUpDate(fmt.parse(json.getString("signUpDate")));
			}
			user.setState(json.getString("state"));

			userRepository.save(user);

			logger.info(new Date().toString() + " -- User Scheduler execute : " + json.getString("id"));
		}

	}

	// 하루 간격으로 탈퇴한지 24개월 지난 계정 삭제
	@Scheduled(cron = "0 0 0 * * *")
	@Override
	public void deleteUserScheduler() throws Exception {
		scheduleSQL.deleteUserScheduler();
	}

}
