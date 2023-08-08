package com.example.shopBackend.account;

import com.example.shopBackend.item.Item;
import com.example.shopBackend.item.ItemRepository;
import com.example.shopBackend.item.ItemService;
import com.example.shopBackend.role.RoleRepository;
import exception.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
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

	public AccountService(AccountRepository accountRepository, RoleRepository roleRepository, ItemService itemService, ItemRepository itemRepository) {
		this.accountRepository = accountRepository;
		this.roleRepository = roleRepository;
		this.itemService = itemService;
		this.itemRepository = itemRepository;
	}

	/**
	 * Saves a new account to the database.
	 *
	 * @param accounts
	 * 		  The account to be added to the database.
	 * @return saved accounts
	 */
	public List<Account> saveAllAccounts(List<Account> accounts) {

		// loop all inputs and checks for errors.
		for (Account account : accounts) {
			int roleId = account.getRole().getId();

			if (roleRepository.findById(roleId).isEmpty()) {
				throw new BadRequestException(
						"role with id: " + roleId + " does not exist");
			}

			if (!account.getPassword().matches("^(?=.*\\w)(?=.*\\d)(?=.*[@$!%*#?&])[\\w@$!%*#?&]{8,}")) {
				throw new BadRequestException(
						"password doesn't include an uppercase letter, number or special character os is min length 8");
			}

			if (accountRepository.findByUsername(account.getUsername()).orElse(null) != null) {
				throw new BadRequestException(
						"an account with username: " + account.getUsername() + " already exists");
			}

			if (accountRepository.findByEmail(account.getEmail()).orElse(null) != null) {
				throw new BadRequestException(
						"an account with email: " + account.getEmail() + " already exists");
			}
		}

		return accountRepository.saveAll(accounts);
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
