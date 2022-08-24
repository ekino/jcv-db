plugins {
    java

    id("org.springframework.boot") version "2.2.5.RELEASE"
}

val jsonassertVersion: String  = "1.5.0"
val jcvVersion: String = "1.4.2"

apply {
    plugin("io.spring.dependency-management")
}

repositories {
    mavenCentral()
}

dependencies {

    implementation("org.springframework.boot:spring-boot-starter-data-cassandra")
    implementation("org.springframework.boot:spring-boot-starter-web")

    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")

    implementation("io.dropwizard.metrics:metrics-core:3.2.2")
    implementation("com.datastax.oss:java-driver-query-builder:4.2.0")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.assertj:assertj-core")
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation("org.skyscreamer:jsonassert:$jsonassertVersion")
    testImplementation("com.ekino.oss.jcv:jcv-core:$jcvVersion")
    testImplementation("com.ekino.oss.jcv-db:jcv-db-cassandra:0.0.5")
    testImplementation("commons-io:commons-io:2.6")
}

tasks {
    withType<JavaCompile> {
        sourceCompatibility = JavaVersion.VERSION_1_8.toString()
    }

    withType<Test> {
        useJUnitPlatform()
        jvmArgs("-Duser.language=en")
    }
}
