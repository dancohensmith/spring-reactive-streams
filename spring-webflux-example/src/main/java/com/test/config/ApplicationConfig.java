package com.test.config;

import com.test.LocalResponseService;
import com.test.UsersRepository;
import com.test.WebfluxController;
import com.test.WebfluxFnHandler;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.config.EnableWebFlux;

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
}
