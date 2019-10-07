package io.chiu.backend.health;

import io.netty.handler.codec.http.HttpResponseStatus;
import java.util.function.BiFunction;
import org.reactivestreams.Publisher;
import reactor.netty.http.server.HttpServerRequest;
import reactor.netty.http.server.HttpServerResponse;

public class HealthHandler implements BiFunction<HttpServerRequest, HttpServerResponse, Publisher<Void>> {

    @Override
    public Publisher<Void> apply(HttpServerRequest request, HttpServerResponse response) {
        return response.status(HttpResponseStatus.OK);
    }
}
