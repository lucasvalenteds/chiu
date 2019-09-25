package io.chiu.backend.ingest;

import reactor.core.publisher.Mono;

public interface IngestRepository {

    Mono<SensorData> save(SensorData data);
}
