import net.researchgate.release.ReleasePlugin

plugins {
    base
    kotlin("jvm") version "1.9.21"
    id("org.jetbrains.dokka") version "1.7.10" apply false
    id("com.ekino.oss.plugin.kotlin-quality") version "4.2.0" apply false
    id("net.researchgate.release") version "3.0.2"
}

allprojects {
    group = "com.ekino.oss.jcv-db"

    repositories {
        mavenCentral()
    }

    project.extra.set("jcv-core.version", "1.5.0")
    project.extra.set("jsonassert.version", "1.5.0")
    project.extra.set("postgres.version", "42.5.0")
    project.extra.set("mssql.version", "11.2.0.jre11")
    project.extra.set("mysql.version", "8.0.29")
    project.extra.set("junit.version", "5.9.0")
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
                    name = "JCV-DB"
                    description = "JSON Content Validator for database (JCV-DB)."
                    url = "https://github.com/ekino/jcv-db"
                    licenses {
                        license {
                            name = "MIT License (MIT)"
                            url = "https://opensource.org/licenses/mit-license"
                        }
                    }
                    developers {
                        developer {
                            name = "Nicolas Gunther"
                            email = "nicolas.gunther@ekino.com"
                            organization = "ekino"
                            organizationUrl = "https://www.ekino.com/"
                        }
                    }
                    scm {
                        connection = "scm:git:git://github.com/ekino/jcv-db.git"
                        developerConnection = "scm:git:ssh://github.com:ekino/jcv-db.git"
                        url = "https://github.com/ekino/jcv-db"
                    }
                    organization {
                        name = "ekino"
                        url = "https://www.ekino.com/"
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
