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

    testImplementation(kotlin("test"))
    testImplementation("org.spockframework:spock-core:2.3-groovy-4.0")

}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(21)
}