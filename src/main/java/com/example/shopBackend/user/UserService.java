package com.example.shopBackend.user;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Services for user.
 * @author iiro
 *
 */
@Service
public class UserService {
	@Autowired
	private UserRepository userRepository;
	
	/**
	 * Saves a new user to the database.
	 * @param {User} user
	 * 		  The user to be added to the database.
	 * @return
	 */
	public List<User> saveAllReviews(List<User> user) {
		return userRepository.saveAll(user);
	}
}
