package io.chiu.backend;

import io.chiu.backend.ingest.IngestRepository;
import java.util.Random;
import java.util.UUID;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = AppConfiguration.class)
class MainTest {

    @Autowired
    private IngestRepository repository;

    private final Random random = new Random();

    @RepeatedTest(100000)
    void testFloodingData() {
        SensorData sensorData = new SensorData(
            UUID.randomUUID(),
            random.nextInt(150)
        );

        Mono<SensorData> result = repository.save(sensorData);

        StepVerifier.create(result)
            .expectNextCount(1)
            .expectComplete()
            .verify();
    }
}
