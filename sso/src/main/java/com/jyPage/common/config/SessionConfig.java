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
			HttpSession session = sessions.get(key);
			if (session != null && session.getAttribute(type) != null
					&& session.getAttribute(type).toString().equals(compareId)) {
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
	public void sessionCreated(HttpSessionEvent session) {
		sessions.put(session.getSession().getId(), session.getSession());
	}

	// 세션값 지우기
	@Override
	public void sessionDestroyed(HttpSessionEvent session) {
		if (sessions.get(session.getSession().getId()) != null) {
			sessions.get(session.getSession().getId()).invalidate();
			sessions.remove(session.getSession().getId());
		}
	}

}
