package com.example.shopBackend.item;

import com.example.shopBackend.ShopBackendApplication;
import com.example.shopBackend.category.Category;
import com.example.shopBackend.category.CategoryRepository;
import com.example.shopBackend.user.User;
import com.example.shopBackend.user.UserRepository;
import com.example.shopBackend.words.Words;
import com.example.shopBackend.words.WordsRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ActiveProfiles("test")
@DataJpaTest()
@ContextConfiguration(classes = ShopBackendApplication.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ItemRepositoryTest {
	
	@Autowired
	private UserRepository testUserRepository;

	@Autowired
	private ItemRepository testItemRepository;
	
	@Autowired
	private CategoryRepository testCategoryRepository;
	
	@Autowired
	private WordsRepository testWordsRepository;
	
	@AfterEach
	void deleteAll() {
		testItemRepository.deleteAll();
	}
	
	@Test
	void ItemFindAllByUserIdWorks() {
		User user = testUserRepository.findById(1).orElse(null);
		Category category = testCategoryRepository.findById(1).orElse(null);
		Words words = testWordsRepository.findById(2).orElse(null);
		
		Item item = new Item(
				1,
				"new item title",
				user,
				4,
				category,
				words,
				"test desc"
				);
		testItemRepository.save(item);
		
		Pageable pageRequest = PageRequest.of(0, 4);

		assert user != null;
		List<Item> foundEntity = testItemRepository.findAllUserId(user.getId(), pageRequest);
        List<Item> foundNoneEntity = testItemRepository.findAllUserId(user.getId() + 1, pageRequest);
		assertEquals(1, foundEntity.size());
		assertEquals(0, foundNoneEntity.size());
	}

	@Test
	void ItemFindItemCountForUserIdWorks() {
		User user = testUserRepository.findById(1).orElse(null);
		Category category = testCategoryRepository.findById(1).orElse(null);
		Words words = testWordsRepository.findById(2).orElse(null);

		Item item = new Item(
				1,
				"new item title",
				user,
				4,
				category,
				words,
				"test desc"
		);
		testItemRepository.save(item);

		assert user != null;
		int foundEntity = testItemRepository.findItemCountForUserId(user.getId());
		int foundNoneEntity = testItemRepository.findItemCountForUserId(user.getId() + 1);
		assertEquals(1, foundEntity);
		assertEquals(0, foundNoneEntity);
	}

	@Test
	void ItemFindAvgRatingForUserWorks() {
		User user = testUserRepository.findById(1).orElse(null);
		Category category = testCategoryRepository.findById(1).orElse(null);
		Words words = testWordsRepository.findById(2).orElse(null);
		Words words2 = testWordsRepository.findById(3).orElse(null);

		Item item = new Item(
				"new item title",
				user,
				4,
				category,
				words,
				"test desc"
		);
		Item item2 = new Item(
				"new item title",
				user,
				2,
				category,
				words2,
				"test desc"
		);
		testItemRepository.save(item);
		testItemRepository.save(item2);

		assert user != null;
		float foundEntity = testItemRepository.findItemAvgRatingForUserId(user.getId()).orElse(-2F);
		Float notFoundEntity = testItemRepository.findItemAvgRatingForUserId(user.getId() + 1).orElse(-2F);
		assertEquals(3.3333333F, foundEntity);
		assertEquals(-2F, notFoundEntity);
	}
}