package io.chiu.backend.ingest;

import java.util.function.BiFunction;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Mono;
import reactor.netty.http.websocket.WebsocketInbound;
import reactor.netty.http.websocket.WebsocketOutbound;

public class IngestHandler implements BiFunction<WebsocketInbound, WebsocketOutbound, Publisher<Void>> {

    @Override
    public Publisher<Void> apply(WebsocketInbound in, WebsocketOutbound out) {
        return out.sendString(Mono.just("Hello World!").repeat(2));
    }
}
