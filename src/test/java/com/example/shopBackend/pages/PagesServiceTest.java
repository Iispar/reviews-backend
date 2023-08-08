package com.example.shopBackend.pages;

import com.example.shopBackend.ShopBackendApplication;
import com.example.shopBackend.account.Account;
import com.example.shopBackend.account.AccountRepository;
import com.example.shopBackend.category.Category;
import com.example.shopBackend.item.Item;
import com.example.shopBackend.item.ItemRepository;
import com.example.shopBackend.review.Review;
import com.example.shopBackend.review.ReviewRepository;
import com.example.shopBackend.words.Words;
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
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ActiveProfiles("test")
@DataJpaTest()
@ContextConfiguration(classes = ShopBackendApplication.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ExtendWith(MockitoExtension.class)
class PagesServiceTest {
    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private PagesService testPagesService;

    @Test
    void getHomepageForAccountWorks() {
        Pageable reviewPageReq = PageRequest.of(0, 4, Sort.by("review_date").ascending());
        Pageable itemPageReq = PageRequest.of(0, 4, Sort.by("item_rating").ascending());

        given(accountRepository.findById(any())).willReturn(Optional.of(new Account()));
        given(reviewRepository.findCountWithAccountId(anyInt())).willReturn(2);
        given(itemRepository.findItemCountForAccountId(anyInt())).willReturn(2);

        testPagesService.getHomepageForAccount(1);

        verify(reviewRepository).findAllAccountId(1, reviewPageReq);
        verify(itemRepository).findAllAccountId(1, itemPageReq);
        verify(reviewRepository).findChartForAccountByWeek(1);
        verify(reviewRepository).findCountWithAccountId(1);
        verify(itemRepository).findItemCountForAccountId(1);
        verify(itemRepository).findItemAvgRatingForAccountId(1);
    }

    @Test
    void getHomepageForAccountWorksWithNoReviews() {
        Pageable reviewPageReq = PageRequest.of(0, 4, Sort.by("review_date").ascending());
        Pageable itemPageReq = PageRequest.of(0, 4, Sort.by("item_rating").ascending());

        given(accountRepository.findById(any())).willReturn(Optional.of(new Account()));
        given(reviewRepository.findCountWithAccountId(anyInt())).willReturn(0);
        given(itemRepository.findItemCountForAccountId(anyInt())).willReturn(0);

        given(reviewRepository.findAllAccountId(anyInt(), any())).willReturn(new ArrayList<>());
        given(itemRepository.findAllAccountId(anyInt(), any())).willReturn(new ArrayList<>());
        given(reviewRepository.findChartForAccountByWeek(anyInt())).willReturn(new ArrayList<>());

        given(itemRepository.findItemAvgRatingForAccountId(anyInt())).willReturn(Optional.empty());

        testPagesService.getHomepageForAccount(1);

        verify(reviewRepository).findAllAccountId(1, reviewPageReq);
        verify(itemRepository).findAllAccountId(1, itemPageReq);
        verify(reviewRepository).findChartForAccountByWeek(1);
        verify(reviewRepository).findCountWithAccountId(1);
        verify(itemRepository).findItemCountForAccountId(1);
        verify(itemRepository).findItemAvgRatingForAccountId(1);
    }

    @Test
    void getHomepageForAccountThrowsWithBadId() {
        Pageable reviewPageReq = PageRequest.of(0, 4, Sort.by("review_date").ascending());

        given(accountRepository.findById(any())).willReturn(Optional.empty());

        assertThatThrownBy(() -> testPagesService.getHomepageForAccount(1))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("no Accounts with id: 1 exists");

        verify(reviewRepository, never()).findAllAccountId(1, reviewPageReq);
    }

    @Test
    void getItemPageForItemWorks() {
        Pageable reviewPageReq = PageRequest.of(0, 4, Sort.by("review_date").ascending());

        given(itemRepository.findById(any())).willReturn(Optional.of(new Item(1, "test", new Account(), 4.2F, new Category(), new Words(1, List.of("1"), List.of("1")), "desc")));
        given(reviewRepository.findAllItemId(anyInt(), any())).willReturn(List.of(new Review()));

        testPagesService.getItemPageForItem(1);

        verify(reviewRepository).findAllItemId(1, reviewPageReq);
        verify(reviewRepository).findChartForItemByWeek(1);
        verify(itemRepository).findById(1);
    }

    @Test
    void getItemPageForItemThrowsWithBadItemId() {

        given(itemRepository.findById(any())).willReturn(Optional.empty());

        assertThatThrownBy(() -> testPagesService.getItemPageForItem(1))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("no items with id: 1 exists");

        verify(reviewRepository, never()).findChartForItemByWeek(1);
    }
}
