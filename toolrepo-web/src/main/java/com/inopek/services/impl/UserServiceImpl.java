package com.inopek.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.inopek.beans.UserBean;
import com.inopek.dao.UserDao;
import com.inopek.services.UserService;


@Service
public class UserServiceImpl implements UserService {
	
	@Autowired
	private UserDao	userDao;
	
	public UserBean findByImeiNumber(String imeiNumber) {
		return userDao.findByImeiNumber(imeiNumber);
	}
	
}
