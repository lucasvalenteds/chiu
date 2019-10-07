package io.chiu.backend.ingest;

import io.chiu.backend.SensorData;
import reactor.core.publisher.Mono;

public interface IngestRepository {

    Mono<SensorData> save(SensorData data);
}
