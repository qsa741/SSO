package com.jyPage.sso.scheduler;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.jyPage.sso.entity.Users;
import com.jyPage.sso.repository.UsersRepository;

@Service
public class ScheduleServiceImpl implements ScheduleService {

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

			user.setId((String) json.get("id"));
			user.setPw((String) json.get("pw"));
			user.setEmail((String) json.get("email"));
			user.setPhone((String) json.get("phone"));
			user.setDbId((String) json.get("dbId"));
			user.setDbPw((String) json.get("dbPw"));
			if (json.get("signUpDate").equals("null")) {
				time = fmt.format(new Date());
				user.setSignUpDate(fmt.parse(time));
			} else {
				user.setSignUpDate((Date) json.get("signUpDate"));
			}
			user.setState((String) json.get("state"));

			userRepository.save(user);
		}

		// System.out.println(new Date().toString() + " -- User Scheduler " +
		// list.size() + " rows execute");
	}

	// 하루 간격으로 탈퇴한지 24개월 지난 계정 삭제
	@Scheduled(cron = "0 0 0 * * *")
	@Override
	public void deleteUserScheduler() throws Exception {
		scheduleSQL.deleteUserScheduler();
	}

}
