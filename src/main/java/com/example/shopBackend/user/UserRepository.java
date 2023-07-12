package com.example.shopBackend.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.example.shopBackend.review.Review;

/**
 * Repo for the users.
 * @author iiro
 *
 */
@Repository
public interface UserRepository extends PagingAndSortingRepository<User, Integer>, JpaRepository<User, Integer> {

}
