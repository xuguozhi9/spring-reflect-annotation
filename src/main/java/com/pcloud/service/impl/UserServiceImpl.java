package com.pcloud.service.impl;

import com.pcloud.entity.User;
import com.pcloud.service.UserService;
import com.plcoud.enums.Component;

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
