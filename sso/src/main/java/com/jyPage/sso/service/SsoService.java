package com.jyPage.sso.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jyPage.sso.entity.Users;


public interface SsoService {
	
	// 회원가입 중복여부 확인
	public long signUpCheck(Users user);
	
	// 아이디, 이메일 매칭 여부
	public long emailCheck(Users user);
	
	// 메일로 비밀번호 보내기
	public void sendMail(Users user) throws Exception;
	
	// User 저장
	public void saveUser(Users user)  throws Exception;
	
	// Sign In
	public int signIn(Users user, String auto, HttpServletResponse response) throws Exception;
	
	// 세션 sid로 user 가져오기
	public Users getSessionUser() throws Exception;
	
	// user 삭제
	public void deleteUser(HttpServletRequest request, HttpServletResponse response) throws Exception;
	
	// 로그아웃
	public void signOut(HttpServletRequest request, HttpServletResponse response);
	
	// 자동 로그인 확인
	public boolean autoSignInCheck(HttpServletRequest request) throws Exception ;
	
}
