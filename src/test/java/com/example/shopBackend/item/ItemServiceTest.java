package com.example.shopBackend.item;

import com.example.shopBackend.ShopBackendApplication;
import com.example.shopBackend.category.Category;
import com.example.shopBackend.category.CategoryRepository;
import com.example.shopBackend.role.Role;
import com.example.shopBackend.user.User;
import com.example.shopBackend.user.UserRepository;
import com.example.shopBackend.words.WordsRepository;
import exception.BadRequestException;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ActiveProfiles("test")
@DataJpaTest()
@ContextConfiguration(classes = ShopBackendApplication.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ExtendWith(MockitoExtension.class)
class ItemServiceTest {
    @Mock
    private ItemRepository itemRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private WordsRepository wordsRepository;

    @InjectMocks
    private ItemService testItemService;

    @Test
    void getItemsForUserWorks() {
        given(userRepository.findById(any())).willReturn(Optional.of(new User()));

        Pageable pageRequest = PageRequest.of(0, 6);
        testItemService.getItemsForUser(1, 0);

        verify(itemRepository).findAllUserId(1, pageRequest);
    }

    @Test
    void getItemsForUserThrowsErrorWithNoMatchingUser() {
        given(userRepository.findById(any())).willReturn(Optional.empty());

        assertThatThrownBy(() -> testItemService.getItemsForUser(1, 0))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("No users exists with id 1");

        Pageable pageRequest = PageRequest.of(0, 6);

        verify(itemRepository, never()).findAllUserId(1, pageRequest);
    }

    @Test
    void getItemsForUserThrowsErrorWithNegativePage() {
        given(userRepository.findById(any())).willReturn(Optional.of(new User()));

        assertThatThrownBy(() -> testItemService.getItemsForUser(1, -1))
                .isInstanceOf(java.lang.IllegalArgumentException.class)
                .hasMessageContaining("Page index must not be less than zero");
    }

    @Test
    void deleteItem() {
        given(itemRepository.findById(any())).willReturn(Optional.of(new Item()));

        testItemService.deleteItem(0);

        verify(itemRepository).deleteById(0);
    }

    @Test
    void deleteItemThrowsErrorWithNoMatchingItem() {
        given(itemRepository.findById(any())).willReturn(Optional.empty());

        assertThatThrownBy(() ->  testItemService.deleteItem(0))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("No items exists with id 0");

        verify(itemRepository, never()).deleteById(0);
    }

    @Test
    void saveReviewWorks() {
        given(userRepository.findById(any())).willReturn(Optional.of(new User()));
        given(categoryRepository.findById(any())).willReturn(Optional.of(new Category()));

        List<Item> list = new ArrayList<Item>();
        User user = new User(1, "test name", "test username", "testPass", "testEmail", new Role());
        Category category = new Category("test category");
        Item item1 = new Item(
                "test title",
                user,
                0,
                category,
                null,
                "test desc"

        );
        Item item2 = new Item(
                "test title 2",
                user,
                0,
                category,
                null,
                "test desc"

        );

        list.add(item1);
        list.add(item2);

        testItemService.saveAllItems(list);

        verify(itemRepository).saveAll(list);
    }

    @Test
    void saveReviewThrowsWithNegativeRating() {
        List<Item> list = new ArrayList<Item>();
        User user = new User(1, "test name", "test username", "testPass", "testEmail", new Role());
        Category category = new Category("test category");
        Item item1 = new Item(
                "test title",
                user,
                -2,
                category,
                null,
                "test desc"

        );
        Item item2 = new Item(
                "test title 2",
                user,
                0,
                category,
                null,
                "test desc"

        );

        list.add(item1);
        list.add(item2);

        assertThatThrownBy(() ->  testItemService.saveAllItems(list))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("item with invalid rating. Has to be between 0-5.");

        verify(itemRepository, never()).saveAll(list);
    }

    @Test
    void saveReviewThrowsWithTooLargeRating() {
        List<Item> list = new ArrayList<Item>();
        User user = new User(1, "test name", "test username", "testPass", "testEmail", new Role());
        Category category = new Category("test category");
        Item item1 = new Item(
                "test title",
                user,
                10,
                category,
                null,
                "test desc"

        );
        Item item2 = new Item(
                "test title 2",
                user,
                0,
                category,
                null,
                "test desc"

        );

        list.add(item1);
        list.add(item2);

        assertThatThrownBy(() ->  testItemService.saveAllItems(list))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("item with invalid rating. Has to be between 0-5.");

        verify(itemRepository, never()).saveAll(list);
    }

    @Test
    void saveReviewThrowsWithTooLongTitle() {
        List<Item> list = new ArrayList<Item>();
        User user = new User(1, "test name", "test username", "testPass", "testEmail", new Role());
        Category category = new Category("test category");
        Item item1 = new Item(
                "this title will be way too long to be allowed as the value. this title will be way too long to be allowed as the value.",
                user,
                0,
                category,
                null,
                "test desc"

        );
        Item item2 = new Item(
                "test title 2",
                user,
                0,
                category,
                null,
                "test desc"

        );

        list.add(item1);
        list.add(item2);

        assertThatThrownBy(() ->  testItemService.saveAllItems(list))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("item with invalid title. Length has to be between 3 and 50 characters");

        verify(itemRepository, never()).saveAll(list);
    }

    @Test
    void saveReviewThrowsWithTooShortTitle() {
        List<Item> list = new ArrayList<Item>();
        User user = new User(1, "test name", "test username", "testPass", "testEmail", new Role());
        Category category = new Category("test category");
        Item item1 = new Item(
                "th",
                user,
                0,
                category,
                null,
                "test desc"

        );
        Item item2 = new Item(
                "test title 2",
                user,
                0,
                category,
                null,
                "test desc"

        );

        list.add(item1);
        list.add(item2);

        assertThatThrownBy(() ->  testItemService.saveAllItems(list))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("item with invalid title. Length has to be between 3 and 50 characters");

        verify(itemRepository, never()).saveAll(list);
    }

    @Test
    void saveReviewThrowsWithBadDesc() {
        List<Item> list = new ArrayList<Item>();
        User user = new User(1, "test name", "test username", "testPass", "testEmail", new Role());
        Category category = new Category("test category");
        Item item1 = new Item(
                "test title",
                user,
                0,
                category,
                null,
                "too long of a desc. too long of a desc. too long of a desc. too long of a desc. too long of a desc. too long of a desc. too long of a desc. too long of a desc. too long of a desc. too long of a desc. too long of a desc. too long of a desc. too long of a desc. too long of a desc. too long of a desc. too long of a desc. too long of a desc. too long of a desc. too long of a desc. too long of a desc. too long of a desc. too long of a desc. too long of a desc. too long of a desc. too long of a desc. too long of a desc. too long of a desc. too long of a desc. too long of a desc. too long of a desc. vtoo long of a desc. too long of a desc. too long of a desc. too long of a desc. too long of a desc. too long of a desc. too long of a desc. too long of a desc. too long of a desc. too long of a desc. too long of a desc. too long of a desc. too long of a desc. too long of a desc. too long of a desc. too long of a desc. too long of a desc. too long of a desc. too long of a desc. too long of a desc"

        );
        Item item2 = new Item(
                "test title 2",
                user,
                0,
                category,
                null,
                "test desc"

        );

        list.add(item1);
        list.add(item2);

        assertThatThrownBy(() ->  testItemService.saveAllItems(list))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("item with invalid desc. Length has to be under 300 characters");

        verify(itemRepository, never()).saveAll(list);
    }

    @Test
    void saveReviewThrowsWithBadCategoryId() {
        given(categoryRepository.findById(any())).willReturn(Optional.empty());
        List<Item> list = new ArrayList<Item>();
        User user = new User(1, "test name", "test username", "testPass", "testEmail", new Role());
        Category category = new Category("test category");
        Item item1 = new Item(
                "test title",
                user,
                0,
                category,
                null,
                "test desc"
        );
        Item item2 = new Item(
                "test title 2",
                user,
                0,
                category,
                null,
                "test desc"

        );

        list.add(item1);
        list.add(item2);

        assertThatThrownBy(() ->  testItemService.saveAllItems(list))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("category with id: " + category.getId() + " does not exist");

        verify(itemRepository, never()).saveAll(list);
    }

    @Test
    void saveReviewThrowsWithBadUserId() {
        given(categoryRepository.findById(any())).willReturn(Optional.of(new Category()));
        given(userRepository.findById(any())).willReturn(Optional.empty());
        List<Item> list = new ArrayList<Item>();
        User user = new User(1, "test name", "test username", "testPass", "testEmail", new Role());
        Category category = new Category("test category");
        Item item1 = new Item(
                "test title",
                user,
                0,
                category,
                null,
                "test desc"
        );
        Item item2 = new Item(
                "test title 2",
                user,
                0,
                category,
                null,
                "test desc"

        );

        list.add(item1);
        list.add(item2);

        assertThatThrownBy(() ->  testItemService.saveAllItems(list))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("user with id: " + user.getId() + " does not exist");

        verify(itemRepository, never()).saveAll(list);
    }

    @Test
    void updateItemWorks() {
        given(categoryRepository.findById(any())).willReturn(Optional.of(new Category()));

        User user = new User(1, "test name", "test username", "testPass", "testEmail", new Role());
        Category category = new Category("test category");

        Item item = new Item(
                "test title",
                user,
                0,
                category,
                null,
                "test desc"
        );

        given(itemRepository.findById(any())).willReturn(Optional.of(item));
        testItemService.updateItem(item.getId(), item);

        verify(itemRepository).save(item);
    }

    @Test
    void updateItemThrowsErrorWithNoFoundItem() {
        User user = new User(1, "test name", "test username", "testPass", "testEmail", new Role());
        Category category = new Category("test category");

        Item item = new Item(
                "test title",
                user,
                0,
                category,
                null,
                "test desc"
        );

        given(itemRepository.findById(any())).willReturn(Optional.empty());

        assertThatThrownBy(() ->  testItemService.updateItem(item.getId(), item))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("No items exists with id: " + item.getId());

        verify(itemRepository, never()).save(item);
    }

    @Test
    void updateItemThrowsErrorWithTooLongTitle() {
        given(categoryRepository.findById(any())).willReturn(Optional.of(new Category()));
        User user = new User(1, "test name", "test username", "testPass", "testEmail", new Role());
        Category category = new Category("test category");

        Item item = new Item(
                "this title will be too long. this title will be too long. this title will be too long. this title will be too long.",
                user,
                0,
                category,
                null,
                "test desc"
        );

        given(itemRepository.findById(any())).willReturn(Optional.of(item));

        assertThatThrownBy(() ->  testItemService.updateItem(item.getId(), item))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("item with invalid title. Length has to be between 3 and 50 characters");

        verify(itemRepository, never()).save(item);
    }

    @Test
    void updateItemThrowsErrorWithTooShortTitle() {
        given(categoryRepository.findById(any())).willReturn(Optional.of(new Category()));
        User user = new User(1, "test name", "test username", "testPass", "testEmail", new Role());
        Category category = new Category("test category");

        Item item = new Item(
                "il",
                user,
                0,
                category,
                null,
                "test desc"
        );

        given(itemRepository.findById(any())).willReturn(Optional.of(item));

        assertThatThrownBy(() ->  testItemService.updateItem(item.getId(), item))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("item with invalid title. Length has to be between 3 and 50 characters");

        verify(itemRepository, never()).save(item);
    }

    @Test
    void updateItemThrowsErrorWithBadDesc() {
        given(categoryRepository.findById(any())).willReturn(Optional.of(new Category()));
        User user = new User(1, "test name", "test username", "testPass", "testEmail", new Role());
        Category category = new Category("test category");

        Item item = new Item(
                "test title",
                user,
                0,
                category,
                null,
                "test desc that is too long. test desc that is too long.  test desc that is too long. test desc that is too long. test desc that is too long. test desc that is too long. test desc that is too long. test desc that is too long. vtest desc that is too long. test desc that is too long. test desc that is too long. test desc that is too long. test desc that is too long. test desc that is too long. test desc that is too long. "
        );

        given(itemRepository.findById(any())).willReturn(Optional.of(item));

        assertThatThrownBy(() ->  testItemService.updateItem(item.getId(), item))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("item with invalid desc. Length has to be under 300 characters");

        verify(itemRepository, never()).save(item);
    }

    @Test
    void updateItemThrowsErrorWithNoCategory() {
        User user = new User(1, "test name", "test username", "testPass", "testEmail", new Role());
        Category category = new Category("test category");

        Item item = new Item(
                "test title",
                user,
                0,
                category,
                null,
                "test desc"
        );

        given(itemRepository.findById(any())).willReturn(Optional.of(item));

        assertThatThrownBy(() ->  testItemService.updateItem(item.getId(), item))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("No categories exists with id: " + item.getCategory().getId());

        verify(itemRepository, never()).save(item);
    }
}