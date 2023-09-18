package com.example.shopBackend.item;

import com.example.shopBackend.account.Account;
import com.example.shopBackend.category.Category;
import com.example.shopBackend.response.ListRes;
import com.example.shopBackend.role.Role;
import com.example.shopBackend.security.Authorization;
import com.example.shopBackend.words.Words;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(value=ItemController.class)
@ContextConfiguration(classes = {ItemController.class, Authorization.class})
@EnableMethodSecurity
class ItemControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    Authorization authorization;

    @MockBean
    ItemService itemService;

    @Test
    void addItemWorks() throws Exception {
        Account account = new Account(
                1,
                "name",
                "username",
                "pass",
                "email",
                new Role()
        );
        Category category = new Category("category");
        Words words = new Words(1, List.of("1"), List.of("1"));
        Item item = new Item(
                "test title",
                account,
                1,
                category,
                words
        );

        given(itemService.saveAllItems(any())).willReturn(List.of(item));
        given(authorization.addItemsAreOwn(any(), any())).willReturn(true);
        mockMvc.perform(post("/api/item/add").with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        [{
                            "title": "test title",
                            "Account": {
                                "id": 1
                            },
                            "category": {
                                "id": 1
                            },
                            "rating": "1",
                            "words": null,
                            "desc": "test desc"
                        }]
                        """)
                        .with(user(account)))

                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value(item.getTitle()))
                .andExpect(jsonPath("$[0].account.name").value(item.getAccount().getName()))
                .andExpect(jsonPath("$[0].category.name").value(item.getCategory().getName()))
                .andExpect(jsonPath("$[0].rating").value(item.getRating()))
                .andExpect(jsonPath("$[0].words.id").value(item.getWords().getId()));
    }

    @Test
    void addItemThrowsWithNoItemGiven() throws Exception {
        Account account = new Account(
                1,
                "name",
                "username",
                "pass",
                "email",
                new Role()
        );
        mockMvc.perform(post("/api/item/add").with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("")
                        .with(user(account)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void addItemThrowsWithNotOwnItem() throws Exception {
        Account account = new Account(
                1,
                "name",
                "username",
                "pass",
                "email",
                new Role()
        );
        mockMvc.perform(post("/api/item/add").with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        [{
                            "title": "test title",
                            "Account": {
                                "id": 2
                            },
                            "category": {
                                "id": 1
                            },
                            "rating": "1",
                            "words": null,
                            "desc": "test desc"
                        }]
                        """)
                        .with(user(account)))
                .andExpect(status().isForbidden());
    }

    @Test
    void getItemsForAccountWorks() throws Exception {
        Account account = new Account(
                1,
                "name",
                "username",
                "pass",
                "email",
                new Role()
        );
        Category category = new Category("category");
        Words words = new Words(1, List.of("1"), List.of("1"));
        ItemWithReviews item = new ItemWithReviews() {
            @Override
            public double getId() {
                return 0;
            }

            @Override
            public int getRating() {
                return 0;
            }

            @Override
            public String getTitle() {
                return null;
            }

            @Override
            public String getReviews() {
                return null;
            }
        };

        given(itemService.getItemsForAccount(anyInt(), anyInt(), any(), any())).willReturn(new ListRes(List.of(item), true));

        mockMvc.perform(get("/api/item/get?accountId=1&page=0&sort=none&sortDir=none", 1, 0).with(csrf())
                .with(user(account)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nextPage").value(true))
                .andExpect(jsonPath("$.responseList[0].title").value(item.getTitle()))
                .andExpect(jsonPath("$.responseList[0].rating").value(item.getRating()));
    }

    @Test
    void getItemsForAccountThrowsWithNoParams() throws Exception {
        Account account = new Account(
                1,
                "name",
                "username",
                "pass",
                "email",
                new Role()
        );
        ItemWithReviews item = new ItemWithReviews() {
            @Override
            public double getId() {
                return 0;
            }

            @Override
            public int getRating() {
                return 0;
            }

            @Override
            public String getTitle() {
                return null;
            }

            @Override
            public String getReviews() {
                return null;
            }
        };

        given(itemService.getItemsForAccount(anyInt(), anyInt(), any(), any())).willReturn(new ListRes(List.of(item), true));
        mockMvc.perform(get("/api/item/get").with(csrf())
                .with(user(account)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getItemsForAccountThrowsWithNotOwnItems() throws Exception {
        Account account = new Account(
                1,
                "name",
                "username",
                "pass",
                "email",
                new Role()
        );
        mockMvc.perform(get("/api/item/get?accountId=2&page=0&sort=none&sortDir=none", 2, 0).with(csrf())
                .with(user(account)))
                .andExpect(status().isForbidden());
    }

    @Test
    void getItemsWithTitleForAccountWorks() throws Exception {
        Account account = new Account(
                1,
                "name",
                "username",
                "pass",
                "email",
                new Role()
        );
        ItemWithReviews item = new ItemWithReviews() {
            @Override
            public double getId() {
                return 0;
            }

            @Override
            public int getRating() {
                return 0;
            }

            @Override
            public String getTitle() {
                return null;
            }

            @Override
            public String getReviews() {
                return null;
            }
        };

        given(itemService.getItemsForAccountWithTitleAndSorts(any(), anyInt(), anyInt(), any(), any())).willReturn(new ListRes(List.of(item), true));

        mockMvc.perform(get("/api/item/get/search?title=ti&accountId=1&page=0&sort=none&sortDir=none", 1, 0).with(csrf())
                .with(user(account)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nextPage").value(true))
                .andExpect(jsonPath("$.responseList[0].title").value(item.getTitle()))
                .andExpect(jsonPath("$.responseList[0].rating").value(item.getRating()));
    }

    @Test
    void getItemsForAccountWithTitleThrowsWithNoParams() throws Exception {
        Account account = new Account(
                1,
                "name",
                "username",
                "pass",
                "email",
                new Role()
        );
        Category category = new Category("category");
        Words words = new Words(1, List.of("1"), List.of("1"));
        ItemWithReviews item = new ItemWithReviews() {
            @Override
            public double getId() {
                return 0;
            }

            @Override
            public int getRating() {
                return 0;
            }

            @Override
            public String getTitle() {
                return null;
            }

            @Override
            public String getReviews() {
                return null;
            }
        };

        given(itemService.getItemsForAccount(anyInt(), anyInt(), any(), any())).willReturn(new ListRes(List.of(item), true));
        mockMvc.perform(get("/api/item/get/search").with(csrf())
                .with(user(account)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getItemsForAccountWithTitleThrowsWithNotOwnItem() throws Exception {
        Account account = new Account(
                1,
                "name",
                "username",
                "pass",
                "email",
                new Role()
        );
        Category category = new Category("category");
        Words words = new Words(1, List.of("1"), List.of("1"));

        mockMvc.perform(get("/api/item/get/search?title=ti&accountId=2&page=0&sort=none&sortDir=none", 2, 0).with(csrf())
                        .with(user(account)))
                .andExpect(status().isForbidden());
    }

    @Test
    void deleteItemWorks() throws Exception {
        Account account = new Account(
                1,
                "name",
                "username",
                "pass",
                "email",
                new Role()
        );
        given(authorization.isOwnItem(any(), anyInt())).willReturn(true);
        given(itemService.deleteItem(anyInt())).willReturn(true);
        mockMvc.perform(delete("/api/item/del?itemId=1", 1).with(csrf())
                .with(user(account))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

    @Test
    void deleteItemReturnsFalseIfFails() throws Exception {
        Account account = new Account(
                1,
                "name",
                "username",
                "pass",
                "email",
                new Role()
        );
        given(itemService.deleteItem(anyInt())).willReturn(false);
        given(authorization.isOwnItem(any(), anyInt())).willReturn(true);
        mockMvc.perform(delete("/api/item/del?itemId=1", 1).with(csrf())
                .with(user(account))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("false"));
    }

    @Test
    void deleteItemThrowsWithNoParams() throws Exception {
        Account account = new Account(
                1,
                "name",
                "username",
                "pass",
                "email",
                new Role()
        );
        given(itemService.deleteItem(anyInt())).willReturn(true);
        mockMvc.perform(delete("/api/item/del").with(csrf())
                .with(user(account)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deleteItemThrowsWithNotOwnItem() throws Exception {
        Account account = new Account(
                4,
                "name",
                "username",
                "pass",
                "email",
                new Role()
        );
        given(itemService.deleteItem(anyInt())).willReturn(true);
        given(authorization.isOwnItem(any(), anyInt())).willReturn(false);
        mockMvc.perform(delete("/api/item/del?itemId=1").with(csrf())
                        .with(user(account)))
                .andExpect(status().isForbidden());
    }

    @Test
    void updateItemWorks() throws Exception {
        Account account = new Account(
                1,
                "name",
                "username",
                "pass",
                "email",
                new Role()
        );
        Category category = new Category("category");
        Words words = new Words(1, List.of("1"), List.of("1"));
        Item item = new Item(
                "test title",
                account,
                1,
                category,
                words
        );

        given(itemService.updateItem(anyInt(), any())).willReturn(item);
        given(authorization.isOwnItem(any(), anyInt())).willReturn(true);
        mockMvc.perform(put("/api/item/update?itemId=1", 1).with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "title": "postTest 12",
                            "Account": {
                                "id": 1
                            },
                            "category": {
                                "id": 1
                            },
                            "rating": "1",
                            "words": null,
                            "desc": "test desc"
                        }""")
                .with(user(account)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value(item.getTitle()))
                .andExpect(jsonPath("$.account.name").value(item.getAccount().getName()))
                .andExpect(jsonPath("$.category.name").value(item.getCategory().getName()))
                .andExpect(jsonPath("$.rating").value(item.getRating()))
                .andExpect(jsonPath("$.words.id").value(item.getWords().getId()));
    }

    @Test
    void updateItemThrowsWithNoParams() throws Exception {
        Account account = new Account(
                1,
                "name",
                "username",
                "pass",
                "email",
                new Role()
        );
        mockMvc.perform(put("/api/item/update").with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "title": "postTest 12",
                            "Account": {
                                "id": 1
                            },
                            "category": {
                                "id": 1
                            },
                            "rating": "1",
                            "words": null,
                            "desc": "test desc"
                        }""")
                .with(user(account)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateItemThrowsWithNoContent() throws Exception {
        Account account = new Account(
                1,
                "name",
                "username",
                "pass",
                "email",
                new Role()
        );
        mockMvc.perform(put("/api/item/update?itemId=1").with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content("")
                .with(user(account)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateItemThrowsWithNotOwnItem() throws Exception {
        Account account = new Account(
                4,
                "name",
                "username",
                "pass",
                "email",
                new Role()
        );
        mockMvc.perform(put("/api/item/update?itemId=1").with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                            "title": "postTest 12",
                            "Account": {
                                "id": 1
                            },
                            "category": {
                                "id": 1
                            },
                            "rating": "1",
                            "words": null,
                            "desc": "test desc"
                        }""")
                        .with(user(account)))
                .andExpect(status().isForbidden());
    }
}
