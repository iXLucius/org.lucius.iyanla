package org.lucius.iyanla.model.auth;

import java.io.Serializable;

public class User implements Serializable {

	private static final long serialVersionUID = -5918618021753103359L;

	private Long id;
	private String username;
	private String password;
	private int age;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

}
