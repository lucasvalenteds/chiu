import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import com.heroku.sdk.gradle.HerokuPluginExtension
import org.jetbrains.kotlin.gradle.dsl.Coroutines
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val herokuAppName: String by project
val logbackVersion: String by project
val ktorVersion: String by project
val kotlinVersion: String by project

plugins {
    application
    kotlin("jvm") version "1.3.10"
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
    compile("org.jetbrains.kotlin:kotlin-stdlib-jdk8:$kotlinVersion")
    compile("io.ktor:ktor-server-netty:$ktorVersion")
    compile("ch.qos.logback:logback-classic:$logbackVersion")
    compile("io.ktor:ktor-server-core:$ktorVersion")
    compile("io.ktor:ktor-jackson:$ktorVersion")
    compile("io.ktor:ktor-metrics:$ktorVersion")
    compile("io.ktor:ktor-server-host-common:$ktorVersion")
    compile("io.ktor:ktor-websockets:$ktorVersion")
    testCompile("io.ktor:ktor-server-tests:$ktorVersion")
    compile("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.9.7")
    compile("org.mongodb:mongodb-driver-reactivestreams:1.10.0")
    compile("org.jetbrains.kotlinx:kotlinx-coroutines-reactive:1.0.1")
    testImplementation("io.mockk:mockk:1.8.13.kotlin13")
    testImplementation("io.kotlintest:kotlintest-runner-junit5:3.1.10")
    testImplementation("io.strikt:strikt-core:0.17.0")
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
    includeBuildDir = false
    processTypes = mapOf("web" to "java -jar $jarFilePath")
}
