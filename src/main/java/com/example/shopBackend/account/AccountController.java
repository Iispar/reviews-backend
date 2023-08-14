package com.example.shopBackend.account;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * The controller for the Account table
 * @author iiro
 *
 */
@RestController
@RequestMapping("/api/account")
public class AccountController {
	private final AccountService accountService;
	
	@Autowired
	public AccountController(AccountService accountService) {
		this.accountService = accountService;
	}
	
	/**
	 * API POST call to /api/account/add with content in the body that describes the added account
	 * will add it to the database.
	 * @param account
	 * 	      The account to be added to the database
	 * @return saved accounts
	 */
	@PostMapping("/add")
	public List<Account> add(@RequestBody List<Account> account) {
		return accountService.saveAllAccounts(account);
	}

	/**
	 * API PUT call to /api/account/update?accountId=(input) with content in the body that describes the updates to the account
	 * will update it to the database.
	 * @param account
	 * 	      The account to be updated to the database
	 * @param accountId
	 * 	      The accountId of the account to be updated
	 * @return updated account
	 */
	@PutMapping("/update")
	public Account update(
			@RequestParam int accountId,
			@RequestBody Account account) {
		return accountService.updateAccount(accountId, account);
	}

	/**
	 * API DELETE call to /api/account/del?accountId=(input)
	 * will delete the account with the corresponding id.
	 * @return True if successful. Error otherwise
	 */
	@DeleteMapping("/del")
	public boolean deleteAccount(@RequestParam("accountId") int id) {

		return Boolean.TRUE.equals(accountService.deleteAccount(id));
	}

	/**
	 * API POST call to /api/account/login with the login info in the body
	 * will reference the database to see if the login info is correct.
	 * @return accountId
	 */
	@PostMapping("/login")
	public boolean login(@RequestParam("accountId") int id) {

		return Boolean.TRUE.equals(accountService.deleteAccount(id));
	}
}
