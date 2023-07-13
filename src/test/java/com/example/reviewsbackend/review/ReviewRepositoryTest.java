package com.example.reviewsbackend.review;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.sql.Date;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;

import com.example.shopBackend.ShopBackendApplication;
import com.example.shopBackend.item.Item;
import com.example.shopBackend.item.ItemRepository;
import com.example.shopBackend.review.Review;
import com.example.shopBackend.review.ReviewRepository;
import com.example.shopBackend.user.User;
import com.example.shopBackend.user.UserRepository;

@SpringBootTest(classes = ShopBackendApplication.class)
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
// @AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
class ReviewRepositoryTest {
	
	@Autowired
	private UserRepository testUserRepository;
	
	@Autowired
	private ReviewRepository testReviewRepository;

	@Autowired
	private ItemRepository testItemRepository;

	@Test
	void addReviewWorks() {
		User user = testUserRepository.findById(1);
		Item item = testItemRepository.findById(1);
		Review review = new Review(
				new Date(0),
				"review body for test",
				"review title for test",
				null,
				null,
				user,
				4,
				item
				);
		testReviewRepository.save(review);
		
        Review foundEntity = testReviewRepository.findById(review.getId());
        assertNotNull(foundEntity);
	}
	
}
