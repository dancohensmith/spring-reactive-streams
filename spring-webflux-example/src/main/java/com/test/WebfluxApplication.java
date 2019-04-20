package com.test;

import com.test.config.ApplicationConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.config.EnableWebFlux;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;


public class WebfluxApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApplicationConfig.class, args);
    }


}
