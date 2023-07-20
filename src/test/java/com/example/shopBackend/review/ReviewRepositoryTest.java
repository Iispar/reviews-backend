package com.example.shopBackend.review;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.Date;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import com.example.shopBackend.ShopBackendApplication;
import com.example.shopBackend.item.Item;
import com.example.shopBackend.item.ItemRepository;
import com.example.shopBackend.user.User;
import com.example.shopBackend.user.UserRepository;

@ActiveProfiles("test")
@DataJpaTest()
@ContextConfiguration(classes = ShopBackendApplication.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ReviewRepositoryTest {
	
	@Autowired
	private UserRepository testUserRepository;
	
	@Autowired
	private ReviewRepository testReviewRepository;

	@Autowired
	private ItemRepository testItemRepository;
	
	@AfterEach
	void deleteAll() {
		testReviewRepository.deleteAll();
	}
	
	@Test
	void reviewFindAllUserIdWorks() throws Exception {
		User user = testUserRepository.findById(1).orElseThrow();
		Item item = testItemRepository.findById(1).orElseThrow();
		Review review = new Review(
				new Date(0),
				"review body for test",
				"review title for test",
				0,
				0,
				user,
				4,
				item
				);

		Review review2 = new Review(
				new Date(1),
				"review 2 body for test",
				"review 2 title for test",
				0,
				0,
				user,
				2,
				item
				);
		
		testReviewRepository.save(review);
		testReviewRepository.save(review2);
		
		Pageable pageRequest = PageRequest.of(0, 4);
		
        List<Review> foundEntity = testReviewRepository.findAllUserId(user.getId(), pageRequest);
        List<Review> foundNoneEntity = testReviewRepository.findAllUserId(user.getId() + 1, pageRequest);
        assertTrue(foundEntity.size() == 2);
        assertTrue(foundNoneEntity.size() == 0);
	}
	
	@Test
	void reviewfindAllByItemIdWorks() {
		User user = testUserRepository.findById(1).orElseThrow();
		Item item = testItemRepository.findById(1).orElseThrow();
		Review review = new Review(
				new Date(0),
				"review body for test",
				"hello item",
				0,
				0,
				user,
				4,
				item
				);

		Review review2 = new Review(
				new Date(1),
				"review 2 body for test",
				"test item 2",
				0,
				0,
				user,
				2,
				item
				);
		
		testReviewRepository.save(review);
		testReviewRepository.save(review2);
		
		Pageable pageRequest = PageRequest.of(0, 4);
		
        List<Review> foundEntity = testReviewRepository.findAllItemId(item.getId(), pageRequest);
        List<Review> foundNoneIdentity = testReviewRepository.findAllItemId(item.getId() + 1, pageRequest);
        assertTrue(foundEntity.size() == 2);
        assertTrue(foundNoneIdentity.size() == 0);
	}
	
	@Test
	void reviewfindAllByTitleWorks() {
		User user = testUserRepository.findById(1).orElseThrow();
		Item item = testItemRepository.findById(1).orElseThrow();
		Review review = new Review(
				new Date(0),
				"review body for test",
				"hello item",
				0,
				0,
				user,
				4,
				item
				);

		Review review2 = new Review(
				new Date(1),
				"review 2 body for test",
				"test item 2",
				0,
				0,
				user,
				2,
				item
				);
		
		testReviewRepository.save(review);
		testReviewRepository.save(review2);
		
		Pageable pageRequest = PageRequest.of(0, 4);
		
        List<Review> foundEntity = testReviewRepository.findAllByTitleForItem("%item%", item.getId(), pageRequest);
        List<Review> foundOneIdentity = testReviewRepository.findAllByTitleForItem("%hello%", item.getId(), pageRequest);
        List<Review> foundNoneIdentity = testReviewRepository.findAllByTitleForItem("%original%", item.getId(), pageRequest);
        assertTrue(foundEntity.size() == 2);
        assertTrue(foundOneIdentity.size() == 1);
        assertTrue(foundNoneIdentity.size() == 0);
	}
	
	@Test
	void reviewfindChartMonthByUserIdWorks() {
		User user = testUserRepository.findById(1).orElseThrow();
		Item item = testItemRepository.findById(1).orElseThrow();
		Review review = new Review(
				Date.valueOf("2023-01-01"),
				"review body for test",
				"hello item",
				0,
				0,
				user,
				4,
				item
				);

		Review review2 = new Review(
				Date.valueOf("2023-02-01"),
				"review 2 body for test",
				"test item 2",
				0,
				0,
				user,
				2,
				item
				);
		
		Review review3 = new Review(
				Date.valueOf("2022-01-05"),
				"review 3 body for test",
				"test item 3",
				0,
				0,
				user,
				3,
				item
				);
		
		testReviewRepository.save(review);
		testReviewRepository.save(review2);
		testReviewRepository.save(review3);
		
        List<Chart> foundEntity = testReviewRepository.findChartForUserByMonth(user.getId());
        List<Chart> foundNoneEntity = testReviewRepository.findChartForUserByMonth(user.getId() + 1);
        
        assertTrue(foundEntity.size() == 2);
        assertTrue(foundNoneEntity.size() == 0);
	}
	
	@Test
	void reviewfindChartWeekByUserIdWorks() {
		User user = testUserRepository.findById(1).orElseThrow();
		Item item = testItemRepository.findById(1).orElseThrow();
		Review review = new Review(
				Date.valueOf("2022-01-04"),
				"review body for test",
				"hello item",
				0,
				0,
				user,
				4,
				item
				);

		Review review2 = new Review(
				Date.valueOf("2022-02-01"),
				"review 2 body for test",
				"test item 2",
				0,
				0,
				user,
				2,
				item
				);
		
		Review review3 = new Review(
				Date.valueOf("2022-01-05"),
				"review 3 body for test",
				"test item 3",
				0,
				0,
				user,
				3,
				item
				);
		
		testReviewRepository.save(review);
		testReviewRepository.save(review2);
		testReviewRepository.save(review3);
		
        List<Chart> foundEntity = testReviewRepository.findChartForUserByWeek(user.getId());
        List<Chart> foundNoneEntity = testReviewRepository.findChartForUserByWeek(user.getId() + 1);
        assertTrue(foundEntity.size() == 2);
        assertTrue(foundNoneEntity.size() == 0);
	}
	
	@Test
	void reviewfindChartMonthByItemIdWorks() {
		User user = testUserRepository.findById(1).orElseThrow();
		Item item = testItemRepository.findById(1).orElseThrow();
		Review review = new Review(
				Date.valueOf("2023-01-01"),
				"review body for test",
				"hello item",
				0,
				0,
				user,
				4,
				item
				);

		Review review2 = new Review(
				Date.valueOf("2023-02-01"),
				"review 2 body for test",
				"test item 2",
				0,
				0,
				user,
				2,
				item
				);
		
		Review review3 = new Review(
				Date.valueOf("2022-01-05"),
				"review 3 body for test",
				"test item 3",
				0,
				0,
				user,
				3,
				item
				);
		
		testReviewRepository.save(review);
		testReviewRepository.save(review2);
		testReviewRepository.save(review3);
		
        List<Chart> foundEntity = testReviewRepository.findChartForItemByMonth(item.getId());
        List<Chart> foundNoneEntity = testReviewRepository.findChartForItemByMonth(item.getId() + 1);
        
        assertTrue(foundEntity.size() == 2);
        assertTrue(foundNoneEntity.size() == 0);
	}
	
	@Test
	void reviewfindChartWeekByItemIdWorks() {
		User user = testUserRepository.findById(1).orElseThrow();
		Item item = testItemRepository.findById(1).orElseThrow();
		Review review = new Review(
				Date.valueOf("2022-01-04"),
				"review body for test",
				"hello item",
				0,
				0,
				user,
				4,
				item
				);

		Review review2 = new Review(
				Date.valueOf("2022-02-01"),
				"review 2 body for test",
				"test item 2",
				0,
				0,
				user,
				2,
				item
				);
		
		Review review3 = new Review(
				Date.valueOf("2022-01-05"),
				"review 3 body for test",
				"test item 3",
				0,
				0,
				user,
				3,
				item
				);
		
		testReviewRepository.save(review);
		testReviewRepository.save(review2);
		testReviewRepository.save(review3);
		
        List<Chart> foundEntity = testReviewRepository.findChartForItemByWeek(item.getId());
        List<Chart> foundNoneEntity = testReviewRepository.findChartForItemByWeek(item.getId() + 1);
        assertTrue(foundEntity.size() == 2);
        assertTrue(foundNoneEntity.size() == 0);
	}
}
