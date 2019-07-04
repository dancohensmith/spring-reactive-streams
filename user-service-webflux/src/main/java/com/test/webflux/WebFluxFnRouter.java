package com.test.webflux;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Component
@RequiredArgsConstructor(staticName = "usingHandler")
public class WebFluxFnRouter {

    private final WebfluxFnHandler handler;

    public RouterFunction<ServerResponse> route() {
        return RouterFunctions
                .route()
                .path("/api",  this::routerFunctions)
                .build();
    }

  private RouterFunction<ServerResponse> routerFunctions(RouterFunctions.Builder builder) {
        return builder
                .GET("/users",  handler::users).build();

    }

}
