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
import reactor.core.publisher.EmitterProcessor;
import reactor.core.publisher.FluxProcessor;
import reactor.netty.http.server.HttpServerRequest;
import reactor.netty.http.server.HttpServerResponse;

public class ExportHandler implements BiFunction<HttpServerRequest, HttpServerResponse, Publisher<Void>> {

    private static final Logger log = LogManager.getLogger(ExportHandler.class);

    private final ObjectMapper objectMapper;
    private final EmitterProcessor<SensorData> eventBus;

    public ExportHandler(ObjectMapper objectMapper, EmitterProcessor<SensorData> eventBus) {
        this.objectMapper = objectMapper;
        this.eventBus = eventBus;
    }

    @Override
    public Publisher<Void> apply(HttpServerRequest request, HttpServerResponse response) {
        return response.sse()
            .send(
                FluxProcessor.from(eventBus)
                    .doOnNext(log::info)
                    .map(Throwing.function(objectMapper::writeValueAsString))
                    .map(this::toEvent)
                    .map(this::toSsePayload)
            );
    }

    private ServerSentEvent<String> toEvent(String sensorData) {
        return new ServerSentEvent<>(UUID.randomUUID().toString(), "noise", sensorData);
    }

    private ByteBuf toSsePayload(ServerSentEvent<String> payload) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            outputStream.write(("id: " + payload.getId() + "\n").getBytes());
            outputStream.write(("event: " + payload.getEvent() + "\n").getBytes());
            outputStream.write(("data: " + payload.getData() + "\n").getBytes());
            outputStream.write("\n\n".getBytes());
        } catch (IOException exception) {
            log.error(exception);
        }

        return ByteBufAllocator.DEFAULT
            .buffer()
            .writeBytes(outputStream.toByteArray());
    }
}
