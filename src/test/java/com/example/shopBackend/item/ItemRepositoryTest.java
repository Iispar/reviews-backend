package com.example.shopBackend.item;


import com.example.shopBackend.ShopBackendApplication;
import com.example.shopBackend.account.Account;
import com.example.shopBackend.account.AccountRepository;
import com.example.shopBackend.category.Category;
import com.example.shopBackend.category.CategoryRepository;
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
	private AccountRepository testaccountRepository;

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
	void ItemFindAllByAccountIdWorks() {
		Account account = testaccountRepository.findById(1).orElse(null);
		Category category = testCategoryRepository.findById(1).orElse(null);
		Words words = testWordsRepository.findById(2).orElse(null);
		
		Item item = new Item(
				1,
				"new item title",
				account,
				4,
				category,
				words,
				"test desc"
				);
		testItemRepository.save(item);
		
		Pageable pageRequest = PageRequest.of(0, 4);

		assert account != null;
		List<Item> foundEntity = testItemRepository.findAllAccountId(account.getId(), pageRequest);
        List<Item> foundNoneEntity = testItemRepository.findAllAccountId(account.getId() + 1, pageRequest);
		assertEquals(1, foundEntity.size());
		assertEquals(0, foundNoneEntity.size());
	}

	@Test
	void ItemFindItemCountForAccountIdWorks() {
		Account account = testaccountRepository.findById(1).orElse(null);
		Category category = testCategoryRepository.findById(1).orElse(null);
		Words words = testWordsRepository.findById(2).orElse(null);

		Item item = new Item(
				1,
				"new item title",
				account,
				4,
				category,
				words,
				"test desc"
		);
		testItemRepository.save(item);

		assert account != null;
		int foundEntity = testItemRepository.findItemCountForAccountId(account.getId());
		int foundNoneEntity = testItemRepository.findItemCountForAccountId(account.getId() + 1);
		assertEquals(1, foundEntity);
		assertEquals(0, foundNoneEntity);
	}

	@Test
	void ItemFindAvgRatingForAccountWorks() {
		Account account = testaccountRepository.findById(1).orElse(null);
		Category category = testCategoryRepository.findById(1).orElse(null);
		Words words = testWordsRepository.findById(2).orElse(null);
		Words words2 = testWordsRepository.findById(3).orElse(null);

		Item item = new Item(
				"new item title",
				account,
				4,
				category,
				words,
				"test desc"
		);
		Item item2 = new Item(
				"new item title",
				account,
				2,
				category,
				words2,
				"test desc"
		);
		testItemRepository.save(item);
		testItemRepository.save(item2);

		assert account != null;
		float foundEntity = testItemRepository.findItemAvgRatingForAccountId(account.getId()).orElse(-2F);
		Float notFoundEntity = testItemRepository.findItemAvgRatingForAccountId(account.getId() + 1).orElse(-2F);
		assertEquals(3.3333333F, foundEntity);
		assertEquals(-2F, notFoundEntity);
	}
}