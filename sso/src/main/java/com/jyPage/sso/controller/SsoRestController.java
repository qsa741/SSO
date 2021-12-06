package com.jyPage.sso.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jyPage.sso.entity.Users;
import com.jyPage.sso.service.SsoServiceImpl;

@RestController
@RequestMapping("/users")
public class SsoRestController {
	
	@Autowired
	private SsoServiceImpl ssoService;

	// ID, Email, Phone 유효성 검사
	@RequestMapping("/signUpCheck")
	public long signUpCheck(Users user) {
		return ssoService.signUpCheck(user);
	}
	
	// Email 확인
	@RequestMapping(value = "/emailCheck")
	public long emailCheck(Users user) {
		return ssoService.emailCheck(user);
	}
	
	// DB ID/PW 전송
	@RequestMapping("/sendDbInfo")
	public Map<String, String> sendDbInfo() {
		Map<String, String> map = new HashMap<String, String>();
		
		map.put("dbId", ssoService.sessionDBID());
		map.put("dbPw", ssoService.sessionDBPW());
		
		return map;
	}
	
}
