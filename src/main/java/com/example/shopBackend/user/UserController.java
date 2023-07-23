package com.example.shopBackend.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
		return true;
	}

	/**
	 * API PUT call to /api/user/update?userId=(input) with content in the body that describes the updates to the user.
	 * Will update it to the database.
	 * @param {User} user
	 * 	      The user to be updated to the database
	 * @param {string} userId
	 * 	      The userId of the user to be updated
	 * @return updated user
	 */
	@PutMapping("/update")
	public User update(@RequestParam int userId, @RequestBody User user) {
		return userService.updateUser(userId, user);
	}
}
