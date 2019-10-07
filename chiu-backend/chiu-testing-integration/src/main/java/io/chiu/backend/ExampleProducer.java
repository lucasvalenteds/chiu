package io.chiu.backend;

import com.devskiller.jfairy.Fairy;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.lambdas.Throwing;
import io.chiu.backend.ingest.NoiseLevel;
import java.time.Duration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.env.Environment;
import reactor.core.publisher.Flux;
import reactor.netty.http.client.HttpClient;

public class ExampleProducer {

    private static final Logger log = LogManager.getLogger(ExampleProducer.class);

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(Beans.class);
        ObjectMapper objectMapper = context.getBean(ObjectMapper.class);
        Environment environment = context.getBean(Environment.class);
        Fairy fairy = Fairy.create();

        Flux<Integer> numberGenerator = Flux.generate(sink ->
            sink.next(fairy.baseProducer().randomBetween(40, 60))
        );

        HttpClient.create()
            .baseUrl(environment.getProperty("chiu.url.ingest", String.class, "ws://localhost:8080"))
            .websocket()
            .uri("/")
            .handle((in, out) -> {
                out.sendString(
                    numberGenerator
                        .map(NoiseLevel::new)
                        .map(Throwing.function(objectMapper::writeValueAsString))
                        .delayElements(Duration.ofMillis(250))
                        .doOnNext(log::info)
                ).then().subscribe();

                return Flux.never();
            })
            .blockLast();
    }
}
