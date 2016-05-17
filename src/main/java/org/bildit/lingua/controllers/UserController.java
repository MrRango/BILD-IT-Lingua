package org.bildit.lingua.controllers;

import java.util.List;

import org.bildit.lingua.model.User;
import org.bildit.lingua.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class UserController {
	
	@Autowired
	UserService userService;
	
	/**
	 * @Author Bojan Aleksic
	 * @param model
	 * @return
	 * List all users. Modify or delete later.
	 */
	@RequestMapping("/get-users")
	public String getAllUsers(Model model) {
		List<User> users = userService.findeAllUsers();
		model.addAttribute("users", users);
		return "users";
	}
	
	/**
	 * @Author Bojan Aleksic
	 * @param id
	 * @param model
	 * @return
	 * Method for testing user-account purposes.
	 * Modify or delete later 
	 */
	@RequestMapping("/user-account")
	public String userAccount(@RequestParam("id") Long id, Model model) {
		User user = userService.getUser(id);
		model.addAttribute("user", user);
		return "user-account";
	}
	
}