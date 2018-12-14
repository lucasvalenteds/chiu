package io.chiu.app.testing

import io.chiu.app.NoiseReport
import io.chiu.app.configuration.Database

class InMemoryDatabase : Database {
    override suspend fun saveNoiseReport(report: NoiseReport) = Unit
}