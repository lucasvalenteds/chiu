package io.chiu.backend.ingest;

import io.chiu.backend.SensorData;
import io.lettuce.core.api.reactive.RedisStringReactiveCommands;
import reactor.core.publisher.Mono;

public class IngestRepositoryRedis implements IngestRepository {

    private final RedisStringReactiveCommands<String, String> commands;

    public IngestRepositoryRedis(RedisStringReactiveCommands<String, String> commands) {
        this.commands = commands;
    }

    @Override
    public Mono<SensorData> save(SensorData data) {
        return commands.set(data.getId().toString(), String.valueOf(data.getLevel()))
            .map(it -> data);
    }
}
