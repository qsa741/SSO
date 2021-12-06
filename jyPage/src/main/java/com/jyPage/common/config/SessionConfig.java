package com.jyPage.common.config;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

@WebListener
public class SessionConfig implements HttpSessionListener {

	private static final Map<String, HttpSession> sessions = new ConcurrentHashMap<>();

	// 중복 로그인 확인
	public synchronized static String getSessionIdCheck(String type, String compareId) {
		String result = "";
		for (String key : sessions.keySet()) {
			HttpSession hs = sessions.get(key);
			if (hs != null && hs.getAttribute(type) != null && hs.getAttribute(type).toString().equals(compareId)) {
				result = key.toString();
			}
		}

		removeSessionForDoubleLogin(result);
		return result;
	}

	// 세션 없애기
	private static void removeSessionForDoubleLogin(String userId) {
		if (userId != null && userId.length() > 0) {
			sessions.get(userId).invalidate();
			sessions.remove(userId);
		}
	}

	// 세션 로그인 시 sessions에 풋
	@Override
	public void sessionCreated(HttpSessionEvent se) {
		System.out.println(se.getSession().getId());
		sessions.put(se.getSession().getId(), se.getSession());
	}

	// 세션값 지우기
	@Override
	public void sessionDestroyed(HttpSessionEvent se) {
		if (sessions.get(se.getSession().getId()) != null) {
			sessions.get(se.getSession().getId()).invalidate();
			sessions.remove(se.getSession().getId());
		}
	}

}
