package com.example.shopBackend.review;

import com.example.shopBackend.ShopBackendApplication;
import com.example.shopBackend.account.Account;
import com.example.shopBackend.account.AccountRepository;
import com.example.shopBackend.item.Item;
import com.example.shopBackend.item.ItemRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import java.sql.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@DataJpaTest()
@ContextConfiguration(classes = ShopBackendApplication.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ReviewRepositoryTest {
	
	@Autowired
	private AccountRepository testaccountRepository;
	
	@Autowired
	private ReviewRepository testReviewRepository;

	@Autowired
	private ItemRepository testItemRepository;
	
	@AfterEach
	void deleteAll() {
		testReviewRepository.deleteAll();
	}
	
	@Test
	void reviewFindAllAccountIdWorks() {
		Account account = testaccountRepository.findById(1).orElseThrow();
		Item item = testItemRepository.findById(1).orElseThrow();
		Review review = new Review(
				new Date(0),
				"review body for test",
				"review title for test",
				0,
				0,
				account,
				4,
				item
				);

		Review review2 = new Review(
				new Date(1),
				"review 2 body for test",
				"review 2 title for test",
				0,
				0,
				account,
				2,
				item
				);
		
		testReviewRepository.save(review);
		testReviewRepository.save(review2);
		
		Pageable pageRequest = PageRequest.of(0, 4);
		
        List<Review> foundEntity = testReviewRepository.findAllAccountId(account.getId(), pageRequest);
        List<Review> foundNoneEntity = testReviewRepository.findAllAccountId(account.getId() + 2, pageRequest);
		assertEquals(3, foundEntity.size());
		assertEquals(0, foundNoneEntity.size());
	}
	
	@Test
	void reviewFindAllByItemIdWorks() {
		Account account = testaccountRepository.findById(1).orElseThrow();
		Item item = testItemRepository.findById(1).orElseThrow();
		Review review = new Review(
				new Date(0),
				"review body for test",
				"hello item",
				0,
				0,
				account,
				4,
				item
				);

		Review review2 = new Review(
				new Date(1),
				"review 2 body for test",
				"test item 2",
				0,
				0,
				account,
				2,
				item
				);
		
		testReviewRepository.save(review);
		testReviewRepository.save(review2);
		
		Pageable pageRequest = PageRequest.of(0, 4);
		
        List<Review> foundEntity = testReviewRepository.findAllItemId(item.getId(), pageRequest);
        List<Review> foundNoneIdentity = testReviewRepository.findAllItemId(item.getId() + 2, pageRequest);
		assertEquals(3, foundEntity.size());
		assertEquals(0, foundNoneIdentity.size());
	}
	
	@Test
	void reviewFindAllByTitleWorks() {
		Account account = testaccountRepository.findById(1).orElseThrow();
		Item item = testItemRepository.findById(1).orElseThrow();
		Review review = new Review(
				new Date(0),
				"review body for test",
				"hello item",
				0,
				0,
				account,
				4,
				item
				);

		Review review2 = new Review(
				new Date(1),
				"review 2 body for test",
				"test item 2",
				0,
				0,
				account,
				2,
				item
				);
		
		testReviewRepository.save(review);
		testReviewRepository.save(review2);
		
		Pageable pageRequest = PageRequest.of(0, 4);
		
        List<Review> foundEntity = testReviewRepository.findAllByTitleForItem("%item%", item.getId(), pageRequest);
        List<Review> foundOneIdentity = testReviewRepository.findAllByTitleForItem("%hello%", item.getId(), pageRequest);
        List<Review> foundNoneIdentity = testReviewRepository.findAllByTitleForItem("%original%", item.getId(), pageRequest);
		assertEquals(2, foundEntity.size());
		assertEquals(1, foundOneIdentity.size());
		assertEquals(0, foundNoneIdentity.size());
	}

	@Test
	void reviewFindAllByBodyWorks() {
		Account account = testaccountRepository.findById(1).orElseThrow();
		Item item = testItemRepository.findById(1).orElseThrow();
		Review review = new Review(
				new Date(0),
				"review body for test",
				"hello item",
				0,
				0,
				account,
				4,
				item
		);

		Review review2 = new Review(
				new Date(1),
				"review 2 body for test",
				"test item 2",
				0,
				0,
				account,
				2,
				item
		);

		testReviewRepository.save(review);
		testReviewRepository.save(review2);

		Pageable pageRequest = PageRequest.of(0, 4);

		List<Review> foundEntity = testReviewRepository.findAllByBodyForItem("%body%", item.getId(), pageRequest);
		List<Review> foundOneIdentity = testReviewRepository.findAllByBodyForItem("%review 2%", item.getId(), pageRequest);
		List<Review> foundNoneIdentity = testReviewRepository.findAllByBodyForItem("%original%", item.getId(), pageRequest);
		assertEquals(3, foundEntity.size());
		assertEquals(1, foundOneIdentity.size());
		assertEquals(0, foundNoneIdentity.size());
	}
	
	@Test
	void reviewFindChartMonthByAccountIdWorks() {
		Account account = testaccountRepository.findById(1).orElseThrow();
		Item item = testItemRepository.findById(1).orElseThrow();
		Review review = new Review(
				Date.valueOf("2023-01-01"),
				"review body for test",
				"hello item",
				0,
				0,
				account,
				4,
				item
				);

		Review review2 = new Review(
				Date.valueOf("2023-02-01"),
				"review 2 body for test",
				"test item 2",
				0,
				0,
				account,
				2,
				item
				);
		
		Review review3 = new Review(
				Date.valueOf("2022-01-05"),
				"review 3 body for test",
				"test item 3",
				0,
				0,
				account,
				3,
				item
				);
		
		testReviewRepository.save(review);
		testReviewRepository.save(review2);
		testReviewRepository.save(review3);
		
        List<Chart> foundEntity = testReviewRepository.findChartForAccountByMonth(account.getId());
        List<Chart> foundNoneEntity = testReviewRepository.findChartForAccountByMonth(account.getId() + 2);

		assertEquals(2, foundEntity.size());
		assertEquals(0, foundNoneEntity.size());
	}

	@Test
	void reviewFindDistributionByAccount() {
		Account account = testaccountRepository.findById(1).orElseThrow();
		Item item = testItemRepository.findById(1).orElseThrow();
		Review review = new Review(
				Date.valueOf("2023-01-01"),
				"review body for test",
				"hello item",
				0,
				0,
				account,
				4,
				item
		);

		Review review2 = new Review(
				Date.valueOf("2023-02-01"),
				"review 2 body for test",
				"test item 2",
				0,
				0,
				account,
				2,
				item
		);

		Review review3 = new Review(
				Date.valueOf("2022-01-05"),
				"review 3 body for test",
				"test item 3",
				0,
				0,
				account,
				3,
				item
		);

		testReviewRepository.save(review);
		testReviewRepository.save(review2);
		testReviewRepository.save(review3);

		List<BarChart> foundEntity = testReviewRepository.findRatingDistributionWithAccountId(account.getId());
		List<BarChart> foundNoneEntity = testReviewRepository.findRatingDistributionWithAccountId(account.getId() + 2);

		assertEquals(3, foundEntity.size());
		assertEquals(0, foundNoneEntity.size());
	}
	
	@Test
	void reviewFindChartWeekByAccountIdWorks() {
		Account account = testaccountRepository.findById(1).orElseThrow();
		Item item = testItemRepository.findById(1).orElseThrow();
		Review review = new Review(
				Date.valueOf("2022-01-04"),
				"review body for test",
				"hello item",
				0,
				0,
				account,
				4,
				item
				);

		Review review2 = new Review(
				Date.valueOf("2022-02-01"),
				"review 2 body for test",
				"test item 2",
				0,
				0,
				account,
				2,
				item
				);
		
		Review review3 = new Review(
				Date.valueOf("2022-01-05"),
				"review 3 body for test",
				"test item 3",
				0,
				0,
				account,
				3,
				item
				);
		
		testReviewRepository.save(review);
		testReviewRepository.save(review2);
		testReviewRepository.save(review3);
		
        List<Chart> foundEntity = testReviewRepository.findChartForAccountByWeek(account.getId());
        List<Chart> foundNoneEntity = testReviewRepository.findChartForAccountByWeek(account.getId() + 2);
		assertEquals(2, foundEntity.size());
		assertEquals(0, foundNoneEntity.size());
	}
	
	@Test
	void reviewFindChartMonthByItemIdWorks() {
		Account account = testaccountRepository.findById(1).orElseThrow();
		Item item = testItemRepository.findById(1).orElseThrow();
		Review review = new Review(
				Date.valueOf("2023-01-01"),
				"review body for test",
				"hello item",
				0,
				0,
				account,
				4,
				item
				);

		Review review2 = new Review(
				Date.valueOf("2023-02-01"),
				"review 2 body for test",
				"test item 2",
				0,
				0,
				account,
				2,
				item
				);
		
		Review review3 = new Review(
				Date.valueOf("2022-01-05"),
				"review 3 body for test",
				"test item 3",
				0,
				0,
				account,
				3,
				item
				);
		
		testReviewRepository.save(review);
		testReviewRepository.save(review2);
		testReviewRepository.save(review3);
		
        List<Chart> foundEntity = testReviewRepository.findChartForItemByMonth(item.getId());
        List<Chart> foundNoneEntity = testReviewRepository.findChartForItemByMonth(item.getId() + 2);

		assertEquals(2, foundEntity.size());
		assertEquals(0, foundNoneEntity.size());
	}
	
	@Test
	void reviewFindChartWeekByItemIdWorks() {
		Account account = testaccountRepository.findById(1).orElseThrow();
		Item item = testItemRepository.findById(1).orElseThrow();
		Review review = new Review(
				Date.valueOf("2022-01-04"),
				"review body for test",
				"hello item",
				0,
				0,
				account,
				4,
				item
				);

		Review review2 = new Review(
				Date.valueOf("2022-02-01"),
				"review 2 body for test",
				"test item 2",
				0,
				0,
				account,
				2,
				item
				);
		
		Review review3 = new Review(
				Date.valueOf("2022-01-05"),
				"review 3 body for test",
				"test item 3",
				0,
				0,
				account,
				3,
				item
				);
		
		testReviewRepository.save(review);
		testReviewRepository.save(review2);
		testReviewRepository.save(review3);
		
        List<Chart> foundEntity = testReviewRepository.findChartForItemByWeek(item.getId());
        List<Chart> foundNoneEntity = testReviewRepository.findChartForItemByWeek(item.getId() + 2);
		assertEquals(2, foundEntity.size());
		assertEquals(0, foundNoneEntity.size());
	}

	@Test
	void reviewsFindAllBodysWithItemIdWorks() {
		Account account = testaccountRepository.findById(1).orElseThrow();
		Item item = testItemRepository.findById(1).orElseThrow();
		String string = "test";
		Review review = new Review(
				Date.valueOf("2022-01-04"),
				string,
				"hello item",
				0,
				0,
				account,
				4,
				item
		);

		Review review2 = new Review(
				Date.valueOf("2022-02-01"),
				"review 2 body for test",
				"test item 2",
				0,
				0,
				account,
				2,
				item
		);

		Review review3 = new Review(
				Date.valueOf("2022-01-05"),
				"review 3 body for test",
				"test item 3",
				0,
				0,
				account,
				3,
				item
		);

		testReviewRepository.save(review);
		testReviewRepository.save(review2);
		testReviewRepository.save(review3);

		List<String> foundEntity = testReviewRepository.findAllBodysWithItemId(item.getId());
		List<String> foundNoneEntity = testReviewRepository.findAllBodysWithItemId(item.getId() + 2);
		assertTrue(string.equals(foundEntity.get(1)));
		assertEquals(4, foundEntity.size());
		assertEquals(0, foundNoneEntity.size());
	}

	@Test
	void reviewsFindAllRatingsWithItemIdWorks() {
		Account account = testaccountRepository.findById(1).orElseThrow();
		Item item = testItemRepository.findById(1).orElseThrow();
		Review review = new Review(
				Date.valueOf("2022-01-04"),
				"review body for test",
				"hello item",
				0,
				0,
				account,
				4,
				item
		);

		Review review2 = new Review(
				Date.valueOf("2022-02-01"),
				"review 2 body for test",
				"test item 2",
				0,
				0,
				account,
				2,
				item
		);

		Review review3 = new Review(
				Date.valueOf("2022-01-05"),
				"review 3 body for test",
				"test item 3",
				0,
				0,
				account,
				3,
				item
		);

		testReviewRepository.save(review);
		testReviewRepository.save(review2);
		testReviewRepository.save(review3);

		List<Integer> foundEntity = testReviewRepository.findAllRatingsWithItemId(item.getId());
		List<Integer> foundNoneEntity = testReviewRepository.findAllRatingsWithItemId(item.getId() + 2);
		assertEquals(4, (int) foundEntity.get(0));
		assertEquals(4, foundEntity.size());
		assertEquals(0, foundNoneEntity.size());
	}

	@Test
	void reviewsFindCountWithAccountIdWorks() {
		Account account = testaccountRepository.findById(1).orElseThrow();
		Item item = testItemRepository.findById(1).orElseThrow();
		Review review = new Review(
				Date.valueOf("2022-01-04"),
				"review body for test",
				"hello item",
				0,
				0,
				account,
				4,
				item
		);

		Review review2 = new Review(
				Date.valueOf("2022-02-01"),
				"review 2 body for test",
				"test item 2",
				0,
				0,
				account,
				2,
				item
		);

		Review review3 = new Review(
				Date.valueOf("2022-01-05"),
				"review 3 body for test",
				"test item 3",
				0,
				0,
				account,
				3,
				item
		);

		testReviewRepository.save(review);
		testReviewRepository.save(review2);
		testReviewRepository.save(review3);

		int foundEntity = testReviewRepository.findCountWithAccountId(item.getId());
		int notFoundEntity = testReviewRepository.findCountWithAccountId(item.getId() + 2);


		assertEquals(4, foundEntity);
		assertEquals(0, notFoundEntity);
	}

	@Test
	void reviewFindCountWithItemIdWorks() {
		Account account = testaccountRepository.findById(1).orElseThrow();
		Item item = testItemRepository.findById(1).orElseThrow();
		Review review = new Review(
				new Date(0),
				"review body for test",
				"hello item",
				0,
				0,
				account,
				4,
				item
		);

		Review review2 = new Review(
				new Date(1),
				"review 2 body for test",
				"test item 2",
				0,
				0,
				account,
				2,
				item
		);

		testReviewRepository.save(review);
		testReviewRepository.save(review2);

		Pageable pageRequest = PageRequest.of(0, 4);

		int foundEntity = testReviewRepository.findReviewCountForItem(item.getId());
		int foundNoneIdentity = testReviewRepository.findReviewCountForItem(item.getId() + 2);
		assertEquals(3, foundEntity);
		assertEquals(0, foundNoneIdentity);
	}

	@Test
	void reviewFindPosCountWithItemIdWorks() {
		Account account = testaccountRepository.findById(1).orElseThrow();
		Item item = testItemRepository.findById(1).orElseThrow();
		Review review = new Review(
				new Date(0),
				"review body for test",
				"hello item",
				0,
				0,
				account,
				4,
				item
		);

		Review review2 = new Review(
				new Date(1),
				"review 2 body for test",
				"test item 2",
				0,
				0,
				account,
				2,
				item
		);

		testReviewRepository.save(review);
		testReviewRepository.save(review2);

		Pageable pageRequest = PageRequest.of(0, 4);

		int foundEntity = testReviewRepository.findPosReviewCountForItem(item.getId());
		int foundNoneIdentity = testReviewRepository.findPosReviewCountForItem(item.getId() + 2);
		assertEquals(2, foundEntity);
		assertEquals(0, foundNoneIdentity);
	}

	@Test
	void reviewFindNegCountWithItemIdWorks() {
		Account account = testaccountRepository.findById(1).orElseThrow();
		Item item = testItemRepository.findById(1).orElseThrow();
		Review review = new Review(
				new Date(0),
				"review body for test",
				"hello item",
				0,
				0,
				account,
				4,
				item
		);

		Review review2 = new Review(
				new Date(1),
				"review 2 body for test",
				"test item 2",
				0,
				0,
				account,
				2,
				item
		);

		testReviewRepository.save(review);
		testReviewRepository.save(review2);

		Pageable pageRequest = PageRequest.of(0, 4);

		int foundEntity = testReviewRepository.findNegReviewCountForItem(item.getId());
		int foundNoneIdentity = testReviewRepository.findNegReviewCountForItem(item.getId() + 2);
		assertEquals(1, foundEntity);
		assertEquals(0, foundNoneIdentity);
	}
}
