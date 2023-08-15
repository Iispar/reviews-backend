package com.example.shopBackend.account;

import com.example.shopBackend.item.Item;
import com.example.shopBackend.item.ItemRepository;
import com.example.shopBackend.item.ItemService;
import com.example.shopBackend.role.RoleRepository;
import com.example.shopBackend.security.AuthRequest;
import com.example.shopBackend.security.AuthRes;
import com.example.shopBackend.security.JwtService;
import exception.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Services for account.
 * @author iiro
 *
 */
@Service
public class AccountService {
	@Autowired
	private AccountRepository accountRepository;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private ItemService itemService;

	@Lazy
	@Autowired
	private ItemRepository itemRepository;

	private final PasswordEncoder passwordEncoder;
	private final JwtService jwtService;
	private final AuthenticationManager authenticationManager;

	public AccountService(AccountRepository accountRepository, RoleRepository roleRepository, ItemService itemService, ItemRepository itemRepository, PasswordEncoder passwordEncoder, JwtService jwtService, AuthenticationManager authenticationManager) {
		this.accountRepository = accountRepository;
		this.roleRepository = roleRepository;
		this.itemService = itemService;
		this.itemRepository = itemRepository;
		this.passwordEncoder = passwordEncoder;
		this.jwtService = jwtService;
		this.authenticationManager = authenticationManager;
	}

	/**
	 * Saves a new account to the database.
	 *
	 * @param account
	 * 		  The account to be added to the database.
	 * @return saved accounts
	 */
	public AuthRes saveAccounts(Account account) {

			int roleId = account.getRole().getId();

			if (roleRepository.findById(roleId).isEmpty()) {
				throw new BadRequestException(
						"role with id: " + roleId + " does not exist");
			}

			if (!account.getPassword().matches("^(?=.*\\w)(?=.*\\d)(?=.*[@$!%*#?&])[\\w@$!%*#?&]{8,}")) {
				throw new BadRequestException(
						"password doesn't include an uppercase letter, number or special character os is min length 8");
			}

			account.setPassword(passwordEncoder.encode(account.getPassword()));

			if (accountRepository.findByUsername(account.getUsername()).orElse(null) != null) {
				throw new BadRequestException(
						"an account with username: " + account.getUsername() + " already exists");
			}

			if (accountRepository.findByEmail(account.getEmail()).orElse(null) != null) {
				throw new BadRequestException(
						"an account with email: " + account.getEmail() + " already exists");
			}

			accountRepository.save(account);
			var jwtToken = jwtService.newToken(account);
			return AuthRes.builder().token(jwtToken).build();
	}

	public AuthRes login(AuthRequest request) {
		authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
		);
		var account = accountRepository.findByUsername(request.getUsername()).orElseThrow(); // catch
		var jwtToken = jwtService.newToken(account);

		return AuthRes.builder().token(jwtToken).build();
	}


	/**
	 * Updates an account in the database.
	 * @param accountId
	 * 		  The id of the account to be updated
	 * @param account
	 * 		  The account to be updated to the database.
	 * @return updated account
	 */
	public Account updateAccount(int accountId, Account account) {
		Account foundAccount = accountRepository.findById(accountId).orElse(null);

		if (foundAccount == null) {
			throw new BadRequestException(
					"No accounts exists with id: " + accountId);
		}

		int roleId = account.getRole().getId();
		if (roleRepository.findById(roleId).isEmpty()) {
			throw new BadRequestException(
					"role with id: " + roleId + " does not exist");
		}

		if (!account.getPassword().matches("^(?=.*\\w)(?=.*\\d)(?=.*[@$!%*#?&])[\\w@$!%*#?&]{8,}")) {
			throw new BadRequestException(
					"password doesn't include an uppercase letter, number or special character os is min length 8");
		}

		if (accountRepository.findByUsername(account.getUsername()).orElse(null) != null && !account.getUsername().equals(foundAccount.getUsername())) {
			throw new BadRequestException(
					"an account with username: " + account.getUsername() + " already exists");
		}

		if (accountRepository.findByEmail(account.getEmail()).orElse(null) != null && !account.getEmail().equals(foundAccount.getEmail())) {
			throw new BadRequestException(
					"an account with email: " + account.getEmail() + " already exists");
		}

		foundAccount.setEmail(account.getEmail());
		foundAccount.setName(account.getName());
		foundAccount.setPassword(account.getPassword());
		foundAccount.setUsername(account.getUsername());

		return accountRepository.save(foundAccount);
	}

	/**
	 * deletes an account from the database.
	 * @param id
	 * 		  The id of the account to be deleted from the database.
	 * @return true if successful
	 */
	public Boolean deleteAccount(int id){
		if(accountRepository.findById(id).isEmpty()) {
			throw new BadRequestException(
					"No Accounts exists with id " + id);
		}

		List<Item> items = itemRepository.findAll();

		// deletes all items that account has.
		for (Item item : items) {
			if (item.getAccount().getId() == id) {
				try {
					itemService.deleteItem(item.getId());
				} catch (Exception e) {
					throw new BadRequestException("error: " + e.getMessage() + ". While deleting item with id: " + item.getId());
				}
			}
		}

		accountRepository.deleteById(id);

		if(accountRepository.findById(id).isPresent()) {
			throw new BadRequestException(
					"deletion failed");
		}
		return true;
	}
}
