package com.example.shopBackend.item;

import com.example.shopBackend.account.Account;
import com.example.shopBackend.role.Role;
import com.example.shopBackend.security.JwtService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, properties="secret.key=GjPID0mTzi+PkzF2qZxlrUWq/g+XuJT28aegItULfXM=")
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class ItemIntegrationTest {
    @Autowired
    private WebTestClient webClient;

    @Autowired
    private JwtService jwtService;

    @Autowired
    public ItemRepository itemRepository;

    @Test
    void getItemsWorks() {
        Account account = new Account(
                1,
                "test",
                "initSeller",
                "initSeller pass",
                "email",
                new Role()
        );
        String token = jwtService.newToken(account);
        webClient.get().uri("/api/item/get?accountId=1&page=0")
                .headers(http -> http.setBearerAuth(token))
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$[0].title").isEqualTo("initItem title")
                .jsonPath("$[0].id").isEqualTo(1)
                .jsonPath("$[0].account.id").isEqualTo(1)
                .jsonPath("$[0].category.id").isEqualTo(1)
                .jsonPath("$[0].rating").isEqualTo(4)
                .jsonPath("$[0].words.id").isEqualTo(1);

    }

    @Test
    void addItemsWorks() {
        Account account = new Account(
                1,
                "test",
                "initSeller",
                "initSeller pass",
                "email",
                new Role()
        );
        String token = jwtService.newToken(account);
        int items = itemRepository.findAll().size();
        webClient.post().uri("/api/item/add")
                .contentType(MediaType.APPLICATION_JSON)
                .headers(http -> http.setBearerAuth(token))
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
                            "rating": "1"
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
                .jsonPath("$[0].words.id").isEqualTo(5);

        assertEquals(itemRepository.findAll().size(), items + 1);
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @Test
    void deleteItemWorks() {
        Account account = new Account(
                1,
                "test",
                "initSeller",
                "initSeller pass",
                "email",
                new Role()
        );
        String token = jwtService.newToken(account);
        webClient.delete().uri("/api/item/del?itemId=1")
                .headers(http -> http.setBearerAuth(token))
                .exchange()
                .expectStatus().isOk()
                .expectBody().toString().equals("true");

        assertEquals(1, itemRepository.findAll().size());

    }

    @Test
    void updateItemWorks() {
        Account account = new Account(
                1,
                "test",
                "initSeller",
                "initSeller pass",
                "email",
                new Role()
        );
        String token = jwtService.newToken(account);
        webClient.put().uri("/api/item/update?itemId=1")
                .contentType(MediaType.APPLICATION_JSON)
                .headers(http -> http.setBearerAuth(token))
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
                            "rating": 4
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
                .jsonPath("$.words.id").isEqualTo(1);
    }
}
