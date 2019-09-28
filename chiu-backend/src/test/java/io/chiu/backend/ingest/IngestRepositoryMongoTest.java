package io.chiu.backend.ingest;

import com.mongodb.ConnectionString;
import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoCollection;
import com.mongodb.reactivestreams.client.MongoDatabase;
import io.chiu.backend.SensorData;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.reactivestreams.Subscriber;
import reactor.test.StepVerifier;

@SuppressWarnings({"unchecked", "ResultOfMethodCallIgnored"})
class IngestRepositoryMongoTest {

    private final ConnectionString connectionString = Mockito.mock(ConnectionString.class);
    private final MongoClient client = Mockito.mock(MongoClient.class);
    private final MongoDatabase database = Mockito.mock(MongoDatabase.class);
    private final MongoCollection collection = Mockito.mock(MongoCollection.class);

    private final IngestRepository repository = new IngestRepositoryMongo(client, connectionString);

    @Test
    void testItCanPersistSensorData() {
        SensorData sensorData = Mockito.mock(SensorData.class);

        Mockito.when(connectionString.getDatabase()).thenReturn("chiu");
        Mockito.when(connectionString.getCollection()).thenReturn("noise");
        Mockito.when(client.getDatabase("chiu")).thenReturn(database);
        Mockito.when(database.getCollection("noise")).thenReturn(collection);
        Mockito.when(collection.insertOne(Mockito.any())).thenReturn(Subscriber::onComplete);
        Mockito.when(sensorData.getId()).thenReturn(UUID.randomUUID());
        Mockito.when(sensorData.getLevel()).thenReturn(65);

        StepVerifier.create(repository.save(sensorData))
            .expectNextCount(1)
            .expectComplete()
            .verify();

        Mockito.verify(sensorData, Mockito.times(1)).getId();
        Mockito.verify(sensorData, Mockito.times(1)).getLevel();
    }
}