package com.test;

import com.test.model.UserRegisteredEvent;
import com.test.service.LocalResponseService;
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

@Component
@RequiredArgsConstructor
@Slf4j
public class WebfluxFnHandler {


    private final LocalResponseService localResponseService;

    Mono<ServerResponse> retrieveBlocking(ServerRequest serverRequest) {
        return ServerResponse.ok()
                .body(localResponseService.blocking(delay(serverRequest)), LocalResponseService.Response.class);
    }

    Mono<ServerResponse> retrieveNonBlocking(ServerRequest serverRequest) {
        return ServerResponse.ok().body(localResponseService.nonBlocking(delay(serverRequest)), LocalResponseService.Response.class);
    }

    Mono<ServerResponse> retrieveLegacyBlocking(ServerRequest serverRequest) {
        return Mono.fromCallable(() -> localResponseService.blockingLegacy(delay(serverRequest)))
                .subscribeOn(Schedulers.elastic())
                .flatMap(response -> ServerResponse.ok().syncBody(response));

    }

    Mono<ServerResponse> retreiveUsers(ServerRequest serverRequest) {
        WebClient client = WebClient.create("http://localhost:8081");
        return ServerResponse.ok().body(client
                .get()
                .uri("/api/users")
                .retrieve()
                .bodyToMono(String.class), String.class);
    }

    Mono<ServerResponse> userRegistrations(ServerRequest serverRequest) {
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_STREAM_JSON)
                .body(userRegistrations(), UserRegisteredEvent.class);
    }

    Flux<UserRegisteredEvent> userRegistrations() {
        return Flux.range(0, 10000).subscribeOn(Schedulers.single())
                .map(id -> new UserRegisteredEvent(id, "John Smith " + id))
                .onBackpressureDrop(userRegisteredEvent -> log.info("Dropped {}", userRegisteredEvent));
    }


    private long delay(ServerRequest serverRequest) {
        return Long.parseLong(serverRequest.pathVariable(WebFluxFnRouter.DELAY_PATH_VAR));
    }

}
