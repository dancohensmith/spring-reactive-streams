package com.test;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Component
@RequiredArgsConstructor
public class WebfluxFnHandler {


    private final LocalResponseService localResponseService;
    private final UsersRepository usersRepository;

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
        return ServerResponse.ok().body(usersRepository.users(), String.class);
    }

    private long delay(ServerRequest serverRequest) {
        return Long.parseLong(serverRequest.pathVariable(WebFluxFnRouter.DELAY_PATH_VAR));
    }

}
