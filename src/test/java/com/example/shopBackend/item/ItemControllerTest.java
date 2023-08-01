package com.example.shopBackend.item;

import com.example.shopBackend.category.Category;
import com.example.shopBackend.role.Role;
import com.example.shopBackend.user.User;
import com.example.shopBackend.words.Words;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ItemController.class)
class ItemControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    ItemService itemService;

    @Test
    void addItemWorks() throws Exception {
        User user = new User(
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
                user,
                1,
                category,
                words,
                "test desc"

        );

        given(itemService.saveAllItems(any())).willReturn(List.of(item));

        mockMvc.perform(post("/api/item/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        [{
                            "title": "test title",
                            "user": {
                                "id": 1
                            },
                            "category": {
                                "id": 1
                            },
                            "rating": "1",
                            "words": null,
                            "desc": "test desc"
                        }]
                        """))

                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("test title"))
                .andExpect(jsonPath("$[0].user.name").value("name"))
                .andExpect(jsonPath("$[0].category.name").value("category"))
                .andExpect(jsonPath("$[0].rating").value(1))
                .andExpect(jsonPath("$[0].words.id").value(1))
                .andExpect(jsonPath("$[0].desc").value("test desc"));
    }

    @Test
    void addItemThrowsWithNoItemGiven() throws Exception {
        mockMvc.perform(post("/api/item/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getItemsForUserWorks() throws Exception {
        User user = new User(
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
                user,
                1,
                category,
                words,
                "test desc"

        );

        given(itemService.getItemsForUser(anyInt(), anyInt(), any(), any())).willReturn(List.of(item));

        mockMvc.perform(get("/api/item/get?userId=1&page=0", 1, 0))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("test title"))
                .andExpect(jsonPath("$[0].user.name").value("name"))
                .andExpect(jsonPath("$[0].rating").value(1))
                .andExpect(jsonPath("$[0].category.name").value("category"))
                .andExpect(jsonPath("$[0].words.id").value(1))
                .andExpect(jsonPath("$[0].desc").value("test desc"));
    }

    @Test
    void getItemsForUserThrowsWithNoParams() throws Exception {
        User user = new User(
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
                user,
                1,
                category,
                words,
                "test desc"

        );

        given(itemService.getItemsForUser(anyInt(), anyInt(), any(), any())).willReturn(List.of(item));
        mockMvc.perform(get("/api/item/get"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getItemsForUserThrowsWithNoResults() throws Exception {
        given(itemService.getItemsForUser(anyInt(), anyInt(), any(), any())).willReturn(new ArrayList<>());
        mockMvc.perform(get("/api/item/get?userId=1&page=0", 1, 0)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest())
            .andExpect(content().string("found no reviews with user id"));
    }

    @Test
    void deleteItemWorks() throws Exception {
        given(itemService.deleteItem(anyInt())).willReturn(true);
        mockMvc.perform(delete("/api/item/del?itemId=1", 1)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

    @Test
    void deleteItemReturnsFalseIfFails() throws Exception {
        given(itemService.deleteItem(anyInt())).willReturn(false);
        mockMvc.perform(delete("/api/item/del?itemId=1", 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("false"));
    }

    @Test
    void deleteItemThrowsWithNoParams() throws Exception {
        given(itemService.deleteItem(anyInt())).willReturn(false);
        mockMvc.perform(delete("/api/item/del"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateItemWorks() throws Exception {
        User user = new User(
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
                user,
                1,
                category,
                words,
                "test desc"

        );

        given(itemService.updateItem(anyInt(), any())).willReturn(item);
        mockMvc.perform(put("/api/item/update?itemId=1", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "title": "postTest 12",
                            "user": {
                                "id": 1
                            },
                            "category": {
                                "id": 1
                            },
                            "rating": "1",
                            "words": null,
                            "desc": "test desc"
                        }"""))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("test title"))
                .andExpect(jsonPath("$.user.name").value("name"))
                .andExpect(jsonPath("$.category.name").value("category"))
                .andExpect(jsonPath("$.rating").value(1))
                .andExpect(jsonPath("$.words.id").value(1))
                .andExpect(jsonPath("$.desc").value("test desc"));
    }

    @Test
    void updateItemThrowsWithNoParams() throws Exception {
        mockMvc.perform(put("/api/item/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "title": "postTest 12",
                            "user": {
                                "id": 1
                            },
                            "category": {
                                "id": 1
                            },
                            "rating": "1",
                            "words": null,
                            "desc": "test desc"
                        }"""))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateItemThrowsWithNoContent() throws Exception {
        mockMvc.perform(put("/api/item/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().isBadRequest());
    }
}
