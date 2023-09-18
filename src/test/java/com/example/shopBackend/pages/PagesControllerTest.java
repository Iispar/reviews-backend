package com.example.shopBackend.pages;

import com.example.shopBackend.account.Account;
import com.example.shopBackend.category.Category;
import com.example.shopBackend.item.Item;
import com.example.shopBackend.response.ListRes;
import com.example.shopBackend.review.BarChart;
import com.example.shopBackend.review.Chart;
import com.example.shopBackend.review.Review;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(value= PagesController.class)
@ContextConfiguration(classes = {PagesController.class, Authorization.class})
@EnableMethodSecurity
class PagesControllerTest {
    @Autowired
    MockMvc mockMvc;

    @MockBean
    Authorization authorization;

    @MockBean
    PagesService pagesService;

    @Test
    void getHomePageForAccountWorks() throws Exception {
        Chart chart = new Chart() {

            @Override
            public double getRating() {
                return 1;
            }

            @Override
            public int getCount() {
                return 1;
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
        BarChart barChart = new BarChart() {

            @Override
            public int getRating() {
                return 1;
            }

            @Override
            public double getCount() {
                return 1;
            }
        };
        String title="title";
        Homepage homepage = new Homepage(
                "test",
                new ListRes(List.of(new Review(
                        new Date(9),
                        "body",
                        title,
                        5,
                        5,
                        new Account(),
                        4,
                        new Item()
                )), true),
                List.of(new Item(
                        1,
                        "title",
                        new Account(),
                        2F,
                        new Category(),
                        new Words()
                )),
                2.3F,
                2,
                2,
                List.of(barChart),
                List.of(chart)
        );

        Account account = new Account(1, "test", "test", "test", "test", new Role());
        given(pagesService.getHomepageForAccount(anyInt())).willReturn(homepage);

        mockMvc.perform(get("/api/pages/get/home?accountId=1", 1).with(csrf())
                .with(user(account)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.latestReviews.responseList[0].title").value(title))
                .andExpect(jsonPath("$.ratingsAvg").value(homepage.getRatingsAvg()))
                .andExpect(jsonPath("$.itemsCount").value(homepage.getItemsCount()))
                .andExpect(jsonPath("$.reviewsCount").value(homepage.getReviewsCount()))
                .andExpect(jsonPath("$.chart[0].count").value(homepage.getChart().get(0).getCount()));
    }

    @Test
    void getHomePageForAccountThrowsWithNoParams() throws Exception {
        Account account = new Account(1, "test", "test", "test", "test", new Role());

        mockMvc.perform(get("/api/pages/get/home").with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .with(user(account)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getHomePageForAccountThrowsWithNotOwnHomepage() throws Exception {
        Account account = new Account(1, "test", "test", "test", "test", new Role());

        mockMvc.perform(get("/api/pages/get/home?accountId=2").with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(user(account)))
                .andExpect(status().isForbidden());
    }

    @Test
    void getItemPageForItemWorks() throws Exception {
        Chart chart = new Chart() {

            @Override
            public double getRating() {
                return 1;
            }

            @Override
            public int getCount() {
                return 1;
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

        String title = "title";
        ItemPage itempage = new ItemPage(
                "title",
                4,
                2,
                2,
                4,
                new ListRes(List.of(new Review(
                        new Date(9),
                        "body",
                        title,
                        5,
                        5,
                        new Account(),
                        4,
                        new Item()
                )), true),
                List.of(chart),
                List.of("pos"),
                List.of("neg")
        );

        Account account = new Account(1, "test", "test", "test", "test", new Role());
        given(authorization.isOwnItem(any(), anyInt())).willReturn(true);
        given(pagesService.getItemPageForItem(anyInt())).willReturn(itempage);

        mockMvc.perform(get("/api/pages/get/item?itemId=1", 1).with(csrf())
                .with(user(account)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.reviews.responseList[0].title").value(title))
                .andExpect(jsonPath("$.chart[0].count").value(itempage.getChart().get(0).getCount()))
                .andExpect(jsonPath("$.topPos[0]").value(itempage.getTopPos().get(0)))
                .andExpect(jsonPath("$.title").value(itempage.getTitle()))
                .andExpect(jsonPath("$.topNeg[0]").value(itempage.getTopNeg().get(0)))
                .andExpect(jsonPath("$.reviewsCount").value(itempage.getReviewsCount()))
                .andExpect(jsonPath("$.rating").value(itempage.getRating()));
    }

    @Test
    void getItemPageForItemThrowsWithNoParams() throws Exception {
        Account account = new Account(1, "test", "test", "test", "test", new Role());

        mockMvc.perform(get("/api/pages/get/item").with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .with(user(account)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getItemPageForItemThrowsWithNotOwnItem() throws Exception {
        Account account = new Account(1, "test", "test", "test", "test", new Role());
        given(authorization.isOwnItem(any(), anyInt())).willReturn(false);
        mockMvc.perform(get("/api/pages/get/item?itemId=2").with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .with(user(account)))
                .andExpect(status().isForbidden());
    }
}
