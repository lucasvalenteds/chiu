import com.github.lkishalmi.gradle.gatling.GatlingPluginExtension

plugins {
    java
    scala
    id("com.github.lkishalmi.gatling") version "3.2.9"
}

dependencies {
    implementation("org.scala-lang", "scala-library", properties["version.scala"].toString())
    gatling("io.netty", "netty-tcnative-boringssl-static", properties["version.netty.ssl"].toString())
    gatling("io.gatling", "gatling-app", properties["version.gatling"].toString())
    gatling("io.gatling", "gatling-core", properties["version.gatling"].toString())
    gatling("io.gatling", "gatling-http", properties["version.gatling"].toString())
    gatling("io.gatling.highcharts", "gatling-charts-highcharts", properties["version.gatling"].toString())
}

configure<GatlingPluginExtension> {
    jvmArgs = listOf(
            "-server", "-Xmx1G",
            "-XX:InitiatingHeapOccupancyPercent=75",
            "-XX:+ParallelRefProcEnabled",
            "-XX:+PerfDisableSharedMem",
            "-XX:+OptimizeStringConcat",
            "-XX:+HeapDumpOnOutOfMemoryError",
            "-Djava.net.preferIPv4Stack=true",
            "-Djava.net.preferIPv6Addresses=false"
    )
}
