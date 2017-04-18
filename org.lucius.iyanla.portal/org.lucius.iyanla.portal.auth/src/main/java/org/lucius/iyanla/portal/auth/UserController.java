package org.lucius.iyanla.portal.auth;

import java.util.List;

import javax.annotation.Resource;

import org.lucius.iyanla.model.auth.User;
import org.lucius.iyanla.service.auth.IUserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/user")
public class UserController {
	
	@Resource
	private IUserService userService;
	
	@RequestMapping(value = "/{userId}")
	public String getUserById(@PathVariable Long userId,Model model){
		User user = userService.findUserById(userId);
		model.addAttribute("user", user);
		return "login";
	}
	
	@RequestMapping(value = "/index")
	public String index(Model model){
		List<User> users = userService.findAllUsers();
		model.addAttribute("users", users);
		return "index";
	}
	
	@RequestMapping(value = "/info/{userId}")
	@ResponseBody
	public User info(@PathVariable Long userId,Model model){
		User user = userService.findUserById(userId);
		return user;
	}
}
