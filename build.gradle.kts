import net.researchgate.release.ReleasePlugin

plugins {
    base
    kotlin("jvm") version "1.3.50"
    id("net.researchgate.release") version "2.8.1"
}

allprojects {
    group = "com.ekino.oss.jcv-db"

    repositories {
        mavenCentral()
        jcenter()
    }

    project.extra.set("jcv-core.version", "1.4.1")
    project.extra.set("jsonassert.version", "1.5.0")
    project.extra.set("postgre.version", "42.2.8")
    project.extra.set("mssql.version", "7.4.1.jre8")
    project.extra.set("mysql.version", "8.0.17")
    project.extra.set("junit.version", "5.5.1")
    project.extra.set("testcontainers.version", "1.12.1")
}

tasks.create("printVersion") {
    doLast {
        val version: String by project
        println(version)
    }
}

subprojects {

    apply<MavenPublishPlugin>()
    apply<ReleasePlugin>()

    configure<PublishingExtension> {
        publications {
            create<MavenPublication>("mavenJava") {
                pom {
                    name.set("JCV-DB")
                    description.set("JSON Content Validator for database (JCV-DB).")
                    url.set("https://github.com/ekino/jcv-db")
                    licenses {
                        license {
                            name.set("MIT License (MIT)")
                            url.set("https://opensource.org/licenses/mit-license")
                        }
                    }
                    developers {
                        developer {
                            name.set("Nicolas Gunther")
                            email.set("nicolas.gunther@ekino.com")
                            organization.set("ekino")
                            organizationUrl.set("https://www.ekino.com/")
                        }
                    }
                    scm {
                        connection.set("scm:git:git://github.com/ekino/jcv-db.git")
                        developerConnection.set("scm:git:ssh://github.com:ekino/jcv-db.git")
                        url.set("https://github.com/ekino/jcv-db")
                    }
                    organization {
                        name.set("ekino")
                        url.set("https://www.ekino.com/")
                    }
                }
                repositories {
                    maven {
                        val ossrhUrl: String? by project
                        val ossrhUsername: String? by project
                        val ossrhPassword: String? by project

                        url = uri(ossrhUrl ?: "")

                        credentials {
                            username = ossrhUsername
                            password = ossrhPassword
                        }
                    }
                }
            }
        }
    }
}