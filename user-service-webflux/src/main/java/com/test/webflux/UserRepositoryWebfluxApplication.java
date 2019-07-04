package com.test.webflux;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.config.EnableWebFlux;
import org.springframework.web.reactive.function.server.RouterFunction;

@SpringBootConfiguration
@EnableWebFlux
@EnableAutoConfiguration
public class UserRepositoryWebfluxApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserRepositoryWebfluxApplication.class, args);
    }

    @Bean
    public WebfluxFnHandler webfluxFnHandler(){
        return new WebfluxFnHandler();
    }

    @Bean
    public RouterFunction<?> routerFunction(WebfluxFnHandler handler){
        return WebFluxFnRouter.usingHandler(handler).route();
    }
}
