package com.example.shopBackend.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

/**
 * Repo for the users.
 * @author iiro
 *
 */
@Repository
public interface UserRepository extends PagingAndSortingRepository<User, Integer>, JpaRepository<User, Integer> {
	
	/**
	 * Override findById to return only one User.
	 * @param {int} id
	 * 		  id to be used for query
	 * @return one User user.
	 */
	@Query(value = "SELECT * FROM accounts u WHERE u.account_id = ?1", nativeQuery = true)
	User findById(int id);
}
