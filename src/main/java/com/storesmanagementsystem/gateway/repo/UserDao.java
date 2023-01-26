package com.storesmanagementsystem.gateway.repo;

import com.storesmanagementsystem.gateway.service.UserInfoBean;

public interface UserDao {
	UserInfoBean getUserByUserUd(String userId);
}
