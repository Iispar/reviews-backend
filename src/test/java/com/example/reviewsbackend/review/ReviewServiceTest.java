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
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import com.example.shopBackend.ShopBackendApplication;
import com.example.shopBackend.item.Item;
import com.example.shopBackend.item.ItemRepository;
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
	
	@Mock
	private ItemRepository itemRepository;

	@InjectMocks
	private ReviewService testReviewService;
	
	@Test
	void GetAllReviewsForUserWorks() {
		given(userRepository.findById(any())).willReturn(Optional.of(new User()));

		Pageable pageRequest = PageRequest.of(0, 4);
		testReviewService.getReviewsForUser(1, 0);
		
		verify(reviewRepository).findAllUserId(1, pageRequest);
	}
	
	@Test
	void GetAllReviewsForUserThrowsErrorWithNegativePage() {
		given(userRepository.findById(any())).willReturn(Optional.of(new User()));

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
	
	@Test
	void GetAllReviewsForItemWorks() {
		given(itemRepository.findById(any())).willReturn(Optional.of(new Item()));

		Pageable pageRequest = PageRequest.of(0, 4);
		testReviewService.getReviewsForItem(1, 0);
		
		verify(reviewRepository).findAllItemId(1, pageRequest);
	}
	
	@Test
	void GetAllReviewsForItemThrowsErrorWithNegativePage() {
		given(itemRepository.findById(any())).willReturn(Optional.of(new Item()));

		testReviewService.getReviewsForItem(-1, 0);
		
		assertThatThrownBy(() -> testReviewService.getReviewsForItem(0, -1))
			.isInstanceOf(java.lang.IllegalArgumentException.class)
			.hasMessageContaining("Page index must not be less than zero");
		
		Pageable pageRequest = PageRequest.of(0, 4);
		
		verify(reviewRepository, never()).findAllItemId(0, pageRequest);
	}
	
	@Test
	void GetAllReviewsForItemThrowsErrorWithNotMatchingUserId() {
		given(itemRepository.findById(any())).willReturn(Optional.empty());
		
		assertThatThrownBy(() -> testReviewService.getReviewsForItem(1, 0))
			.isInstanceOf(BadRequestException.class)
			.hasMessageContaining("No items exists with this id");
		
		Pageable pageRequest = PageRequest.of(0, 4);
		
		verify(reviewRepository, never()).findAllItemId(0, pageRequest);
	}
	
	
}
