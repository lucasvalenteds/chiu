import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import com.heroku.sdk.gradle.HerokuPluginExtension
import org.jetbrains.kotlin.gradle.dsl.Coroutines
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    application
    kotlin("jvm") version "1.3.11"
    id("com.github.johnrengelman.shadow") version "4.0.3"
    id("com.heroku.sdk.heroku-gradle") version "1.0.4"
}

group = "io.chiu"
version = "0.1.0"

application {
    mainClassName = "io.ktor.server.netty.EngineMain"
}

repositories {
    mavenLocal()
    jcenter()
    maven { url = uri("https://kotlin.bintray.com/ktor") }
}

dependencies {
    implementation("org.jetbrains.kotlin", "kotlin-stdlib-jdk8", extra["version.kotlin"].toString())
    implementation("io.ktor", "ktor-server-netty", extra["version.ktor"].toString())
    implementation("ch.qos.logback", "logback-classic", extra["version.logback"].toString())
    implementation("io.ktor", "ktor-server-core", extra["version.ktor"].toString())
    implementation("io.ktor", "ktor-server-host-common", extra["version.ktor"].toString())
    implementation("io.ktor", "ktor-websockets", extra["version.ktor"].toString())
    testImplementation("io.ktor", "ktor-server-tests", extra["version.ktor"].toString())
    implementation("com.fasterxml.jackson.core", "jackson-core", extra["version.jackson"].toString())
    implementation("com.fasterxml.jackson.core", "jackson-databind", extra["version.jackson"].toString())
    implementation("com.fasterxml.jackson.datatype", "jackson-datatype-jsr310", extra["version.jackson"].toString())
    implementation("com.fasterxml.jackson.module", "jackson-module-kotlin", extra["version.jackson"].toString())
    implementation("org.mongodb", "mongodb-driver-reactivestreams", extra["version.mongodb"].toString())
    implementation("org.jetbrains.kotlinx", "kotlinx-coroutines-reactive", extra["version.coroutines"].toString())
    testImplementation("io.mockk", "mockk", extra["version.mockk"].toString())
    testImplementation("io.kotlintest", "kotlintest-runner-junit5", extra["version.kotlintest"].toString())
    testImplementation("io.strikt", "strikt-core", extra["version.strikt"].toString())
}

tasks.withType<Test> {
    useJUnitPlatform()
}

val shadowJar: ShadowJar by tasks
val jarFilePath = shadowJar.destinationDir
    .relativeTo(project.projectDir)
    .resolve(shadowJar.archiveName)

configure<HerokuPluginExtension> {
    appName = extra["heroku.appName"].toString()
    includes = listOf(jarFilePath.toString())
    includeBuildDir = true
    processTypes = mapOf("web" to "java -jar $jarFilePath")
}
