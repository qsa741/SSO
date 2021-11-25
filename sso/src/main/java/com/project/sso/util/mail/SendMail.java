package com.project.sso.util.mail;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import com.project.sso.entity.Users;

// 메일 전송 클래스
public class SendMail {

	static final String host = "smtp.naver.com"; // google일경우 smtp.gmail.com
	static final String username = "qsa741"; // 보내는 사람(이메일)
	static final String password = "@YJYoung@95"; // 비밀번호
	static final int port = 465;

	public void sendPassword(Users user) throws Exception {

		String recive = user.getEmail();
		String title = user.getId() + "님의 비밀번호입니다.";
		String content = user.getId() + "님의 비밀번호는 " + user.getPw() + "입니다.";

		Properties props = System.getProperties();

		props.put("mail.smtp.host", host);
		props.put("mail.smtp.port", port);
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.ssl.enable", "true");
		props.put("mail.smtp.ssl.trust", host);

		javax.mail.Session session = javax.mail.Session.getDefaultInstance(props, new javax.mail.Authenticator() {
			String un = username;
			String pw = password;

			protected javax.mail.PasswordAuthentication getPasswordAuthentication() {
				return new javax.mail.PasswordAuthentication(un, pw);
			}
		});
		session.setDebug(true);

		Message mimeMessage = new MimeMessage(session);
		mimeMessage.setFrom(new InternetAddress(username + "@naver.com"));
		mimeMessage.setRecipient(Message.RecipientType.TO, new InternetAddress(recive));
		mimeMessage.setSubject(title);
		mimeMessage.setText(content);

		Transport.send(mimeMessage);
	}

}
