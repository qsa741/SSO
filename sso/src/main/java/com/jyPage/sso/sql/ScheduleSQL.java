package com.jyPage.sso.sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.jyPage.common.util.ConvertSqlToString;
import com.jyPage.exception.JYException;

@Service
public class ScheduleSQL {

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
	
	// 가입/수정이 요청된 레코드 저장
	public List<Map<String, Object>> userScheduler() throws JYException {
		// 스케줄러가 읽지 않은 데이터 찾기
		String selectSQL = converter.Convert("sql/ScheduleSQL/userSchedulerSelect.sql");
		// 스케줄러가 읽은 데이터 readCheck = 'Y'로 업데이트
		String updateSQL = converter.Convert("sql/ScheduleSQL/userSchedulerUpdate.sql");
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

		Connection conn = null;
		PreparedStatement pre = null;
		ResultSet result = null;

		try {
			Class.forName(driver);
			conn = DriverManager.getConnection(url, username, password);
			conn.setAutoCommit(false);
			pre = conn.prepareStatement(selectSQL);
			result = pre.executeQuery();

			ResultSetMetaData metaData = result.getMetaData();

			Map<String, Object> map;
			String col;

			while (result.next()) {
				map = new LinkedHashMap<>();

				for (int i = 0; i < 4; i++) {
					col = metaData.getColumnName(i + 1);
					map.put(col, result.getString(col));
				}

				list.add(map);
				pre = conn.prepareStatement(updateSQL);
				pre.setString(1, (String) map.get("SCHEDULENUM"));
				pre.executeUpdate();
			}

			result.close();
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

		return list;

	}

	// 탈퇴 후 2년이 지난 레코드 삭제
	public void deleteUserScheduler() throws JYException {
		String deleteSQL = converter.Convert("sql/ScheduleSQL/deleteUserScheduler.sql");

		Connection conn = null;
		PreparedStatement pre = null;
		ResultSet result = null;

		try {
			Class.forName(driver);
			conn = DriverManager.getConnection(url, username, password);
			conn.setAutoCommit(false);
			pre = conn.prepareStatement(deleteSQL);
			result = pre.executeQuery();

			result.close();
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
