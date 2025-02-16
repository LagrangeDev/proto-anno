plugins {
    id("java")
}

group = "org.lagrangecore"
version = "0.1.0"

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.google.protobuf:protobuf-java:4.29.3")
    implementation("org.jetbrains:annotations:24.0.0")
    implementation("it.unimi.dsi:fastutil:8.5.15")
}

tasks.test {
    useJUnitPlatform()
}