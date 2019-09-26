package io.chiu.backend.ingest;

import com.mongodb.ConnectionString;
import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.Success;
import io.chiu.backend.SensorData;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bson.Document;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Mono;

public class IngestRepositoryMongo implements IngestRepository {

    private static final Logger log = LogManager.getLogger(IngestRepositoryMongo.class);

    private final MongoClient client;
    private final ConnectionString connectionString;

    public IngestRepositoryMongo(MongoClient client, ConnectionString connectionString) {
        this.client = client;
        this.connectionString = connectionString;
    }

    @Override
    public Mono<SensorData> save(SensorData data) {
        Document document = new Document()
            .append("_id", data.getId().toString())
            .append("level", data.getLevel());

        Publisher<Success> result = client.getDatabase(connectionString.getDatabase())
            .getCollection(connectionString.getCollection())
            .insertOne(document);

        return Mono.from(result)
            .doOnError(log::error)
            .then(Mono.just(data))
            .doOnNext(log::info);
    }
}
