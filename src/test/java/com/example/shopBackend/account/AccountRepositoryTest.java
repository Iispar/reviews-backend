package com.example.shopBackend.account;

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

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

@ActiveProfiles("test")
@DataJpaTest()
@ContextConfiguration(classes = ShopBackendApplication.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class AccountRepositoryTest {
    @Autowired
    private AccountRepository testaccountRepository;

    @Autowired
    private RoleRepository testRoleRepository;

    @Autowired
    private ItemRepository testItemRepository;

    @AfterEach
    void deleteAll() {
        testaccountRepository.deleteAll();
    }

    @Test
    void AccountFindByEmailWorks() {
        Role role = testRoleRepository.findById(1).orElseThrow();
        Account account = new Account(
                1,
                "test name",
                "username",
                "passWord123!",
                "example@gmail.com",
                role
        );

        testaccountRepository.save(account);

        Account foundEntity = testaccountRepository.findByEmail(account.getEmail()).orElse(null);
        Account foundNoneEntity = testaccountRepository.findByEmail("empty").orElse(null);
        assertNotNull(foundEntity);
        assertNull(foundNoneEntity);
    }

    @Test
    void AccountFindByUsernameWorks() {
        Role role = testRoleRepository.findById(1).orElseThrow();
        Account account = new Account(
                1,
                "test name",
                "username",
                "passWord123!",
                "example@gmail.com",
                role
        );

        testaccountRepository.save(account);

        Account foundEntity = testaccountRepository.findByUsername(account.getUsername()).orElse(null);
        Account foundNoneEntity = testaccountRepository.findByUsername("empty").orElse(null);
        assertNotNull(foundEntity);
        assertNull(foundNoneEntity);
    }
}
