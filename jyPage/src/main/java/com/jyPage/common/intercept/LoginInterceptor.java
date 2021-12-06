package com.jyPage.common.intercept;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

@SuppressWarnings("deprecation")
public class LoginInterceptor extends HandlerInterceptorAdapter {

	// 일부 페이지 비로그인으로 접근시 로그인 페이지로 이동
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		final HttpSession session = request.getSession();
		String path = request.getRequestURI();

		if (path.contains("/users/modifyUser") || path.contains("/users/deleteUser")) {
			if (session.getAttribute("JYSESSION") == null) {
				response.sendRedirect("/users/signIn");
				return false;
			}
		}
		
		if(path.contains("/users/signUp") || path.contains("/users/signIn")) {
			if (session.getAttribute("JYSESSION") != null) {
				response.sendRedirect("/dbms/dbms");
				return false;
			}
		}

		return true;
	}
	
}
