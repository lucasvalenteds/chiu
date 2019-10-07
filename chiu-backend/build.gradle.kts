import org.gradle.api.tasks.testing.logging.TestLogEvent

plugins {
    java
}

subprojects {
    apply(plugin = "java")

    repositories {
        jcenter()
    }

    dependencies {
        implementation("org.apache.logging.log4j", "log4j-api", properties["version.log4j"].toString())
        implementation("org.apache.logging.log4j", "log4j-core", properties["version.log4j"].toString())
        implementation("org.slf4j", "slf4j-simple", properties["version.slf4j"].toString())
        testImplementation("org.junit.jupiter", "junit-jupiter", properties["version.junit"].toString())
    }

    configure<JavaPluginConvention> {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    tasks.withType<Test> {
        useJUnitPlatform()
        testLogging {
            events = setOf(TestLogEvent.PASSED, TestLogEvent.FAILED, TestLogEvent.SKIPPED, TestLogEvent.STANDARD_ERROR)
        }
    }
}