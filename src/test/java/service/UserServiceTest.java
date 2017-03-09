package service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.demo.model.User;
import com.demo.service.UserService;

public class UserServiceTest {

	private final UserService userService;
	
	public UserServiceTest() {
		userService = new UserService();
	}
	
	@Before
	public void init() {
		
	}
	
	@Test
	public void getUserListTest() {
		List<User> users = userService.getUserList();
		Assert.assertEquals(1, users.size());
	}
	
	@Test
	public void getUserTest() {
		long id = 1;
		User user = userService.getUser(id);
		Assert.assertNotNull(user);
	}
	
	public void createUserTest() {
		Map<String, Object> fieldMap = new HashMap<String, Object>();
		fieldMap.put("name", "user100");
		fieldMap.put("mobile", "13560421111");
		fieldMap.put("email", "ewabebe1@163.com");
		boolean result = userService.createUser(fieldMap);
		Assert.assertTrue(result);
	}
	
	public void updateUserTest() {
		long id=1;
		Map<String, Object> fieldMap = new HashMap<String, Object>();
		fieldMap.put("email", "iyihua@163.com");
		boolean result = userService.updateUser(id, fieldMap);
		Assert.assertTrue(result);
	}
	
	public void deleteUserTest() {
		long id=1;
		boolean result = userService.deleteUser(id);
		Assert.assertTrue(result);
	}
}
