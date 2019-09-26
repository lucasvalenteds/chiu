package io.chiu.backend;

import com.devskiller.jfairy.Fairy;
import java.time.Duration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import reactor.core.publisher.Flux;
import reactor.netty.http.client.HttpClient;

public class ExampleProducer {

    private static final Logger log = LogManager.getLogger(ExampleProducer.class);

    public static void main(String[] args) {
        Fairy fairy = Fairy.create();

        Flux<String> numberGenerator = Flux.generate(sink ->
            sink.next(
                String.valueOf(
                    fairy.baseProducer().randomBetween(40, 60)
                )
            )
        );

        HttpClient.create()
            .baseUrl("ws://localhost:8080")
            .websocket()
            .uri("/")
            .handle((in, out) -> {
                out.sendString(
                    numberGenerator
                        .delayElements(Duration.ofMillis(250))
                        .doOnNext(log::info)
                ).then().subscribe();

                return Flux.never();
            })
            .blockLast();
    }
}
