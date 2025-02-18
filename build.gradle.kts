plugins {
    id("java-library")
    id("maven-publish")
}

group = "org.lagrangecore"
version = "0.2.1"

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
    api("it.unimi.dsi:fastutil:8.5.15")
    api("org.jetbrains:annotations:24.0.0")

    testCompileOnly("org.projectlombok:lombok:1.18.36")
    testAnnotationProcessor("org.projectlombok:lombok:1.18.36")
}

tasks.test {
    useJUnitPlatform()
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            groupId = project.group.toString()
            version = project.version.toString()
            artifactId = rootProject.name

            from(components["java"])
        }
    }
}
