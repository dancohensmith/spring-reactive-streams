
package com.test;

import com.test.service.LocalResponseService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@RestController
@RequestMapping("/mvc")
@RequiredArgsConstructor
public class Controller {

    private final LocalResponseService localResponseService;

    @GetMapping(value = "/blocking/{delay}")
    public LocalResponseService.Response blocking(@PathVariable("delay") int delay) {
        return localResponseService.blocking(delay).block();
    }

    @GetMapping(value = "/nonBlocking/{delay}")
    public LocalResponseService.Response nonBlocking(@PathVariable("delay") int delay) {
        return localResponseService.nonBlocking(delay).block();
    }

    @GetMapping(value = "/legacyBlocking/{delay}")
    public LocalResponseService.Response legacyBlocking(@PathVariable("delay") int delay) {
        return Mono.fromCallable(() -> localResponseService.blockingLegacy(delay))
                .subscribeOn(Schedulers.elastic()).block();
    }

    @GetMapping(value = "/ui/users")
    public String getUsers() {
        WebClient client = WebClient.create("http://localhost:8081");
        return client
                .get()
                .uri("/api/users")
                .retrieve()
                .bodyToMono(String.class).block();
    }
}