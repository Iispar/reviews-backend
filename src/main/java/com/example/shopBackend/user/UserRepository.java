package com.example.shopBackend.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repo for the users.
 * @author iiro
 *
 */
@Repository
public interface UserRepository extends PagingAndSortingRepository<User, Integer>, JpaRepository<User, Integer> {
    /**
     * Query to find user with email
     * @param email
     *        email to search a user for
     * @return the items that matched the query
     */
    @Query(value = "SELECT * FROM accounts a WHERE a.account_email = ?1", nativeQuery = true)
    Optional<User> findByEmail(String email);

    /**
     * Query to find user with username
     * @param username
     *        Username to search a user for
     * @return the items that matched the query
     */
    @Query(value = "SELECT * FROM accounts a WHERE a.account_username = ?1", nativeQuery = true)
    Optional<User> findByUsername(String username);
}
