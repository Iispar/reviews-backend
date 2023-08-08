package com.example.shopBackend.account;

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
class AccountIntegrationTest {
    @Autowired
    private WebTestClient webClient;

    @Autowired
    public AccountRepository accountRepository;

    @Test
    void addAccountWorks() {
        int items = accountRepository.findAll().size();
        webClient.post().uri("/api/account/add")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(
                        """
                        [{
                            "name": "test",
                            "username": "testUsername",
                            "password": "testPass123!",
                            "email": "testEmail",
                            "role": {
                                "id": 1
                            }
                        }]
                        """
                )
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$[0].name").isEqualTo("test")
                .jsonPath("$[0].username").isEqualTo("testUsername")
                .jsonPath("$[0].password").isEqualTo("testPass123!")
                .jsonPath("$[0].email").isEqualTo("testEmail")
                .jsonPath("$[0].role.id").isEqualTo(1);

        assertEquals(accountRepository.findAll().size(), items + 1);
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @Test
    void deleteAccountWorks() {
        webClient.delete().uri("/api/account/del?accountId=1")
                .exchange()
                .expectStatus().isOk()
                .expectBody().toString().equals("true");

        assertEquals(1, accountRepository.findAll().size());

    }

    @Test
    void updateAccountWorks() {
        webClient.put().uri("/api/account/update?accountId=1")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(
                        """
                        {
                            "name": "newName",
                            "username": "newUser",
                            "password": "newPass1!",
                            "email": "newEmail",
                            "role": {
                                "id": 1
                            }
                        }
                        """
                )
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.name").isEqualTo("newName")
                .jsonPath("$.username").isEqualTo("newUser")
                .jsonPath("$.password").isEqualTo("newPass1!")
                .jsonPath("$.email").isEqualTo("newEmail")
                .jsonPath("$.role.id").isEqualTo(1);
    }
}
