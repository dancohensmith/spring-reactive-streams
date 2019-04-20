package com.test;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.*;

@Slf4j
@RequiredArgsConstructor(staticName = "usingHandler")
public class WebFluxFnRouter {


    private final WebfluxFnHandler handler;
    static final String DELAY_PATH_VAR = "delay";

    public RouterFunction<ServerResponse> route() {
        return RouterFunctions.route().path("/fn",  this::routerFunctions).build();
    }

  private RouterFunction<ServerResponse> routerFunctions(RouterFunctions.Builder builder) {
        return builder.GET("/blocking/{" + DELAY_PATH_VAR + "}",  handler::retrieveBlocking).
                GET("/nonBlocking/{" + DELAY_PATH_VAR + "}", handler::retrieveNonBlocking)
                .GET("/legacyNonBlocking/{" + DELAY_PATH_VAR + "}", handler::retrieveLegacyBlocking)
                .GET("/ui/users", handler::retreiveUsers).build();
    }

}
