package com.example.shopBackend.account;

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

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class AccountIntegrationTest {

    @Autowired
    private WebTestClient webClient;

    @Autowired
    private JwtService jwtService;

    @Autowired
    public AccountRepository accountRepository;

    @Test
    void addAccountWorks() {
        int items = accountRepository.findAll().size();
        webClient.post().uri("/api/account/add")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(
                        """
                        {
                            "name": "test",
                            "username": "testUsername",
                            "password": "testPass123!",
                            "email": "testEmail",
                            "role": {
                                "id": 1
                            }
                        }
                        """
                )
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.token").exists();

        assertEquals(accountRepository.findAll().size(), items + 1);
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @Test
    void deleteAccountWorks() {
        Account account = new Account(
                1,
                "test",
                "initSeller username",
                "initSeller pass",
                "email",
                new Role()
        );
        String token = jwtService.newToken(account);

        webClient.delete().uri("/api/account/del?accountId=1")
                .headers(http -> http.setBearerAuth(token))
                .exchange()
                .expectStatus().isOk()
                .expectBody().toString().equals("true");

        assertEquals(1, accountRepository.findAll().size());

    }

    @Test
    void updateAccountWorks() {
        Account account = new Account(
                1,
                "test",
                "initSeller username",
                "initSeller pass",
                "email",
                new Role()
        );
        String token = jwtService.newToken(account);
        webClient.put().uri("/api/account/update?accountId=1")
                .contentType(MediaType.APPLICATION_JSON)
                .headers(http -> http.setBearerAuth(token))
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
