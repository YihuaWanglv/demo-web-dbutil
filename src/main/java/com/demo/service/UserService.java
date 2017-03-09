package com.demo.service;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.demo.model.User;
import com.demo.util.DatabaseHelper;

public class UserService {

	private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);

	public List<User> getUserList() {
		String sql = "select * from user";
		return DatabaseHelper.queryEntityList(User.class, sql);

	}

	public User getUser(Long id) {
		String sql = "select * from user where id=?";
		return DatabaseHelper.queryEntity(User.class, sql, id);
	}

	public boolean createUser(Map<String, Object> fieldMap) {

		return DatabaseHelper.insertEntity(User.class, fieldMap);
	}

	public boolean updateUser(Long id, Map<String, Object> fieldMap) {

		return DatabaseHelper.updateEntity(User.class, id, fieldMap);
	}

	public boolean deleteUser(Long id) {

		return DatabaseHelper.deleteEntity(User.class, id);
	}

}
