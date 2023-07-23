package com.example.shopBackend.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * The controller for the user table
 * @author iiro
 *
 */
@RestController
@RequestMapping("/api/user")
public class UserController {
	private UserService userService;
	
	@Autowired
	public UserController(UserService userService) {
		this.userService = userService;
	}
	
	/**
	 * API GET call to /api/user/add with content in the body that describes the added user.
	 * Will add it to the database.
	 * @param {User} user
	 * 	      The user to be added to the database
	 * @return True if successful. False otherwise
	 */
	@PostMapping("/add")
	public Boolean add(@RequestBody List<User> user) {
		userService.saveAllUsers(user);
		// TODO: rate reviews
		// TODO: calc new average for item
		// TODO: calc new topwords
		return true;
	}
}
