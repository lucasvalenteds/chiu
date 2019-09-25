package io.chiu.backend.ingest;

import java.util.function.Consumer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.netty.http.server.HttpServer;
import reactor.netty.http.server.HttpServerRoutes;

@Configuration
class AppConfiguration {

    @Bean
    Consumer<HttpServerRoutes> router() {
        return router -> {
        };
    }

    @Bean
    HttpServer server(Consumer<HttpServerRoutes> router) {
        return HttpServer.create()
            .port(8080)
            .route(router);
    }
}
