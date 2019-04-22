package com.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class WebfluxControllerTest {
    @Autowired
    private WebTestClient webTestClient;

    @Test
    public void testNonBlockingDelayEndpoint() throws Exception {
        webTestClient.get()
                .uri("/controller/nonBlocking/100")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(WebfluxController.Response.class)
                .consumeWith(result -> {
                            WebfluxController.Response response = result.getResponseBody();
                            assertThat(response).isNotNull();
                            assertThat(response.isSuccess()).isTrue();
                            assertThat(response.getDelayInMillis()).isEqualTo(100);

                        }
                );
    }

}
