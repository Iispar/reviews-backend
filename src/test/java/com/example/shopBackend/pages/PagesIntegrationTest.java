package com.example.shopBackend.pages;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class PagesIntegrationTest {
    @Autowired
    private WebTestClient webClient;

    @Test
    void getHomePageWorks() {
        webClient.get().uri("/api/pages/get/home?accountId=1")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.latestReviews[0].id").isEqualTo(1)
                .jsonPath("$.topItems[0].id").isEqualTo(1)
                .jsonPath("$.chart[0].count").isEqualTo(1)
                .jsonPath("$.ratingsAvg").isEqualTo(4)
                .jsonPath("$.itemsCount").isEqualTo(1);
    }

    @Test
    void getItemPageWorks() {
        webClient.get().uri("/api/pages/get/item?itemId=1")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.latestReviews[0].id").isEqualTo(1)
                .jsonPath("$.chart[0].count").isEqualTo(1)
                .jsonPath("$.topNeg").isEqualTo(null)
                .jsonPath("$.topPos").isEqualTo(null);
    }
}
