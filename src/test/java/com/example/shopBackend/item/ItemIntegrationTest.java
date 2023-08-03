package com.example.shopBackend.item;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@Disabled
public class ItemIntegrationTest {
    @Autowired
    private WebTestClient webClient;

    @Test
    void getItems() {
        Item item = new Item(
        );
        webClient.get().uri("/api/item/get?accountId=1&page=0")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$[0].title").isEqualTo("initItem title")
                .jsonPath("$[0].id").isEqualTo(1)
                .jsonPath("$[0].account").isEqualTo(1)
                .jsonPath("$[0].category").isEqualTo(1)
                .jsonPath("$[0].rating").isEqualTo(4)
                .jsonPath("$[0].words").isEqualTo(1)
                .jsonPath("$[0].desc").isEqualTo("test desc");


    }
}
