import org.gradle.api.tasks.testing.logging.TestExceptionFormat

plugins {
    kotlin("jvm") version "1.9.20"
    groovy
}

group = "be.vreijsenj.aoc"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.apache.groovy:groovy:4.0.16")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")
    implementation("tools.aqua:z3-turnkey:4.12.2.1")

    testImplementation(kotlin("test"))
    testImplementation("org.spockframework:spock-core:2.3-groovy-4.0")

}

tasks.test {
    useJUnitPlatform()
    testLogging {
        exceptionFormat = TestExceptionFormat.FULL
    }
}

kotlin {
    jvmToolchain(21)
}
