package com.example.shopBackend.account;


import com.example.shopBackend.role.Role;
import com.example.shopBackend.response.AuthRes;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(value=AccountController.class)
@ContextConfiguration(classes = AccountController.class)
@EnableMethodSecurity
class AccountControllerTest {
    @Autowired
    MockMvc mockMvc;

    @MockBean
    AccountService accountService;

    @Test
    void getAccountWorks() throws Exception {
        Account account = new Account(
                1,
                "name",
                "username",
                "pass",
                "email",
                new Role(
                        1,
                        "role"
                )
        );
        given(accountService.getAccount(anyInt())).willReturn(account);

        mockMvc.perform(get("/api/account/get?accountId=1", 1).with(csrf())
                .with(user(account)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(account.getName()))
                .andExpect(jsonPath("$.email").value(account.getEmail()))
                .andExpect(jsonPath("$.password").value("pass"))
                .andExpect(jsonPath("$.username").value(account.getUsername()));
    }

    @Test
    void getAccountThrowsWithNoParams() throws Exception {
        Account account = new Account(
                1,
                "updatedName",
                "updatedUsername",
                "updatedPass",
                "updatedEmail",
                new Role(
                        1,
                        "role"
                )
        );
        mockMvc.perform(get("/api/account/get").with(csrf())
                        .with(user(account)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getAccountThrowsWithNotOwnAccount() throws Exception {
        Account account = new Account(
                1,
                "name",
                "username",
                "pass",
                "email",
                new Role(
                        1,
                        "role"
                )
        );
        mockMvc.perform(get("/api/account/get?accountId=2", 1).with(csrf())
                .with(user(account)))
                .andExpect(status().isForbidden());
    }

    @Test
    void addAccountWorks() throws Exception {

        given(accountService.saveAccount(any())).willReturn(new AuthRes("token", 1));

        mockMvc.perform(post("/api/account/add").with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                            "name": "seller",
                            "username": "sellerUsername",
                            "password": "SellerPass123",
                            "email": "sellerEmail",
                            "role": {
                                "id": 1
                            }
                        }
                        """)
                        .with(user(new Account())))

                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("token"))
                .andExpect(jsonPath("$.accountId").value(1));
    }

    @Test
    void addAccountThrowsWithNoItemGiven() throws Exception {
        mockMvc.perform(post("/api/account/add").with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("")
                        .with(user(new Account())))
                .andExpect(status().isBadRequest());
    }

    @Test
    void loginWorks() throws Exception {
        given(accountService.login(any())).willReturn(new AuthRes("token", 1));
        mockMvc.perform(post("/api/account/login").with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                            "username": "sellerUsername",
                            "password": "SellerPass123"
                        }
                        """)
                        .with(user(new Account())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("token"))
                .andExpect(jsonPath("$.accountId").value(1));
    }

    @Test
    void loginThrowsWithNoItem() throws Exception {
        given(accountService.login(any())).willReturn(new AuthRes("token", 1));
        mockMvc.perform(post("/api/account/login").with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("")
                        .with(user(new Account())))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateAccountWorks() throws Exception {
        Account account = new Account(
                1,
                "updatedName",
                "updatedUsername",
                "updatedPass",
                "updatedEmail",
                new Role(
                        1,
                        "role"
                )
        );

        given(accountService.updateAccount(anyInt(), any())).willReturn(true);

        mockMvc.perform(put("/api/account/update?accountId=1", 1).with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "name": "updatedName",
                            "username": "updatedUsername",
                            "password": "updatedPass",
                            "email": "updatedEmail",
                            "role": {
                                "id": 1
                            }
                        }
                        """)
                .with(user(account)))

                .andExpect(status().isOk());
    }
    @Test
    void updateAccountThrowsWithNoParams() throws Exception {
        Account account = new Account(
                1,
                "updatedName",
                "updatedUsername",
                "updatedPass",
                "updatedEmail",
                new Role(
                        1,
                        "role"
                )
        );

        mockMvc.perform(put("/api/account/update").with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                            "name": "updatedName",
                            "username": "updatedUsername",
                            "password": "updatedPass",
                            "email": "updatedEmail",
                            "role": {
                                "id": 1
                            }
                        }
                        """)
                        .with(user(account)))

                .andExpect(status().isBadRequest());
    }

    @Test
    void updateAccountThrowsWithNoContent() throws Exception {
        Account account = new Account(
                1,
                "updatedName",
                "updatedUsername",
                "updatedPass",
                "updatedEmail",
                new Role(
                        1,
                        "role"
                )
        );

        mockMvc.perform(put("/api/account/update?accountId=1", 1).with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content("")
                .with(user(account)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateAccountThrowsWithNotOwnAccount() throws Exception {
        Account account = new Account(
                1,
                "updatedName",
                "updatedUsername",
                "updatedPass",
                "updatedEmail",
                new Role(
                        1,
                        "role"
                )
        );

        mockMvc.perform(put("/api/account/update?accountId=2").with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                            "name": "updatedName",
                            "username": "updatedUsername",
                            "password": "updatedPass",
                            "email": "updatedEmail",
                            "role": {
                                "id": 1
                            }
                        }
                        """)
                        .with(user(account)))

                .andExpect(status().isForbidden());
    }

    @Test
    void deleteAccountWorks() throws Exception {
        Account account = new Account(
                1,
                "updatedName",
                "updatedUsername",
                "updatedPass",
                "updatedEmail",
                new Role(
                        1,
                        "role"
                )
        );
        given(accountService.deleteAccount(anyInt())).willReturn(true);
        mockMvc.perform(delete("/api/account/del?accountId=1", 1).with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .with(user(account)))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

    @Test
    void deleteAccountReturnsFalseIfFails() throws Exception {
        Account account = new Account(
                1,
                "updatedName",
                "updatedUsername",
                "updatedPass",
                "updatedEmail",
                new Role(
                        1,
                        "role"
                )
        );
        given(accountService.deleteAccount(anyInt())).willReturn(false);
        mockMvc.perform(delete("/api/account/del?accountId=1", 1).with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(user(account)))
                .andExpect(status().isOk())
                .andExpect(content().string("false"));
    }

    @Test
    void deleteAccountThrowsWithNoParams() throws Exception {
        Account account = new Account(
                1,
                "updatedName",
                "updatedUsername",
                "updatedPass",
                "updatedEmail",
                new Role(
                        1,
                        "role"
                )
        );
        given(accountService.deleteAccount(anyInt())).willReturn(true);
        mockMvc.perform(delete("/api/account/del").with(csrf())
                .with(user(account)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deleteAccountThrowsWithNotOwnAccount() throws Exception {
        Account account = new Account(
                1,
                "updatedName",
                "updatedUsername",
                "updatedPass",
                "updatedEmail",
                new Role(
                        1,
                        "role"
                )
        );
        given(accountService.deleteAccount(anyInt())).willReturn(true);
        mockMvc.perform(delete("/api/account/del?accountId=2").with(csrf())
                .with(user(account)))
                .andExpect(status().isForbidden());
    }
}
