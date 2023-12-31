package com.jyPage.sso.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.jyPage.common.config.SessionConfig;
import com.jyPage.common.util.AES256;
import com.jyPage.common.util.SendMail;
import com.jyPage.network.NetworkServiceImpl;
import com.jyPage.sso.config.Action;
import com.jyPage.sso.entity.Users;
import com.jyPage.sso.repository.UsersRepository;
import com.jyPage.sso.sql.SsoSQL;

@Service
public class SsoServiceImpl implements SsoService {

	@Autowired
	private UsersRepository userRepository;

	@Autowired
	private NetworkServiceImpl networkService;

	@Autowired
	private SsoSQL ssoSQL;

	@Value("${sso.properties.session-id}")
	private String sessionID;
	@Value("${sso.properties.session-db-id}")
	private String sessionDBID;
	@Value("${sso.properties.session-db-pw}")
	private String sessionDBPW;

	// ID, Email, Phone 중복시 1 반환
	@Override
	public long signUpCheck(Users user) {
		long result = 0;

		if (user.getId() != null)
			result = userRepository.countByIdIs(user.getId());
		else if (user.getEmail() != null)
			result = userRepository.countByEmailIs(user.getEmail());
		else if (user.getPhone() != null)
			result = userRepository.countByPhoneIs(user.getPhone());

		return result;
	}

	// 아이디, 이메일 매칭 여부
	@Override
	public long emailCheck(Users user) {
		return userRepository.countByIdIsAndEmailIs(user.getId(), user.getEmail());
	}

	// 메일로 비밀번호 보내기
	@Override
	public void sendMail(Users user) throws Exception {
		AES256 aes = new AES256();
		user = userRepository.findById(user.getId()).get();
		user.setPw(aes.decrypt(user.getPw()));
		SendMail sm = new SendMail();
		sm.sendPassword(user);
	}

	// 입력받은 데이터로 Users에 저장
	// Pw 암호화
	@Override
	public void saveUser(HttpSession session, Users user) throws Exception {
		AES256 aes = new AES256();
		String action = Action.CREATE.name();
		if (user.getId() == null) {
			String id = sessionID();
			
			Users newUser = userRepository.findById(id).get();
			user.setId(id);
			user.setSignUpDate(newUser.getSignUpDate());
			
			action = Action.UPDATE.name();
			
			setSession(user.getId(), user.getDbId(), user.getDbPw());
		}
		user.setState("Y");
		user.setPw(aes.encrypt(user.getPw()));
		
		if (user.getDbPw() != null) {
			user.setDbPw(aes.encrypt(user.getDbPw()));
		}

		networkService.saveUser(user, action);
	}

	// 자동 로그인 확인
	@Override
	public boolean autoSignInCheck(HttpServletRequest request) throws Exception {
		String sid = sessionID();
		AES256 aes = new AES256();

		if (sid == null) {
			Cookie[] cookies = request.getCookies();
			if (cookies != null) {
				for (Cookie c : cookies) {
					if (Objects.equals(c.getName(),"auto")) {
						SessionConfig.getSessionIdCheck(sessionID, c.getValue());
						Users newUser = userRepository.findById(c.getValue()).get();
						setSession(c.getValue(), newUser.getDbId(), aes.decrypt(newUser.getDbPw()));
						ssoSQL.saveUserAction(Action.READ.name(), c.getValue());

						return true;
					}
				}
			}
		} else {
			Users newUser = userRepository.findById(sid).get();
			setSession(sid, newUser.getDbId(), aes.decrypt(newUser.getDbPw()));
			ssoSQL.saveUserAction(Action.READ.name(), sid);

			return true;
		}
		return false;
	}

	// 로그인 res = 0 존재하지 않는 아이디일 경우, res = 1 정상 로그인, res = 2 비밀번호가 틀렸을 경우,
	// res = 3 탈퇴된 계정일 경우
	@Override
	public int signIn(Users user, String auto, HttpServletResponse response) throws Exception {
		long count = userRepository.countByIdIs(user.getId());
		int res = 0;

		if (count == 1) {
			Users newUser = userRepository.findById(user.getId()).get();
			if (newUser.getState().equals("N")) {
				res = 3;
			} else {
				AES256 aes = new AES256();
				String pw = aes.decrypt(newUser.getPw());

				if (Objects.equals(pw, user.getPw())) {
					SessionConfig.getSessionIdCheck(sessionID, user.getId());
					setSession(user.getId(), newUser.getDbId(), aes.decrypt(newUser.getDbPw()));

					ssoSQL.saveUserAction(Action.READ.name(), user.getId());
					if (auto != null) {
						Cookie cookie = new Cookie("auto", user.getId());
						cookie.setMaxAge(60 * 60 * 24 * 30);
						response.addCookie(cookie);
					}

					res = 1;
				} else {
					res = 2;
				}
			}
		}

		return res;
	}

	// 세션 정보로 user 정보 가져오기
	@Override
	public Users getSessionUser() throws Exception {
		AES256 aes = new AES256();
		String id = sessionID();
		
		Users user = userRepository.findById(id).get();
		user.setPw(aes.decrypt(user.getPw()));
		user.setDbPw(aes.decrypt(user.getDbPw()));

		return user;
	}

	// 유저 탈퇴
	@Override
	public void deleteUser(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String id = sessionID();
		SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String time = fmt.format(new Date());
		
		Users user = userRepository.findById(id).get();
		user.setState("N");
		user.setUnSignDate(fmt.parse(time));
		
		userRepository.save(user);
		ssoSQL.saveUserAction(Action.DELETE.name(), id);
		signOut(request, response);
	}

	// 로그아웃
	@Override
	public void signOut(HttpServletRequest request, HttpServletResponse response) {
		Cookie[] cookies = request.getCookies();

		if (cookies != null) {
			for (Cookie c : cookies) {
				if (Objects.equals(c.getName(),"auto")) {
					c.setMaxAge(0);
					response.addCookie(c);
				}
			}
		}

		HttpSession session = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest()
				.getSession(true);
		session.invalidate();
	}

	// 세션 ID 가져오기
	public String sessionID() {
		return (String) RequestContextHolder.getRequestAttributes().getAttribute(sessionID,
				RequestAttributes.SCOPE_SESSION);
	}

	// 세션 세팅
	public void setSession(String sessionID, String sessionDBID, String sessionDBPW) {
		RequestContextHolder.getRequestAttributes().setAttribute(this.sessionID, sessionID,
				RequestAttributes.SCOPE_SESSION);
		RequestContextHolder.getRequestAttributes().setAttribute(this.sessionDBID, sessionDBID,
				RequestAttributes.SCOPE_SESSION);
		RequestContextHolder.getRequestAttributes().setAttribute(this.sessionDBPW, sessionDBPW,
				RequestAttributes.SCOPE_SESSION);
	}

}
