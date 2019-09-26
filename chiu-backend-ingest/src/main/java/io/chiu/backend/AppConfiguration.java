package io.chiu.backend;

import com.mongodb.ConnectionString;
import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoClients;
import io.chiu.backend.ingest.IngestHandler;
import io.chiu.backend.ingest.IngestRepository;
import io.chiu.backend.ingest.IngestRepositoryMongo;
import java.util.function.Consumer;
import org.springframework.beans.factory.annotation.Autowired;
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
    ConnectionString connectionString() {
        return new ConnectionString(environment.getProperty(
            "database.url",
            String.class,
            "mongodb://admin:password@localhost:27017/chiu.events"
        ));
    }

    @Bean
    MongoClient mongoClient(ConnectionString connectionString) {
        return MongoClients.create(connectionString);
    }

    @Bean
    IngestRepository ingestRepository(MongoClient client, ConnectionString connectionString) {
        return new IngestRepositoryMongo(client, connectionString);
    }

    @Bean
    IngestHandler ingestHandler(IngestRepository repository) {
        return new IngestHandler(repository);
    }

    @Bean
    Consumer<HttpServerRoutes> router(IngestHandler ingestHandler) {
        return router -> {
            router
                .ws("/", ingestHandler);
        };
    }

    @Bean
    HttpServer server(Consumer<HttpServerRoutes> router) {
        return HttpServer.create()
            .port(environment.getProperty("server.port", Integer.class, 8080))
            .route(router);
    }
}