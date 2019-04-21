package com.test.model;

import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

public class UsersRepository {


    private final WebClient client = WebClient.create("http://localhost:8081");

    public Mono<String> users(){
        return client
                .get()
                .uri("/api/users")
                .retrieve()
                .bodyToMono(String.class);
    }
}
