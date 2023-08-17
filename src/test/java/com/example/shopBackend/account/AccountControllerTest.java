package com.example.shopBackend.account;


import com.example.shopBackend.role.Role;
import com.example.shopBackend.security.AuthRes;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(value =AccountController.class, excludeAutoConfiguration = {SecurityAutoConfiguration.class})
@ContextConfiguration(classes = AccountController.class)
class AccountControllerTest {
    @Autowired
    MockMvc mockMvc;

    @MockBean
    AccountService accountService;

    @Test
    void addAccountWorks() throws Exception {
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

        given(accountService.saveAccount(any())).willReturn(new AuthRes("token"));

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
                        """))

                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("token"));
    }

    @Test
    void addAccountThrowsWithNoItemGiven() throws Exception {
        mockMvc.perform(post("/api/account/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(""))
                .andExpect(status().isBadRequest());
    }

    @Test
    void loginWorks() throws Exception {
        given(accountService.login(any())).willReturn(new AuthRes("token"));
        mockMvc.perform(post("/api/account/login").with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                            "username": "sellerUsername",
                            "password": "SellerPass123"
                        }
                        """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("token"));
    }

    @Test
    void loginThrowsWithNoItem() throws Exception {
        given(accountService.login(any())).willReturn(new AuthRes("token"));
        mockMvc.perform(post("/api/account/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(""))
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

        given(accountService.updateAccount(anyInt(), any())).willReturn(account);

        mockMvc.perform(put("/api/account/update?accountId=1", 1)
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
                        """))

                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(account.getId()))
                .andExpect(jsonPath("$.name").value(account.getName()))
                .andExpect(jsonPath("$.username").value(account.getUsername()))
                .andExpect(jsonPath("$.password").value(account.getPassword()))
                .andExpect(jsonPath("$.email").value(account.getEmail()))
                .andExpect(jsonPath("$.role.name").value(account.getRole().getName()));
    }
    @Test
    void updateAccountThrowsWithNoParams() throws Exception {

        mockMvc.perform(put("/api/account/update")
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
                        """))

                .andExpect(status().isBadRequest());
    }

    @Test
    void updateAccountThrowsWithNoContent() throws Exception {

        mockMvc.perform(put("/api/account/update?accountId=1", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(""))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deleteAccountWorks() throws Exception {
        given(accountService.deleteAccount(anyInt())).willReturn(true);
        mockMvc.perform(delete("/api/account/del?accountId=1", 1)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

    @Test
    void deleteAccountReturnsFalseIfFails() throws Exception {
        given(accountService.deleteAccount(anyInt())).willReturn(false);
        mockMvc.perform(delete("/api/account/del?accountId=1", 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("false"));
    }

    @Test
    void deleteAccountThrowsWithNoParams() throws Exception {
        given(accountService.deleteAccount(anyInt())).willReturn(true);
        mockMvc.perform(delete("/api/account/del"))
                .andExpect(status().isBadRequest());
    }
}
