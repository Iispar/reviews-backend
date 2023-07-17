package item;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;

import com.example.shopBackend.ShopBackendApplication;
import com.example.shopBackend.category.Category;
import com.example.shopBackend.category.CategoryRepository;
import com.example.shopBackend.item.Item;
import com.example.shopBackend.item.ItemRepository;
import com.example.shopBackend.user.User;
import com.example.shopBackend.user.UserRepository;
import com.example.shopBackend.words.Words;
import com.example.shopBackend.words.WordsRepository;

@ActiveProfiles("test")
@SpringBootTest(classes = ShopBackendApplication.class)
// @DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
// @ExtendWith(SpringExtension.class)
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
	void findAllByUserIfWorks() {
		User user = testUserRepository.findById(1);
		Category category = testCategoryRepository.findById(1);
		Words words = testWordsRepository.findById(2);
		Item item = new Item(
				"new item title",
				user,
				"4",
				category,
				words
				);
		testItemRepository.save(item);
		
		Pageable pageRequest = PageRequest.of(0, 4);
		
        List<Item> foundEntity = testItemRepository.findAllUserId(user.getId(), pageRequest);
        List<Item> foundNoneEntity = testItemRepository.findAllUserId(user.getId() + 1, pageRequest);
        assertTrue(foundEntity.size() == 2);
        assertTrue(foundNoneEntity.size() == 0);
	}
}