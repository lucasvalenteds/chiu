package io.chiu.backend.export;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.lambdas.Throwing;
import io.chiu.backend.SensorData;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpHeaderValues;
import io.netty.handler.codec.http.HttpResponseStatus;
import java.util.Random;
import java.util.UUID;
import java.util.function.BiFunction;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Mono;
import reactor.netty.http.server.HttpServerRequest;
import reactor.netty.http.server.HttpServerResponse;

public class ExportHandler implements BiFunction<HttpServerRequest, HttpServerResponse, Publisher<Void>> {

    private static final Logger log = LogManager.getLogger(ExportHandler.class);

    private final Random random = new Random();
    private final ObjectMapper objectMapper;

    public ExportHandler(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public Publisher<Void> apply(HttpServerRequest request, HttpServerResponse response) {
        return Mono.just(new SensorData(UUID.randomUUID(), random.nextInt()))
            .map(Throwing.function(objectMapper::writeValueAsString))
            .doOnNext(log::info)
            .flatMapMany(it ->
                response
                    .status(HttpResponseStatus.OK)
                    .header(HttpHeaderNames.CONTENT_TYPE, HttpHeaderValues.APPLICATION_JSON)
                    .sendString(Mono.just(it))
            );
    }
}
