package com.example.reviewsbackend.review;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.example.shopBackend.review.ReviewRepository;

// TODO:
@DataJpaTest
public class ReviewRepositoryTest {
	
	@Autowired
	private ReviewRepository testRepository;
	
	@AfterEach
	void tearDown() {
		testRepository.deleteAll();
	}

	@Test
	void addReviewWorks() {
		
	}
	
}
