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
	 * @param {User} user
	 * 		  The user to be added to the database.
	 * @return
	 */
	public List<User> saveAllUsers(List<User> users) {

		for (int i = 0; i < users.size(); i += 1) {
			int roleId = users.get(i).getRole().getId();
			if (roleRepository.findById(roleId).isEmpty()) {
				throw new BadRequestException(
						"role with id: " + roleId + " does not exist");
			}

			if (!users.get(i).getPassword().matches("^(?=.*\\w)(?=.*\\d)(?=.*[@$!%*#?&])[\\w\\d@$!%*#?&]{8,}")) {
				throw new BadRequestException(
						"password doesn't include an uppercase letter, number or special character os is min length 8");
			}

			if (userRepository.findByUsername(users.get(i).getUsername()).orElse(null) == null) {
				throw new BadRequestException(
						"an user with username: " + users.get(i).getUsername() + " already exists");
			}

			if (userRepository.findByEmail(users.get(i).getEmail()).orElse(null) == null) {
				throw new BadRequestException(
						"an user with email: " + users.get(i).getEmail() + " already exists");
			}
		}

		return userRepository.saveAll(users);
	}

	// TODO: UPDATES.
}
