package service;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.demo.model.User;
import com.demo.service.UserService;
import com.demo.util.DatabaseHelper;

public class UserServiceTest {

	private final UserService userService;

	public UserServiceTest() {
		userService = new UserService();
	}

	@Before
	public void init() throws IOException {
		DatabaseHelper.executeSqlFile("sql/user_init.sql");
	}

	@Test
	public void getUserListTest() {
		List<User> users = userService.getUserList();
		Assert.assertEquals(2, users.size());
	}

	@Test
	public void getUserTest() {
		long id = 1;
		User user = userService.getUser(id);
		Assert.assertNotNull(user);
	}

	@Test
	public void createUserTest() {
		Map<String, Object> fieldMap = new HashMap<String, Object>();
		fieldMap.put("name", "user100");
		fieldMap.put("mobile", "13560421111");
		fieldMap.put("email", "ewabebe1@163.com");
		boolean result = userService.createUser(fieldMap);
		Assert.assertTrue(result);
	}

	@Test
	public void updateUserTest() {
		long id = 1;
		Map<String, Object> fieldMap = new HashMap<String, Object>();
		fieldMap.put("email", "iyihua@163.com");
		boolean result = userService.updateUser(id, fieldMap);
		Assert.assertTrue(result);
	}

	@Test
	public void deleteUserTest() {
		long id = 1;
		boolean result = userService.deleteUser(id);
		Assert.assertTrue(result);
	}
}
