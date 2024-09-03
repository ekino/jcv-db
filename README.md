# JCV-DB

JSON Content Validator for Database (JCV-DB) allow you to compare database contents against a JSON with [JCV](https://github.com/ekino/jcv) validators.

[![Build Status](https://travis-ci.org/ekino/jcv-db.svg?branch=master)](https://travis-ci.org/ekino/jcv-db)
[![GitHub (pre-)release](https://img.shields.io/github/release/ekino/jcv-db/all.svg)](https://github.com/ekino/jcv-db/releases)
[![Maven Central](https://img.shields.io/maven-central/v/com.ekino.oss.jcv-db/jcv-db-core)](https://search.maven.org/search?q=g:com.ekino.oss.jcv-db)
[![GitHub license](https://img.shields.io/github/license/ekino/jcv.svg)](https://github.com/ekino/jcv/blob/master/LICENSE.md)

## Table of contents

* [Summary](#summary)
* [Prerequisites](#prerequisites)
* [Quick start](#quick-start)
    * [Assertj-db module](#assertj-db)
    * [JDBC module](#jdbc-module)
    * [Cassandra module](#cassandra-module)
    * [Mongo database module](#mongo-database-module)

## Summary

JCV-DB provides assertions to validate database content against a json file. The goal is for now to support SQL and NoSQL databases : PostgreSQL, MySQL, MSSQL, Cassandra and mongoDB. 
[JCV](https://github.com/ekino/jcv) validators are also supported making tests light and exhaustive.

## Prerequisites

If you want to test this project locally we must have the following software install:
-	Java 11 (if launching without using Gradle)
-	Docker
-	Docker compose

Some tests require that the database container is running. To start the containers, run the following command in the project root:
````bash
docker compose up -d
````

## Quick Start

### Assertj-db

A JCV-DB module that supports [Assertj-db](https://github.com/joel-costigliola/assertj-db). Only PostgreSQL, MySQL, MSSQL server are supported in this module.

#### Example

````kotlin
import static com.ekino.oss.jcv.db.assertj.DbComparatorAssertJ.assertThatTable

@Test
fun testContentTable() {
    assertThatTable(assertDb.table("table_name"))
    .isValidAgainst("""[
        {
            "id": "e3881fb1-b3dd-4701-b08c-f5d17389edfa",
            "number_field": 1,
            "boolean_field: true,
            "string_field": "Hello"
        }
    ]""".trimIndent())
}
````

#### Dependencies

Maven

```xml
<dependencies>
  ...
  <dependency>
    <groupId>org.skyscreamer</groupId>
    <artifactId>jsonassert</artifactId>
    <version>1.5.0</version>
    <scope>test</scope>
  </dependency>
  <dependency>
    <groupId>org.assertj</groupId>
    <artifactId>assertj-db</artifactId>
    <version>1.2.0</version>
    <scope>test</scope>
  </dependency>
  <dependency>
    <groupId>com.ekino.oss.jcv</groupId>
    <artifactId>jcv-core</artifactId>
    <version>1.4.1</version>
    <scope>test</scope>
  </dependency>
  <dependency>
    <groupId>com.ekino.oss.jcv-db</groupId>
    <artifactId>jcv-db-assertj-db</artifactId>
    <version>0.0.5</version>
    <scope>test</scope>
  </dependency>
  ...
</dependencies>
```

Gradle

```groovy
dependencies {
    ...
    testImplementation 'org.skyscreamer:jsonassert:1.5.0'
    testImplementation 'org.assertj:assertj-db:1.2.0'
    testImplementation 'com.ekino.oss.jcv:jcv-core:1.4.1'
    testImplementation 'com.ekino.oss.jcv-db:jcv-db-assertj-db:0.0.3'
    ...
}
```
### Jdbc module

A JCV-DB module that allow you to execute sql requests. Only PostgreSQL, MySQL, MSSQL server are supported in this module.

#### Example

````kotlin
import static com.ekino.oss.jcv.db.jdbc.DbComparatorJDBC.assertThatQuery

@Test
fun testContentTable() {
    assertThatQuery("select * from table_name")
    .isValidAgainst("""[
        {
            "id": "e3881fb1-b3dd-4701-b08c-f5d17389edfa",
            "number_field": 1,
            "boolean_field: true,
            "string_field": "Hello"
        }
    ]""".trimIndent())
}
````

#### Dependencies

Maven

```xml
<dependencies>
  ...
  <dependency>
    <groupId>org.skyscreamer</groupId>
    <artifactId>jsonassert</artifactId>
    <version>1.5.0</version>
    <scope>test</scope>
  </dependency>
  <dependency>
    <groupId>org.assertj</groupId>
    <artifactId>assertj-db</artifactId>
    <version>1.2.0</version>
    <scope>test</scope>
  </dependency>
  <dependency>
    <groupId>com.ekino.oss.jcv</groupId>
    <artifactId>jcv-core</artifactId>
    <version>1.4.1</version>
    <scope>test</scope>
  </dependency>
  <dependency>
    <groupId>com.ekino.oss.jcv-db</groupId>
    <artifactId>jcv-db-jdbc</artifactId>
    <version>0.0.5</version>
    <scope>test</scope>
  </dependency>
  ...
</dependencies>
```

Gradle

```groovy
dependencies {
    ...
    testImplementation 'org.skyscreamer:jsonassert:1.5.0'
    testImplementation 'org.assertj:assertj-db:1.2.0'
    testImplementation 'com.ekino.oss.jcv:jcv-core:1.4.1'
    testImplementation 'com.ekino.oss.jcv-db:jcv-db-jdbc:0.0.3'
    ...
}
```

### Cassandra module

A JCV-DB module that allow you to execute cql requests and criteria builder in cassandra database

#### Example

````kotlin
import static com.ekino.oss.jcv.db.cassandra.DbComparatorCassandra.assertThatQuery

@Test
fun testContentTable() {
    assertThatQuery("select * from table_name")
    .isValidAgainst("""[
        {
            "id": "e3881fb1-b3dd-4701-b08c-f5d17389edfa",
            "number_field": 1,
            "boolean_field: true,
            "string_field": "Hello"
        }
    ]""".trimIndent())
}
````

#### Dependencies

Maven

```xml
<dependencies>
  ...
  <dependency>
    <groupId>org.skyscreamer</groupId>
    <artifactId>jsonassert</artifactId>
    <version>1.5.0</version>
    <scope>test</scope>
  </dependency>
  <dependency>
    <groupId>org.assertj</groupId>
    <artifactId>assertj-db</artifactId>
    <version>1.2.0</version>
    <scope>test</scope>
  </dependency>
  <dependency>
    <groupId>com.ekino.oss.jcv</groupId>
    <artifactId>jcv-core</artifactId>
    <version>1.4.1</version>
    <scope>test</scope>
  </dependency>
  <dependency>
    <groupId>com.ekino.oss.jcv-db</groupId>
    <artifactId>jcv-db-cassandra</artifactId>
    <version>0.0.5</version>
    <scope>test</scope>
  </dependency>
  ...
</dependencies>
```

Gradle

```groovy
dependencies {
    ...
    testImplementation 'org.skyscreamer:jsonassert:1.5.0'
    testImplementation 'org.assertj:assertj-db:1.2.0'
    testImplementation 'com.ekino.oss.jcv:jcv-core:1.4.1'
    testImplementation 'com.ekino.oss.jcv-db:jcv-db-cassandra:0.0.3'
    ...
}
```

### Mongo database module

A JCV-DB module that allow you to mongo database content against a json file.

#### Example

````kotlin
import static com.ekino.oss.jcv.db.mongo.DbComparatorMongo.assertThatCollection

@Test
fun testContentTable() {
    assertThatQuery(database.getCollection("testCollection").find(eq("name", "test-mongo-db"))
    .isValidAgainst("""[
        {
            "id": "e3881fb1-b3dd-4701-b08c-f5d17389edfa",
            "number_field": 1,
            "boolean_field: true,
            "string_field": "Hello"
        }
    ]""".trimIndent())
}
````

#### Dependencies

Maven

```xml
<dependencies>
  ...
  <dependency>
    <groupId>org.skyscreamer</groupId>
    <artifactId>jsonassert</artifactId>
    <version>1.5.0</version>
    <scope>test</scope>
  </dependency>
  <dependency>
    <groupId>org.assertj</groupId>
    <artifactId>assertj-db</artifactId>
    <version>1.2.0</version>
    <scope>test</scope>
  </dependency>
  <dependency>
    <groupId>com.ekino.oss.jcv</groupId>
    <artifactId>jcv-core</artifactId>
    <version>1.4.1</version>
    <scope>test</scope>
  </dependency>
  <dependency>
    <groupId>com.ekino.oss.jcv-db</groupId>
    <artifactId>jcv-db-mongo</artifactId>
    <version>0.0.5</version>
    <scope>test</scope>
  </dependency>
  ...
</dependencies>
```

Gradle

```groovy
dependencies {
    ...
    testImplementation 'org.skyscreamer:jsonassert:1.5.0'
    testImplementation 'org.assertj:assertj-db:1.2.0'
    testImplementation 'com.ekino.oss.jcv:jcv-core:1.4.1'
    testImplementation 'com.ekino.oss.jcv-db:jcv-db-mongo:0.0.1'
    ...
}
```


