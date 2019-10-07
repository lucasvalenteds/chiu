package io.chiu.backend.ingest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.lambdas.Throwing;
import io.chiu.backend.SensorData;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import reactor.core.publisher.DirectProcessor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.netty.DisposableServer;
import reactor.netty.http.client.HttpClient;
import reactor.netty.http.server.HttpServer;
import reactor.test.StepVerifier;

class IngestHandlerTest {

    private final int serverPort = 8080;
    private final String endpoint = "/ingest";

    private IngestRepository repository = Mono::just;
    private ObjectMapper objectMapper = new ObjectMapper();
    private DirectProcessor<SensorData> eventBus = DirectProcessor.create();
    private HttpServer server = HttpServer.create()
        .port(serverPort)
        .route(router -> router.ws(endpoint, new IngestHandler(repository, objectMapper, eventBus)));

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

    @RepeatedTest(10)
    void testItReturnOKForEveryConnection() {
        Flux<String> response = client.handle((in, out) -> {
            Flux<String> levelsToSendAsJson = Flux.just(new NoiseLevel(1), new NoiseLevel(2))
                .map(Throwing.function(objectMapper::writeValueAsString));

            out.sendString(levelsToSendAsJson).then().subscribe();

            return in.receive().asString();
        });

        StepVerifier.create(response)
            .expectNext("OK")
            .expectNext("OK")
            .thenCancel()
            .verify();
    }

    @RepeatedTest(10)
    void testItClosesTheConnectionWhenInputIsNotValid() {
        Flux<Void> response = client.handle((in, out) ->
            out.sendString(Flux.just("not a valid noise level instance"))
        );

        StepVerifier.create(response)
            .expectNextCount(0)
            .expectComplete()
            .verify();
    }
}
