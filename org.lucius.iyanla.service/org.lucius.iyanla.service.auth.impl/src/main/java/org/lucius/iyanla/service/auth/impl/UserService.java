package org.lucius.iyanla.service.auth.impl;

import java.util.List;

import javax.annotation.Resource;

import org.lucius.iyanla.dao.auth.IUserDao;
import org.lucius.iyanla.model.auth.User;
import org.lucius.iyanla.service.auth.IUserService;
import org.springframework.stereotype.Service;

@Service
public class UserService implements IUserService {
	
	@Resource
	private IUserDao userDao;

	@Override
	public User findUserById(Long id) {
		return userDao.findUserById(1L);
	}

	@Override
	public List<User> findAllUsers() {
		return userDao.findAllUsers();
	}

}
