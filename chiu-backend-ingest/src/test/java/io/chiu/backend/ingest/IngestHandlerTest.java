package io.chiu.backend.ingest;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.netty.DisposableServer;
import reactor.netty.http.client.HttpClient;
import reactor.netty.http.server.HttpServer;
import reactor.test.StepVerifier;

class IngestHandlerTest {

    private final int serverPort = 8080;
    private final String endpoint = "/ingest";

    private HttpServer server = HttpServer.create()
        .port(serverPort)
        .route(router -> router.ws(endpoint, new IngestHandler()));

    private final HttpClient.WebsocketSender client = HttpClient.create()
        .baseUrl("ws://localhost:" + serverPort)
        .websocket()
        .uri(endpoint);

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
    void testItReturnOKForEveryConnection() {
        Flux<String> response = client.handle((in, out) ->
            in.receive().asString()
        );

        StepVerifier.create(response)
            .expectNext("Hello World!")
            .expectComplete()
            .verify();
    }
}
