package com.test.config;

import com.test.WebFluxFnRouter;
import com.test.service.LocalResponseService;
import com.test.model.UsersRepository;
import com.test.WebfluxController;
import com.test.WebfluxFnHandler;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.config.EnableWebFlux;
import org.springframework.web.reactive.function.server.RouterFunction;

@Configuration
@ComponentScan(basePackageClasses = ApplicationConfig.class)
@SpringBootConfiguration
@EnableWebFlux
@EnableAutoConfiguration
public class ApplicationConfig {
    @Bean
    public LocalResponseService localResponseService(){
        return new LocalResponseService();
    }

    @Bean
    public UsersRepository usersRepository(){
        return new UsersRepository();
    }


    @Bean
    public WebfluxController webfluxController(LocalResponseService localResponseService, UsersRepository  usersRepository){
        return new WebfluxController(usersRepository, localResponseService);
    }

    @Bean
    public WebfluxFnHandler webfluxFnHandler(LocalResponseService localResponseService, UsersRepository  usersRepository){
        return new WebfluxFnHandler(localResponseService, usersRepository);
    }

    @Bean
    public RouterFunction<?> routerFunction(WebfluxFnHandler handler){
        return WebFluxFnRouter.usingHandler(handler).route();
    }
}
