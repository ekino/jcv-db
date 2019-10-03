# JCV-DB

JSON Content Validator for Database (JCV-DB) allow you to compare database contents against a JSON with JCV validators.

## Table of contents

## Summary

JCV-DB provides assertions to validate database content against a json file. The goal is for now to support SQL and NoSQL databases : PostgreSQL, MySQL, MSSQL, Cassandra and mongoDB. 
[JCV](https://github.com/ekino/jcv) validators are also supported making tests light and exhaustive.

## Quick Start

### Core module

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
    <artifactId>jcv-assertj-db</artifactId>
    <version>0.0.1</version>
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
    testImplementation 'jcvcom.ekino.oss.jcv-db:jcv-assertj-db:0.0.1'
    ...
}
```
### Jdbc

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
    <artifactId>jcv-jdbc</artifactId>
    <version>0.0.1</version>
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
    testImplementation 'jcvcom.ekino.oss.jcv-db:jcv-jdbc:0.0.1'
    ...
}
```

### Cassandra

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
    <artifactId>jcv-cassandra</artifactId>
    <version>0.0.1</version>
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
    testImplementation 'jcvcom.ekino.oss.jcv-db:jcv-cassandra:0.0.1'
    ...
}
```

### Mongo-db

A JCV-DB module that allow you 

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
    <artifactId>jcv-mongo</artifactId>
    <version>0.0.1</version>
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
    testImplementation 'jcvcom.ekino.oss.jcv-db:jcv-mongo:0.0.1'
    ...
}
```

## Validators

