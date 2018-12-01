import org.jetbrains.kotlin.gradle.dsl.Coroutines
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val logbackVersion: String by project
val ktorVersion: String by project
val kotlinVersion: String by project

plugins {
    application
    kotlin("jvm") version "1.3.10"
}

group = "app"
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
    compile("io.ktor:ktor-locations:$ktorVersion")
    compile("io.ktor:ktor-metrics:$ktorVersion")
    compile("io.ktor:ktor-server-host-common:$ktorVersion")
    compile("io.ktor:ktor-websockets:$ktorVersion")
    testCompile("io.ktor:ktor-server-tests:$ktorVersion")
}
