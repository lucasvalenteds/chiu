package io.chiu.backend;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import reactor.netty.http.client.HttpClient;

public class ExampleConsumer {

    private static final Logger log = LogManager.getLogger(ExampleConsumer.class);

    public static void main(String[] args) {
        HttpClient.create()
            .baseUrl("http://localhost:8080")
            .get()
            .uri("/export")
            .response((response, body) -> body.asString())
            .doOnNext(log::info)
            .doOnComplete(() -> log.info("Consumer connected"))
            .blockLast();
    }
}
