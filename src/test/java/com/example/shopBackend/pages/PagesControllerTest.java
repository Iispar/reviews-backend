package com.example.shopBackend.pages;

import com.example.shopBackend.account.Account;
import com.example.shopBackend.category.Category;
import com.example.shopBackend.item.Item;
import com.example.shopBackend.item.ItemController;
import com.example.shopBackend.review.Chart;
import com.example.shopBackend.review.Review;
import com.example.shopBackend.words.Words;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import java.sql.Date;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(value= PagesController.class, excludeAutoConfiguration = {SecurityAutoConfiguration.class})
@ContextConfiguration(classes = PagesController.class)
class PagesControllerTest {
    @Autowired
    MockMvc mockMvc;

    @MockBean
    PagesService pagesService;

    @Test
    void getHomePageForAccountWorks() throws Exception {
        Chart chart = new Chart() {

            @Override
            public int getRating() {
                return 1;
            }

            @Override
            public int getCount() {
                return 1;
            }
        };
        Homepage homepage = new Homepage(
                List.of(new Review(
                        new Date(9),
                        "body",
                        "title",
                        5,
                        5,
                        new Account(),
                        4,
                        new Item()
                )),
                List.of(new Item(
                        1,
                        "title",
                        new Account(),
                        2F,
                        new Category(),
                        new Words(),
                        "desc"
                )),
                2.3F,
                2,
                2,
                List.of(chart)
        );
        given(pagesService.getHomepageForAccount(anyInt())).willReturn(homepage);

        mockMvc.perform(get("/api/pages/get/home?accountId=1", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.latestReviews[0].title").value(homepage.getLatestReviews().get(0).getTitle()))
                .andExpect(jsonPath("$.topItems[0].desc").value(homepage.getTopItems().get(0).getDesc()))
                .andExpect(jsonPath("$.ratingsAvg").value(homepage.getRatingsAvg()))
                .andExpect(jsonPath("$.itemsCount").value(homepage.getItemsCount()))
                .andExpect(jsonPath("$.reviewsCount").value(homepage.getReviewsCount()))
                .andExpect(jsonPath("$.chart[0].count").value(homepage.getChart().get(0).getCount()));
    }

    @Test
    void getHomePageForAccountThrowsWithNoParams() throws Exception {
        mockMvc.perform(get("/api/pages/get/home")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getItemPageForItemWorks() throws Exception {
        Chart chart = new Chart() {

            @Override
            public int getRating() {
                return 1;
            }

            @Override
            public int getCount() {
                return 1;
            }
        };

        ItemPage itempage = new ItemPage(
                List.of(new Review(
                        new Date(9),
                        "body",
                        "title",
                        5,
                        5,
                        new Account(),
                        4,
                        new Item()
                )),
                List.of(chart),
                List.of("pos"),
                List.of("neg")
        );

        given(pagesService.getItemPageForItem(anyInt())).willReturn(itempage);

        mockMvc.perform(get("/api/pages/get/item?itemId=1", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.latestReviews[0].title").value(itempage.getLatestReviews().get(0).getTitle()))
                .andExpect(jsonPath("$.chart[0].count").value(itempage.getChart().get(0).getCount()))
                .andExpect(jsonPath("$.topPos[0]").value(itempage.getTopPos().get(0)))
                .andExpect(jsonPath("$.topNeg[0]").value(itempage.getTopNeg().get(0)));
    }

    @Test
    void getItemPageForItemThrowsWithNoParams() throws Exception {
        mockMvc.perform(get("/api/pages/get/home")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
}
