package com.example.shopBackend.account;

import com.example.shopBackend.role.RoleRepository;
import exception.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
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
	
	/**
	 * Saves a new account to the database.
	 *
	 * @param accounts The account to be added to the database.
	 * @return saved accounts
	 */
	public List<Account> saveAllAccounts(List<Account> accounts) {

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

			if (accountRepository.findByusername(account.getusername()).orElse(null) != null) {
				throw new BadRequestException(
						"an account with username: " + account.getusername() + " already exists");
			}

			if (accountRepository.findByEmail(account.getEmail()).orElse(null) != null) {
				throw new BadRequestException(
						"an account with email: " + account.getEmail() + " already exists");
			}
		}

		return accountRepository.saveAll(accounts);
	}


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

		if (accountRepository.findByusername(account.getusername()).orElse(null) != null && !account.getusername().equals(foundAccount.getusername())) {
			throw new BadRequestException(
					"an account with username: " + account.getusername() + " already exists");
		}

		if (accountRepository.findByEmail(account.getEmail()).orElse(null) != null && !account.getEmail().equals(foundAccount.getEmail())) {
			throw new BadRequestException(
					"an account with email: " + account.getEmail() + " already exists");
		}

		foundAccount.setEmail(account.getEmail());
		foundAccount.setName(account.getName());
		foundAccount.setPassword(account.getPassword());
		foundAccount.setusername(account.getPassword());

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

		accountRepository.deleteById(id);

		if(accountRepository.findById(id).isPresent()) {
			throw new BadRequestException(
					"deletion failed");
		}
		return true;
	}
}
