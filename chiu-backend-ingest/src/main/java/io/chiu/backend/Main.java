package io.chiu.backend;

import java.time.Duration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import reactor.netty.http.server.HttpServer;

public class Main {

    private static final Logger log = LogManager.getLogger(Main.class);

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(
            AppConfiguration.class
        );

        context.getBean(HttpServer.class)
            .bindUntilJavaShutdown(
                Duration.ofSeconds(60),
                url -> log.info("Server running on port " + url.port())
            );
    }
}