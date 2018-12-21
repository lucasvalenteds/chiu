import com.diffplug.gradle.spotless.SpotlessExtension

val versionGatling: String by project
val versionNetty: String by project
val versionScala: String by project
val versionSlf4j: String by project

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
    implementation("org.scala-lang:scala-library:$versionScala")
    implementation("org.slf4j:slf4j-nop:$versionSlf4j")
    implementation("io.gatling:gatling-app:$versionGatling")
    implementation("io.gatling:gatling-core:$versionGatling")
    implementation("io.gatling:gatling-http:$versionGatling")
    implementation("io.gatling.highcharts:gatling-charts-highcharts:$versionGatling")
    implementation("io.netty:netty-tcnative-boringssl-static:$versionNetty")
}

configure<SpotlessExtension> {
    scala {
        scalafmt()
    }
}
