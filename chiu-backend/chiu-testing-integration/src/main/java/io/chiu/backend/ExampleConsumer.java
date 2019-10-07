package io.chiu.backend;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.env.Environment;
import reactor.netty.http.client.HttpClient;

public class ExampleConsumer {

    private static final Logger log = LogManager.getLogger(ExampleConsumer.class);

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(Beans.class);
        Environment environment = context.getBean(Environment.class);

        HttpClient.create()
            .baseUrl(environment.getProperty("chiu.url.export", String.class, "http://localhost:8080"))
            .get()
            .uri("/export")
            .response((response, body) -> body.asString())
            .doOnNext(log::info)
            .doOnComplete(() -> log.info("Consumer connected"))
            .blockLast();
    }
}
