package com.jyPage.sso.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.jyPage.exception.JYException;
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
	@RequestMapping("/emailCheck")
	public long emailCheck(Users user) {
		return ssoService.emailCheck(user);
	}
	
	@RequestMapping("/errorTest")
	public void errorTest() throws JYException {
		throw new JYException("test");
	}
}
