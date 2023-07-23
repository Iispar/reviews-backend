package com.example.shopBackend.user;

import com.example.shopBackend.ShopBackendApplication;
import com.example.shopBackend.item.ItemRepository;
import com.example.shopBackend.role.Role;
import com.example.shopBackend.role.RoleRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import static org.junit.jupiter.api.Assertions.assertTrue;

@ActiveProfiles("test")
@DataJpaTest()
@ContextConfiguration(classes = ShopBackendApplication.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UserRepositoryTest {
    @Autowired
    private UserRepository testUserRepository;

    @Autowired
    private RoleRepository testRoleRepository;

    @Autowired
    private ItemRepository testItemRepository;

    @AfterEach
    void deleteAll() {
        testUserRepository.deleteAll();
    }

    @Test
    void userFindByEmailWorks() throws Exception {
        Role role = testRoleRepository.findById(1).orElseThrow();
        User user = new User(
                1,
                "test name",
                "username",
                "passWord123!",
                "example@gmail.com",
                role
        );

        testUserRepository.save(user);

        User foundEntity = testUserRepository.findByEmail(user.getEmail()).orElse(null);
        User foundNoneEntity = testUserRepository.findByEmail("empty").orElse(null);
        assertTrue(foundEntity != null);
        assertTrue(foundNoneEntity == null);
    }

    @Test
    void userFindByUsernameWorks() throws Exception {
        Role role = testRoleRepository.findById(1).orElseThrow();
        User user = new User(
                1,
                "test name",
                "username",
                "passWord123!",
                "example@gmail.com",
                role
        );

        testUserRepository.save(user);

        User foundEntity = testUserRepository.findByUsername(user.getUsername()).orElse(null);
        User foundNoneEntity = testUserRepository.findByUsername("empty").orElse(null);
        assertTrue(foundEntity != null);
        assertTrue(foundNoneEntity == null);
    }
}
