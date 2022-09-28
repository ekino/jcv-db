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

    implementation(group = "org.postgresql", name = "postgresql", version = "${project.extra["postgres.version"]}")
    implementation(group = "com.microsoft.sqlserver", name = "mssql-jdbc", version = "${project.extra["mssql.version"]}")
    implementation(group = "mysql", name = "mysql-connector-java", version = "${project.extra["mysql.version"]}")
    implementation(group = "org.springframework", name = "spring-jdbc", version = "5.3.23")
    implementation(group = "com.fasterxml.jackson.core", name = "jackson-databind", version = "2.12.6.1")

    testImplementation(group = "org.junit.jupiter", name = "junit-jupiter", version = "${project.extra["junit.version"]}")
    testImplementation(group = "org.assertj", name = "assertj-core", version = "3.23.1")
}
