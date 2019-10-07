package io.chiu.backend.export;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.chiu.backend.SensorData;
import io.netty.handler.codec.http.HttpHeaderNames;
import java.time.Duration;
import java.util.UUID;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertFalse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.EmitterProcessor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.netty.DisposableServer;
import reactor.netty.http.client.HttpClient;
import reactor.netty.http.server.HttpServer;
import reactor.test.StepVerifier;

class ExportHandlerTest {

    private final int serverPort = 8080;
    private final String endpoint = "/export";

    private EmitterProcessor<SensorData> eventBus = EmitterProcessor.create();
    private final HttpServer server = HttpServer.create()
        .port(serverPort)
        .route(router -> router.get(endpoint, new ExportHandler(new ObjectMapper(), eventBus)));

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
    void testItReturnsSensorDataAsJson() {
        Mono.just("")
            .delaySubscription(Duration.ofSeconds(1))
            .subscribe(it -> {
                eventBus.onNext(new SensorData(UUID.fromString("41fe50fc-8186-4683-8c46-a0fe313d0cbc"), 65));
                eventBus.onNext(new SensorData(UUID.fromString("41fe50fc-8186-4683-8c46-a0fe313d0cbc"), 55));
                eventBus.onNext(new SensorData(UUID.fromString("41fe50fc-8186-4683-8c46-a0fe313d0cbc"), 45));
                eventBus.onNext(new SensorData(UUID.fromString("41fe50fc-8186-4683-8c46-a0fe313d0cbc"), 35));
            });

        Flux<String> response = HttpClient.create()
            .baseUrl("http://localhost:" + serverPort)
            .headers(x -> x.set(HttpHeaderNames.ACCEPT, "text/event-stream"))
            .get()
            .uri(endpoint)
            .responseContent()
            .retain()
            .asString()
            .take(3);

        StepVerifier.create(response)
            .consumeNextWith(ExportHandlerTest::assertResponse)
            .consumeNextWith(ExportHandlerTest::assertResponse)
            .consumeNextWith(ExportHandlerTest::assertResponse)
            .thenCancel()
            .verify();
    }

    private static void assertResponse(String body) {
        assertFalse(body.isEmpty());
    }
}