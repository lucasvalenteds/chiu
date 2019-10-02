package io.chiu.backend;

import com.devskiller.jfairy.Fairy;
import java.time.Duration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import reactor.core.publisher.Flux;
import reactor.netty.http.client.HttpClient;

@Configuration
@PropertySource("classpath:application.properties")
public class ExampleProducer {

    private static final Logger log = LogManager.getLogger(ExampleProducer.class);

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(ExampleConsumer.class);
        Environment environment = context.getBean(Environment.class);
        Fairy fairy = Fairy.create();

        Flux<String> numberGenerator = Flux.generate(sink ->
            sink.next(
                String.valueOf(
                    fairy.baseProducer().randomBetween(40, 60)
                )
            )
        );

        HttpClient.create()
            .baseUrl(environment.getProperty("chiu.url.ingest", String.class, "ws://localhost:8080"))
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
