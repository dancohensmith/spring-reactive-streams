package com.test;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;

@Component
@RequiredArgsConstructor
@Slf4j
public class WebfluxFnHandler {

    private final WebClient client;

    @Data
    @RequiredArgsConstructor
    private static class UserRegisteredEvent {
        private final long id;
        private final String name;
    }

    @Data
    @RequiredArgsConstructor
    private static class Response {
        private final boolean success;
        private final long delayInMillis;
    }

    Mono<ServerResponse> retrieveBlocking(ServerRequest serverRequest) {
        long delay = getRequestedDelay(serverRequest);
        return ServerResponse.ok()
                .body(Mono.just(new Response(true, delay))
                        .doOnNext(response -> {
                            try {
                                Thread.sleep(delay);
                            } catch (InterruptedException e) {
                                throw new RuntimeException(e);
                            }
                        }), Response.class);
    }

    Mono<ServerResponse> retrieveNonBlocking(ServerRequest serverRequest) {
        long delay = getRequestedDelay(serverRequest);
        Mono<Response> response = Mono
                .just(new Response(true, delay))
                .delayElement(Duration.ofMillis(delay));
        return ServerResponse
                .ok()
                .body(response, Response.class);
    }

    // TODO this is pretty much the same as /blocking/{delay}, replace?
    Mono<ServerResponse> retrieveLegacyBlocking(ServerRequest serverRequest) {
        return Mono.fromCallable(() -> {
                    long delay = getRequestedDelay(serverRequest);
                    try {
                        Thread.sleep(delay);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    return new Response(true, delay);
                })
                .subscribeOn(Schedulers.elastic())
                .flatMap(response -> ServerResponse.ok().syncBody(response));
    }



    Mono<ServerResponse> retrieveUsers(ServerRequest serverRequest) {
        Mono<String> response = client
                .get()
                .uri("/api/users")
                .retrieve()
                .bodyToMono(String.class);
        return ServerResponse
                .ok()
                .contentType(APPLICATION_JSON_UTF8)
                .body(response, String.class);
    }

    Mono<ServerResponse> userRegistrations(ServerRequest serverRequest) {
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_STREAM_JSON)
                .body(userRegistrations(), UserRegisteredEvent.class);
    }

    Flux<UserRegisteredEvent> userRegistrations() {
        return Flux
                .range(0, 1000)
                .subscribeOn(Schedulers.single())
                .map(id -> new UserRegisteredEvent(id, "John Smith " + id))
                .onBackpressureDrop(userRegisteredEvent -> log.info("Dropped {}", userRegisteredEvent));
    }

    private long getRequestedDelay(ServerRequest serverRequest) {
        return Long.parseLong(serverRequest.pathVariable("delay"));
    }

}
