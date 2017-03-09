package com.demo.service;

import java.sql.Connection;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.demo.model.User;
import com.demo.util.DatabaseHelperV2;

public class UserV2Service {

	private static final Logger LOGGER = LoggerFactory.getLogger(UserV2Service.class);

	public List<User> getUserList() {
		Connection conn = DatabaseHelperV2.getConnection();
		try {
			String sql = "select * from user";
			return DatabaseHelperV2.queryEntityList(User.class, conn, sql);
		} finally {
			DatabaseHelperV2.closeConnection(conn);
		}

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
