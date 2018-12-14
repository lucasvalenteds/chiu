package io.chiu.app.configuration

import com.mongodb.reactivestreams.client.MongoCollection
import io.chiu.app.NoiseReport
import kotlinx.coroutines.reactive.awaitSingle
import org.bson.Document

interface Database {

    suspend fun saveNoiseReport(report: NoiseReport)
}

class DatabaseNoSQL(private val collection: MongoCollection<Document>) : Database {

    override suspend fun saveNoiseReport(report: NoiseReport) {
        val document = Document()
            .append("_id", report.id)
            .append("level", report.level)
            .append("timestamp", report.timestamp)

        collection.insertOne(document).awaitSingle()
    }
}