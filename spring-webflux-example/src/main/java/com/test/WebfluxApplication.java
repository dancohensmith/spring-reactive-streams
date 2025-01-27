package com.test;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.config.EnableWebFlux;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.server.RouterFunction;


@SpringBootConfiguration
@EnableWebFlux
@EnableAutoConfiguration
public class WebfluxApplication {
    public static void main(String[] args) {
        SpringApplication.run(WebfluxApplication.class, args);
    }

    @Bean
    public WebfluxController webfluxController(WebClient webClient){
        return new WebfluxController(webClient);
    }

    @Bean
    public WebfluxFnHandler webfluxFnHandler(WebClient webClient){
        return new WebfluxFnHandler(webClient);
    }

    @Bean
    public RouterFunction<?> routerFunction(WebfluxFnHandler handler){
        return WebFluxFnRouter.usingHandler(handler).route();
    }

    @Bean
    public WebClient webClient(){
        return WebClient.create("http://localhost:8081");
    }
}
