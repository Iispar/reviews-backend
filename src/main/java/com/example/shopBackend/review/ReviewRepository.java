package com.example.shopBackend.review;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Integer> {
	
	@Query(value = "SELECT * FROM Review r WHERE r.user_id = 1", nativeQuery = true)
	List<Review> findAllUserId(int id);
}

