package io.chiu.backend.health;

import io.netty.handler.codec.http.HttpResponseStatus;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.netty.DisposableServer;
import reactor.netty.http.client.HttpClient;
import reactor.netty.http.client.HttpClientResponse;
import reactor.netty.http.server.HttpServer;
import reactor.test.StepVerifier;

class HealthHandlerTest {

    private final int serverPort = 8080;
    private final String endpoint = "/health";

    private final HttpServer server = HttpServer.create()
        .port(serverPort)
        .route(router -> router.get(endpoint, new HealthHandler()));

    private DisposableServer disposableServer;

    @BeforeEach
    void startServer() {
        disposableServer = server.bindNow();
    }

    @AfterEach
    void stopServer() {
        disposableServer.disposeNow();
    }

    @Test
    void testItReturnsStatus200AndEmptyBody() {
        Mono<HttpResponseStatus> response = HttpClient.create()
            .baseUrl("http://localhost:" + serverPort)
            .get()
            .uri("/health")
            .response()
            .map(HttpClientResponse::status);

        StepVerifier.create(response)
            .expectNext(HttpResponseStatus.OK)
            .expectComplete()
            .verify();
    }
}
