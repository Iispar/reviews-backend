package com.example.shopBackend.pages;

import com.example.shopBackend.account.Account;
import com.example.shopBackend.role.Role;
import com.example.shopBackend.security.JwtService;
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

    @Autowired
    private JwtService jwtService;

    @Test
    void getHomePageWorks() {
        Account account = new Account(
                1,
                "test",
                "initSeller",
                "initSeller pass",
                "email",
                new Role()
        );
        String token = jwtService.newToken(account);
        webClient.get().uri("/api/pages/get/home?accountId=1")
                .headers(http -> http.setBearerAuth(token))
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
        Account account = new Account(
                1,
                "test",
                "initSeller",
                "initSeller pass",
                "email",
                new Role()
        );
        String token = jwtService.newToken(account);
        webClient.get().uri("/api/pages/get/item?itemId=1")
                .headers(http -> http.setBearerAuth(token))
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.latestReviews[0].id").isEqualTo(1)
                .jsonPath("$.chart[0].count").isEqualTo(1)
                .jsonPath("$.topNeg").isEqualTo(null)
                .jsonPath("$.topPos").isEqualTo(null);
    }
}
