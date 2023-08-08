package com.example.shopBackend.item;

import com.example.shopBackend.ShopBackendApplication;
import com.example.shopBackend.account.Account;
import com.example.shopBackend.account.AccountRepository;
import com.example.shopBackend.category.Category;
import com.example.shopBackend.category.CategoryRepository;
import com.example.shopBackend.review.Review;
import com.example.shopBackend.review.ReviewRepository;
import com.example.shopBackend.review.ReviewService;
import com.example.shopBackend.role.Role;
import com.example.shopBackend.words.Words;
import com.example.shopBackend.words.WordsRepository;
import exception.BadRequestException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
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
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@SuppressWarnings({"unused", "unchecked"})
@ActiveProfiles("test")
@DataJpaTest()
@ContextConfiguration(classes = ShopBackendApplication.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ExtendWith(MockitoExtension.class)
class ItemServiceTest {
    @Mock
    private ItemRepository itemRepository;

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private WordsRepository wordsRepository;

    @Mock
    private ReviewService reviewService;

    @InjectMocks
    private ItemService testItemService;

    @Test
    void getItemsForAccountWorksWithAsc() {
        given(accountRepository.findById(any())).willReturn(Optional.of(new Account()));

        Pageable pageRequest = PageRequest.of(0, 6, Sort.by("item_rating").ascending());
        testItemService.getItemsForAccount(1, 0, "item_rating", "asc");

        verify(itemRepository).findAllAccountId(1, pageRequest);
    }

    @Test
    void getItemsForAccountWorksWithDesc() {
        given(accountRepository.findById(any())).willReturn(Optional.of(new Account()));

        Pageable pageRequest = PageRequest.of(0, 6, Sort.by("item_rating").descending());
        testItemService.getItemsForAccount(1, 0, "item_rating", "desc");

        verify(itemRepository).findAllAccountId(1, pageRequest);
    }

    @Test
    void getItemsForAccountThrowsErrorWithNoMatchingAccount() {
        given(accountRepository.findById(any())).willReturn(Optional.empty());

        assertThatThrownBy(() -> testItemService.getItemsForAccount(1, 0, "none", "none"))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("No Accounts exists with id 1");

        Pageable pageRequest = PageRequest.of(0, 6);

        verify(itemRepository, never()).findAllAccountId(1, pageRequest);
    }

    @Test
    void getItemsForAccountThrowsErrorWithNegativePage() {
        given(accountRepository.findById(any())).willReturn(Optional.of(new Account()));

        assertThatThrownBy(() -> testItemService.getItemsForAccount(1, -1, "none", "none"))
                .isInstanceOf(java.lang.IllegalArgumentException.class)
                .hasMessageContaining("Page index must not be less than zero");
    }

    @Test
    void GetItemsForAccountThrowsErrorWithBadSortDir() {

        assertThatThrownBy(() -> testItemService.getItemsForAccount(1, 0, "item_rating", "ascending"))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("sort direction ascending is not supported. Has to be either asc or desc.");

        Pageable pageRequest = PageRequest.of(0, 4);

        verify(reviewRepository, never()).findAllAccountId(1, pageRequest);
    }

    @Test
    void GetItemsForAccountThrowsErrorWithBadSort() {

        assertThatThrownBy(() -> testItemService.getItemsForAccount(1, 0, "item_name", "asc"))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("sort item_name is not a valid value for a sort in the entity.");

        Pageable pageRequest = PageRequest.of(0, 4);

        verify(reviewRepository, never()).findAllAccountId(1, pageRequest);
    }

    @Test
    void deleteItem() {
        given(itemRepository.findById(any())).willReturn(Optional.of(new Item()), Optional.empty());

        testItemService.deleteItem(0);

        verify(itemRepository).deleteById(0);
    }

    @Test
    void deleteItemThrowsWithFailedReviewDeletion() {
        Review review = new Review(
                new Date(1),
                "title1",
                "body1",
                1,
                0,
                new Account(),
                2,
                new Item(1, "title", new Account(), 4F, new Category(), new Words(), "desc")
        );

        given(itemRepository.findById(any())).willReturn(Optional.of(new Item()), Optional.empty());
        given(reviewRepository.findAll()).willReturn(List.of(review));
        given(reviewService.deleteReview(anyInt())).willThrow(new RuntimeException());

        assertThatThrownBy(() ->  testItemService.deleteItem(1))
                .isInstanceOf(java.lang.RuntimeException.class)
                .hasMessageContaining("error: null. While deleting review with id: 0");

        verify(itemRepository, never()).deleteById(0);
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
    void deleteItemThrowsErrorWithFailedDelete() {
        given(itemRepository.findById(any())).willReturn(Optional.of(new Item()));

        assertThatThrownBy(() ->  testItemService.deleteItem(0))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("Failed to delete item");

        verify(itemRepository).deleteById(0);
    }

    @Test
    void saveItemWorks() {
        given(accountRepository.findById(any())).willReturn(Optional.of(new Account()));
        given(categoryRepository.findById(any())).willReturn(Optional.of(new Category()));

        List<Item> list = new ArrayList<>();
        Account account = new Account(1, "test name", "test username", "testPass", "testEmail", new Role());
        Category category = new Category("test category");
        Item item1 = new Item(
                1,
                "test title",
                account,
                0,
                category,
                null,
                "test desc"

        );
        Item item2 = new Item(
                2,
                "test title 2",
                account,
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
    void saveItemThrowsWithNegativeRating() {
        List<Item> list = new ArrayList<>();
        Account account = new Account(1, "test name", "test username", "testPass", "testEmail", new Role());
        Category category = new Category("test category");
        Item item1 = new Item(
                1,
                "test title",
                account,
                -2,
                category,
                null,
                "test desc"

        );
        Item item2 = new Item(
                2,
                "test title 2",
                account,
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
    void saveItemThrowsWithTooLargeRating() {
        List<Item> list = new ArrayList<>();
        Account account = new Account(1, "test name", "test username", "testPass", "testEmail", new Role());
        Category category = new Category("test category");
        Item item1 = new Item(
                1,
                "test title",
                account,
                10,
                category,
                null,
                "test desc"

        );
        Item item2 = new Item(
                2,
                "test title 2",
                account,
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
    void saveItemThrowsWithTooLongTitle() {
        List<Item> list = new ArrayList<>();
        Account account = new Account(1, "test name", "test username", "testPass", "testEmail", new Role());
        Category category = new Category("test category");
        Item item1 = new Item(
                1,
                "this title will be way too long to be allowed as the value. this title will be way too long to be allowed as the value.",
                account,
                0,
                category,
                null,
                "test desc"

        );
        Item item2 = new Item(
                2,
                "test title 2",
                account,
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
    void saveItemThrowsWithTooShortTitle() {
        List<Item> list = new ArrayList<>();
        Account account = new Account(1, "test name", "test username", "testPass", "testEmail", new Role());
        Category category = new Category("test category");
        Item item1 = new Item(
                1,
                "th",
                account,
                0,
                category,
                null,
                "test desc"

        );
        Item item2 = new Item(
                2,
                "test title 2",
                account,
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
    void saveItemThrowsWithBadDesc() {
        List<Item> list = new ArrayList<>();
        Account account = new Account(1, "test name", "test username", "testPass", "testEmail", new Role());
        Category category = new Category("test category");
        Item item1 = new Item(
                1,
                "test title",
                account,
                0,
                category,
                null,
                "too long of a desc. too long of a desc. too long of a desc. too long of a desc. too long of a desc. too long of a desc. too long of a desc. too long of a desc. too long of a desc. too long of a desc. too long of a desc. too long of a desc. too long of a desc. too long of a desc. too long of a desc. too long of a desc. too long of a desc. too long of a desc. too long of a desc. too long of a desc. too long of a desc. too long of a desc. too long of a desc. too long of a desc. too long of a desc. too long of a desc. too long of a desc. too long of a desc. too long of a desc. too long of a desc. too long of a desc. too long of a desc. too long of a desc. too long of a desc. too long of a desc. too long of a desc. too long of a desc. too long of a desc. too long of a desc. too long of a desc. too long of a desc. too long of a desc. too long of a desc. too long of a desc. too long of a desc. too long of a desc. too long of a desc. too long of a desc. too long of a desc. too long of a desc"

        );
        Item item2 = new Item(
                2,
                "test title 2",
                account,
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
    void saveItemThrowsWithBadCategoryId() {
        given(categoryRepository.findById(any())).willReturn(Optional.empty());
        List<Item> list = new ArrayList<>();
        Account account = new Account(1, "test name", "test username", "testPass", "testEmail", new Role());
        Category category = new Category("test category");
        Item item1 = new Item(
                1,
                "test title",
                account,
                0,
                category,
                null,
                "test desc"
        );
        Item item2 = new Item(
                2,
                "test title 2",
                account,
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
    void saveItemThrowsWithBadAccountId() {
        given(categoryRepository.findById(any())).willReturn(Optional.of(new Category()));
        given(accountRepository.findById(any())).willReturn(Optional.empty());
        List<Item> list = new ArrayList<>();
        Account account = new Account(1, "test name", "test username", "testPass", "testEmail", new Role());
        Category category = new Category("test category");
        Item item1 = new Item(
                1,
                "test title",
                account,
                0,
                category,
                null,
                "test desc"
        );
        Item item2 = new Item(
                2,
                "test title 2",
                account,
                0,
                category,
                null,
                "test desc"

        );

        list.add(item1);
        list.add(item2);

        assertThatThrownBy(() ->  testItemService.saveAllItems(list))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("Account with id: " + account.getId() + " does not exist");

        verify(itemRepository, never()).saveAll(list);
    }

    @Test
    void updateItemWorks() {
        given(categoryRepository.findById(any())).willReturn(Optional.of(new Category()));

        Account account = new Account(1, "test name", "test username", "testPass", "testEmail", new Role());
        Category category = new Category("test category");

        Item item = new Item(
                1,
                "test title",
                account,
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
        Account account = new Account(1, "test name", "test username", "testPass", "testEmail", new Role());
        Category category = new Category("test category");

        Item item = new Item(
                1,
                "test title",
                account,
                0,
                category,
                null,
                "test desc"
        );

        given(itemRepository.findById(any())).willReturn(Optional.empty());
        int itemId = item.getId();
        assertThatThrownBy(() ->  testItemService.updateItem(itemId, item))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("No items exists with id: " + item.getId());

        verify(itemRepository, never()).save(item);
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "this title will be too long. this title will be too long. this title will be too long. this title will be too long.",
            "il"
    })
    void updateItemThrowsErrorWithBadTitle(String title) {
        given(categoryRepository.findById(any())).willReturn(Optional.of(new Category()));
        Account account = new Account(1, "test name", "test username", "testPass", "testEmail", new Role());
        Category category = new Category("test category");

        Item item = new Item(
                1,
                title,
                account,
                0,
                category,
                null,
                "test desc"
        );

        given(itemRepository.findById(any())).willReturn(Optional.of(item));
        int itemId = item.getId();
        assertThatThrownBy(() ->  testItemService.updateItem(itemId, item))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("item with invalid title. Length has to be between 3 and 50 characters");

        verify(itemRepository, never()).save(item);
    }

    @Test
    void updateItemThrowsErrorWithBadDesc() {
        given(categoryRepository.findById(any())).willReturn(Optional.of(new Category()));
        Account account = new Account(1, "test name", "test username", "testPass", "testEmail", new Role());
        Category category = new Category("test category");

        Item item = new Item(
                1,
                "test title",
                account,
                0,
                category,
                null,
                "test desc that is too long. test desc that is too long.  test desc that is too long. test desc that is too long. test desc that is too long. test desc that is too long. test desc that is too long. test desc that is too long. test desc that is too long. test desc that is too long. test desc that is too long. test desc that is too long. test desc that is too long. test desc that is too long. test desc that is too long. "
        );

        given(itemRepository.findById(any())).willReturn(Optional.of(item));
        int itemId = item.getId();
        assertThatThrownBy(() ->  testItemService.updateItem(itemId, item))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("item with invalid desc. Length has to be under 300 characters");

        verify(itemRepository, never()).save(item);
    }

    @Test
    void updateItemThrowsErrorWithNoCategory() {
        Account account = new Account(1, "test name", "test username", "testPass", "testEmail", new Role());
        Category category = new Category("test category");

        Item item = new Item(
                1,
                "test title",
                account,
                0,
                category,
                null,
                "test desc"
        );

        given(itemRepository.findById(any())).willReturn(Optional.of(item));
        int itemId = item.getId();
        assertThatThrownBy(() ->  testItemService.updateItem(itemId, item))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("No categories exists with id: " + item.getCategory().getId());

        verify(itemRepository, never()).save(item);
    }
    @Test
    void updateItemRatingAndWordsWorks() {
        given(reviewRepository.findAllRatingsWithItemId(anyInt())).willReturn(List.of(5, 1));

        Account account = new Account(1, "test name", "test username", "testPass", "testEmail", new Role());
        Category category = new Category("test category");

        List<Review> reviews = new ArrayList<>();

        List<String> posWords = new ArrayList<>();
        List<String> negWords = new ArrayList<>();

        posWords.add("most pos");
        posWords.add("2.");
        posWords.add("3.");
        posWords.add("4.");
        posWords.add("5.");

        negWords.add("most neg");
        negWords.add("2.");
        negWords.add("3.");
        negWords.add("4.");
        negWords.add("5.");

        Item item = new Item(
                1,
                "test title",
                account,
                3,
                category,
                new Words(1, posWords, negWords),
                "test desc"
        );

        Review review1 = new Review(
                new Date(1),
                "this item is really good and i loved it",
                "title",
                0,
                0,
                account,
                5,
                item
        );
        Review review2 = new Review(
                new Date(1),
                "this item is really bad",
                "title1",
                0,
                0,
                account,
                1,
                item
        );

        reviews.add(review1);
        reviews.add(review2);

        given(itemRepository.findById(any())).willReturn(Optional.of(item));
        testItemService.updateItemRatingAndWords(item.getId(), posWords, negWords);

        verify(itemRepository).save(item);
    }

    @Test
    void updateItemRatingAndWordsThrowsWithBadItemId() {

        Account account = new Account(1, "test name", "test username", "testPass", "testEmail", new Role());
        Category category = new Category("test category");

        List<Review> reviews = new ArrayList<>();

        List<String> posWords = new ArrayList<>();
        List<String> negWords = new ArrayList<>();

        posWords.add("most pos");
        posWords.add("2.");
        posWords.add("3.");
        posWords.add("4.");
        posWords.add("5.");

        negWords.add("most neg");
        negWords.add("2.");
        negWords.add("3.");
        negWords.add("4.");
        negWords.add("5.");

        Item item = new Item(
                1,
                "test title",
                account,
                3,
                category,
                new Words(1, posWords, negWords),
                "test desc"
        );

        Review review1 = new Review(
                new Date(1),
                "this item is really good and i loved it",
                "title",
                0,
                0,
                account,
                5,
                item
        );
        Review review2 = new Review(
                new Date(1),
                "this item is really bad",
                "title1",
                0,
                0,
                account,
                1,
                item
        );

        reviews.add(review1);
        reviews.add(review2);

        given(itemRepository.findById(any())).willReturn(Optional.empty());
        int itemId = item.getId();
        assertThatThrownBy(() ->  testItemService.updateItemRatingAndWords(itemId, posWords, negWords))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("no items with id: " + item.getId() + " exists");

        verify(itemRepository, never()).save(item);
    }

    @Test
    void updateItemRatingAndWordsThrowsWithTooLargeRating() {
        given(reviewRepository.findAllRatingsWithItemId(anyInt())).willReturn(List.of(10, 5));

        Account account = new Account(1, "test name", "test username", "testPass", "testEmail", new Role());
        Category category = new Category("test category");

        List<Review> reviews = new ArrayList<>();

        List<String> posWords = new ArrayList<>();
        List<String> negWords = new ArrayList<>();

        posWords.add("most pos");
        posWords.add("2.");
        posWords.add("3.");
        posWords.add("4.");
        posWords.add("5.");

        negWords.add("most neg");
        negWords.add("2.");
        negWords.add("3.");
        negWords.add("4.");
        negWords.add("5.");

        Item item = new Item(
                1,
                "test title",
                account,
                0,
                category,
                new Words(1, posWords, negWords),
                "test desc"
        );

        Review review1 = new Review(
                new Date(1),
                "this item is really good and i loved it",
                "title",
                0,
                0,
                account,
                8,
                item
        );
        Review review2 = new Review(
                new Date(1),
                "this item is really bad",
                "title1",
                0,
                0,
                account,
                10,
                item
        );

        reviews.add(review1);
        reviews.add(review2);

        given(itemRepository.findById(any())).willReturn(Optional.of(item));

        int itemId = item.getId();
        assertThatThrownBy(() -> testItemService.updateItemRatingAndWords(itemId, posWords, negWords))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("item rating invalid. Allowed only 1-5");

        verify(itemRepository, never()).save(item);
    }

    @Test
    void updateItemRatingAndWordsThrowsWithTooSmallRating() {
        given(reviewRepository.findAllRatingsWithItemId(anyInt())).willReturn(List.of(0, 0));

        Account account = new Account(1, "test name", "test username", "testPass", "testEmail", new Role());
        Category category = new Category("test category");

        List<Review> reviews = new ArrayList<>();

        List<String> posWords = new ArrayList<>();
        List<String> negWords = new ArrayList<>();

        posWords.add("most pos");
        posWords.add("2.");
        posWords.add("3.");
        posWords.add("4.");
        posWords.add("5.");

        negWords.add("most neg");
        negWords.add("2.");
        negWords.add("3.");
        negWords.add("4.");
        negWords.add("5.");

        Item item = new Item(
                1,
                "test title",
                account,
                0,
                category,
                new Words(1, posWords, negWords),
                "test desc"
        );

        Review review1 = new Review(
                new Date(1),
                "this item is really good and i loved it",
                "title",
                0,
                0,
                account,
                0,
                item
        );
        Review review2 = new Review(
                new Date(1),
                "this item is really bad",
                "title1",
                0,
                0,
                account,
                0,
                item
        );

        reviews.add(review1);
        reviews.add(review2);

        given(itemRepository.findById(any())).willReturn(Optional.of(item));
        int itemId = item.getId();
        assertThatThrownBy(() -> testItemService.updateItemRatingAndWords(itemId, posWords, negWords))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("item rating invalid. Allowed only 1-5");

        verify(itemRepository, never()).save(item);
    }

    @Test
    void updateItemRatingAndWordsThrowsWithTooManyPosWords() {
        given(reviewRepository.findAllRatingsWithItemId(anyInt())).willReturn(List.of(5, 1));

        Account account = new Account(1, "test name", "test username", "testPass", "testEmail", new Role());
        Category category = new Category("test category");

        List<Review> reviews = new ArrayList<>();

        List<String> posWords = new ArrayList<>();
        List<String> negWords = new ArrayList<>();

        posWords.add("most pos");
        posWords.add("2.");
        posWords.add("3.");
        posWords.add("4.");
        posWords.add("5.");
        posWords.add("6.");

        negWords.add("most neg");
        negWords.add("2.");
        negWords.add("3.");
        negWords.add("4.");
        negWords.add("5.");

        Item item = new Item(
                1,
                "test title",
                account,
                2,
                category,
                new Words(1, posWords, negWords),
                "test desc"
        );

        Review review1 = new Review(
                new Date(1),
                "this item is really good and i loved it",
                "title",
                0,
                0,
                account,
                4,
                item
        );
        Review review2 = new Review(
                new Date(1),
                "this item is really bad",
                "title1",
                0,
                0,
                account,
                1,
                item
        );

        reviews.add(review1);
        reviews.add(review2);

        given(itemRepository.findById(any())).willReturn(Optional.of(item));
        int itemId = item.getId();
        assertThatThrownBy(() -> testItemService.updateItemRatingAndWords(itemId, posWords, negWords))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("pos words invalid. Has to have 1 to 5 items.");

        verify(itemRepository, never()).save(item);
    }

    @Test
    void updateItemRatingAndWordsThrowsWithNotEnoughPosWords() {
        given(reviewRepository.findAllRatingsWithItemId(anyInt())).willReturn(List.of(5, 1));

        Account account = new Account(1, "test name", "test username", "testPass", "testEmail", new Role());
        Category category = new Category("test category");

        List<Review> reviews = new ArrayList<>();

        List<String> posWords = new ArrayList<>();
        List<String> negWords = new ArrayList<>();

        negWords.add("most neg");
        negWords.add("2.");
        negWords.add("3.");
        negWords.add("4.");
        negWords.add("5.");

        Item item = new Item(
                1,
                "test title",
                account,
                2,
                category,
                new Words(1, posWords, negWords),
                "test desc"
        );

        Review review1 = new Review(
                new Date(1),
                "this item is really good and i loved it",
                "title",
                0,
                0,
                account,
                4,
                item
        );
        Review review2 = new Review(
                new Date(1),
                "this item is really bad",
                "title1",
                0,
                0,
                account,
                1,
                item
        );

        reviews.add(review1);
        reviews.add(review2);

        given(itemRepository.findById(any())).willReturn(Optional.of(item));

        int itemId = item.getId();
        assertThatThrownBy(() -> testItemService.updateItemRatingAndWords(itemId, posWords, negWords))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("pos words invalid. Has to have 1 to 5 items.");

        verify(itemRepository, never()).save(item);
    }

    @Test
    void updateItemRatingAndWordsThrowsWithTooManyNegWords() {
        given(reviewRepository.findAllRatingsWithItemId(anyInt())).willReturn(List.of(5, 1));

        Account account = new Account(1, "test name", "test username", "testPass", "testEmail", new Role());
        Category category = new Category("test category");

        List<Review> reviews = new ArrayList<>();

        List<String> posWords = new ArrayList<>();
        List<String> negWords = new ArrayList<>();

        posWords.add("most pos");
        posWords.add("2.");
        posWords.add("3.");
        posWords.add("4.");
        posWords.add("5.");

        negWords.add("most neg");
        negWords.add("2.");
        negWords.add("3.");
        negWords.add("4.");
        negWords.add("5.");
        negWords.add("6.");

        Item item = new Item(
                1,
                "test title",
                account,
                2,
                category,
                new Words(1, posWords, negWords),
                "test desc"
        );

        Review review1 = new Review(
                new Date(1),
                "this item is really good and i loved it",
                "title",
                0,
                0,
                account,
                4,
                item
        );
        Review review2 = new Review(
                new Date(1),
                "this item is really bad",
                "title1",
                0,
                0,
                account,
                1,
                item
        );

        reviews.add(review1);
        reviews.add(review2);

        given(itemRepository.findById(any())).willReturn(Optional.of(item));

        int itemId = item.getId();
        assertThatThrownBy(() -> testItemService.updateItemRatingAndWords(itemId, posWords, negWords))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("neg words invalid. Has to have 1 to 5 items.");

        verify(itemRepository, never()).save(item);
    }

    @Test
    void updateItemRatingAndWordsThrowsWithNotEnoughNegWords() {
        given(reviewRepository.findAllRatingsWithItemId(anyInt())).willReturn(List.of(5, 1));

        Account account = new Account(1, "test name", "test username", "testPass", "testEmail", new Role());
        Category category = new Category("test category");

        List<Review> reviews = new ArrayList<>();

        List<String> posWords = new ArrayList<>();
        List<String> negWords = new ArrayList<>();

        posWords.add("most neg");
        posWords.add("2.");
        posWords.add("3.");
        posWords.add("4.");
        posWords.add("5.");

        Item item = new Item(
                1,
                "test title",
                account,
                2,
                category,
                new Words(1, posWords, negWords),
                "test desc"
        );

        Review review1 = new Review(
                new Date(1),
                "this item is really good and i loved it",
                "title",
                0,
                0,
                account,
                4,
                item
        );
        Review review2 = new Review(
                new Date(1),
                "this item is really bad",
                "title1",
                0,
                0,
                account,
                1,
                item
        );

        reviews.add(review1);
        reviews.add(review2);

        given(itemRepository.findById(any())).willReturn(Optional.of(item));

        int itemId = item.getId();
        assertThatThrownBy(() -> testItemService.updateItemRatingAndWords(itemId, posWords, negWords))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("neg words invalid. Has to have 1 to 5 items.");

        verify(itemRepository, never()).save(item);
    }
}