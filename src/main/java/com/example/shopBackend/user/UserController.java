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
	private final UserService userService;
	
	@Autowired
	public UserController(UserService userService) {
		this.userService = userService;
	}
	
	/**
	 * API GET call to /api/user/add with content in the body that describes the added user.
	 * Will add it to the database.
	 * @param user
	 * 	      The user to be added to the database
	 * @return saved users
	 */
	@PostMapping("/add")
	public List<User> add(@RequestBody List<User> user) {
		return userService.saveAllUsers(user);
	}

	/**
	 * API PUT call to /api/user/update?userId=(input) with content in the body that describes the updates to the user.
	 * Will update it to the database.
	 * @param user
	 * 	      The user to be updated to the database
	 * @param userId
	 * 	      The userId of the user to be updated
	 * @return updated user
	 */
	@PutMapping("/update")
	public User update(
			@RequestParam int userId,
			@RequestBody User user) {
		return userService.updateUser(userId, user);
	}

	/**
	 * API DELETE call to /api/user/del?userId=(input)
	 * will delete the user with the corresponding id.
	 * @return True if successful. False otherwise
	 */
	@DeleteMapping("/del")
	public boolean deleteUser(@RequestParam("userId") int id) {
		return Boolean.TRUE.equals(userService.deleteUser(id));
	}
}
