package com.example.shopBackend.item;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class ItemIntegrationTest {
    @Autowired
    private WebTestClient webClient;

    @Autowired
    public ItemRepository itemRepository;

    @Test
    void getItemsWorks() {
        webClient.get().uri("/api/item/get?accountId=1&page=0")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$[0].title").isEqualTo("initItem title")
                .jsonPath("$[0].id").isEqualTo(1)
                .jsonPath("$[0].account.id").isEqualTo(1)
                .jsonPath("$[0].category.id").isEqualTo(1)
                .jsonPath("$[0].rating").isEqualTo(4)
                .jsonPath("$[0].words.id").isEqualTo(1)
                .jsonPath("$[0].desc").isEqualTo("test desc");

    }

    @Test
    void addItemsWorks() {
        int items = itemRepository.findAll().size();
        webClient.post().uri("/api/item/add")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(
                        """
                        [{
                            "title": "test 1",
                            "account": {
                                "id": 1
                            },
                            "category": {
                                "id": 1
                            },
                            "rating": "1",
                            "desc": "test desc"
                        }]
                        """
                )
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$[0].title").isEqualTo("test 1")
                .jsonPath("$[0].account.id").isEqualTo(1)
                .jsonPath("$[0].category.id").isEqualTo(1)
                .jsonPath("$[0].rating").isEqualTo(1)
                .jsonPath("$[0].words.id").isEqualTo(4)
                .jsonPath("$[0].desc").isEqualTo("test desc");

        assertEquals(itemRepository.findAll().size(), items + 1);
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @Test
    void deleteItemWorks() {
        webClient.delete().uri("/api/item/del?itemId=1")
                .exchange()
                .expectStatus().isOk()
                .expectBody().toString().equals("true");

        assertEquals(0, itemRepository.findAll().size());

    }

    @Test
    void updateItemWorks() {
        webClient.put().uri("/api/item/update?itemId=1")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(
                        """
                        {
                            "title": "test 1",
                            "account": {
                                "id": 1
                            },
                            "category": {
                                "id": 1
                            },
                            "words": {
                                "id": 1
                            },
                            "rating": 4,
                            "desc": "test desc"
                        }
                        """
                )
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.title").isEqualTo("test 1")
                .jsonPath("$.account.id").isEqualTo(1)
                .jsonPath("$.category.id").isEqualTo(1)
                .jsonPath("$.rating").isEqualTo(4)
                .jsonPath("$.words.id").isEqualTo(1)
                .jsonPath("$.desc").isEqualTo("test desc");
    }
}