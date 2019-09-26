import com.github.lkishalmi.gradle.gatling.GatlingPluginExtension
import org.gradle.api.tasks.testing.logging.TestLogEvent

plugins {
    java
    application
    scala
    id("com.github.lkishalmi.gatling") version "3.2.9"
    id("com.github.johnrengelman.shadow") version "5.1.0"
}

repositories {
    jcenter()
}

dependencies {
    implementation("org.springframework", "spring-webflux", properties["version.spring"].toString())
    implementation("org.springframework", "spring-websocket", properties["version.spring"].toString())
    implementation("org.springframework", "spring-context", properties["version.spring"].toString())
    compileOnly("org.projectlombok", "lombok", properties["version.lombok"].toString())
    annotationProcessor("org.projectlombok", "lombok", properties["version.lombok"].toString())
    implementation("javax", "javaee-api", properties["version.javax"].toString())
    implementation("org.mongodb", "mongodb-driver-reactivestreams", properties["version.mongodb"].toString())
    implementation("com.github.fge", "throwing-lambdas", properties["version.throwing"].toString())
    implementation("io.projectreactor", "reactor-core", properties["version.reactor"].toString())
    implementation("io.projectreactor.netty", "reactor-netty", properties["version.netty"].toString())
    implementation("com.fasterxml.jackson.core", "jackson-databind", properties["version.jackson"].toString())
    implementation("org.apache.logging.log4j", "log4j-api", properties["version.log4j"].toString())
    implementation("org.apache.logging.log4j", "log4j-core", properties["version.log4j"].toString())
    implementation("org.slf4j", "slf4j-simple", properties["version.slf4j"].toString())
    implementation("com.devskiller", "jfairy", properties["version.jfairy"].toString())
    testImplementation("com.jayway.jsonpath", "json-path", properties["version.jsonpath"].toString())
    testImplementation("io.projectreactor", "reactor-test", properties["version.reactor"].toString())
    testImplementation("org.hamcrest", "hamcrest-all", properties["version.hamcrest"].toString())
    testImplementation("org.junit.jupiter", "junit-jupiter", properties["version.junit"].toString())
    testImplementation("org.mockito", "mockito-core", properties["version.mockito"].toString())
    testImplementation("org.springframework", "spring-test", properties["version.spring"].toString())

    implementation("org.scala-lang", "scala-library", properties["version.scala"].toString())
    implementation("io.netty", "netty-tcnative-boringssl-static", properties["version.netty.ssl"].toString())
    testImplementation("io.gatling", "gatling-app", properties["version.gatling"].toString())
    testImplementation("io.gatling", "gatling-core", properties["version.gatling"].toString())
    testImplementation("io.gatling", "gatling-http", properties["version.gatling"].toString())
    testImplementation("io.gatling.highcharts", "gatling-charts-highcharts", properties["version.gatling"].toString())
}

configure<ApplicationPluginConvention> {
    mainClassName = "io.chiu.backend.Main"
}

configure<JavaPluginConvention> {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
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

tasks.withType<Test> {
    useJUnitPlatform()
    testLogging {
        events = setOf(
                TestLogEvent.PASSED,
                TestLogEvent.FAILED,
                TestLogEvent.SKIPPED,
                TestLogEvent.STANDARD_ERROR
        )
    }
}

val integrationTest: SourceSet by sourceSets.creating {
    compileClasspath += sourceSets[sourceSets.main.name].output
}

configurations {
    getByName(integrationTest.name + "Implementation") {
        extendsFrom(configurations.testImplementation.get())
    }

    getByName(integrationTest.name + "RuntimeOnly") {
        extendsFrom(configurations.testRuntime.get())
    }
}

tasks.register<Test>("itest") {
    testClassesDirs = sourceSets[integrationTest.name].output.classesDirs
    classpath += sourceSets[integrationTest.name].runtimeClasspath
    systemProperty("junit.jupiter.execution.parallel.enabled", true);
}
