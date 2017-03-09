package com.demo.service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.demo.model.User;
import com.demo.util.PropsUtil;

public class UserV1Service {

	private static final Logger LOGGER = LoggerFactory.getLogger(UserV1Service.class);

	private static final String DRIVER;
	private static final String URL;
	private static final String USERNAME;
	private static final String PASSWORD;

	static {
		Properties conf = PropsUtil.loadProps("config.properties");
		DRIVER = conf.getProperty("jdbc.driver");
		URL = conf.getProperty("jdbc.url");
		USERNAME = conf.getProperty("jdbc.username");
		PASSWORD = conf.getProperty("jdbc.password");

		try {
			Class.forName(DRIVER);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	public List<User> getUserList() {
		Connection conn = null;

		try {
			List<User> list = new ArrayList<User>();
			String sql = "select * from user";
			conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
			PreparedStatement stmt = conn.prepareStatement(sql);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				User user = new User();
				user.setId(rs.getLong("id"));
				user.setName(rs.getString("name"));
				user.setMobile(rs.getString("mobile"));
				user.setEmail(rs.getString("email"));
				list.add(user);
			}
			return list;
		} catch (SQLException e) {
			e.printStackTrace();
			LOGGER.error("execute sql failure", e);
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
					LOGGER.error("execute sql failure", e);
				}
			}
		}

		return null;
	}

	public User getUser(Long id) {

		return null;
	}

	public boolean createUser(Map<String, Object> fieldMap) {

		return false;
	}

	public boolean updateUser(Long id, Map<String, Object> fieldMap) {

		return false;
	}

	public boolean deleteUser(Long id) {

		return false;
	}

}
