package com.example.shopBackend.account;

import com.example.shopBackend.security.AuthRequest;
import com.example.shopBackend.security.AuthRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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
	 * API GET call to /api/account/get?accountId=(input)
	 * will return account that matched the id
	 * @param accountId
	 * 	      The account to be added to the database
	 * @return Account that matches
	 */
	@PreAuthorize("#id == authentication.principal.id")
	@GetMapping("/get")
	public Account get(@RequestParam int accountId) {
		return accountService.getAccount(accountId);
	}
	
	/**
	 * API POST call to /api/account/add with content in the body that describes the added account
	 * will add it to the database.
	 * @param account
	 * 	      The account to be added to the database
	 * @return Jwt token
	 */
	@PostMapping("/add")
	public AuthRes add(@RequestBody Account account) {
		return accountService.saveAccount(account);
	}

	/**
	 * API POST call to /api/account/login with content in the body that describes the account trying to log in
	 * with username and password.
	 * @param request
	 * 		  The username and password trying to log in.
	 * @return Jwt token
	 */
	@PostMapping("/login")
	public AuthRes login(@RequestBody AuthRequest request) {
		return accountService.login(request);
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
	@PreAuthorize("#id == authentication.principal.id")
	@PutMapping("/update")
	public boolean update(
			@RequestParam int accountId,
			@RequestBody Account account) {
		return accountService.updateAccount(accountId, account);
	}

	/**
	 * API DELETE call to /api/account/del?accountId=(input)
	 * will delete the account with the corresponding id.
	 * @return True if successful. Error otherwise
	 */
	@PreAuthorize("#id == authentication.principal.id")
	@DeleteMapping("/del")
	public boolean deleteAccount(@RequestParam("accountId") int id) {
		return Boolean.TRUE.equals(accountService.deleteAccount(id));
	}
}
