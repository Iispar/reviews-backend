package com.example.shopBackend.review;

import com.example.shopBackend.account.Account;
import com.example.shopBackend.category.Category;
import com.example.shopBackend.item.Item;
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

import java.sql.Date;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(value= ReviewController.class)
@ContextConfiguration(classes = {ReviewController.class, Authorization.class})
@EnableMethodSecurity
class ReviewControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    Authorization authorization;

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
                new Words()
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
        given(authorization.addReviewsAreOwn(any(), any())).willReturn(true);

        mockMvc.perform(post("/api/review/add").with(csrf())
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
                        """)
                        .with(user(account)))

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
        Account account = new Account(
                1,
                "name",
                "username",
                "pass",
                "email",
                new Role()
        );
        mockMvc.perform(post("/api/review/add").with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("")
                        .with(user(account)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void addReviewThrowsWithNotOwnItems() throws Exception {
        Account account = new Account(
                1,
                "name",
                "username",
                "pass",
                "email",
                new Role()
        );
        given(authorization.addReviewsAreOwn(any(), any())).willReturn(false);
        mockMvc.perform(post("/api/review/add").with(csrf())
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
                                "id": 4
                                },
                            "item": {
                                "id": 1
                            }
                        }]
                        """)
                        .with(user(account)))
                .andExpect(status().isForbidden());
    }

    @Test
    void getReviewsForAccountWorks() throws Exception {
        Item item = new Item(
                1,
                "test title",
                new Account(),
                1,
                new Category(),
                new Words()
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

        given(reviewService.getReviewsForAccount(anyInt(), anyInt(), any(), any())).willReturn(new ListRes(List.of(review), true));

        mockMvc.perform(get("/api/review/get/account?accountId=1&page=0", 1, 0).with(csrf())
                .with(user(account)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nextPage").value(true))
                .andExpect(jsonPath("$.responseList[0].title").value(review.getTitle()))
                .andExpect(jsonPath("$.responseList[0].body").value(review.getBody()))
                .andExpect(jsonPath("$.responseList[0].date").value("1970-01-01"))
                .andExpect(jsonPath("$.responseList[0].rating").value(review.getRating()))
                .andExpect(jsonPath("$.responseList[0].likes").value(review.getLikes()))
                .andExpect(jsonPath("$.responseList[0].dislikes").value(review.getDislikes()))
                .andExpect(jsonPath("$.responseList[0].account.id").value(review.getAccount().getId()))
                .andExpect(jsonPath("$.responseList[0].item.id").value(review.getItem().getId()));
    }

    @Test
    void getReviewsForAccountThrowsWithNoParams() throws Exception {
        Item item = new Item(
                1,
                "test title",
                new Account(),
                1,
                new Category(),
                new Words()
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

        given(reviewService.getReviewsForAccount(anyInt(), anyInt(), any(), any())).willReturn(new ListRes(List.of(review), true));
        mockMvc.perform(get("/api/review/get/account").with(csrf())
                .with(user(account)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getReviewsForAccountThrowsWithNotOwnReviews() throws Exception {

        Account account = new Account(
                1,
                "name",
                "username",
                "pass",
                "email",
                new Role()
        );

        mockMvc.perform(get("/api/review/get/account?accountId=2&page=0").with(csrf())
                        .with(user(account)))
                .andExpect(status().isForbidden());
    }

    @Test
    void getReviewsForItemWorks() throws Exception {
        Item item = new Item(
                1,
                "test title",
                new Account(),
                1,
                new Category(),
                new Words()
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

        given(reviewService.getReviewsForItem(anyInt(), anyInt(), any(), any())).willReturn(new ListRes(List.of(review), true));
        given(authorization.isOwnItem(any(), anyInt())).willReturn(true);

        mockMvc.perform(get("/api/review/get/item?itemId=1&page=0&sort=review_date&sortDir=desc", 1, 0).with(csrf())
                .with(user(account)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nextPage").value(true))
                .andExpect(jsonPath("$.responseList[0].title").value(review.getTitle()))
                .andExpect(jsonPath("$.responseList[0].body").value(review.getBody()))
                .andExpect(jsonPath("$.responseList[0].date").value("1970-01-01"))
                .andExpect(jsonPath("$.responseList[0].rating").value(review.getRating()))
                .andExpect(jsonPath("$.responseList[0].likes").value(review.getLikes()))
                .andExpect(jsonPath("$.responseList[0].dislikes").value(review.getDislikes()))
                .andExpect(jsonPath("$.responseList[0].account.id").value(review.getAccount().getId()))
                .andExpect(jsonPath("$.responseList[0].item.id").value(review.getItem().getId()));
    }

    @Test
    void getReviewsForItemThrowsWithNoParams() throws Exception {
        Item item = new Item(
                1,
                "test title",
                new Account(),
                1,
                new Category(),
                new Words()

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

        given(reviewService.getReviewsForItem(anyInt(), anyInt(), any(), any())).willReturn(new ListRes(List.of(review), true));
        mockMvc.perform(get("/api/review/get/item").with(csrf())
                .with(user(account)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getReviewsForItemThrowsWithNotOwnItem() throws Exception {

        Account account = new Account(
                1,
                "name",
                "username",
                "pass",
                "email",
                new Role()
        );

        given(authorization.isOwnItem(any(), anyInt())).willReturn(false);

        mockMvc.perform(get("/api/review/get/item?itemId=1&page=0&sort=review_date&sortDir=desc").with(csrf())
                        .with(user(account)))
                .andExpect(status().isForbidden());
    }

    @Test
    void deleteReviewWorks() throws Exception {
        Account account = new Account(
                1,
                "name",
                "username",
                "pass",
                "email",
                new Role()
        );

        given(authorization.isOwnReview(any(), anyInt())).willReturn(true);
        given(reviewService.deleteReview(anyInt())).willReturn(true);
        mockMvc.perform(delete("/api/review/del?reviewId=1", 1).with(csrf())
                        .with(user(account))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

    @Test
    void deleteReviewReturnsFalseIfFails() throws Exception {
        Account account = new Account(
                1,
                "name",
                "username",
                "pass",
                "email",
                new Role()
        );
        given(reviewService.deleteReview(anyInt())).willReturn(false);
        given(authorization.isOwnReview(any(), anyInt())).willReturn(true);
        mockMvc.perform(delete("/api/review/del?reviewId=1", 1).with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(user(account)))
                .andExpect(status().isOk())
                .andExpect(content().string("false"));
    }

    @Test
    void deleteReviewThrowsWithNoParams() throws Exception {
        Account account = new Account(
                1,
                "name",
                "username",
                "pass",
                "email",
                new Role()
        );
        given(reviewService.deleteReview(anyInt())).willReturn(true);
        mockMvc.perform(delete("/api/review/del").with(csrf())
                        .with(user(account)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deleteReviewThrowsWithNotOwnItem() throws Exception {
        Account account = new Account(
                1,
                "name",
                "username",
                "pass",
                "email",
                new Role()
        );

        given(authorization.isOwnReview(any(), anyInt())).willReturn(false);
        given(reviewService.deleteReview(anyInt())).willReturn(true);
        mockMvc.perform(delete("/api/review/del?reviewId=2").with(csrf())
                        .with(user(account)))
                .andExpect(status().isForbidden());
    }

    @Test
    void getChartForAccountWorks() throws Exception {
        Account account = new Account(
                1,
                "name",
                "username",
                "pass",
                "email",
                new Role()
        );

        Chart chart = new Chart() {
            @Override
            public double getRating() {
                return 1;
            }

            @Override
            public int getCount() {
                return 2;
            }

            @Override
            public String getTime() {
                return "2";
            }
            @Override
            public String getTimeYear() {
                return "2";
            }
        };

        given(reviewService.getChartForAccount(any(), anyInt())).willReturn(List.of(chart));

        mockMvc.perform(get("/api/review/get/chart/account?accountId=1&time=week", 1, 0).with(csrf())
                        .with(user(account)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].count").value(chart.getCount()))
                .andExpect(jsonPath("$[0].rating").value(chart.getRating()));
    }

    @Test
    void getChartForAccountThrowsWithNoParams() throws Exception {
        Account account = new Account(
                1,
                "name",
                "username",
                "pass",
                "email",
                new Role()
        );
        Chart chart = new Chart() {
            @Override
            public double getRating() {
                return 1;
            }

            @Override
            public int getCount() {
                return 2;
            }

            @Override
            public String getTime() {
                return "2";
            }

            @Override
            public String getTimeYear() {
                return "2";
            }
        };

        given(reviewService.getChartForAccount(any(), anyInt())).willReturn(List.of(chart));

        mockMvc.perform(get("/api/review/get/chart/account", 1, 0).with(csrf())
                .with(user(account)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getChartForAccountThrowsWithNotOwnChart() throws Exception {
        Account account = new Account(
                1,
                "name",
                "username",
                "pass",
                "email",
                new Role()
        );

        mockMvc.perform(get("/api/review/get/chart/account?accountId=2", 1, 0).with(csrf())
                        .with(user(account)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getChartForItemWorks() throws Exception {
        Account account = new Account(
                1,
                "name",
                "username",
                "pass",
                "email",
                new Role()
        );
        Chart chart = new Chart() {
            @Override
            public double getRating() {
                return 1;
            }

            @Override
            public int getCount() {
                return 2;
            }

            @Override
            public String getTime() {
                return "2";
            }

            @Override
            public String getTimeYear() {
                return "2";
            }
        };

        given(reviewService.getChartForItem(any(), anyInt())).willReturn(List.of(chart));
        given(authorization.isOwnItem(any(), anyInt())).willReturn(true);

        mockMvc.perform(get("/api/review/get/chart/item?itemId=1&time=week", 1, 0).with(csrf())
                        .with(user(account)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].count").value(chart.getCount()))
                .andExpect(jsonPath("$[0].rating").value(chart.getRating()));
    }

    @Test
    void getChartForItemThrowsWithNoParams() throws Exception {
        Account account = new Account(
                1,
                "name",
                "username",
                "pass",
                "email",
                new Role()
        );
        Chart chart = new Chart() {
            @Override
            public double getRating() {
                return 1;
            }

            @Override
            public int getCount() {
                return 2;
            }

            @Override
            public String getTime() {
                return "2";
            }

            @Override
            public String getTimeYear() {
                return "2";
            }
        };

        given(reviewService.getChartForItem(any(), anyInt())).willReturn(List.of(chart));

        mockMvc.perform(get("/api/review/get/chart/item", 1, 0).with(csrf())
                .with(user(account)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getChartForItemThrowsWithNotOwnItem() throws Exception {
        Account account = new Account(
                1,
                "name",
                "username",
                "pass",
                "email",
                new Role()
        );

        given(authorization.isOwnItem(any(), anyInt())).willReturn(false);

        mockMvc.perform(get("/api/review/get/chart/item?itemId=1&time=week", 1, 0).with(csrf())
                        .with(user(account)))
                .andExpect(status().isForbidden());
    }

    @Test
    void getReviewsWithSearchForItemWorks() throws Exception {
        Item item = new Item(
                1,
                "test title",
                new Account(),
                1,
                new Category(),
                new Words()
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

        given(reviewService.getReviewsWithSearchForItem(any(), anyInt(), anyInt(), any(), any())).willReturn(new ListRes(List.of(review), true));
        given(authorization.isOwnItem(any(), anyInt())).willReturn(true);


        mockMvc.perform(get("/api/review/get/search?search=ti&itemId=4&sort=review_date&sortDir=asc&page=0", "test", 4, "review_date", "asc", 0).with(csrf())
                .with(user(account)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nextPage").value(true))
                .andExpect(jsonPath("$.responseList[0].title").value(review.getTitle()))
                .andExpect(jsonPath("$.responseList[0].body").value(review.getBody()))
                .andExpect(jsonPath("$.responseList[0].date").value("1970-01-01"))
                .andExpect(jsonPath("$.responseList[0].rating").value(review.getRating()))
                .andExpect(jsonPath("$.responseList[0].likes").value(review.getLikes()))
                .andExpect(jsonPath("$.responseList[0].dislikes").value(review.getDislikes()))
                .andExpect(jsonPath("$.responseList[0].account.id").value(review.getAccount().getId()))
                .andExpect(jsonPath("$.responseList[0].item.id").value(review.getItem().getId()));
    }

    @Test
    void getReviewsWithSearchForItemThrowsWithNoParams() throws Exception {
        Item item = new Item(
                1,
                "test title",
                new Account(),
                1,
                new Category(),
                new Words()
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

        given(reviewService.getReviewsWithSearchForItem(any(), anyInt(), anyInt(), any(), any())).willReturn(new ListRes(List.of(review), true));


        mockMvc.perform(get("/api/review/get/search", "test", 4, "review_date", "asc", 0).with(csrf())
                .with(user(account)))
                .andExpect(status().isBadRequest());
    }
    @Test
    void getReviewsWithSearchForItemThrowsWithNotOwnItem() throws Exception {

        Account account = new Account(
                1,
                "name",
                "username",
                "pass",
                "email",
                new Role()
        );

        given(authorization.isOwnItem(any(), anyInt())).willReturn(false);

        mockMvc.perform(get("/api/review/get/search?search=ti&itemId=4&sort=review_date&sortDir=asc&page=0", "test", 4, "review_date", "asc", 0).with(csrf())
                        .with(user(account)))
                .andExpect(status().isForbidden());
    }
}
