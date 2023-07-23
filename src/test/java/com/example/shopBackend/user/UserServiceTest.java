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
        given(userRepository.findByUsername(any())).willReturn(Optional.empty());
        given(userRepository.findByEmail(any())).willReturn(Optional.empty());

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
        given(userRepository.findByUsername(any())).willReturn(Optional.of(new User()));

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
        given(userRepository.findByUsername(any())).willReturn(Optional.empty());
        given(userRepository.findByEmail(any())).willReturn(Optional.of(new User()));

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

    @Test
    void updateUserWorks() {
        given(roleRepository.findById(any())).willReturn(Optional.of(new Role()));
        given(userRepository.findByUsername(any())).willReturn(Optional.empty());
        given(userRepository.findByEmail(any())).willReturn(Optional.empty());

        User user = new User(1, "test name", "test username", "testPass", "testEmail", new Role());
        Role role = new Role(1, "test category");

        User updateUser = new User(
                1,
                "test2 name",
                "username 2",
                "passWord321!",
                "example2@gmail.com",
                role
        );

        given(userRepository.findById(any())).willReturn(Optional.of(user));
        testUserService.updateUser(updateUser.getId(), updateUser);

        verify(userRepository).save(user);
    }

    @Test
    void updateUserThrowsWithBadRole() {
        given(roleRepository.findById(any())).willReturn(Optional.empty());

        User user = new User(1, "test name", "test username", "testPass", "testEmail", new Role());
        Role role = new Role(1, "test category");

        User updateUser = new User(
                1,
                "test2 name",
                "username 2",
                "passWord321!",
                "example2@gmail.com",
                role
        );

        given(userRepository.findById(any())).willReturn(Optional.of(user));
        assertThatThrownBy(() -> testUserService.updateUser(updateUser.getId(), updateUser))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("role with id: 1 does not exist");

        verify(userRepository, never()).save(updateUser);
    }

    @Test
    void updateUserThrowsWithNoMatchingUser() {

        User user = new User(1, "test name", "test username", "testPass", "testEmail", new Role());
        Role role = new Role(1, "test category");

        User updateUser = new User(
                1,
                "test2 name",
                "username 2",
                "passWord321!",
                "example2@gmail.com",
                role
        );

        given(userRepository.findById(any())).willReturn(Optional.empty());
        assertThatThrownBy(() -> testUserService.updateUser(updateUser.getId(), updateUser))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("No users exists with id: 1");

        verify(userRepository, never()).save(updateUser);
    }

    @Test
    void updateUserThrowsWithBadEmail() {
        given(roleRepository.findById(any())).willReturn(Optional.of(new Role()));
        given(userRepository.findByUsername(any())).willReturn(Optional.empty());
        given(userRepository.findByEmail(any())).willReturn(Optional.of(new User()));

        User user = new User(1, "test name", "test username", "testPass", "testEmail", new Role());
        Role role = new Role(1, "test category");

        User updateUser = new User(
                1,
                "test2 name",
                "username 2",
                "passWord321!",
                "example2@gmail.com",
                role
        );

        given(userRepository.findById(any())).willReturn(Optional.of(user));
        assertThatThrownBy(() -> testUserService.updateUser(updateUser.getId(), updateUser))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("an user with email: " + updateUser.getEmail() + " already exists");

        verify(userRepository, never()).save(updateUser);
    }

    @Test
    void updateUserThrowsWithBadUsername() {
        given(roleRepository.findById(any())).willReturn(Optional.of(new Role()));
        given(userRepository.findByUsername(any())).willReturn(Optional.of(new User()));

        User user = new User(1, "test name", "test username", "testPass", "testEmail", new Role());
        Role role = new Role(1, "test category");

        User updateUser = new User(
                1,
                "test2 name",
                "username 2",
                "passWord321!",
                "example2@gmail.com",
                role
        );

        given(userRepository.findById(any())).willReturn(Optional.of(user));
        assertThatThrownBy(() -> testUserService.updateUser(updateUser.getId(), updateUser))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("an user with username: " + updateUser.getUsername() + " already exists");

        verify(userRepository, never()).save(updateUser);
    }

    @Test
    void updateUserThrowsWithBadPassword() {
        given(roleRepository.findById(any())).willReturn(Optional.of(new Role()));

        User user = new User(1, "test name", "test username", "testPass", "testEmail", new Role());
        Role role = new Role(1, "test category");

        User updateUser = new User(
                1,
                "test2 name",
                "username 2",
                "pass!",
                "example2@gmail.com",
                role
        );

        given(userRepository.findById(any())).willReturn(Optional.of(user));
        assertThatThrownBy(() -> testUserService.updateUser(updateUser.getId(), updateUser))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("password doesn't include an uppercase letter, number or special character os is min length 8");

        verify(userRepository, never()).save(updateUser);
    }

    @Test
    void updateUserWorksWithSameEmailAndUsername() {
        given(roleRepository.findById(any())).willReturn(Optional.of(new Role()));
        given(userRepository.findByUsername(any())).willReturn(Optional.of(new User()));
        given(userRepository.findByEmail(any())).willReturn(Optional.of(new User()));

        User user = new User(1, "test name", "test username", "testPass", "testEmail", new Role());
        Role role = new Role(1, "test category");

        User updateUser = new User(
                1,
                "test2 name",
                "test username",
                "passWord321!",
                "testEmail",
                role
        );

        given(userRepository.findById(any())).willReturn(Optional.of(user));
        testUserService.updateUser(updateUser.getId(), updateUser);

        verify(userRepository).save(user);
    }

    @Test
    void deleteUserWorks() {
        given(userRepository.findById(any())).willReturn(Optional.of(new User()));

        testUserService.deleteUser(0);

        verify(userRepository).deleteById(0);
    }

    @Test
    void deleteUserThrowsErrorWithNoMatchingUser() {
        given(userRepository.findById(any())).willReturn(Optional.empty());

        assertThatThrownBy(() ->  testUserService.deleteUser(0))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("No users exists with id 0");

        verify(userRepository, never()).deleteById(0);
    }
}