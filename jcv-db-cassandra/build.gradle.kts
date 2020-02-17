import org.jetbrains.dokka.gradle.DokkaTask
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm")
    `java-library`
    signing
    jacoco
    id("org.jmailen.kotlinter") version "2.1.1"
    id("org.jetbrains.dokka") version "0.9.18"
}

configurations {
    implementation {
        resolutionStrategy.failOnVersionConflict()
    }
}

val javadocJar by tasks.registering(Jar::class) {
    dependsOn("dokka")
    archiveClassifier.set("javadoc")
    from(buildDir.resolve("dokka"))
}

java {
    withSourcesJar()
}

tasks {
    withType<KotlinCompile> {
        kotlinOptions {
            jvmTarget = JavaVersion.VERSION_1_8.toString()
        }
    }

    withType<Test> {
        useJUnitPlatform()
        jvmArgs("-Duser.language=en")
    }

    withType<DokkaTask> {
        reportUndocumented = false
    }

    artifacts {
        archives(jar)
        archives(javadocJar)
    }
}

val publicationName = "mavenJava"

publishing {
    publications {
        named<MavenPublication>(publicationName) {
            artifact(javadocJar.get())
            
            from(components["java"])
        }
    }
}

signing {
    sign(publishing.publications[publicationName])
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))

    api(project(":jcv-db-core"))
    implementation(group = "com.ekino.oss.jcv", name = "jcv-core", version = "${project.extra["jcv-core.version"]}")
    implementation(group = "org.skyscreamer", name = "jsonassert", version = "${project.extra["jsonassert.version"]}")
    implementation("com.datastax.oss:java-driver-core:4.3.1")
    implementation("com.datastax.oss:java-driver-query-builder:4.3.1")

    testImplementation(group = "org.junit.jupiter", name = "junit-jupiter", version = "${project.extra["junit.version"]}")
    testImplementation(group = "org.testcontainers", name = "junit-jupiter", version = "${project.extra["testcontainers.version"]}")
    testImplementation(group = "org.testcontainers", name = "testcontainers", version = "${project.extra["testcontainers.version"]}")
    testImplementation(group = "org.testcontainers", name = "cassandra", version = "${project.extra["testcontainers.version"]}")
}