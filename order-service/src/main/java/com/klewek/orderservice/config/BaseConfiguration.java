package com.klewek.orderservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;

@Configuration
public class BaseConfiguration {

    @Bean
    Clock clock() {
        return Clock.systemUTC();
    }
}
