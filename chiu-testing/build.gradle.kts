import com.diffplug.gradle.spotless.SpotlessExtension
import com.github.lkishalmi.gradle.gatling.GatlingPluginExtension

plugins {
    java
    scala
    id("com.github.lkishalmi.gatling") version "3.0.2"
    id("com.diffplug.gradle.spotless") version "3.16.0"
}

repositories {
    jcenter()
}

dependencies {
    implementation("org.scala-lang", "scala-library", extra["version.scala"].toString())
    implementation("org.slf4j", "slf4j-nop", extra["version.slf4j"].toString())
    implementation("io.gatling", "gatling-app", extra["version.gatling"].toString())
    implementation("io.gatling", "gatling-core", extra["version.gatling"].toString())
    implementation("io.gatling", "gatling-http", extra["version.gatling"].toString())
    implementation("io.gatling.highcharts", "gatling-charts-highcharts", extra["version.gatling"].toString())
    implementation("io.netty", "netty-tcnative-boringssl-static", extra["version.netty"].toString())
}

configure<GatlingPluginExtension> {
    jvmArgs = listOf(
        "-server", "-Xmx1G",
        "-XX:+UseG1GC", "-XX:MaxGCPauseMillis=30",
        "-XX:G1HeapRegionSize=16m",
        "-XX:InitiatingHeapOccupancyPercent=75",
        "-XX:+ParallelRefProcEnabled",
        "-XX:+PerfDisableSharedMem",
        "-XX:+OptimizeStringConcat",
        "-XX:+HeapDumpOnOutOfMemoryError",
        "-Djava.net.preferIPv4Stack=true",
        "-Djava.net.preferIPv6Addresses=false"
    )
}

configure<SpotlessExtension> {
    scala {
        scalafmt()
    }
}
