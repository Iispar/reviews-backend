package com.example.reviewsbackend.review;

import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;

import com.example.shopBackend.ShopBackendApplication;
import com.example.shopBackend.review.ReviewRepository;
import com.example.shopBackend.review.ReviewService;

@ActiveProfiles("test")
@SpringBootTest(classes = ShopBackendApplication.class)
// @DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
// @ExtendWith(SpringExtension.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ExtendWith(MockitoExtension.class)
public class ReviewServiceTest {

	
	@Mock
	private ReviewRepository reviewRepository;
	private ReviewService testReviewService;
	
	@BeforeEach
	void setUp() {
		testReviewService = new ReviewService(reviewRepository);
		
	}
	
	@Test
	void GetAllReviewsForUserWorks() {

		Pageable pageRequest = PageRequest.of(0, 4);
		testReviewService.getReviewsForUser(1, 0);
		
		verify(reviewRepository).findAllUserId(1, pageRequest);
	}
}
