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
	 * API GET call to /api/account/add with content in the body that describes the added account.
	 * Will add it to the database.
	 * @param account
	 * 	      The account to be added to the database
	 * @return saved accounts
	 */
	@PostMapping("/add")
	public List<Account> add(@RequestBody List<Account> account) {
		return accountService.saveAllAccounts(account);
	}

	/**
	 * API PUT call to /api/account/update?accountId=(input) with content in the body that describes the updates to the account.
	 * Will update it to the database.
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
	 * @return True if successful. False otherwise
	 */
	@DeleteMapping("/del")
	public boolean deleteAccount(@RequestParam("accountId") int id) {

		return Boolean.TRUE.equals(accountService.deleteAccount(id));
	}
}
