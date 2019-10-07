package io.chiu.backend;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.ConnectionString;
import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoClients;
import io.chiu.backend.export.ExportHandler;
import io.chiu.backend.health.HealthHandler;
import io.chiu.backend.ingest.IngestHandler;
import io.chiu.backend.ingest.IngestRepository;
import io.chiu.backend.ingest.IngestRepositoryMongo;
import io.chiu.backend.ingest.IngestRepositoryRedis;
import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import io.lettuce.core.api.reactive.RedisStringReactiveCommands;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.cors.CorsConfig;
import io.netty.handler.codec.http.cors.CorsConfigBuilder;
import io.netty.handler.codec.http.cors.CorsHandler;
import java.util.function.Consumer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import reactor.core.publisher.DirectProcessor;
import reactor.netty.http.server.HttpServer;
import reactor.netty.http.server.HttpServerRoutes;

@Configuration
@PropertySource("classpath:application.properties")
class AppConfiguration {

    @Autowired
    private Environment environment;

    @Bean
    ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    @Bean
    DirectProcessor<SensorData> eventBus() {
        return DirectProcessor.create();
    }

    @Bean
    RedisURI redisURI() {
        return RedisURI.create(
            environment.getProperty("database.url", String.class, "redis://localhost:6379/")
        );
    }

    @Bean
    RedisClient redisClient(RedisURI redisURI) {
        return RedisClient.create(redisURI);
    }

    @Bean
    RedisStringReactiveCommands<String, String> commands(RedisClient client) {
        return client.connect().reactive();
    }

    ConnectionString connectionString() {
        return new ConnectionString(environment.getProperty(
            "database.url",
            String.class,
            "mongodb://admin:password@localhost:27017/chiu.events"
        ));
    }

    MongoClient mongoClient(ConnectionString connectionString) {
        return MongoClients.create(connectionString);
    }

    IngestRepository ingestRepository(MongoClient client, ConnectionString connectionString) {
        return new IngestRepositoryMongo(client, connectionString);
    }

    @Bean
    IngestRepository ingestRepository(RedisStringReactiveCommands<String, String> commands) {
        return new IngestRepositoryRedis(commands);
    }

    @Bean
    IngestHandler ingestHandler(IngestRepository repository, DirectProcessor<SensorData> eventBus) {
        return new IngestHandler(repository, eventBus);
    }

    @Bean
    ExportHandler exportHandler(ObjectMapper objectMapper, DirectProcessor<SensorData> eventBus) {
        return new ExportHandler(objectMapper, eventBus);
    }

    @Bean
    HealthHandler healthHandler() {
        return new HealthHandler();
    }

    @Bean
    Consumer<HttpServerRoutes> router(IngestHandler ingestHandler, ExportHandler exportHandler, HealthHandler healthHandler) {
        return router ->
            router
                .ws("/", ingestHandler)
                .get("/export", exportHandler)
                .get("/health", healthHandler);
    }

    @Bean
    CorsConfig corsConfig() {
        String frontend = environment.getProperty("frontend.url", String.class, "http://localhost:8081");
        return CorsConfigBuilder.forOrigin(frontend)
            .allowedRequestHeaders("*")
            .allowedRequestMethods(HttpMethod.GET)
            .build();
    }

    @Bean
    HttpServer server(Consumer<HttpServerRoutes> router, CorsConfig corsConfig) {
        return HttpServer.create()
            .port(environment.getProperty("server.port", Integer.class, 8080))
            .tcpConfiguration(it ->
                it.doOnConnection(connection ->
                    connection.addHandlerLast(new CorsHandler(corsConfig))
                )
            )
            .route(router)
            .forwarded(true);
    }
}
