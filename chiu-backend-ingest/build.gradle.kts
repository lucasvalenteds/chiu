import org.gradle.api.tasks.testing.logging.TestLogEvent

plugins {
    java
    application
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
    implementation("org.slf4j", "slf4j-simple", "2.0.0-alpha0")
    implementation("com.devskiller", "jfairy", properties["version.jfairy"].toString())
    testImplementation("com.jayway.jsonpath", "json-path", properties["version.jsonpath"].toString())
    testImplementation("io.projectreactor", "reactor-test", properties["version.reactor"].toString())
    testImplementation("org.hamcrest", "hamcrest-all", properties["version.hamcrest"].toString())
    testImplementation("org.junit.jupiter", "junit-jupiter", properties["version.junit"].toString())
    testImplementation("org.mockito", "mockito-core", properties["version.mockito"].toString())
    testImplementation("org.springframework", "spring-test", properties["version.spring"].toString())
}

configure<ApplicationPluginConvention> {
    mainClassName = "io.chiu.backend.ingest.Main"
}

configure<JavaPluginConvention> {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

tasks.withType<Test> {
    useJUnitPlatform()
    testLogging {
        events = setOf(
            TestLogEvent.PASSED,
            TestLogEvent.FAILED,
            TestLogEvent.SKIPPED
        )
    }
}
