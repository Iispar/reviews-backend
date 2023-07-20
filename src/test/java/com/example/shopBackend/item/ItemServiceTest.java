package com.example.shopBackend.item;

import com.example.shopBackend.ShopBackendApplication;
import com.example.shopBackend.user.User;
import com.example.shopBackend.user.UserRepository;
import exception.BadRequestException;
import org.junit.jupiter.api.Disabled;
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
    @Disabled
    void saveReview() {

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
}