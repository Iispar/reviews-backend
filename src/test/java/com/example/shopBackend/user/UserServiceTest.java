package com.example.shopBackend.user;

import com.example.shopBackend.ShopBackendApplication;
import com.example.shopBackend.role.Role;
import com.example.shopBackend.role.RoleRepository;
import exception.BadRequestException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
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
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private UserService testUserService;

    @Test
    void saveAllUsers() {
        given(roleRepository.findById(any())).willReturn(Optional.of(new Role()));
        given(userRepository.findByUsername(any())).willReturn(Optional.of(new User()));
        given(userRepository.findByEmail(any())).willReturn(Optional.of(new User()));

        List<User> list = new ArrayList<User>();
        Role role = new Role(1, "test category");
        User user1 = new User(
                1,
                "test name",
                "username",
                "passWord123!",
                "example@gmail.com",
                role
        );

        User user2 = new User(
                2,
                "test2 name",
                "username 2",
                "passWord321!",
                "example2@gmail.com",
                role
        );


        list.add(user1);
        list.add(user2);

        testUserService.saveAllUsers(list);

        verify(userRepository).saveAll(list);
    }

    @Test
    void saveAllUsersThrowsWithBadRoleId() {
        given(roleRepository.findById(any())).willReturn(Optional.empty());

        List<User> list = new ArrayList<User>();
        Role role = new Role(1, "test category");
        User user1 = new User(
                1,
                "test name",
                "username",
                "passWord123!",
                "example@gmail.com",
                role
        );

        User user2 = new User(
                2,
                "test2 name",
                "username 2",
                "passWord321!",
                "example2@gmail.com",
                role
        );


        list.add(user1);
        list.add(user2);

        assertThatThrownBy(() ->  testUserService.saveAllUsers(list))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("role with id: 1 does not exist");

        verify(userRepository, never()).saveAll(list);
    }

    @Test
    void saveAllUsersThrowsWithBadPasswordCharacter() {
        given(roleRepository.findById(any())).willReturn(Optional.of(new Role()));

        List<User> list = new ArrayList<User>();
        Role role = new Role(1, "test category");
        User user1 = new User(
                1,
                "test name",
                "username",
                "password",
                "example@gmail.com",
                role
        );

        User user2 = new User(
                2,
                "test2 name",
                "username 2",
                "passWord321!",
                "example2@gmail.com",
                role
        );


        list.add(user1);
        list.add(user2);

        assertThatThrownBy(() ->  testUserService.saveAllUsers(list))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("password doesn't include an uppercase letter, number or special character os is min length 8");

        verify(userRepository, never()).saveAll(list);
    }

    @Test
    void saveAllUsersThrowsWithTooShortPassword() {
        given(roleRepository.findById(any())).willReturn(Optional.of(new Role()));

        List<User> list = new ArrayList<User>();
        Role role = new Role(1, "test category");
        User user1 = new User(
                1,
                "test name",
                "username",
                "Pa12!",
                "example@gmail.com",
                role
        );

        User user2 = new User(
                2,
                "test2 name",
                "username 2",
                "passWord321!",
                "example2@gmail.com",
                role
        );


        list.add(user1);
        list.add(user2);

        assertThatThrownBy(() ->  testUserService.saveAllUsers(list))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("password doesn't include an uppercase letter, number or special character os is min length 8");

        verify(userRepository, never()).saveAll(list);
    }

    @Test
    void saveAllUsersThrowsWithNotUniqueUsername() {
        given(roleRepository.findById(any())).willReturn(Optional.of(new Role()));
        given(userRepository.findByUsername(any())).willReturn(Optional.empty());

        List<User> list = new ArrayList<User>();
        Role role = new Role(1, "test category");
        User user1 = new User(
                1,
                "test name",
                "username",
                "PassWord12!",
                "example@gmail.com",
                role
        );

        User user2 = new User(
                2,
                "test2 name",
                "username 2",
                "passWord321!",
                "example2@gmail.com",
                role
        );


        list.add(user1);
        list.add(user2);

        assertThatThrownBy(() ->  testUserService.saveAllUsers(list))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("an user with username: username already exists");

        verify(userRepository, never()).saveAll(list);
    }

    @Test
    void saveAllUsersThrowsWithNotUniqueEmail() {
        given(roleRepository.findById(any())).willReturn(Optional.of(new Role()));
        given(userRepository.findByUsername(any())).willReturn(Optional.of(new User()));
        given(userRepository.findByEmail(any())).willReturn(Optional.empty());

        List<User> list = new ArrayList<User>();
        Role role = new Role(1, "test category");
        User user1 = new User(
                1,
                "test name",
                "username",
                "PassWord12!",
                "example@gmail.com",
                role
        );

        User user2 = new User(
                2,
                "test2 name",
                "username 2",
                "passWord321!",
                "example2@gmail.com",
                role
        );


        list.add(user1);
        list.add(user2);

        assertThatThrownBy(() ->  testUserService.saveAllUsers(list))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("an user with email: example@gmail.com already exists");

        verify(userRepository, never()).saveAll(list);
    }
}