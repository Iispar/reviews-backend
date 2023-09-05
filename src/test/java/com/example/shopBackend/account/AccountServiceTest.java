package com.example.shopBackend.account;


import com.example.shopBackend.ShopBackendApplication;
import com.example.shopBackend.category.Category;
import com.example.shopBackend.item.Item;
import com.example.shopBackend.item.ItemRepository;
import com.example.shopBackend.item.ItemService;
import com.example.shopBackend.role.Role;
import com.example.shopBackend.role.RoleRepository;
import com.example.shopBackend.security.AuthRequest;
import com.example.shopBackend.security.JwtService;
import exception.BadRequestException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

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

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private ItemService itemService;

    @Mock
    private JwtService jwtService;

    @Mock
    AuthenticationManager authenticationManager;

    @InjectMocks
    private AccountService testAccountService;

    @Test
    void getAccountWorks() {
        given(accountRepository.findById(any())).willReturn(Optional.of(new Account()));
        testAccountService.getAccount(1);

        verify(accountRepository).findById(1);

    }

    @Test
    void getAccountThrowsWithNoAccount() {
        assertThatThrownBy(() ->  testAccountService.getAccount(1))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("no accounts with id: 1");

    }

    @Test
    void saveAllAccounts() {
        given(roleRepository.findById(any())).willReturn(Optional.of(new Role()));
        given(accountRepository.findByUsername(any())).willReturn(Optional.empty());
        given(accountRepository.findByEmail(any())).willReturn(Optional.empty());

        Role role = new Role(1, "test category");
        Account account = new Account(
                1,
                "test name",
                "username",
                "passWord123!",
                "example@gmail.com",
                role
        );

        testAccountService.saveAccount(account);

        verify(accountRepository).save(account);
    }

    @Test
    void saveAllAccountsThrowsWithBadRoleId() {
        given(roleRepository.findById(any())).willReturn(Optional.empty());

        Role role = new Role(1, "test category");
        Account account = new Account(
                1,
                "test name",
                "username",
                "passWord123!",
                "example@gmail.com",
                role
        );

        assertThatThrownBy(() ->  testAccountService.saveAccount(account))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("role with id: 1 does not exist");

        verify(accountRepository, never()).save(account);
    }

    @Test
    void saveAllAccountsThrowsWithBadPasswordCharacter() {
        given(roleRepository.findById(any())).willReturn(Optional.of(new Role()));

        Role role = new Role(1, "test category");
        Account account = new Account(
                1,
                "test name",
                "username",
                "password",
                "example@gmail.com",
                role
        );

        assertThatThrownBy(() ->  testAccountService.saveAccount(account))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("password doesn't include an uppercase letter, number or special character os is min length 8");

        verify(accountRepository, never()).save(account);
    }

    @Test
    void saveAllAccountsThrowsWithUsernameSpaceCharacter() {
        given(roleRepository.findById(any())).willReturn(Optional.of(new Role()));

        Role role = new Role(1, "test category");
        Account account = new Account(
                1,
                "test name",
                "user name",
                "password",
                "example@gmail.com",
                role
        );

        assertThatThrownBy(() ->  testAccountService.saveAccount(account))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("username includes one of the following. special character, space or is not between 2-20 characters");

        verify(accountRepository, never()).save(account);
    }

    @Test
    void saveAllAccountsThrowsWithUsernameSpecialCharacter() {
        given(roleRepository.findById(any())).willReturn(Optional.of(new Role()));

        Role role = new Role(1, "test category");
        Account account = new Account(
                1,
                "testname",
                "username!",
                "password",
                "example@gmail.com",
                role
        );

        assertThatThrownBy(() ->  testAccountService.saveAccount(account))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("username includes one of the following. special character, space or is not between 2-20 characters");

        verify(accountRepository, never()).save(account);
    }

    @Test
    void saveAllAccountsThrowsWithTooShortPassword() {
        given(roleRepository.findById(any())).willReturn(Optional.of(new Role()));

        Role role = new Role(1, "test category");
        Account account = new Account(
                1,
                "test name",
                "username",
                "Pa12!",
                "example@gmail.com",
                role
        );

        assertThatThrownBy(() ->  testAccountService.saveAccount(account))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("password doesn't include an uppercase letter, number or special character os is min length 8");

        verify(accountRepository, never()).save(account);
    }

    @Test
    void saveAllAccountsThrowsWithNotUniqueUsername() {
        given(roleRepository.findById(any())).willReturn(Optional.of(new Role()));
        given(accountRepository.findByUsername(any())).willReturn(Optional.of(new Account()));

        Role role = new Role(1, "test category");
        Account account = new Account(
                1,
                "test name",
                "username",
                "PassWord12!",
                "example@gmail.com",
                role
        );

        assertThatThrownBy(() ->  testAccountService.saveAccount(account))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("an account with username: username already exists");

        verify(accountRepository, never()).save(account);
    }

    @Test
    void saveAllAccountsThrowsWithNotUniqueEmail() {
        given(roleRepository.findById(any())).willReturn(Optional.of(new Role()));
        given(accountRepository.findByUsername(any())).willReturn(Optional.empty());
        given(accountRepository.findByEmail(any())).willReturn(Optional.of(new Account()));

        Role role = new Role(1, "test category");
        Account account = new Account(
                1,
                "test name",
                "username",
                "PassWord12!",
                "example@gmail.com",
                role
        );

        assertThatThrownBy(() ->  testAccountService.saveAccount(account))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("an account with email: example@gmail.com already exists");

        verify(accountRepository, never()).save(account);
    }

    @Test
    void loginWorks() {

        Account account = new Account(1, "test name", "test username", "testPass", "testEmail", new Role());
        AuthRequest request = new AuthRequest(
                "test",
                "test"
        );

        given(accountRepository.findByUsername(any())).willReturn(Optional.of(account));
        testAccountService.login(request);

        verify(accountRepository).findByUsername("test");
    }




    @Test
    void loginThrowsWithNoUserInDB() {

        given(accountRepository.findByUsername(any())).willReturn(Optional.empty());

        Account account = new Account(1, "test name", "test username", "testPass", "testEmail", new Role());
        AuthRequest request = new AuthRequest(
                "test",
                "test"
        );

        assertThatThrownBy(() -> testAccountService.login(request))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("username not found");

        verify(accountRepository).findByUsername("test");
    }

    @Test
    void updateAccountWorks() {
        given(roleRepository.findById(any())).willReturn(Optional.of(new Role()));
        given(accountRepository.findByUsername(any())).willReturn(Optional.empty());
        given(accountRepository.findByEmail(any())).willReturn(Optional.empty());

        Account account = new Account(1, "test name", "test username", "testPass", "testEmail", new Role());
        Role role = new Role(1, "test category");

        Account updateAccount = new Account(
                1,
                "test2 name",
                "username",
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
        given(accountRepository.findByUsername(any())).willReturn(Optional.empty());
        given(accountRepository.findByEmail(any())).willReturn(Optional.of(new Account()));

        Account account = new Account(1, "test name", "test username", "testPass", "testEmail", new Role());
        Role role = new Role(1, "test category");

        Account updateAccount = new Account(
                1,
                "test2 name",
                "username2",
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
    void updateAccountThrowsWithBadUsername() {
        given(roleRepository.findById(any())).willReturn(Optional.of(new Role()));
        given(accountRepository.findByUsername(any())).willReturn(Optional.of(new Account()));

        Account account = new Account(1, "test name", "test username", "testPass", "testEmail", new Role());
        Role role = new Role(1, "test category");

        Account updateAccount = new Account(
                1,
                "test2 name",
                "username2",
                "passWord321!",
                "example2@gmail.com",
                role
        );

        given(accountRepository.findById(any())).willReturn(Optional.of(account));
        int AccountId = updateAccount.getId();
        assertThatThrownBy(() -> testAccountService.updateAccount(AccountId, updateAccount))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("an account with username: " + updateAccount.getUsername() + " already exists");

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
                "username2",
                "psas!",
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
    void updateAccountThrowsWithUsernameSpace() {
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
                .hasMessageContaining("username includes one of the following. special character, space or is not between 2-20 characters");

        verify(accountRepository, never()).save(updateAccount);
    }

    @Test
    void updateAccountWorksWithSameEmailAndUsername() {
        given(roleRepository.findById(any())).willReturn(Optional.of(new Role()));
        given(accountRepository.findByUsername(any())).willReturn(Optional.of(new Account()));
        given(accountRepository.findByEmail(any())).willReturn(Optional.of(new Account()));

        Account account = new Account(1, "test name", "testusername2", "testPass", "testEmail", new Role());
        Role role = new Role(1, "test category");

        Account updateAccount = new Account(
                1,
                "test2 name",
                "testusername2",
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
        Item item = new Item(
                1,
                "test title",
                new Account(
                        1,
                        "name",
                        "username",
                        "pass",
                        "email",
                        new Role()
                ),
                0,
                new Category(),
                null
        );

        given(accountRepository.findById(any())).willReturn(Optional.of(new Account()), Optional.empty());
        given(itemRepository.findAll()).willReturn(List.of(item));

        testAccountService.deleteAccount(0);

        verify(accountRepository).deleteById(0);
    }

    @Test
    void deleteAccountThrowsWithFailureToDeleteItems() {
        Item item = new Item(
                1,
                "test title",
                new Account(
                        1,
                        "name",
                        "username",
                        "pass",
                        "email",
                        new Role()
                ),
                0,
                new Category(),
                null
        );

        given(accountRepository.findById(any())).willReturn(Optional.of(new Account()), Optional.empty());
        given(itemRepository.findAll()).willReturn(List.of(item));
        given(itemService.deleteItem(anyInt())).willThrow(new RuntimeException());

        assertThatThrownBy(() ->  testAccountService.deleteAccount(1))
                .isInstanceOf(java.lang.RuntimeException.class)
                .hasMessageContaining("error: null. While deleting item with id: 1");

        verify(accountRepository, never()).deleteById(0);
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