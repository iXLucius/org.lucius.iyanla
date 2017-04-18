package org.lucius.iyanla.dao.auth;

import java.util.List;

import org.lucius.iyanla.model.auth.User;

public interface IUserDao {
	
	User findUserById(Long id);
	
	List<User> findAllUsers();
	
}
