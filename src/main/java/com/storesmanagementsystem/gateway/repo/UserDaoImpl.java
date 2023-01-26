package com.storesmanagementsystem.gateway.repo;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.storesmanagementsystem.gateway.service.UserInfoBean;

@Repository
public class UserDaoImpl implements UserDao {

	@PersistenceUnit
	private EntityManagerFactory fact;

	@Override
	public UserInfoBean getUserByUserUd(String userId) {
		EntityManager mgr = fact.createEntityManager();
		try {
			String sqlString = "select * from user_info  where userId=" + userId;
			Query query = mgr.createNativeQuery(sqlString, UserInfoBean.class);
//			query.setParameter("userId", userId);
			Object bean = query.getSingleResult();
			if (null != bean) {
				UserInfoBean userBean = (UserInfoBean) bean;
//				UserInfoBean userBean = new ModelMapper().map(bean, UserInfoBean.class);

				return userBean;
			} else {
				return null;
			}
		} catch (Exception e) {
			for (StackTraceElement ele : e.getStackTrace()) {
				return null;
			}
		}
		return null;
	}

}
