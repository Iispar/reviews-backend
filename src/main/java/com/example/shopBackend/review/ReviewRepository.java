package com.example.shopBackend.review;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Integer> {
	
	@Query(value = "SELECT * FROM Review r WHERE r.review_user = ?1 LIMIT ?2,?3", nativeQuery = true)
	List<Review> findAllUserId(int id, int from, int to);
	
	@Query(value = "SELECT * FROM Review r WHERE r.review_title LIKE ?1 AND r.review_item = ?2", nativeQuery = true)
	List<Review> findAllByTitleForUser(String title, int id);
}

