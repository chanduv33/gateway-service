package com.storesmanagementsystem.gateway.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.storesmanagementsystem.gateway.repo.UserDao;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserDao dao;

	@Autowired
	private UserDetailsImpl userDeatils;

	UserInfoBean bean;

	@Override
	public UserInfoBean getUserByUserUd(String userId) {
		bean = dao.getUserByUserUd(userId);
		if (bean != null) {
			userDeatils.setUser(bean);
		} else {
			throw new UsernameNotFoundException("User Not Found");
		}
		return bean;
	}

}
