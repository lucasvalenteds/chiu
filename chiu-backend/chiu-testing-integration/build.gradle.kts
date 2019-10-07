plugins {
    java
    application
}

dependencies {
    implementation(project(":chiu-server"))
    implementation("io.projectreactor", "reactor-core", properties["version.reactor"].toString())
    testImplementation("io.projectreactor", "reactor-test", properties["version.reactor"].toString())
    implementation("io.projectreactor.netty", "reactor-netty", properties["version.netty"].toString())
    implementation("org.springframework", "spring-context", properties["version.spring"].toString())
    testImplementation("org.springframework", "spring-test", properties["version.spring"].toString())
    implementation("com.fasterxml.jackson.core", "jackson-databind", properties["version.jackson"].toString())
    implementation("com.github.fge", "throwing-lambdas", properties["version.throwing"].toString())
    implementation("com.devskiller", "jfairy", properties["version.jfairy"].toString())
}

tasks.register<JavaExec>("runConsumer") {
    classpath = sourceSets["main"].runtimeClasspath
    main = "io.chiu.backend.ExampleConsumer"
}

tasks.register<JavaExec>("runProducer") {
    classpath = sourceSets["main"].runtimeClasspath
    main = "io.chiu.backend.ExampleProducer"
}