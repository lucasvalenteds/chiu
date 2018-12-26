import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import com.heroku.sdk.gradle.HerokuPluginExtension
import org.jetbrains.kotlin.gradle.dsl.Coroutines
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val herokuAppName: String by project

val versionJackson: String by project
val versionKotlinCoroutines: String by project
val versionKotlinStdlib: String by project
val versionKotlinTest: String by project
val versionKtor: String by project
val versionLogback: String by project
val versionMockk: String by project
val versionMongoDbDriver: String by project
val versionStrikt: String by project

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
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:$versionKotlinStdlib")
    implementation("io.ktor:ktor-server-netty:$versionKtor")
    implementation("ch.qos.logback:logback-classic:$versionLogback")
    implementation("io.ktor:ktor-server-core:$versionKtor")
    implementation("io.ktor:ktor-server-host-common:$versionKtor")
    implementation("io.ktor:ktor-websockets:$versionKtor")
    testImplementation("io.ktor:ktor-server-tests:$versionKtor")
    implementation("com.fasterxml.jackson.core:jackson-core:$versionJackson")
    implementation("com.fasterxml.jackson.core:jackson-databind:$versionJackson")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:$versionJackson")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:$versionJackson")
    implementation("org.mongodb:mongodb-driver-reactivestreams:$versionMongoDbDriver")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactive:$versionKotlinCoroutines")
    testImplementation("io.mockk:mockk:$versionMockk")
    testImplementation("io.kotlintest:kotlintest-runner-junit5:$versionKotlinTest")
    testImplementation("io.strikt:strikt-core:$versionStrikt")
}

tasks.withType<Test> {
    useJUnitPlatform()
}

val shadowJar: ShadowJar by tasks
val jarFilePath = shadowJar.destinationDir
    .relativeTo(project.projectDir)
    .resolve(shadowJar.archiveName)

configure<HerokuPluginExtension> {
    appName = herokuAppName
    includes = listOf(jarFilePath.toString())
    includeBuildDir = true
    processTypes = mapOf("web" to "java -jar $jarFilePath")
}
