package com.example.shopBackend.user;

import com.example.shopBackend.role.Role;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
public class UserControllerTest {
    @Autowired
    MockMvc mockMvc;

    @MockBean
    UserService userService;

    @Test
    void addUserWorks() throws Exception {
        User user = new User(
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

        given(userService.saveAllUsers(any())).willReturn(List.of(user));

        mockMvc.perform(post("/api/user/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        [{
                            "name": "seller",
                            "username": "sellerUsername",
                            "password": "SellerPass123",
                            "email": "sellerEmail",
                            "role": {
                                "id": 1
                            }
                        }]
                        """))

                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(user.getId()))
                .andExpect(jsonPath("$[0].name").value(user.getName()))
                .andExpect(jsonPath("$[0].username").value(user.getUsername()))
                .andExpect(jsonPath("$[0].password").value(user.getPassword()))
                .andExpect(jsonPath("$[0].email").value(user.getEmail()))
                .andExpect(jsonPath("$[0].role.name").value(user.getRole().getName()));
    }

    @Test
    void addUserThrowsWithNoItemGiven() throws Exception {
        mockMvc.perform(post("/api/user/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateUserWorks() throws Exception {
        User user = new User(
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

        given(userService.updateUser(anyInt(), any())).willReturn(user);

        mockMvc.perform(put("/api/user/update?userId=1", 1)
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
                .andExpect(jsonPath("$.id").value(user.getId()))
                .andExpect(jsonPath("$.name").value(user.getName()))
                .andExpect(jsonPath("$.username").value(user.getUsername()))
                .andExpect(jsonPath("$.password").value(user.getPassword()))
                .andExpect(jsonPath("$.email").value(user.getEmail()))
                .andExpect(jsonPath("$.role.name").value(user.getRole().getName()));
    }
    @Test
    void updateUserThrowsWithNoParams() throws Exception {

        mockMvc.perform(put("/api/user/update")
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
    void updateUserThrowsWithNoContent() throws Exception {

        mockMvc.perform(put("/api/user/update?userId=1", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(""))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deleteUserWorks() throws Exception {
        given(userService.deleteUser(anyInt())).willReturn(true);
        mockMvc.perform(delete("/api/user/del?userId=1", 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

    @Test
    void deleteUserReturnsFalseIfFails() throws Exception {
        given(userService.deleteUser(anyInt())).willReturn(false);
        mockMvc.perform(delete("/api/user/del?userId=1", 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("false"));
    }

    @Test
    void deleteUserThrowsWithNoParams() throws Exception {
        given(userService.deleteUser(anyInt())).willReturn(true);
        mockMvc.perform(delete("/api/user/del"))
                .andExpect(status().isBadRequest());
    }
}
