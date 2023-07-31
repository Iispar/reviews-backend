package com.example.shopBackend.user;

import com.example.shopBackend.role.RoleRepository;
import exception.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Services for user.
 * @author iiro
 *
 */
@Service
public class UserService {
	@Autowired
	private UserRepository userRepository;

	@Autowired
	private RoleRepository roleRepository;
	
	/**
	 * Saves a new user to the database.
	 *
	 * @param users The user to be added to the database.
	 * @return saved users
	 */
	public List<User> saveAllUsers(List<User> users) {

		for (User user : users) {
			int roleId = user.getRole().getId();
			if (roleRepository.findById(roleId).isEmpty()) {
				throw new BadRequestException(
						"role with id: " + roleId + " does not exist");
			}

			if (!user.getPassword().matches("^(?=.*\\w)(?=.*\\d)(?=.*[@$!%*#?&])[\\w@$!%*#?&]{8,}")) {
				throw new BadRequestException(
						"password doesn't include an uppercase letter, number or special character os is min length 8");
			}

			if (userRepository.findByUsername(user.getUsername()).orElse(null) != null) {
				throw new BadRequestException(
						"an user with username: " + user.getUsername() + " already exists");
			}

			if (userRepository.findByEmail(user.getEmail()).orElse(null) != null) {
				throw new BadRequestException(
						"an user with email: " + user.getEmail() + " already exists");
			}
		}

		return userRepository.saveAll(users);
	}


	public User updateUser(int userId, User user) {
		User foundUser = userRepository.findById(userId).orElse(null);
		if (foundUser == null) {
			throw new BadRequestException(
					"No users exists with id: " + userId);
		}

		int roleId = user.getRole().getId();
		if (roleRepository.findById(roleId).isEmpty()) {
			throw new BadRequestException(
					"role with id: " + roleId + " does not exist");
		}

		if (!user.getPassword().matches("^(?=.*\\w)(?=.*\\d)(?=.*[@$!%*#?&])[\\w@$!%*#?&]{8,}")) {
			throw new BadRequestException(
					"password doesn't include an uppercase letter, number or special character os is min length 8");
		}

		if (userRepository.findByUsername(user.getUsername()).orElse(null) != null && !user.getUsername().equals(foundUser.getUsername())) {
			throw new BadRequestException(
					"an user with username: " + user.getUsername() + " already exists");
		}

		if (userRepository.findByEmail(user.getEmail()).orElse(null) != null && !user.getEmail().equals(foundUser.getEmail())) {
			throw new BadRequestException(
					"an user with email: " + user.getEmail() + " already exists");
		}

		foundUser.setEmail(user.getEmail());
		foundUser.setName(user.getName());
		foundUser.setPassword(user.getPassword());
		foundUser.setUsername(user.getPassword());

		return userRepository.save(foundUser);
	}

	/**
	 * deletes an user from the database.
	 * @param id
	 * 		  The id of the user to be deleted from the database.
	 * @return true if successful
	 */
	public Boolean deleteUser(int id){
		if(userRepository.findById(id).isEmpty()) {
			throw new BadRequestException(
					"No users exists with id " + id);
		}

		userRepository.deleteById(id);

		if(userRepository.findById(id).isPresent()) {
			throw new BadRequestException(
					"deletion failed");
		}
		return true;
	}
}
