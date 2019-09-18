package com.huan.web.service.impl;

import com.huan.web.entity.User;
import com.huan.web.service.UserService;
import com.huan.spring.enums.Component;

@Component
public class UserServiceImpl implements UserService{

	@Override
	public User get() {
		User user = new User();
		user.setUsername("admin");
		user.setPassword("123");
		return user;
	}

}
