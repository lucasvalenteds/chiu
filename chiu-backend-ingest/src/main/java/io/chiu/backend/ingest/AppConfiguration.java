package io.chiu.backend.ingest;

import java.util.function.Consumer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import reactor.netty.http.server.HttpServer;
import reactor.netty.http.server.HttpServerRoutes;

@Configuration
@PropertySource("classpath:application.properties")
class AppConfiguration {

    @Autowired
    private Environment environment;

    @Bean
    Consumer<HttpServerRoutes> router() {
        return router -> {
        };
    }

    @Bean
    HttpServer server(Consumer<HttpServerRoutes> router) {
        return HttpServer.create()
            .port(environment.getProperty("server.port", Integer.class, 8080))
            .route(router);
    }
}
