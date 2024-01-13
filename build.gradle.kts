import net.researchgate.release.ReleasePlugin

plugins {
    base
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.dokka) apply false
    alias(libs.plugins.kotlin.quality) apply false
    alias(libs.plugins.researchgate.release)
}

allprojects {
    group = "com.ekino.oss.jcv-db"

    repositories {
        mavenCentral()
    }

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
