package io.chiu.backend.export;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.lambdas.Throwing;
import io.chiu.backend.SensorData;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.UUID;
import java.util.function.BiFunction;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.reactivestreams.Publisher;
import org.springframework.http.codec.ServerSentEvent;
import reactor.core.publisher.EmitterProcessor;
import reactor.core.publisher.Flux;
import reactor.netty.http.server.HttpServerRequest;
import reactor.netty.http.server.HttpServerResponse;

public class ExportHandler implements BiFunction<HttpServerRequest, HttpServerResponse, Publisher<Void>> {

    private static final Logger log = LogManager.getLogger(ExportHandler.class);

    private final ObjectMapper objectMapper;
    private final Flux<SensorData> eventBus;

    public ExportHandler(ObjectMapper objectMapper, EmitterProcessor<SensorData> eventBus) {
        this.objectMapper = objectMapper;
        this.eventBus = eventBus;
    }

    @Override
    public Publisher<Void> apply(HttpServerRequest request, HttpServerResponse response) {
        return response.sse()
            .send(
                Flux.from(eventBus)
                    .map(Throwing.function(objectMapper::writeValueAsString))
                    .doOnNext(log::info)
                    .map(this::toEvent)
                    .doOnNext(log::info)
                    .map(this::toSsePayload)
            );
    }

    private ServerSentEvent<String> toEvent(String sensorData) {
        return ServerSentEvent.builder(sensorData)
            .id(UUID.randomUUID().toString())
            .event("noise")
            .data(sensorData)
            .build();
    }

    private ByteBuf toSsePayload(ServerSentEvent<String> payload) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            outputStream.write(("id: " + payload.id() + "\n").getBytes());
            outputStream.write(("event: " + payload.event() + "\n").getBytes());
            outputStream.write(("data: " + payload.data() + "\n").getBytes());
            outputStream.write("\n\n".getBytes());
        } catch (IOException exception) {
            log.error(exception);
        }

        return ByteBufAllocator.DEFAULT
            .buffer()
            .writeBytes(outputStream.toByteArray());
    }
}
