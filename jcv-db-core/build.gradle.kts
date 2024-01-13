import org.jetbrains.dokka.gradle.DokkaTask
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    alias(libs.plugins.kotlin.jvm)
    `java-library`
    signing
    jacoco
    alias(libs.plugins.dokka)
    alias(libs.plugins.kotlin.quality)
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
    archiveClassifier = "javadoc"
    from(layout.buildDirectory.file("dokka"))
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
                reportUndocumented = false
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
    implementation(libs.kotlin.stdlib)
    implementation(libs.kotlin.reflect)

    implementation(libs.jcv.core)
    implementation(libs.jsonassert)
    implementation(libs.jackson.databind)
    implementation(libs.jts.core)

    testImplementation(libs.junit)
    testImplementation(libs.assertk.jvm) {
        exclude(group = "org.jetbrains.kotlin", module = "kotlin-reflect")
    }
}
