package com.example.shopBackend.pages;

import com.example.shopBackend.category.Category;
import com.example.shopBackend.item.Item;
import com.example.shopBackend.review.Chart;
import com.example.shopBackend.review.Review;
import com.example.shopBackend.user.User;
import com.example.shopBackend.words.Words;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.sql.Date;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PagesController.class)
class PagesControllerTest {
    @Autowired
    MockMvc mockMvc;

    @MockBean
    PagesService pagesService;

    @Test
    void getHomePageForUserWorks() throws Exception {
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
                        new User(),
                        4,
                        new Item()
                )),
                List.of(new Item(
                        1,
                        "title",
                        new User(),
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
        given(pagesService.getHomepageForUser(anyInt())).willReturn(homepage);

        mockMvc.perform(get("/api/pages/get/home?userId=1", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.latestReviews[0].title").value("title"))
                .andExpect(jsonPath("$.topItems[0].desc").value("desc"))
                .andExpect(jsonPath("$.ratingsAvg").value(2.3F))
                .andExpect(jsonPath("$.itemsCount").value(2))
                .andExpect(jsonPath("$.reviewsCount").value(2))
                .andExpect(jsonPath("$.chart[0].count").value(1));
    }

    @Test
    void getHomePageForUserThrowsWithNoParams() throws Exception {
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
                        new User(),
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
                .andExpect(jsonPath("$.latestReviews[0].title").value("title"))
                .andExpect(jsonPath("$.chart[0].count").value(1))
                .andExpect(jsonPath("$.topPos[0]").value("pos"))
                .andExpect(jsonPath("$.topNeg[0]").value("neg"));
    }

    @Test
    void getItemPageForItemThrowsWithNoParams() throws Exception {
        mockMvc.perform(get("/api/pages/get/home")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
}
