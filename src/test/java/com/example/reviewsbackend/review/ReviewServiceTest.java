package com.example.reviewsbackend.review;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import java.util.Optional;

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
import org.springframework.data.domain.Sort;

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
//		given(userRepository.findById(any())).willReturn(Optional.of(new User()));
		
		assertThatThrownBy(() -> testReviewService.getReviewsForUser(0, -1))
			.isInstanceOf(java.lang.IllegalArgumentException.class)
			.hasMessageContaining("Page index must not be less than zero");
	}
	
	@Test
	void GetAllReviewsForUserThrowsErrorWithNotMatchingUserId() {
		
		given(userRepository.findById(any())).willReturn(Optional.empty());
		
		assertThatThrownBy(() -> testReviewService.getReviewsForUser(1, 0))
			.isInstanceOf(BadRequestException.class)
			.hasMessageContaining("No users exists with id 1");
		
		Pageable pageRequest = PageRequest.of(0, 4);
		
		verify(reviewRepository, never()).findAllUserId(1, pageRequest);
	}
	
	@Test
	void GetAllReviewsForItemWorks() {
		given(itemRepository.findById(any())).willReturn(Optional.of(new Item()));

		Pageable pageRequest = PageRequest.of(0, 4, Sort.by("review_date").ascending());
		testReviewService.getReviewsForItem(1, 0, "review_date", "asc");
		
		verify(reviewRepository).findAllItemId(1, pageRequest);
	}
	
	@Test
	void GetAllReviewsForItemThrowsErrorWithNegativePage() {
		given(itemRepository.findById(any())).willReturn(Optional.of(new Item()));
		
		assertThatThrownBy(() -> testReviewService.getReviewsForItem(0, -1, "review_date", "asc"))
			.isInstanceOf(java.lang.IllegalArgumentException.class)
			.hasMessageContaining("Page index must not be less than zero");
	}
	
	@Test
	void GetAllReviewsForItemThrowsErrorWithNotMatchingUserId() {
		given(itemRepository.findById(any())).willReturn(Optional.empty());
		
		assertThatThrownBy(() -> testReviewService.getReviewsForItem(1, 0, "review_date", "asc"))
			.isInstanceOf(BadRequestException.class)
			.hasMessageContaining("No items exists with id 1");
		
		Pageable pageRequest = PageRequest.of(0, 4);
		
		verify(reviewRepository, never()).findAllItemId(1, pageRequest);
	}
	
	@Test
	void GetAllReviewsForItemThrowsErrorWithBadSort() {
		given(itemRepository.findById(any())).willReturn(Optional.of(new Item()));
		
		assertThatThrownBy(() -> testReviewService.getReviewsForItem(1, 0, "review_name", "asc"))
			.isInstanceOf(BadRequestException.class)
			.hasMessageContaining("sort review_name is not a valid value for a sort in the entity.");
		
		Pageable pageRequest = PageRequest.of(0, 4);
		
		verify(reviewRepository, never()).findAllItemId(1, pageRequest);
	}
	
	@Test
	void GetAllReviewsForItemThrowsErrorWithBadSortDir() {
		given(itemRepository.findById(any())).willReturn(Optional.of(new Item()));
		
		assertThatThrownBy(() -> testReviewService.getReviewsForItem(1, 0, "review_name", "ascending"))
			.isInstanceOf(BadRequestException.class)
			.hasMessageContaining("sort direction ascending is not supported. Has to be either asc or desc.");
		
		Pageable pageRequest = PageRequest.of(0, 4);
		
		verify(reviewRepository, never()).findAllItemId(1, pageRequest);
	}
	
	@Test
	void getReviewsWithTitleForItemWorks() {
		given(itemRepository.findById(any())).willReturn(Optional.of(new Item()));

		testReviewService.getReviewsWithTitleForItem("test_title", 0, 1,"review_date", "asc");
		
		Pageable pageRequest = PageRequest.of(1, 4, Sort.by("review_date").ascending());

		verify(reviewRepository).findAllByTitleForItem("%test%title%", 0, pageRequest);
	}
	
	@Test
	void getReviewsWithTitleForItemThrowsErrorWithNegativePage() {
		given(itemRepository.findById(any())).willReturn(Optional.of(new Item()));
		
		assertThatThrownBy(() -> testReviewService.getReviewsWithTitleForItem("test_title", 0, -1, "review_date", "asc"))
			.isInstanceOf(java.lang.IllegalArgumentException.class)
			.hasMessageContaining("Page index must not be less than zero");
	}
	
	@Test
	void getReviewsWithTitleForItemThrowsErrorWithNotMatchingUserId() {
		given(itemRepository.findById(any())).willReturn(Optional.empty());
		
		assertThatThrownBy(() ->  testReviewService.getReviewsWithTitleForItem("test_title", 1, 0, "review_date", "asc"))
			.isInstanceOf(BadRequestException.class)
			.hasMessageContaining("No items exists with id 1");
		
		Pageable pageRequest = PageRequest.of(0, 4);
		
		verify(reviewRepository, never()).findAllByTitleForItem("%test%title%", 1, pageRequest);
	}
	
	@Test
	void getReviewsWithTitleForItemThrowsErrorWithBadSort() {
		given(itemRepository.findById(any())).willReturn(Optional.of(new Item()));
		
		assertThatThrownBy(() -> testReviewService.getReviewsWithTitleForItem("test_title", 1, 0, "review_name", "asc"))
			.isInstanceOf(BadRequestException.class)
			.hasMessageContaining("sort review_name is not a valid value for a sort in the entity.");
		
		Pageable pageRequest = PageRequest.of(0, 4);
		
		verify(reviewRepository, never()).findAllByTitleForItem("%test%title%", 1, pageRequest);
	}
	
	@Test
	void getReviewsWithTitleForItemThrowsErrorWithBadSortDir() {
		given(itemRepository.findById(any())).willReturn(Optional.of(new Item()));
		
		assertThatThrownBy(() -> testReviewService.getReviewsWithTitleForItem("test_title", 1, 0, "review_name", "ascending"))
			.isInstanceOf(BadRequestException.class)
			.hasMessageContaining("sort direction ascending is not supported. Has to be either asc or desc.");
		
		Pageable pageRequest = PageRequest.of(0, 4);
		
		verify(reviewRepository, never()).findAllByTitleForItem("%test%title%", 1, pageRequest);
	}
	
	@Test
	void getChartForUserWorksWithMonth() {
		given(userRepository.findById(any())).willReturn(Optional.of(new User()));

		testReviewService.getChartForUser("month", 0);

		verify(reviewRepository).findChartForUserByMonth(0);
	}
	
	@Test
	void getChartForUserWorksWithWeek() {
		given(userRepository.findById(any())).willReturn(Optional.of(new User()));

		testReviewService.getChartForUser("week", 0);

		verify(reviewRepository).findChartForUserByWeek(0);
	}
	
	@Test
	void getChartForUserThrowsErrorWithNotMatchingUser() {
		given(userRepository.findById(any())).willReturn(Optional.empty());
		
		assertThatThrownBy(() ->  testReviewService.getChartForUser("week", 1))
		.isInstanceOf(BadRequestException.class)
		.hasMessageContaining("No users exists with id 1");

		verify(reviewRepository, never()).findChartForUserByWeek(0);
	}
	
	@Test
	void getChartForUserThrowsErrorWithBadTime() {
//		given(userRepository.findById(any())).willReturn(Optional.empty());
		
		assertThatThrownBy(() ->  testReviewService.getChartForUser("weekend", 1))
		.isInstanceOf(BadRequestException.class)
		.hasMessageContaining("time weekend is not a valid value for a timespan . Either week or month");

		verify(reviewRepository, never()).findChartForUserByWeek(0);
	}
	
	@Test
	void getChartForItemWorksWithMonth() {
		given(itemRepository.findById(any())).willReturn(Optional.of(new Item()));

		testReviewService.getChartForItem("month", 0);

		verify(reviewRepository).findChartForItemByMonth(0);
	}
	
	@Test
	void getChartForItemWorksWithWeek() {
		given(itemRepository.findById(any())).willReturn(Optional.of(new Item()));

		testReviewService.getChartForItem("week", 0);

		verify(reviewRepository).findChartForItemByWeek(0);
	}
	
	@Test
	void getChartForItemThrowsErrorWithNotMatchingUser() {
		given(itemRepository.findById(any())).willReturn(Optional.empty());
		
		assertThatThrownBy(() ->  testReviewService.getChartForItem("week", 1))
		.isInstanceOf(BadRequestException.class)
		.hasMessageContaining("No items exists with id 1");

		verify(reviewRepository, never()).findChartForItemByWeek(0);
	}
	
	@Test
	void getChartForItemThrowsErrorWithBadTime() {
//		given(userRepository.findById(any())).willReturn(Optional.empty());
		
		assertThatThrownBy(() ->  testReviewService.getChartForItem("weekend", 1))
		.isInstanceOf(BadRequestException.class)
		.hasMessageContaining("time weekend is not a valid value for a timespan . Either week or month");

		verify(reviewRepository, never()).findChartForItemByWeek(0);
	}
	
	
}
