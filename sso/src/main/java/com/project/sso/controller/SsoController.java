package com.project.sso.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.project.sso.entity.Users;
import com.project.sso.service.SsoServiceImpl;

@RequestMapping("/users")
@Controller
public class SsoController {
	
	@Autowired
	private SsoServiceImpl ssoService;
	
	// DBMS URL
	@Value("${dbms.url}")
	private String url;
	
	// 로그인 페이지 연결
	@RequestMapping("/signIn")
	public String signIn(HttpServletRequest request) throws Exception {
		String view = "";

		// 자동로그인시 메인으로 이동
		if (ssoService.autoSignInCheck(request)) {
			view = "redirect:" + url + "/dbms/dbms";
		} else {
			view = "signIn";
		}

		return view;
	}

	// 로그인 성공시 페이지 연결
	@PostMapping("/signInCheck")
	public String signInCheck(HttpServletResponse response, Users user, String auto, Model model) throws Exception {
		int res = ssoService.signIn(user, auto, response);
		String view = "";
		if (res == 1) {
			view = "redirect:" + url + "/dbms/dbms";
		} else {
			view = "signIn";
		}
		model.addAttribute("warning", res);
		
		return view;
	}

	// 회원가입 페이지 연결
	@RequestMapping("/signUp")
	public String signUp() throws Exception {
		return "signUp";
	}

	// 회원가입 or 회원정보 수정 성공시 데이터 저장
	@RequestMapping("/saveUser")
	public String test(Users user) throws Exception {
		ssoService.saveUser(user);
		
		return "redirect:/users/signIn";
	}

	// 회원 정보 수정
	@RequestMapping("/modifyUser")
	public String modifyUser(Model model) throws Exception {
		model.addAttribute("user", ssoService.getSessionUser());
		
		return "modifyUser";
	}

	// 회원 정보 삭제
	@RequestMapping("/deleteUser")
	public String update(HttpServletRequest request, HttpServletResponse response) throws Exception {
		ssoService.deleteUser(request, response);

		return "redirect:/users/signIn";
	}

	// 로그아웃
	@RequestMapping("/signOut")
	public String SignOut(HttpServletRequest request, HttpServletResponse response) throws Exception {
		ssoService.signOut(request, response);

		return "redirect:/users/signIn";
	}

	// 비밀번호 찾기 페이지 이동
	@RequestMapping("/forgetPassword")
	public String forgetPassword() throws Exception {
		return "forgetPassword";
	}

	// 이메일 전송 후 로그인 페이지로 이동
	@RequestMapping("/sendMail")
	public String sendMail(Users user) throws Exception {
		ssoService.sendMail(user);

		return "redirect:/users/signIn";
	}
	
}
