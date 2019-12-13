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

    val version: String by project
    if (version.endsWith("-SNAPSHOT")) {
        withType<GenerateModuleMetadata>().configureEach {
            enabled = false
        }
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
    implementation(kotlin("stdlib-jdk8", "1.3.50"))
    implementation(kotlin("test-junit", "1.3.50"))
    implementation(kotlin("reflect", "1.3.50"))

    implementation(group = "com.ekino.oss.jcv", name = "jcv-core", version = "${project.extra["jcv-core.version"]}")
    implementation(group = "org.skyscreamer", name = "jsonassert", version = "${project.extra["jsonassert.version"]}")
    implementation(group = "org.json", name = "json", version = "20190722")
    implementation(group = "commons-io", name = "commons-io", version = "2.6")
    implementation(group = "com.fasterxml.jackson.core", name = "jackson-databind", version = "2.10.1")

    implementation(group = "org.locationtech.jts", name = "jts-core", version =  "1.16.1")

    testImplementation(group = "org.junit.jupiter", name = "junit-jupiter", version = "${project.extra["junit.version"]}")

    testImplementation(group = "com.willowtreeapps.assertk", name = "assertk-jvm", version = "0.20") {
        exclude(group = "org.jetbrains.kotlin", module = "kotlin-reflect")
    }
}