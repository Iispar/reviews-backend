package com.example.shopBackend.account;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repo for the accounts.
 * @author iiro
 *
 */
@Repository
public interface AccountRepository extends PagingAndSortingRepository<Account, Integer>, JpaRepository<Account, Integer> {
    /**
     * Query to find account with email
     * @param email
     *        email to search an account for
     * @return the account that matched the query
     */
    @Query(value = "SELECT * FROM accounts a WHERE a.account_email = ?1", nativeQuery = true)
    Optional<Account> findByEmail(String email);

    /**
     * Query to find account with username
     * @param username
     *        username to search an account for
     * @return the account that matched the query
     */
    @Query(value = "SELECT * FROM accounts a WHERE a.account_username = ?1", nativeQuery = true)
    Optional<Account> findByUsername(String username);
}
