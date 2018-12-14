package io.chiu.configuration

import com.mongodb.reactivestreams.client.MongoCollection
import io.chiu.app.NoiseReport
import io.chiu.app.configuration.Database
import io.chiu.app.configuration.DatabaseNoSQL
import io.kotlintest.specs.StringSpec
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.verify
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeoutOrNull
import org.bson.Document
import java.time.Instant
import java.util.Date

class DatabaseNoSQLTest : StringSpec({

    val collection: MongoCollection<Document> = mockk(relaxed = true)
    val repository: Database = DatabaseNoSQL(collection)

    "It persists _id, level and timestamps" {
        runBlocking {
            val report = spyk(NoiseReport("123", 10, Date.from(Instant.now())))

            withTimeoutOrNull(1000) {
                repository.saveNoiseReport(report)
            }

            verify(exactly = 1) { report.id }
            verify(exactly = 1) { report.level }
            verify(exactly = 1) { report.timestamp }
        }
    }
})