package com.example.reviewsbackend.review;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import com.example.shopBackend.ShopBackendApplication;
import com.example.shopBackend.review.ReviewRepository;
import com.example.shopBackend.review.ReviewService;
import com.example.shopBackend.user.User;
import com.example.shopBackend.user.UserRepository;

import exception.BadRequestException;

@ActiveProfiles("test")
@DataJpaTest()
@ContextConfiguration(classes = ShopBackendApplication.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ExtendWith(MockitoExtension.class)
public class ReviewServiceTest {

	
	@Mock
	private ReviewRepository reviewRepository;
	
	@Mock
	private UserRepository userRepository;

	@InjectMocks
	private ReviewService testReviewService;
	
	@BeforeEach
	void SetVerify() {
		given(userRepository.findById(any())).willReturn(Optional.of(new User()));
	}
	
	@Test
	void GetAllReviewsForUserWorks() {

		Pageable pageRequest = PageRequest.of(0, 4);
		testReviewService.getReviewsForUser(1, 0);
		
		verify(reviewRepository).findAllUserId(1, pageRequest);
	}
	
	@Test
	void GetAllReviewsForUserThrowsErrorWithNegativePage() {

		testReviewService.getReviewsForUser(-1, 0);
		
		assertThatThrownBy(() -> testReviewService.getReviewsForUser(0, -1))
			.isInstanceOf(java.lang.IllegalArgumentException.class)
			.hasMessageContaining("Page index must not be less than zero");
		
		Pageable pageRequest = PageRequest.of(0, 4);
		
		verify(reviewRepository, never()).findAllUserId(0, pageRequest);
	}
	
	@Test
	void GetAllReviewsForUserThrowsErrorWithNotMatchingUserId() {
		
		given(userRepository.findById(any())).willReturn(Optional.empty());
		
		assertThatThrownBy(() -> testReviewService.getReviewsForUser(1, 0))
			.isInstanceOf(BadRequestException.class)
			.hasMessageContaining("No users exists with this id");
		
		Pageable pageRequest = PageRequest.of(0, 4);
		
		verify(reviewRepository, never()).findAllUserId(0, pageRequest);
	}
}
