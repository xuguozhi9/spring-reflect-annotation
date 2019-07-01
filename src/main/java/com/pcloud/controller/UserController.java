package com.pcloud.controller;

import com.pcloud.entity.User;
import com.pcloud.service.UserService;
import com.plcoud.enums.Autowired;
import com.plcoud.enums.Controller;
import com.plcoud.enums.RequestMapping;

@Controller
public class UserController {

	@Autowired
	private UserService userService;
	
	@RequestMapping("/get")
	public User get(){
		System.out.println(userService);
		return userService.get();
	}
}
