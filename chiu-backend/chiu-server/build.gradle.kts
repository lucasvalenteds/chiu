plugins {
    java
    application
    id("com.github.johnrengelman.shadow") version "5.1.0"
}

dependencies {
    implementation("io.projectreactor", "reactor-core", properties["version.reactor"].toString())
    testImplementation("io.projectreactor", "reactor-test", properties["version.reactor"].toString())
    implementation("io.projectreactor.netty", "reactor-netty", properties["version.netty"].toString())
    implementation("org.springframework", "spring-context", properties["version.spring"].toString())
    testImplementation("org.springframework", "spring-test", properties["version.spring"].toString())
    implementation("io.lettuce", "lettuce-core", properties["version.redis"].toString())
    implementation("com.fasterxml.jackson.core", "jackson-databind", properties["version.jackson"].toString())
    implementation("com.github.fge", "throwing-lambdas", properties["version.throwing"].toString())
    testImplementation("com.devskiller", "jfairy", properties["version.jfairy"].toString())
    testImplementation("org.mockito", "mockito-core", properties["version.mockito"].toString())
}

configure<ApplicationPluginConvention> {
    mainClassName = "io.chiu.backend.Main"
}