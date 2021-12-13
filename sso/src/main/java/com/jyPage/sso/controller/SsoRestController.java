package com.jyPage.sso.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

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
	
	@RequestMapping("/test")
	public void test() {
		System.out.println(RequestContextHolder.getRequestAttributes().getSessionId());
		System.out.println(RequestContextHolder.getRequestAttributes().getAttribute("JYDBID",
				RequestAttributes.SCOPE_SESSION));
	}
}
