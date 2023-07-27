package com.example.shopBackend.pages;

import com.example.shopBackend.ShopBackendApplication;
import com.example.shopBackend.category.Category;
import com.example.shopBackend.item.Item;
import com.example.shopBackend.item.ItemRepository;
import com.example.shopBackend.review.Review;
import com.example.shopBackend.review.ReviewRepository;
import com.example.shopBackend.user.User;
import com.example.shopBackend.user.UserRepository;
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
public class PagesServiceTest {
    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private PagesService testPagesService;

    @Test
    void getHomepageForUserWorks() {
        Pageable reviewPageReq = PageRequest.of(0, 4, Sort.by("review_date").ascending());
        Pageable itemPageReq = PageRequest.of(0, 4, Sort.by("item_rating").ascending());

        given(userRepository.findById(any())).willReturn(Optional.of(new User()));
        given(reviewRepository.findCountWithUserId(anyInt())).willReturn(2);
        given(itemRepository.findItemCountForUserId(anyInt())).willReturn(2);

        testPagesService.getHomepageForUser(1);

        verify(reviewRepository).findAllUserId(1, reviewPageReq);
        verify(itemRepository).findAllUserId(1, itemPageReq);
        verify(reviewRepository).findChartForUserByWeek(1);
        verify(reviewRepository).findCountWithUserId(1);
        verify(itemRepository).findItemCountForUserId(1);
        verify(itemRepository).findItemAvgRatingForUserId(1);
    }

    @Test
    void getHomepageForUserWorksWithNoReviews() {
        Pageable reviewPageReq = PageRequest.of(0, 4, Sort.by("review_date").ascending());
        Pageable itemPageReq = PageRequest.of(0, 4, Sort.by("item_rating").ascending());

        given(userRepository.findById(any())).willReturn(Optional.of(new User()));
        given(reviewRepository.findCountWithUserId(anyInt())).willReturn(0);
        given(itemRepository.findItemCountForUserId(anyInt())).willReturn(0);

        given(reviewRepository.findAllUserId(anyInt(), any())).willReturn(new ArrayList<>());
        given(itemRepository.findAllUserId(anyInt(), any())).willReturn(new ArrayList<>());
        given(reviewRepository.findChartForUserByWeek(anyInt())).willReturn(new ArrayList<>());

        given(itemRepository.findItemAvgRatingForUserId(anyInt())).willReturn(Optional.empty());

        testPagesService.getHomepageForUser(1);

        verify(reviewRepository).findAllUserId(1, reviewPageReq);
        verify(itemRepository).findAllUserId(1, itemPageReq);
        verify(reviewRepository).findChartForUserByWeek(1);
        verify(reviewRepository).findCountWithUserId(1);
        verify(itemRepository).findItemCountForUserId(1);
        verify(itemRepository).findItemAvgRatingForUserId(1);
    }

    @Test
    void getHomepageForUserThrowsWithBadId() {
        Pageable reviewPageReq = PageRequest.of(0, 4, Sort.by("review_date").ascending());
        Pageable itemPageReq = PageRequest.of(0, 4, Sort.by("item_rating").ascending());

        given(userRepository.findById(any())).willReturn(Optional.empty());

        assertThatThrownBy(() -> testPagesService.getHomepageForUser(1))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("no users with id: 1 exists");

        verify(reviewRepository, never()).findAllUserId(1, reviewPageReq);
    }

    @Test
    void getItempageForItemWorks() {
        Pageable reviewPageReq = PageRequest.of(0, 4, Sort.by("review_date").ascending());

        given(itemRepository.findById(any())).willReturn(Optional.of(new Item(1, "test", new User(), 4.2F, new Category(), new Words(1, List.of("1"), List.of("1")), "desc")));
        given(reviewRepository.findAllItemId(anyInt(), any())).willReturn(List.of(new Review()));

        testPagesService.getItempageForItem(1);

        verify(reviewRepository).findAllItemId(1, reviewPageReq);
        verify(reviewRepository).findChartForItemByWeek(1);
        verify(itemRepository).findById(1);
    }

    @Test
    void getItempageForItemThrowsWithBadItemid() {
        Pageable reviewPageReq = PageRequest.of(0, 4, Sort.by("review_date").ascending());

        given(itemRepository.findById(any())).willReturn(Optional.empty());

        assertThatThrownBy(() -> testPagesService.getItempageForItem(1))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("no items with id: 1 exists");

        verify(reviewRepository, never()).findChartForItemByWeek(1);
    }
}
