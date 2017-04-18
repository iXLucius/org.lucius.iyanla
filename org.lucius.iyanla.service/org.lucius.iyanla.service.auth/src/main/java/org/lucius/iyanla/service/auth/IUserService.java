package org.lucius.iyanla.service.auth;

import java.util.List;

import org.lucius.iyanla.model.auth.User;

public interface IUserService {

	User findUserById(Long id);
	
	List<User> findAllUsers();
	
}
