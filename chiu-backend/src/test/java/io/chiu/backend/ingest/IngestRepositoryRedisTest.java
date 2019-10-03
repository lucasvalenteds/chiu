package io.chiu.backend.ingest;

import io.chiu.backend.SensorData;
import io.lettuce.core.api.reactive.RedisStringReactiveCommands;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

class IngestRepositoryRedisTest {

    private final RedisStringReactiveCommands<String, String> commands = Mockito.mock(RedisStringReactiveCommands.class);
    private final IngestRepositoryRedis repository = new IngestRepositoryRedis(commands);

    @Test
    void testItCanPersistSensorData() {
        UUID id = UUID.randomUUID();
        int level = 42;

        Mockito.when(commands.set(id.toString(), String.valueOf(level))).thenReturn(Mono.just(""));

        Mono<SensorData> result = repository.save(new SensorData(id, level));

        StepVerifier.create(result)
            .consumeNextWith(it -> {
                assertEquals(id, it.getId());
                assertEquals(level, it.getLevel());
            })
            .expectComplete()
            .verify();
    }
}
