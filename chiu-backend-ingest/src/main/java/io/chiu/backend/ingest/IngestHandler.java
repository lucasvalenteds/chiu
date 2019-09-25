package io.chiu.backend.ingest;

import java.util.UUID;
import java.util.function.BiFunction;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.netty.http.websocket.WebsocketInbound;
import reactor.netty.http.websocket.WebsocketOutbound;

public class IngestHandler implements BiFunction<WebsocketInbound, WebsocketOutbound, Publisher<Void>> {

    private static final Logger log = LogManager.getLogger(IngestHandler.class);

    private final IngestRepository repository;

    public IngestHandler(IngestRepository repository) {
        this.repository = repository;
    }

    @Override
    public Publisher<Void> apply(WebsocketInbound in, WebsocketOutbound out) {
        Flux<String> input = in.receive()
            .asString()
            .take(1)
            .map(Integer::parseInt)
            .map(it -> new SensorData(UUID.randomUUID(), it))
            .flatMap(repository::save)
            .map(SensorData::toString);

        Mono<String> output = Mono.just("OK");

        Mono<String> response = Flux.mergeSequential(input, output)
            .doOnNext(log::info)
            .last();

        return out.sendString(response);
    }
}
