package com.example.shopBackend.review;

import com.example.shopBackend.ShopBackendApplication;
import com.example.shopBackend.account.Account;
import com.example.shopBackend.account.AccountRepository;
import com.example.shopBackend.category.Category;
import com.example.shopBackend.item.Item;
import com.example.shopBackend.item.ItemRepository;
import com.example.shopBackend.item.ItemService;
import com.example.shopBackend.role.Role;
import exception.BadRequestException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ActiveProfiles("test")
@DataJpaTest()
@ContextConfiguration(classes = ShopBackendApplication.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class ReviewServiceTest {
	
	@Mock
	private ReviewRepository reviewRepository;
	
	@Mock
	private AccountRepository accountRepository;
	
	@Mock
	private ItemRepository itemRepository;

	@Mock
	private ItemService itemService;

	@Mock
	private ReviewUtil reviewUtil;


	@InjectMocks
	private ReviewService testReviewService;


	@Test
	void GetAllReviewsForAccountWorksWithAsc() {
		given(accountRepository.findById(any())).willReturn(Optional.of(new Account()));

		Pageable pageRequest = PageRequest.of(0, 4, Sort.by("review_date").ascending());
		testReviewService.getReviewsForAccount(1, 0, "review_date", "asc");
		
		verify(reviewRepository).findAllAccountId(1, pageRequest);
	}

	@Test
	void GetAllReviewsForAccountWorksWithDesc() {
		given(accountRepository.findById(any())).willReturn(Optional.of(new Account()));

		Pageable pageRequest = PageRequest.of(0, 4, Sort.by("review_date").descending());
		testReviewService.getReviewsForAccount(1, 0, "review_date", "desc");

		verify(reviewRepository).findAllAccountId(1, pageRequest);
	}
	
	@Test
	void GetAllReviewsForAccountThrowsErrorWithNegativePage() {
//		given(accountRepository.findById(any())).willReturn(Optional.of(new Account()));
		
		assertThatThrownBy(() -> testReviewService.getReviewsForAccount(0, -1, "review_date", "asc"))
			.isInstanceOf(java.lang.IllegalArgumentException.class)
			.hasMessageContaining("Page index must not be less than zero");
	}
	
	@Test
	void GetAllReviewsForAccountThrowsErrorWithNotMatchingAccountId() {
		
		given(accountRepository.findById(any())).willReturn(Optional.empty());
		
		assertThatThrownBy(() -> testReviewService.getReviewsForAccount(1, 0, "review_date", "asc"))
			.isInstanceOf(BadRequestException.class)
			.hasMessageContaining("No Accounts exists with id 1");
		
		Pageable pageRequest = PageRequest.of(0, 4);
		
		verify(reviewRepository, never()).findAllAccountId(1, pageRequest);
	}
	
	@Test
	void GetAllReviewsForItemWorksWithAsc() {
		given(itemRepository.findById(any())).willReturn(Optional.of(new Item()));

		Pageable pageRequest = PageRequest.of(0, 4, Sort.by("review_date").ascending());
		testReviewService.getReviewsForItem(1, 0, "review_date", "asc");
		
		verify(reviewRepository).findAllItemId(1, pageRequest);
	}

	@Test
	void GetAllReviewsForItemWorksWithDesc() {
		given(itemRepository.findById(any())).willReturn(Optional.of(new Item()));

		Pageable pageRequest = PageRequest.of(0, 4, Sort.by("review_date").descending());
		testReviewService.getReviewsForItem(1, 0, "review_date", "desc");

		verify(reviewRepository).findAllItemId(1, pageRequest);
	}

	@Test
	void GetAllReviewsForAccountThrowsErrorWithBadSort() {
		given(itemRepository.findById(any())).willReturn(Optional.of(new Item()));

		assertThatThrownBy(() -> testReviewService.getReviewsForAccount(1, 0, "review_name", "asc"))
				.isInstanceOf(BadRequestException.class)
				.hasMessageContaining("sort review_name is not a valid value for a sort in the entity.");

		Pageable pageRequest = PageRequest.of(0, 4);

		verify(reviewRepository, never()).findAllAccountId(1, pageRequest);
	}

	@Test
	void GetAllReviewsForAccountThrowsErrorWithBadSortDir() {
		given(itemRepository.findById(any())).willReturn(Optional.of(new Item()));
		given(accountRepository.findById(any())).willReturn(Optional.of(new Account()));

		assertThatThrownBy(() -> testReviewService.getReviewsForAccount(1, 0, "review_date", "ascending"))
				.isInstanceOf(BadRequestException.class)
				.hasMessageContaining("sort direction ascending is not supported. Has to be either asc or desc.");

		Pageable pageRequest = PageRequest.of(0, 4);

		verify(reviewRepository, never()).findAllAccountId(1, pageRequest);
	}
	
	@Test
	void GetAllReviewsForItemThrowsErrorWithNegativePage() {
		given(itemRepository.findById(any())).willReturn(Optional.of(new Item()));
		
		assertThatThrownBy(() -> testReviewService.getReviewsForItem(0, -1, "review_date", "asc"))
			.isInstanceOf(java.lang.IllegalArgumentException.class)
			.hasMessageContaining("Page index must not be less than zero");
	}
	
	@Test
	void GetAllReviewsForItemThrowsErrorWithNotMatchingAccountId() {
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
	void getReviewsWithBodyForItemWorksWithAsc() {
		given(itemRepository.findById(any())).willReturn(Optional.of(new Item()));

		testReviewService.getReviewsWithBodyForItem("test_title", 0, 1,"review_date", "asc");
		
		Pageable pageRequest = PageRequest.of(1, 4, Sort.by("review_date").ascending());

		verify(reviewRepository).findAllByBodyForItem("%test%title%", 0, pageRequest);
	}

	@Test
	void getReviewsWithBodyForItemWorksWithDesc() {
		given(itemRepository.findById(any())).willReturn(Optional.of(new Item()));

		testReviewService.getReviewsWithBodyForItem("test_title", 0, 1,"review_date", "desc");

		Pageable pageRequest = PageRequest.of(1, 4, Sort.by("review_date").descending());

		verify(reviewRepository).findAllByBodyForItem("%test%title%", 0, pageRequest);
	}
	
	@Test
	void getReviewsWithBodyForItemThrowsErrorWithNegativePage() {
		given(itemRepository.findById(any())).willReturn(Optional.of(new Item()));
		
		assertThatThrownBy(() -> testReviewService.getReviewsWithBodyForItem("test_title", 0, -1, "review_date", "asc"))
			.isInstanceOf(java.lang.IllegalArgumentException.class)
			.hasMessageContaining("Page index must not be less than zero");
	}
	
	@Test
	void getReviewsWithBodyForItemThrowsErrorWithNotMatchingAccountId() {
		given(itemRepository.findById(any())).willReturn(Optional.empty());
		
		assertThatThrownBy(() ->  testReviewService.getReviewsWithBodyForItem("test_title", 1, 0, "review_date", "asc"))
			.isInstanceOf(BadRequestException.class)
			.hasMessageContaining("No items exists with id 1");
		
		Pageable pageRequest = PageRequest.of(0, 4);
		
		verify(reviewRepository, never()).findAllByBodyForItem("%test%title%", 1, pageRequest);
	}
	
	@Test
	void getReviewsWithBodyForItemThrowsErrorWithBadSort() {
		given(itemRepository.findById(any())).willReturn(Optional.of(new Item()));
		
		assertThatThrownBy(() -> testReviewService.getReviewsWithBodyForItem("test_title", 1, 0, "review_name", "asc"))
			.isInstanceOf(BadRequestException.class)
			.hasMessageContaining("sort review_name is not a valid value for a sort in the entity.");
		
		Pageable pageRequest = PageRequest.of(0, 4);
		
		verify(reviewRepository, never()).findAllByBodyForItem("%test%title%", 1, pageRequest);
	}
	
	@Test
	void getReviewsWithBodyForItemThrowsErrorWithBadSortDir() {
		given(itemRepository.findById(any())).willReturn(Optional.of(new Item()));
		
		assertThatThrownBy(() -> testReviewService.getReviewsWithBodyForItem("test_title", 1, 0, "review_name", "ascending"))
			.isInstanceOf(BadRequestException.class)
			.hasMessageContaining("sort direction ascending is not supported. Has to be either asc or desc.");
		
		Pageable pageRequest = PageRequest.of(0, 4);
		
		verify(reviewRepository, never()).findAllByBodyForItem("%test%title%", 1, pageRequest);
	}


	@Test
	void getReviewsWithTitleForItemWorksWithAsc() {
		given(itemRepository.findById(any())).willReturn(Optional.of(new Item()));

		testReviewService.getReviewsWithTitleForItem("test_title", 0, 1,"review_date", "asc");

		Pageable pageRequest = PageRequest.of(1, 4, Sort.by("review_date").ascending());

		verify(reviewRepository).findAllByTitleForItem("%test%title%", 0, pageRequest);
	}

	@Test
	void getReviewsWithTitleForItemWorksWithDesc() {
		given(itemRepository.findById(any())).willReturn(Optional.of(new Item()));

		testReviewService.getReviewsWithTitleForItem("test_title", 0, 1,"review_date", "desc");

		Pageable pageRequest = PageRequest.of(1, 4, Sort.by("review_date").descending());

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
	void getReviewsWithTitleForItemThrowsErrorWithNotMatchingAccountId() {
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
	void getChartForAccountWorksWithMonth() {
		given(accountRepository.findById(any())).willReturn(Optional.of(new Account()));

		testReviewService.getChartForAccount("month", 0);

		verify(reviewRepository).findChartForAccountByMonth(0);
	}
	
	@Test
	void getChartForAccountWorksWithWeek() {
		given(accountRepository.findById(any())).willReturn(Optional.of(new Account()));

		testReviewService.getChartForAccount("week", 0);

		verify(reviewRepository).findChartForAccountByWeek(0);
	}
	
	@Test
	void getChartForAccountThrowsErrorWithNotMatchingAccount() {
		given(accountRepository.findById(any())).willReturn(Optional.empty());
		
		assertThatThrownBy(() ->  testReviewService.getChartForAccount("week", 1))
		.isInstanceOf(BadRequestException.class)
		.hasMessageContaining("No Accounts exists with id 1");

		verify(reviewRepository, never()).findChartForAccountByWeek(0);
	}
	
	@Test
	void getChartForAccountThrowsErrorWithBadTime() {
//		given(accountRepository.findById(any())).willReturn(Optional.empty());
		
		assertThatThrownBy(() ->  testReviewService.getChartForAccount("weekend", 1))
		.isInstanceOf(BadRequestException.class)
		.hasMessageContaining("time weekend is not a valid value for a timespan . Either week or month");

		verify(reviewRepository, never()).findChartForAccountByWeek(0);
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
	void getChartForItemThrowsErrorWithNotMatchingAccount() {
		given(itemRepository.findById(any())).willReturn(Optional.empty());
		
		assertThatThrownBy(() ->  testReviewService.getChartForItem("week", 1))
		.isInstanceOf(BadRequestException.class)
		.hasMessageContaining("No items exists with id 1");

		verify(reviewRepository, never()).findChartForItemByWeek(0);
	}
	
	@Test
	void getChartForItemThrowsErrorWithBadTime() {
//		given(accountRepository.findById(any())).willReturn(Optional.empty());
		
		assertThatThrownBy(() ->  testReviewService.getChartForItem("weekend", 1))
		.isInstanceOf(BadRequestException.class)
		.hasMessageContaining("time weekend is not a valid value for a timespan . Either week or month");

		verify(reviewRepository, never()).findChartForItemByWeek(0);
	}
	
	@Test
	void deleteReviewWorks() {
		given(reviewRepository.findById(any())).willReturn(Optional.of(new Review()));
		
		testReviewService.deleteReview(0);

		verify(reviewRepository).deleteById(0);
	}
	
	@Test
	void deleteReviewsThrowsErrorWithNoMatchingReview() {
		given(reviewRepository.findById(any())).willReturn(Optional.empty());
		
		assertThatThrownBy(() ->  testReviewService.deleteReview(0))
		.isInstanceOf(BadRequestException.class)
		.hasMessageContaining("No reviews exists with id 0");

		verify(reviewRepository, never()).deleteById(0);
	}
	
	@Test
	void addReviewWorks() {
		List<SingleRatedReview> singleRatedReviews;
		singleRatedReviews = new ArrayList<>();
		SingleRatedReview rate1 = new SingleRatedReview("test 1", 5);
		SingleRatedReview rate2 = new SingleRatedReview("test 2", 2);
		singleRatedReviews.add(rate1);
		singleRatedReviews.add(rate2);
		RatedReviews ratedReviews = new RatedReviews(singleRatedReviews, List.of("1", "2"), List.of("1", "2"));

		when(reviewUtil.rateReviews(any())).thenReturn(ratedReviews);

		given(itemRepository.findById(any())).willReturn(Optional.of(new Item()));
		given(accountRepository.findById(any())).willReturn(Optional.of(new Account()));
		given(reviewRepository.findAllBodysWithItemId(anyInt())).willReturn(new ArrayList<>());
		given(itemService.updateItemRatingAndWords(anyInt(), any(), any())).willReturn(new Item());
		List<Review> list = new ArrayList<>();
		Item item = new Item(1, "test title", null, 1, new Category(), null, "test desc");
		Account account = new Account(1, "test name", "test username", "testPass", "testEmail", new Role());
		Review review1 = new Review(
				new Date(1),
				"this item is really good and i loved it",
				"title",
				0,
				0,
				account,
				2,
				item
				);
		Review review2 = new Review(
				new Date(1),
				"this item is really bad",
				"title1",
				0,
				0,
				account,
				2,
				item
				);
		
		list.add(review1);
		list.add(review2);
		
		testReviewService.saveAllReviews(list);

		verify(reviewRepository).saveAll(list);
	}
	
	@Test
	void addReviewThrowsErrorWithBadLikes() {
		List<SingleRatedReview> singleRatedReviews = new ArrayList<>();
		SingleRatedReview rate1 = new SingleRatedReview("test 1", 5);
		SingleRatedReview rate2 = new SingleRatedReview("test 2", 2);
		singleRatedReviews.add(rate1);
		singleRatedReviews.add(rate2);
		RatedReviews ratedReviews = new RatedReviews(singleRatedReviews, List.of("1", "2"), List.of("1", "2"));

		when(reviewUtil.rateReviews(any())).thenReturn(ratedReviews);

		given(itemRepository.findById(any())).willReturn(Optional.of(new Item()));
		List<Review> list = new ArrayList<>();
		Item item = new Item(1, "test title", null, 1, new Category(), null, "test desc");
		Account account = new Account(1, "test name", "test username", "testPass", "testEmail", new Role());
		Review review1 = new Review(
				new Date(1),
				"title1",
				"body1",
				-1,
				0,
				account,
				2,
				item
				);
		Review review2 = new Review(
				new Date(1),
				"title2",
				"body2",
				0,
				0,
				account,
				2,
				item
				);
		
		list.add(review1);
		list.add(review2);

		assertThatThrownBy(() ->  testReviewService.saveAllReviews(list))
		.isInstanceOf(BadRequestException.class)
		.hasMessageContaining("review with negative likes not allowed");


		verify(reviewRepository, never()).saveAll(list);
	}
	
	@Test
	void addReviewThrowsErrorWithBadDislikes() {
		List<SingleRatedReview> singleRatedReviews = new ArrayList<>();
		SingleRatedReview rate1 = new SingleRatedReview("test 1", 5);
		SingleRatedReview rate2 = new SingleRatedReview("test 2", 2);
		singleRatedReviews.add(rate1);
		singleRatedReviews.add(rate2);
		RatedReviews ratedReviews = new RatedReviews(singleRatedReviews, List.of("1", "2"), List.of("1", "2"));

		when(reviewUtil.rateReviews(any())).thenReturn(ratedReviews);

		given(itemRepository.findById(any())).willReturn(Optional.of(new Item()));
		given(accountRepository.findById(any())).willReturn(Optional.of(new Account()));
		List<Review> list = new ArrayList<>();
		Item item = new Item(1, "test title", null, 1, new Category(), null, "test desc");
		Account account = new Account(1, "test name", "test username", "testPass", "testEmail", new Role());
		Review review1 = new Review(
				new Date(1),
				"title1",
				"body1",
				1,
				0,
				account,
				2,
				item
				);
		Review review2 = new Review(
				new Date(1),
				"title2",
				"body2",
				0,
				-4,
				account,
				2,
				item
				);
		
		list.add(review1);
		list.add(review2);

		assertThatThrownBy(() ->  testReviewService.saveAllReviews(list))
		.isInstanceOf(BadRequestException.class)
		.hasMessageContaining("review with negative dislikes not allowed");


		verify(reviewRepository, never()).saveAll(list);
	}

	@Test
	void addReviewThrowsErrorWithBadAccountId() {
		List<SingleRatedReview> singleRatedReviews = new ArrayList<>();
		SingleRatedReview rate1 = new SingleRatedReview("test 1", 5);
		SingleRatedReview rate2 = new SingleRatedReview("test 2", 2);
		singleRatedReviews.add(rate1);
		singleRatedReviews.add(rate2);
		RatedReviews ratedReviews = new RatedReviews(singleRatedReviews, List.of("1", "2"), List.of("1", "2"));

		when(reviewUtil.rateReviews(any())).thenReturn(ratedReviews);

		given(itemRepository.findById(any())).willReturn(Optional.of(new Item()));
		given(accountRepository.findById(any())).willReturn(Optional.empty());
		List<Review> list = new ArrayList<>();
		Item item = new Item(1, "test title", null, 1, new Category(), null, "test desc");
		Account account = new Account(1, "test name", "test username", "testPass", "testEmail", new Role());
		Review review1 = new Review(
				new Date(1),
				"title1",
				"body1",
				1,
				2,
				account,
				2,
				item
		);
		Review review2 = new Review(
				new Date(1),
				"title2",
				"body2",
				0,
				0,
				account,
				2,
				item
		);

		list.add(review1);
		list.add(review2);

		assertThatThrownBy(() ->  testReviewService.saveAllReviews(list))
				.isInstanceOf(BadRequestException.class)
				.hasMessageContaining("Account with id: " + account.getId() + " does not exist");


		verify(reviewRepository, never()).saveAll(list);
	}

	@Test
	void addReviewThrowsErrorWithTooLargeRating() {
		List<SingleRatedReview> singleRatedReviews = new ArrayList<>();
		SingleRatedReview rate1 = new SingleRatedReview("test 1", 8);
		singleRatedReviews.add(rate1);
		RatedReviews ratedReviews = new RatedReviews(singleRatedReviews, List.of("1"), List.of("1"));

		when(reviewUtil.rateReviews(any())).thenReturn(ratedReviews);

		given(itemRepository.findById(any())).willReturn(Optional.of(new Item()));
		given(accountRepository.findById(any())).willReturn(Optional.of(new Account()));
		List<Review> list = new ArrayList<>();
		Item item = new Item(1, "test title", null, 1, new Category(), null, "test desc");
		Account account = new Account(1, "test name", "test username", "testPass", "testEmail", new Role());
		Review review1 = new Review(
				new Date(1),
				"title1",
				"body1",
				1,
				0,
				account,
				4,
				item
		);

		list.add(review1);

		assertThatThrownBy(() ->  testReviewService.saveAllReviews(list))
				.isInstanceOf(BadRequestException.class)
				.hasMessageContaining("review with invalid rating. Has to be between 0-5.");


		verify(reviewRepository, never()).saveAll(list);
	}

	@Test
	void addReviewThrowsErrorWithTooSmallRating() {
		List<SingleRatedReview> singleRatedReviews = new ArrayList<>();
		SingleRatedReview rate1 = new SingleRatedReview("test 1", -2);
		singleRatedReviews.add(rate1);
		RatedReviews ratedReviews = new RatedReviews(singleRatedReviews, List.of("1"), List.of("1"));

		when(reviewUtil.rateReviews(any())).thenReturn(ratedReviews);
		given(itemRepository.findById(any())).willReturn(Optional.of(new Item()));
		given(accountRepository.findById(any())).willReturn(Optional.of(new Account()));
		List<Review> list = new ArrayList<>();
		Item item = new Item(1, "test title", null, 1, new Category(), null, "test desc");
		Account account = new Account(1, "test name", "test username", "testPass", "testEmail", new Role());
		Review review1 = new Review(
				new Date(1),
				"title1",
				"body1",
				1,
				0,
				account,
				4,
				item
		);

		list.add(review1);

		assertThatThrownBy(() ->  testReviewService.saveAllReviews(list))
				.isInstanceOf(BadRequestException.class)
				.hasMessageContaining("review with invalid rating. Has to be between 0-5.");

		verify(reviewRepository, never()).saveAll(list);
	}

	@Test
	void addReviewThrowsErrorWithBadItemId() {
		given(itemRepository.findById(any())).willReturn(Optional.empty());
		List<Review> list = new ArrayList<>();
		Item item = new Item(1, "test title", null, 1, new Category(), null, "test desc");
		Account account = new Account(1, "test name", "test username", "testPass", "testEmail", new Role());
		Review review1 = new Review(
				new Date(1),
				"title1",
				"body1",
				1,
				0,
				account,
				2,
				item
		);
		Review review2 = new Review(
				new Date(1),
				"title2",
				"body2",
				0,
				0,
				account,
				0,
				item
		);

		list.add(review1);
		list.add(review2);

		assertThatThrownBy(() ->  testReviewService.saveAllReviews(list))
				.isInstanceOf(BadRequestException.class)
				.hasMessageContaining("item with id: " + item.getId() + " does not exist");


		verify(reviewRepository, never()).saveAll(list);
	}

	@Test
	void addReviewThrowsErrorWithDifferentItemIds() {
		List<SingleRatedReview> singleRatedReviews = new ArrayList<>();
		SingleRatedReview rate1 = new SingleRatedReview("test 1", 5);
		SingleRatedReview rate2 = new SingleRatedReview("test 2", 2);
		singleRatedReviews.add(rate1);
		singleRatedReviews.add(rate2);
		RatedReviews ratedReviews = new RatedReviews(singleRatedReviews, List.of("1", "2"), List.of("1", "2"));

		when(reviewUtil.rateReviews(any())).thenReturn(ratedReviews);


		given(itemRepository.findById(any())).willReturn(Optional.of(new Item()));
		given(accountRepository.findById(any())).willReturn(Optional.of(new Account()));
		List<Review> list = new ArrayList<>();
		Item item = new Item(1, "test title", null, 1, new Category(), null, "test desc");
		Item item2 = new Item(2, "test title", null, 1, new Category(), null, "test desc");
		Account account = new Account(1, "test name", "test username", "testPass", "testEmail", new Role());
		Review review1 = new Review(
				new Date(1),
				"title1",
				"body1",
				1,
				0,
				account,
				2,
				item
		);
		Review review2 = new Review(
				new Date(1),
				"title2",
				"body2",
				0,
				0,
				account,
				4,
				item2
		);

		list.add(review1);
		list.add(review2);

		assertThatThrownBy(() ->  testReviewService.saveAllReviews(list))
				.isInstanceOf(BadRequestException.class)
				.hasMessageContaining("all reviews don't have the same id");


		verify(reviewRepository, never()).saveAll(list);
	}

	@Test
	void addReviewThrowsErrorWhenInputEmpty() {
		List<Review> list = new ArrayList<>();

		assertThatThrownBy(() ->  testReviewService.saveAllReviews(list))
				.isInstanceOf(BadRequestException.class)
				.hasMessageContaining("review input cannot be empty");

		verify(reviewRepository, never()).saveAll(list);
	}

	@Test
	void addReviewThrowsErrorWhenRatingFails() {
		given(itemRepository.findById(any())).willReturn(Optional.of(new Item()));
		given(accountRepository.findById(any())).willReturn(Optional.of(new Account()));

		given(reviewUtil.rateReviews(any())).willThrow(new RuntimeException());

		List<Review> list = new ArrayList<>();
		Item item = new Item(1, "test title", null, 1, new Category(), null, "test desc");
		Account account = new Account(1, "test name", "test username", "testPass", "testEmail", new Role());
		Review review1 = new Review(
				new Date(1),
				"title1",
				"body1",
				1,
				0,
				account,
				2,
				item
		);

		list.add(review1);

		assertThatThrownBy(() ->  testReviewService.saveAllReviews(list))
				.isInstanceOf(java.lang.RuntimeException.class)
				.hasMessageContaining("error: null. While calculating reviews");

		verify(reviewRepository, never()).saveAll(list);
	}
}
