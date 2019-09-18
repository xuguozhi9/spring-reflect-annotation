package com.huan.web.controller;

import com.huan.web.entity.User;
import com.huan.web.service.UserService;
import com.huan.spring.enums.Autowired;
import com.huan.spring.enums.Controller;
import com.huan.spring.enums.RequestMapping;

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
