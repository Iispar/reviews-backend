package com.example.shopBackend.account;


import com.example.shopBackend.ShopBackendApplication;
import com.example.shopBackend.item.ItemRepository;
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

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@SuppressWarnings("unchecked")
@ActiveProfiles("test")
@DataJpaTest()
@ContextConfiguration(classes = ShopBackendApplication.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ExtendWith(MockitoExtension.class)
class AccountServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private ItemRepository itemRepository;

    @InjectMocks
    private AccountService testAccountService;

    @Test
    void saveAllAccounts() {
        given(roleRepository.findById(any())).willReturn(Optional.of(new Role()));
        given(accountRepository.findByusername(any())).willReturn(Optional.empty());
        given(accountRepository.findByEmail(any())).willReturn(Optional.empty());

        List<Account> list = new ArrayList<>();
        Role role = new Role(1, "test category");
        Account account1 = new Account(
                1,
                "test name",
                "username",
                "passWord123!",
                "example@gmail.com",
                role
        );

        Account account2 = new Account(
                2,
                "test2 name",
                "username 2",
                "passWord321!",
                "example2@gmail.com",
                role
        );


        list.add(account1);
        list.add(account2);

        testAccountService.saveAllAccounts(list);

        verify(accountRepository).saveAll(list);
    }

    @Test
    void saveAllAccountsThrowsWithBadRoleId() {
        given(roleRepository.findById(any())).willReturn(Optional.empty());

        List<Account> list = new ArrayList<>();
        Role role = new Role(1, "test category");
        Account account1 = new Account(
                1,
                "test name",
                "username",
                "passWord123!",
                "example@gmail.com",
                role
        );

        Account account2 = new Account(
                2,
                "test2 name",
                "username 2",
                "passWord321!",
                "example2@gmail.com",
                role
        );


        list.add(account1);
        list.add(account2);

        assertThatThrownBy(() ->  testAccountService.saveAllAccounts(list))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("role with id: 1 does not exist");

        verify(accountRepository, never()).saveAll(list);
    }

    @Test
    void saveAllAccountsThrowsWithBadPasswordCharacter() {
        given(roleRepository.findById(any())).willReturn(Optional.of(new Role()));

        List<Account> list = new ArrayList<>();
        Role role = new Role(1, "test category");
        Account account1 = new Account(
                1,
                "test name",
                "username",
                "password",
                "example@gmail.com",
                role
        );

        Account account2 = new Account(
                2,
                "test2 name",
                "username 2",
                "passWord321!",
                "example2@gmail.com",
                role
        );


        list.add(account1);
        list.add(account2);

        assertThatThrownBy(() ->  testAccountService.saveAllAccounts(list))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("password doesn't include an uppercase letter, number or special character os is min length 8");

        verify(accountRepository, never()).saveAll(list);
    }

    @Test
    void saveAllAccountsThrowsWithTooShortPassword() {
        given(roleRepository.findById(any())).willReturn(Optional.of(new Role()));

        List<Account> list = new ArrayList<>();
        Role role = new Role(1, "test category");
        Account account1 = new Account(
                1,
                "test name",
                "username",
                "Pa12!",
                "example@gmail.com",
                role
        );

        Account account2 = new Account(
                2,
                "test2 name",
                "username 2",
                "passWord321!",
                "example2@gmail.com",
                role
        );


        list.add(account1);
        list.add(account2);

        assertThatThrownBy(() ->  testAccountService.saveAllAccounts(list))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("password doesn't include an uppercase letter, number or special character os is min length 8");

        verify(accountRepository, never()).saveAll(list);
    }

    @Test
    void saveAllAccountsThrowsWithNotUniqueusername() {
        given(roleRepository.findById(any())).willReturn(Optional.of(new Role()));
        given(accountRepository.findByusername(any())).willReturn(Optional.of(new Account()));

        List<Account> list = new ArrayList<>();
        Role role = new Role(1, "test category");
        Account account1 = new Account(
                1,
                "test name",
                "username",
                "PassWord12!",
                "example@gmail.com",
                role
        );

        Account account2 = new Account(
                2,
                "test2 name",
                "username 2",
                "passWord321!",
                "example2@gmail.com",
                role
        );


        list.add(account1);
        list.add(account2);

        assertThatThrownBy(() ->  testAccountService.saveAllAccounts(list))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("an account with username: username already exists");

        verify(accountRepository, never()).saveAll(list);
    }

    @Test
    void saveAllAccountsThrowsWithNotUniqueEmail() {
        given(roleRepository.findById(any())).willReturn(Optional.of(new Role()));
        given(accountRepository.findByusername(any())).willReturn(Optional.empty());
        given(accountRepository.findByEmail(any())).willReturn(Optional.of(new Account()));

        List<Account> list = new ArrayList<>();
        Role role = new Role(1, "test category");
        Account account1 = new Account(
                1,
                "test name",
                "username",
                "PassWord12!",
                "example@gmail.com",
                role
        );

        Account account2 = new Account(
                2,
                "test2 name",
                "username 2",
                "passWord321!",
                "example2@gmail.com",
                role
        );


        list.add(account1);
        list.add(account2);

        assertThatThrownBy(() ->  testAccountService.saveAllAccounts(list))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("an account with email: example@gmail.com already exists");

        verify(accountRepository, never()).saveAll(list);
    }

    @Test
    void updateAccountWorks() {
        given(roleRepository.findById(any())).willReturn(Optional.of(new Role()));
        given(accountRepository.findByusername(any())).willReturn(Optional.empty());
        given(accountRepository.findByEmail(any())).willReturn(Optional.empty());

        Account account = new Account(1, "test name", "test username", "testPass", "testEmail", new Role());
        Role role = new Role(1, "test category");

        Account updateAccount = new Account(
                1,
                "test2 name",
                "username 2",
                "passWord321!",
                "example2@gmail.com",
                role
        );

        given(accountRepository.findById(any())).willReturn(Optional.of(account));
        testAccountService.updateAccount(updateAccount.getId(), updateAccount);

        verify(accountRepository).save(account);
    }

    @Test
    void updateAccountThrowsWithBadRole() {
        given(roleRepository.findById(any())).willReturn(Optional.empty());

        Account account = new Account(1, "test name", "test username", "testPass", "testEmail", new Role());
        Role role = new Role(1, "test category");

        Account updateAccount = new Account(
                1,
                "test2 name",
                "username 2",
                "passWord321!",
                "example2@gmail.com",
                role
        );

        given(accountRepository.findById(any())).willReturn(Optional.of(account));
        int AccountId = updateAccount.getId();
        assertThatThrownBy(() -> testAccountService.updateAccount(AccountId, updateAccount))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("role with id: 1 does not exist");

        verify(accountRepository, never()).save(updateAccount);
    }

    @Test
    void updateAccountThrowsWithNoMatchingAccount() {

        Role role = new Role(1, "test category");

        Account updateAccount = new Account(
                1,
                "test2 name",
                "username 2",
                "passWord321!",
                "example2@gmail.com",
                role
        );

        given(accountRepository.findById(any())).willReturn(Optional.empty());
        int AccountId = updateAccount.getId();
        assertThatThrownBy(() -> testAccountService.updateAccount(AccountId, updateAccount))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("No accounts exists with id: 1");

        verify(accountRepository, never()).save(updateAccount);
    }

    @Test
    void updateAccountThrowsWithBadEmail() {
        given(roleRepository.findById(any())).willReturn(Optional.of(new Role()));
        given(accountRepository.findByusername(any())).willReturn(Optional.empty());
        given(accountRepository.findByEmail(any())).willReturn(Optional.of(new Account()));

        Account account = new Account(1, "test name", "test username", "testPass", "testEmail", new Role());
        Role role = new Role(1, "test category");

        Account updateAccount = new Account(
                1,
                "test2 name",
                "username 2",
                "passWord321!",
                "example2@gmail.com",
                role
        );

        given(accountRepository.findById(any())).willReturn(Optional.of(account));
        int AccountId = updateAccount.getId();
        assertThatThrownBy(() -> testAccountService.updateAccount(AccountId, updateAccount))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("an account with email: " + updateAccount.getEmail() + " already exists");

        verify(accountRepository, never()).save(updateAccount);
    }

    @Test
    void updateAccountThrowsWithBadusername() {
        given(roleRepository.findById(any())).willReturn(Optional.of(new Role()));
        given(accountRepository.findByusername(any())).willReturn(Optional.of(new Account()));

        Account account = new Account(1, "test name", "test username", "testPass", "testEmail", new Role());
        Role role = new Role(1, "test category");

        Account updateAccount = new Account(
                1,
                "test2 name",
                "username 2",
                "passWord321!",
                "example2@gmail.com",
                role
        );

        given(accountRepository.findById(any())).willReturn(Optional.of(account));
        int AccountId = updateAccount.getId();
        assertThatThrownBy(() -> testAccountService.updateAccount(AccountId, updateAccount))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("an account with username: " + updateAccount.getusername() + " already exists");

        verify(accountRepository, never()).save(updateAccount);
    }

    @Test
    void updateAccountThrowsWithBadPassword() {
        given(roleRepository.findById(any())).willReturn(Optional.of(new Role()));

        Account account = new Account(1, "test name", "test username", "testPass", "testEmail", new Role());
        Role role = new Role(1, "test category");

        Account updateAccount = new Account(
                1,
                "test2 name",
                "username 2",
                "pass!",
                "example2@gmail.com",
                role
        );

        given(accountRepository.findById(any())).willReturn(Optional.of(account));
        int AccountId = updateAccount.getId();
        assertThatThrownBy(() -> testAccountService.updateAccount(AccountId, updateAccount))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("password doesn't include an uppercase letter, number or special character os is min length 8");

        verify(accountRepository, never()).save(updateAccount);
    }

    @Test
    void updateAccountWorksWithSameEmailAndusername() {
        given(roleRepository.findById(any())).willReturn(Optional.of(new Role()));
        given(accountRepository.findByusername(any())).willReturn(Optional.of(new Account()));
        given(accountRepository.findByEmail(any())).willReturn(Optional.of(new Account()));

        Account account = new Account(1, "test name", "test username", "testPass", "testEmail", new Role());
        Role role = new Role(1, "test category");

        Account updateAccount = new Account(
                1,
                "test2 name",
                "test username",
                "passWord321!",
                "testEmail",
                role
        );

        given(accountRepository.findById(any())).willReturn(Optional.of(account));
        testAccountService.updateAccount(updateAccount.getId(), updateAccount);

        verify(accountRepository).save(account);
    }

    @Test
    void deleteAccountWorks() {
        given(accountRepository.findById(any())).willReturn(Optional.of(new Account()), Optional.empty());
        given(itemRepository.findAll()).willReturn(new ArrayList<>());

        testAccountService.deleteAccount(0);

        verify(accountRepository).deleteById(0);
    }

    @Test
    void deleteAccountThrowsErrorWithNoMatchingAccount() {
        given(accountRepository.findById(any())).willReturn(Optional.empty());

        assertThatThrownBy(() ->  testAccountService.deleteAccount(0))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("No Accounts exists with id 0");

        verify(accountRepository, never()).deleteById(0);
    }

    @Test
    void deleteAccountThrowsErrorWithFailedDeletion() {
        given(accountRepository.findById(any())).willReturn(Optional.of(new Account()));

        assertThatThrownBy(() ->  testAccountService.deleteAccount(0))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("deletion failed");

        verify(accountRepository).deleteById(0);
    }
}