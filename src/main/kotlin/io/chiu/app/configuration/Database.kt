package io.chiu.app.configuration

import com.mongodb.reactivestreams.client.MongoClient
import io.chiu.app.NoiseReport
import kotlinx.coroutines.reactive.awaitSingle
import org.bson.Document

interface Database {

    suspend fun saveNoiseReport(report: NoiseReport)
}

class DatabaseNoSQL(
    private val client: MongoClient,
    private val databaseName: String
) : Database {

    override suspend fun saveNoiseReport(report: NoiseReport) {
        val database = client.getDatabase(databaseName)
        val collection = database.getCollection("noises")

        val document = Document()
            .append("_id", report.id)
            .append("level", report.level)
            .append("timestamp", report.timestamp)

        collection.insertOne(document).awaitSingle()
    }
}