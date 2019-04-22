package com.test;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.reactive.function.server.*;

@Slf4j
@RequiredArgsConstructor(staticName = "usingHandler")
public class WebFluxFnRouter {

    private final WebfluxFnHandler handler;

    public RouterFunction<ServerResponse> route() {
        return RouterFunctions
                .route()
                .path("/fn",  this::routerFunctions)
                .build();
    }

  private RouterFunction<ServerResponse> routerFunctions(RouterFunctions.Builder builder) {
        return builder
                .GET("/blocking/{delay}",  handler::retrieveBlocking)
                .GET("/nonBlocking/{delay}", handler::retrieveNonBlocking)
                .GET("/legacyBlocking/{delay}", handler::retrieveLegacyBlocking)
                .GET("/userRegistrations", handler::userRegistrations)
                .GET("/ui/users", handler::retrieveUsers).build();
    }

}
