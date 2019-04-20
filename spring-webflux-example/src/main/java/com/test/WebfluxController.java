package com.test;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.Duration;

@RestController
public class WebfluxController {

    private WebClient webClient = WebClient.create("http://localhost:8081");

    @RequestMapping(value = "/blocking/{delay}")
    @ResponseBody
    public String blocking(@PathVariable("delay") int delay) throws InterruptedException {
        Thread.sleep(delay);
        return createResponse(delay);
    }

    @RequestMapping(value = "/nonBlocking/{delay}")
    @ResponseBody
    public Mono<String> nonBlocking(@PathVariable("delay") int delay) {
        return Mono
                .just(createResponse(delay))
                .delayElement(Duration.ofMillis(delay));
    }

    private String createResponse(int delay) {
        return "{\"success\": true, \"delayInMillis\": " + delay + "}";
    }

    @RequestMapping(value = "/ui/users")
    @ResponseBody
    public Mono<String> getUsers() {
        return webClient
                .get()
                .uri("/api/users")
                .retrieve()
                .bodyToMono(String.class);
    }
}
