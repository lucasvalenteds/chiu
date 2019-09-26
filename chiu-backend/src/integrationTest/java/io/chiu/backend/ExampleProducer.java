package io.chiu.backend;

import java.time.Duration;
import java.util.Random;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import reactor.core.publisher.Flux;
import reactor.netty.http.client.HttpClient;

public class ExampleProducer {

    private static final Logger log = LogManager.getLogger(ExampleProducer.class);

    public static void main(String[] args) {
        Random random = new Random();

        Flux<String> numberGenerator = Flux.generate(sink -> {
            sink.next(String.valueOf(random.nextInt(150)));
        });

        HttpClient.create()
            .baseUrl("ws://localhost:8080")
            .websocket()
            .uri("/")
            .handle((in, out) -> {
                out.sendString(
                    numberGenerator
                        .delayElements(Duration.ofMillis(1))
                        .doOnNext(log::info)
                ).then().subscribe();

                return Flux.never();
            })
            .blockLast();
    }
}
