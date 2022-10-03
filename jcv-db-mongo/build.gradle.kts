import org.jetbrains.dokka.gradle.DokkaTask
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm")
    `java-library`
    signing
    jacoco
    id("org.jetbrains.dokka")
    id("com.ekino.oss.plugin.kotlin-quality")
}

configurations {
    implementation {
        resolutionStrategy.failOnVersionConflict()
    }
}

kotlinQuality {
    customDetektConfig = "config/detekt.yml"
}

val javadocJar by tasks.registering(Jar::class) {
    dependsOn("dokkaHtml")
    archiveClassifier.set("javadoc")
    from(buildDir.resolve("dokka"))
}

java {
    withSourcesJar()
}

tasks {
    withType<KotlinCompile> {
        kotlinOptions {
            jvmTarget = JavaVersion.VERSION_11.toString()
        }
    }

    withType<Test> {
        useJUnitPlatform()
        jvmArgs("-Duser.language=en")
    }

    withType<DokkaTask> {
        dokkaSourceSets {
            configureEach {
                reportUndocumented.set(false)
            }
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
    setRequired { gradle.taskGraph.hasTask("publish") }
    sign(publishing.publications[publicationName])
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))

    api(project(":jcv-db-core"))
    implementation(group = "com.ekino.oss.jcv", name = "jcv-core", version = "${project.extra["jcv-core.version"]}")
    implementation(group = "org.skyscreamer", name = "jsonassert", version = "${project.extra["jsonassert.version"]}")

    implementation("org.mongodb:mongodb-driver-sync:4.7.2")

    testImplementation(group = "org.junit.jupiter", name = "junit-jupiter", version = "${project.extra["junit.version"]}")

    testImplementation(group = "com.willowtreeapps.assertk", name = "assertk-jvm", version = "0.25") {
        exclude(group = "org.jetbrains.kotlin", module = "kotlin-reflect")
    }
}
