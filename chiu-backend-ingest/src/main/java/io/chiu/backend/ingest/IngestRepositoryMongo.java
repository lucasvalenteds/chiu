package io.chiu.backend.ingest;

import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.Success;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bson.Document;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Mono;

public class IngestRepositoryMongo implements IngestRepository {

    private static final Logger log = LogManager.getLogger(IngestRepositoryMongo.class);

    private final MongoClient client;

    public IngestRepositoryMongo(MongoClient client) {
        this.client = client;
    }

    @Override
    public Mono<SensorData> save(SensorData data) {
        Document document = new Document()
            .append("_id", data.getId())
            .append("level", data.getLevel());

        Publisher<Success> result = client.getDatabase("chiu")
            .getCollection("noise")
            .insertOne(document);

        return Mono.from(result)
            .doOnNext(log::info)
            .doOnError(log::error)
            .then(Mono.just(data));
    }
}
