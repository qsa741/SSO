package com.jyPage.sso.sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.jyPage.common.util.ConvertSqlToString;
import com.jyPage.exception.JYException;

@Service
public class SsoSQL {

	// Tibero driver, url, username, password
	@Value("${spring.datasource.driver-class-name}")
	private String driver;

	@Value("${spring.datasource.url}")
	private String url;

	@Value("${spring.datasource.username}")
	private String username;

	@Value("${spring.datasource.password}")
	private String password;

	@Autowired
	private ConvertSqlToString converter;
	
	// 유저활동 저장
	public void saveUserAction(String action, String id) throws JYException {
		String insertSQL = converter.Convert("sql/SsoSQL/saveUserAction.sql");
		
		Connection conn = null;
		PreparedStatement pre = null;

		try {
			Class.forName(driver);
			conn = DriverManager.getConnection(url, username, password);
			conn.setAutoCommit(false);
			pre = conn.prepareStatement(insertSQL);
			pre.setString(1, action);
			pre.setString(2, id);
			pre.executeUpdate();

			pre.close();
			conn.commit();
			conn.close();
		} catch (ClassNotFoundException cnfe) {
			throw new JYException("Class Not Found Exception", cnfe);
		} catch (SQLException se) {
			try {
				conn.rollback();
			} catch (SQLException e) {
				throw new JYException("SQL Exception", e);
			}
			throw new JYException("SQL Exception", se);
		}
	}

	// 전날 userAction 내역 액션별로 카운트
	public JSONObject getUserAction(String action) throws JYException {
		JSONObject json = new JSONObject();

		// 전날 action 총 카운트 구하기
		String actionCounting = converter.Convert("sql/SsoSQL/getUserActionCount.sql");

		// 전날 action이 없었을 경우 빈칸 채우기 용으로 검색
		String actionEmpty = converter.Convert("sql/SsoSQL/getUserActionEmpty.sql");

		Connection conn = null;
		PreparedStatement pre = null;
		ResultSet rs = null;

		try {
			Class.forName(driver);
			conn = DriverManager.getConnection(url, username, password);
			pre = conn.prepareStatement(actionCounting);
			pre.setString(1, action);
			rs = pre.executeQuery();

			// 결과가 없을 경우 빈칸 채우기 사용
			if (!rs.isBeforeFirst()) {
				pre = conn.prepareStatement(actionEmpty);
				rs = pre.executeQuery();
			}

			while (rs.next()) {
				String[] dateArray = rs.getString(1).split("-");
				json.put("year", dateArray[0]);
				json.put("month", dateArray[1]);
				json.put("day", dateArray[2]);
				json.put("count", rs.getInt(2));
			}

			rs.close();
			pre.close();
			conn.close();
		} catch (ClassNotFoundException cnfe) {
			throw new JYException("Class Not Found Exception", cnfe);
		} catch (SQLException se) {
			throw new JYException("SQL Exception", se);
		}

		return json;
	}

	// actionScheduler 테이블에 명령어 등록
	public void actionSchedulerSave(String data) throws JYException {
		String insertSQL = converter.Convert("sql/SsoSQL/actionSchedulerSave.sql");

		Connection conn = null;
		PreparedStatement pre = null;

		try {
			Class.forName(driver);
			conn = DriverManager.getConnection(url, username, password);
			conn.setAutoCommit(false);
			pre = conn.prepareStatement(insertSQL);
			pre.setString(1, data);
			pre.executeUpdate();

			pre.close();
			conn.commit();
			conn.close();
		} catch (ClassNotFoundException cnfe) {
			throw new JYException("Class Not Found Exception", cnfe);
		} catch (SQLException se) {
			try {
				conn.rollback();
			} catch (SQLException e) {
				throw new JYException("SQL Exception", e);
			}
			throw new JYException("SQL Exception", se);
		}
	}
}
