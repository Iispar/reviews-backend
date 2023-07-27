package com.example.shopBackend.pages;

import com.example.shopBackend.ShopBackendApplication;
import com.example.shopBackend.item.ItemRepository;
import com.example.shopBackend.review.ReviewRepository;
import com.example.shopBackend.user.User;
import com.example.shopBackend.user.UserRepository;
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
    void getHomepageForUserThrowsWithNoReviews() {
        Pageable reviewPageReq = PageRequest.of(0, 4, Sort.by("review_date").ascending());
        Pageable itemPageReq = PageRequest.of(0, 4, Sort.by("item_rating").ascending());

        given(userRepository.findById(any())).willReturn(Optional.of(new User()));
        given(reviewRepository.findCountWithUserId(anyInt())).willReturn(0);

        assertThatThrownBy(() -> testPagesService.getHomepageForUser(1))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("no reviews exist with userId");

        verify(reviewRepository, never()).findAllUserId(1, reviewPageReq);
    }

    @Test
    void getHomepageForUserThrowsWithNoItems() {
        Pageable reviewPageReq = PageRequest.of(0, 4, Sort.by("review_date").ascending());
        Pageable itemPageReq = PageRequest.of(0, 4, Sort.by("item_rating").ascending());

        given(userRepository.findById(any())).willReturn(Optional.of(new User()));
        given(reviewRepository.findCountWithUserId(anyInt())).willReturn(2);
        given(itemRepository.findItemCountForUserId(anyInt())).willReturn(0);

        assertThatThrownBy(() -> testPagesService.getHomepageForUser(1))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("no items exist with userId");

        verify(reviewRepository, never()).findAllUserId(1, reviewPageReq);
    }
}
