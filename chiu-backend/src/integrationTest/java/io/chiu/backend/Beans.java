package io.chiu.backend;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:application.properties")
class Beans {

    @Bean
    ObjectMapper objectMapper() {
        return new ObjectMapper();
    }
}
