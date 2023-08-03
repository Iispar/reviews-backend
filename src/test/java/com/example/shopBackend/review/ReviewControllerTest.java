package com.example.shopBackend.review;

import com.example.shopBackend.account.Account;
import com.example.shopBackend.category.Category;
import com.example.shopBackend.item.Item;
import com.example.shopBackend.role.Role;
import com.example.shopBackend.words.Words;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ReviewController.class)
class ReviewControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    ReviewService reviewService;

    @Test
    void addReviewWorks() throws Exception {
        Item item = new Item(
                1,
                "test title",
                new Account(),
                1,
                new Category(),
                new Words(),
                "test desc"

        );

        Account account = new Account(
                1,
                "name",
                "username",
                "pass",
                "email",
                new Role()
        );

        Review review = new Review(
                new Date(3),
                "body",
                "title",
                5,
                3,
                account,
                3,
                item
        );

        given(reviewService.saveAllReviews(any())).willReturn(List.of(review));

        mockMvc.perform(post("/api/review/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        [{
                            "title": "title",
                            "body": "body",
                            "date": "2020-03-15",
                            "rating": 3,
                            "likes":5,
                            "dislikes": 3,
                            "Account": {
                                "id": 1
                                },
                            "item": {
                                "id": 1
                            }
                        }]
                        """))

                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value(review.getTitle()))
                .andExpect(jsonPath("$[0].body").value(review.getBody()))
                .andExpect(jsonPath("$[0].date").value("1970-01-01"))
                .andExpect(jsonPath("$[0].rating").value(review.getRating()))
                .andExpect(jsonPath("$[0].likes").value(review.getLikes()))
                .andExpect(jsonPath("$[0].dislikes").value(review.getDislikes()))
                .andExpect(jsonPath("$[0].account.id").value(review.getAccount().getId()))
                .andExpect(jsonPath("$[0].item.id").value(review.getItem().getId()));
    }

    @Test
    void addReviewThrowsWithNoReviewGiven() throws Exception {
        mockMvc.perform(post("/api/review/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(""))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getReviewsForAccountWorks() throws Exception {
        Item item = new Item(
                1,
                "test title",
                new Account(),
                1,
                new Category(),
                new Words(),
                "test desc"

        );

        Account account = new Account(
                1,
                "name",
                "username",
                "pass",
                "email",
                new Role()
        );

        Review review = new Review(
                new Date(3),
                "body",
                "title",
                5,
                3,
                account,
                3,
                item
        );

        given(reviewService.getReviewsForAccount(anyInt(), anyInt(), any(), any())).willReturn(List.of(review));

        mockMvc.perform(get("/api/review/get/account?accountId=1&page=0", 1, 0))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value(review.getTitle()))
                .andExpect(jsonPath("$[0].body").value(review.getBody()))
                .andExpect(jsonPath("$[0].date").value("1970-01-01"))
                .andExpect(jsonPath("$[0].rating").value(review.getRating()))
                .andExpect(jsonPath("$[0].likes").value(review.getLikes()))
                .andExpect(jsonPath("$[0].dislikes").value(review.getDislikes()))
                .andExpect(jsonPath("$[0].account.id").value(review.getAccount().getId()))
                .andExpect(jsonPath("$[0].item.id").value(review.getItem().getId()));
    }

    @Test
    void getReviewsForAccountThrowsWithNoParams() throws Exception {
        Item item = new Item(
                1,
                "test title",
                new Account(),
                1,
                new Category(),
                new Words(),
                "test desc"

        );

        Account account = new Account(
                1,
                "name",
                "username",
                "pass",
                "email",
                new Role()
        );

        Review review = new Review(
                new Date(3),
                "body",
                "title",
                5,
                3,
                account,
                3,
                item
        );

        given(reviewService.getReviewsForAccount(anyInt(), anyInt(), any(), any())).willReturn(List.of(review));
        mockMvc.perform(get("/api/review/get/account"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getReviewsForAccountThrowsWithNoResults() throws Exception {
        given(reviewService.getReviewsForAccount(anyInt(), anyInt(), any(), any())).willReturn(new ArrayList<>());
        mockMvc.perform(get("/api/review/get/account?accountId=1&page=0", 1, 0)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("found no reviews with Account id"));
    }

    @Test
    void getReviewsForItemWorks() throws Exception {
        Item item = new Item(
                1,
                "test title",
                new Account(),
                1,
                new Category(),
                new Words(),
                "test desc"

        );

        Account account = new Account(
                1,
                "name",
                "username",
                "pass",
                "email",
                new Role()
        );

        Review review = new Review(
                new Date(3),
                "body",
                "title",
                5,
                3,
                account,
                3,
                item
        );

        given(reviewService.getReviewsForItem(anyInt(), anyInt(), any(), any())).willReturn(List.of(review));

        mockMvc.perform(get("/api/review/get/item?itemId=1&page=0&sort=review_date&sortDir=desc", 1, 0))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value(review.getTitle()))
                .andExpect(jsonPath("$[0].body").value(review.getBody()))
                .andExpect(jsonPath("$[0].date").value("1970-01-01"))
                .andExpect(jsonPath("$[0].rating").value(review.getRating()))
                .andExpect(jsonPath("$[0].likes").value(review.getLikes()))
                .andExpect(jsonPath("$[0].dislikes").value(review.getDislikes()))
                .andExpect(jsonPath("$[0].account.id").value(review.getAccount().getId()))
                .andExpect(jsonPath("$[0].item.id").value(review.getItem().getId()));
    }

    @Test
    void getReviewsForItemThrowsWithNoParams() throws Exception {
        Item item = new Item(
                1,
                "test title",
                new Account(),
                1,
                new Category(),
                new Words(),
                "test desc"

        );

        Account account = new Account(
                1,
                "name",
                "username",
                "pass",
                "email",
                new Role()
        );

        Review review = new Review(
                new Date(3),
                "body",
                "title",
                5,
                3,
                account,
                3,
                item
        );

        given(reviewService.getReviewsForItem(anyInt(), anyInt(), any(), any())).willReturn(List.of(review));
        mockMvc.perform(get("/api/review/get/item"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getReviewsForItemThrowsWithNoResults() throws Exception {
        given(reviewService.getReviewsForAccount(anyInt(), anyInt(), any(), any())).willReturn(new ArrayList<>());
        mockMvc.perform(get("/api/review/get/item?itemId=1&page=0&sort=review_date&sortDir=desc", 1, 0)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("found no reviews with item id"));
    }

    @Test
    void deleteReviewWorks() throws Exception {
        given(reviewService.deleteReview(anyInt())).willReturn(true);
        mockMvc.perform(delete("/api/review/del?reviewId=1", 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

    @Test
    void deleteReviewReturnsFalseIfFails() throws Exception {
        given(reviewService.deleteReview(anyInt())).willReturn(false);
        mockMvc.perform(delete("/api/review/del?reviewId=1", 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("false"));
    }

    @Test
    void deleteReviewThrowsWithNoParams() throws Exception {
        given(reviewService.deleteReview(anyInt())).willReturn(true);
        mockMvc.perform(delete("/api/review/del"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getChartForAccountWorks() throws Exception {
        Chart chart = new Chart() {
            @Override
            public int getRating() {
                return 1;
            }

            @Override
            public int getCount() {
                return 2;
            }
        };

        given(reviewService.getChartForAccount(any(), anyInt())).willReturn(List.of(chart));

        mockMvc.perform(get("/api/review/get/chart/account?accountId=1&time=week", 1, 0))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].count").value(chart.getCount()))
                .andExpect(jsonPath("$[0].rating").value(chart.getRating()));
    }

    @Test
    void getChartForAccountThrowsWithNoParams() throws Exception {
        Chart chart = new Chart() {
            @Override
            public int getRating() {
                return 1;
            }

            @Override
            public int getCount() {
                return 2;
            }
        };

        given(reviewService.getChartForAccount(any(), anyInt())).willReturn(List.of(chart));

        mockMvc.perform(get("/api/review/get/chart/account", 1, 0))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getChartForItemWorks() throws Exception {
        Chart chart = new Chart() {
            @Override
            public int getRating() {
                return 1;
            }

            @Override
            public int getCount() {
                return 2;
            }
        };

        given(reviewService.getChartForItem(any(), anyInt())).willReturn(List.of(chart));

        mockMvc.perform(get("/api/review/get/chart/item?itemId=1&time=week", 1, 0))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].count").value(chart.getCount()))
                .andExpect(jsonPath("$[0].rating").value(chart.getRating()));
    }

    @Test
    void getChartForItemThrowsWithNoParams() throws Exception {
        Chart chart = new Chart() {
            @Override
            public int getRating() {
                return 1;
            }

            @Override
            public int getCount() {
                return 2;
            }
        };

        given(reviewService.getChartForItem(any(), anyInt())).willReturn(List.of(chart));

        mockMvc.perform(get("/api/review/get/chart/item", 1, 0))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getReviewsWithTitleForItemWorks() throws Exception {
        Item item = new Item(
                1,
                "test title",
                new Account(),
                1,
                new Category(),
                new Words(),
                "test desc"

        );

        Account account = new Account(
                1,
                "name",
                "username",
                "pass",
                "email",
                new Role()
        );

        Review review = new Review(
                new Date(3),
                "body",
                "title",
                5,
                3,
                account,
                3,
                item
        );

        given(reviewService.getReviewsWithTitleForItem(any(), anyInt(), anyInt(), any(), any())).willReturn(List.of(review));


        mockMvc.perform(get("/api/review/get/search?title=test&itemId=4&sort=review_date&sortDir=asc&page=0", "test", 4, "review_date", "asc", 0))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value(review.getTitle()))
                .andExpect(jsonPath("$[0].body").value(review.getBody()))
                .andExpect(jsonPath("$[0].date").value("1970-01-01"))
                .andExpect(jsonPath("$[0].rating").value(review.getRating()))
                .andExpect(jsonPath("$[0].likes").value(review.getLikes()))
                .andExpect(jsonPath("$[0].dislikes").value(review.getDislikes()))
                .andExpect(jsonPath("$[0].account.id").value(review.getAccount().getId()))
                .andExpect(jsonPath("$[0].item.id").value(review.getItem().getId()));
    }

    @Test
    void getReviewsWithTitleForItemThrowsWithNoParams() throws Exception {
        Item item = new Item(
                1,
                "test title",
                new Account(),
                1,
                new Category(),
                new Words(),
                "test desc"

        );

        Account account = new Account(
                1,
                "name",
                "username",
                "pass",
                "email",
                new Role()
        );

        Review review = new Review(
                new Date(3),
                "body",
                "title",
                5,
                3,
                account,
                3,
                item
        );

        given(reviewService.getReviewsWithTitleForItem(any(), anyInt(), anyInt(), any(), any())).willReturn(List.of(review));


        mockMvc.perform(get("/api/review/get/search", "test", 4, "review_date", "asc", 0))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getReviewsWithTitleForItemThrowsWithNoResults() throws Exception {
        given(reviewService.getReviewsWithTitleForItem(any(), anyInt(), anyInt(), any(), any())).willReturn(new ArrayList<>());
        mockMvc.perform(get("/api/review/get/search?title=test&itemId=4&sort=review_date&sortDir=asc&page=0", "test", 4, "review_date", "asc", 0)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("found no reviews with item id and title"));
    }
}
