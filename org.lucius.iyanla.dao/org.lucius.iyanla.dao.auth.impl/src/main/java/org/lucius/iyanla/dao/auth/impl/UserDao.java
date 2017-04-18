package org.lucius.iyanla.dao.auth.impl;

import java.util.List;

import javax.annotation.Resource;

import org.apache.ibatis.session.SqlSession;
import org.lucius.iyanla.dao.auth.IUserDao;
import org.lucius.iyanla.model.auth.User;
import org.springframework.stereotype.Repository;

@Repository
public class UserDao implements IUserDao{
	
	private static final String MAPPER_NAMESPACE = UserDao.class.getName() + ".";
	
	public UserDao(){
		System.out.println("初始化UserService...");
	}
	
	@Resource
	private SqlSession sqlSession;
	
	@Override
	public User findUserById(Long id) {
		return sqlSession.selectOne(MAPPER_NAMESPACE + "findUserById",id);
	}

	@Override
	public List<User> findAllUsers() {
		return sqlSession.selectList(MAPPER_NAMESPACE + "findAllUsers");
	}

}
