package io.chiu.backend.export;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.ReadContext;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpHeaderValues;
import io.netty.handler.codec.http.HttpResponseStatus;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.netty.DisposableServer;
import reactor.netty.http.client.HttpClient;
import reactor.netty.http.server.HttpServer;
import reactor.test.StepVerifier;
import reactor.util.function.Tuple3;

class ExportHandlerTest {

    private final int serverPort = 8080;
    private final String endpoint = "/export";

    private final HttpServer server = HttpServer.create()
        .port(serverPort)
        .route(router -> router.get(endpoint, new ExportHandler(new ObjectMapper())));

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
        Mono<Tuple3<Integer, String, String>> data = HttpClient.create()
            .baseUrl("http://localhost:" + serverPort)
            .get()
            .uri(endpoint)
            .responseSingle((response, body) ->
                Mono.zip(
                    Mono.just(response.status().code()),
                    Mono.just(response.responseHeaders().getAsString(HttpHeaderNames.CONTENT_TYPE.toString())),
                    body.asString().defaultIfEmpty("")
                )
            );

        StepVerifier.create(data)
            .consumeNextWith(tuple -> {
                assertEquals(HttpResponseStatus.OK.code(), tuple.getT1());
                assertEquals(HttpHeaderValues.APPLICATION_JSON.toString(), tuple.getT2());
                assertNotEquals("", tuple.getT3());

                ReadContext body = JsonPath.parse(tuple.getT3());
                assertNotNull(body.read("$.id", String.class));
                assertNotNull(body.read("$.level", String.class));
            })
            .expectComplete()
            .verify();
    }
}