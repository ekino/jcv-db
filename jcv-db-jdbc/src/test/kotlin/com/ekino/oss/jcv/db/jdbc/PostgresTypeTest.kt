package com.ekino.oss.jcv.db.jdbc

import com.ekino.oss.jcv.core.JsonValidator
import com.ekino.oss.jcv.core.validator.comparator
import com.ekino.oss.jcv.core.validator.validator
import com.ekino.oss.jcv.db.config.DatabaseType
import com.ekino.oss.jcv.db.exception.DbAssertException
import com.ekino.oss.jcv.db.jdbc.extension.KPostgreSQLContainer
import com.ekino.oss.jcv.db.jdbc.mapper.PostgresMapper
import com.ekino.oss.jcv.db.jdbc.util.DBComparatorBuilder
import com.ekino.oss.jcv.db.util.takeIfIsJson
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test
import org.postgresql.util.PGInterval
import org.postgresql.util.PGobject
import org.skyscreamer.jsonassert.ValueMatcherException
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import java.sql.DriverManager
import java.util.UUID

@Testcontainers
class PostgresTypeTest {

    companion object {
        @JvmStatic
        @Container
        val postgreContainer: KPostgreSQLContainer = KPostgreSQLContainer("postgres:11.1")
            .withDatabaseName("postgres-test")
            .withUsername("postgres-user")
            .withPassword("postgres-password")
            .withInitScript("com/ekino/oss/jcv/db/jdbc/postgre/postgre_db_test.sql")
    }

    private fun assertThatQuery(query: String) = DBComparatorBuilder
        .create()
        .connection(DriverManager.getConnection(postgreContainer.jdbcUrl, postgreContainer.username, postgreContainer.password))
        .build(query)

    @Test
    fun `Should test types for postgresql database`() {
        val expected = // language=json
            """
                [
                  {
                    "id": "{#uuid#}",
                    "column_boolean": true,
                    "column_char_varying": "char varying",
                    "column_varchar": "varchar",
                    "column_character": "character",
                    "column_char": "char",
                    "column_text": "text",
                    "column_unlimited_varchar": "{#string_type#}",
                    "column_smallint": 123,
                    "column_int": 12355,
                    "column_serial": 3282,
                    "column_float": 123.2235,
                    "column_real": 125125.13,
                    "column_numeric": 312,
                    "column_date": "2019-06-07",
                    "column_time": "17:12:28",
                    "column_timestamp": "2019-06-06T22:00:00Z",
                    "column_timestamptz": "2019-06-06T22:00:00Z",
                    "column_interval": "0 years 0 mons 100 days 0 hours 0 mins 0.00 secs",
                    "column_json": [
                      {
                        "key": "Hello World !"
                      }
                    ],
                    "column_jsonb": {
                      "key": "Hello World !"
                    },
                    "column_point": { "x": 1, "y": 2 },
                    "column_line": {
                        "a": 39.69354838709677,
                        "b": -1,
                        "c": 75.46774193548387
                    },
                    "column_polygon": [
                        { "x": 1, "y": 2 },
                        { "x": 1, "y": 3 },
                        { "x": 2, "y": 3 },
                        { "x": 2, "y": 2 }
                    ],
                    "column_lseg": [
                        { "x": 1, "y": 2 },
                        { "x": 2, "y": 3 }
                    ],
                    "column_circle": {
                        "center": { "x": 1, "y": 2 },
                        "radius": 3
                    },
                    "column_box": [
                        { "x": 2, "y": 3 },
                        { "x": 1, "y": 2 }
                    ], 
                    "column_path": [
                      { "x": 1, "y": 2 },
                      { "x": 1, "y": 3 },
                      { "x": 2, "y": 3 },
                      { "x": 2, "y": 2 }
                    ],
                    "column_null": null
                  }
                ]
            """.trimIndent()
        assertThatQuery("SELECT * FROM postgres_type_table")
            .isValidAgainst(expected)
    }

    @Test
    fun `Should assert several rows of table`() {
        val expected = // language=json
            """
            [
              {
                "id": "d032dddb-593e-4ce6-a85c-4c2cf07dfcc5",
                "criteria_number": 2,
                "content": "content 3"
              },
              {
                "id": "048802f8-4f3e-4585-95ab-f88de3e6c78b",
                "criteria_number": 3,
                "content": "content 4"
              }
            ]
        """.trimIndent()
        assertThatQuery("SELECT * FROM table_test WHERE criteria_number > 1")
            .isValidAgainst(expected)
    }

    @Test
    fun `Should assert several columns of all rows`() {
        val expected = // language=json
            """
            [
              {
                "id": "07621b34-35dc-4b8f-94f4-b0f7e98b4088",
                "content": "content 1"
              },
              {
                "id": "24640375-108f-4b29-b21c-3092fc02c83e",
                "content": "content 2"
              },
              {
                "id": "d032dddb-593e-4ce6-a85c-4c2cf07dfcc5",
                "content": "content 3"
              },
              {
                "id": "048802f8-4f3e-4585-95ab-f88de3e6c78b",
                "content": "content 4"
              }
            ]
        """.trimIndent()
        assertThatQuery("SELECT id, content FROM table_test")
            .isValidAgainst(expected)
    }

    @Test
    fun `Should assert columns with order`() {
        val expected = // language=json
            """
              [
                {
                  "id": "048802f8-4f3e-4585-95ab-f88de3e6c78b",
                  "criteria_number": 3,
                  "content": "content 4"
                },
                {
                  "id": "d032dddb-593e-4ce6-a85c-4c2cf07dfcc5",
                  "criteria_number": 2,
                  "content": "content 3"
                },
                {
                  "id": "24640375-108f-4b29-b21c-3092fc02c83e",
                  "criteria_number": 1,
                  "content": "content 2"
                },
                {
                  "id": "07621b34-35dc-4b8f-94f4-b0f7e98b4088",
                  "criteria_number": 0,
                  "content": "content 1"
                }
              ]
        """.trimIndent()
        assertThatQuery("SELECT * FROM table_test ORDER BY criteria_number DESC")
            .isValidAgainst(expected)
    }

    @Test
    fun `Should get an error when trying to compare table with invalid order json`() {
        val expected = // language=json
            """
              [
                {
                  "id": "d032dddb-593e-4ce6-a85c-4c2cf07dfcc5",
                  "criteria_number": 2,
                  "content": "content 3"
                },
                {
                  "id": "048802f8-4f3e-4585-95ab-f88de3e6c78b",
                  "criteria_number": 3,
                  "content": "content 4"
                },
                {
                  "id": "24640375-108f-4b29-b21c-3092fc02c83e",
                  "criteria_number": 1,
                  "content": "content 2"
                },
                {
                  "id": "07621b34-35dc-4b8f-94f4-b0f7e98b4088",
                  "criteria_number": 0,
                  "content": "content 1"
                }
              ]
        """.trimIndent()
        assertThatThrownBy {
            assertThatQuery("SELECT * FROM table_test ORDER BY criteria_number DESC")
                .isValidAgainst(expected)
        }.isInstanceOf(AssertionError::class.java)
    }

    @Test
    fun `Should valid table against json with custom connection`() {
        val postgreContainerSecond: KPostgreSQLContainer = KPostgreSQLContainer("postgres:11.1")
            .withDatabaseName("postgres-test-second ")
            .withUsername("postgres-user-second")
            .withPassword("postgres-password-second")
            .withInitScript("com/ekino/oss/jcv/db/jdbc/postgre/postgre_db_test_second.sql")
        postgreContainerSecond.start()

        val expected = // language=json
            """
              [
                {
                  "id": "07621b34-35dc-4b8f-94f4-b0f7e98b4088",
                  "criteria_number": 0,
                  "content": "content 1"
                }
              ]
        """.trimIndent()
        assertThatQuery("SELECT * FROM table_test_second")
            .using(DriverManager.getConnection(postgreContainerSecond.jdbcUrl, postgreContainerSecond.username, postgreContainerSecond.password))
            .isValidAgainst(expected)

        postgreContainerSecond.stop()
    }

    @Test
    fun `Should valid table against json with custom matcher`() {
        val expected = // language=json
            """
              [
                {
                  "id": "07621b34-35dc-4b8f-94f4-b0f7e98b4088",
                  "criteria_number": 0,
                  "content": "content 1 CUSTOM"
                },
                {
                  "id": "24640375-108f-4b29-b21c-3092fc02c83e",
                  "criteria_number": 1,
                  "content": "content 2 CUSTOM"
                },
                {
                  "id": "d032dddb-593e-4ce6-a85c-4c2cf07dfcc5",
                  "criteria_number": 2,
                  "content": "content 3 CUSTOM"
                },
                {
                  "id": "048802f8-4f3e-4585-95ab-f88de3e6c78b",
                  "criteria_number": 3,
                  "content": "content 4 CUSTOM"
                }
              ]
        """.trimIndent()
        assertThatQuery("SELECT * FROM table_test")
            .using(DatabaseType.POSTGRESQL to CustomPostgreSQLMapper())
            .isValidAgainst(expected)
    }

    @Test
    fun `Should valid table against json with custom validator`() {
        val expected = // language=json
            """
              [
                {
                  "id": "07621b34-35dc-4b8f-94f4-b0f7e98b4088",
                  "criteria_number": 0,
                  "content": "{#custom_notempty#}"
                },
                {
                  "id": "24640375-108f-4b29-b21c-3092fc02c83e",
                  "criteria_number": 1,
                  "content": "{#custom_notempty#}"
                },
                {
                  "id": "d032dddb-593e-4ce6-a85c-4c2cf07dfcc5",
                  "criteria_number": 2,
                  "content": "{#custom_notempty#}"
                },
                {
                  "id": "048802f8-4f3e-4585-95ab-f88de3e6c78b",
                  "criteria_number": 3,
                  "content": "{#custom_notempty#}"
                }
              ]
        """.trimIndent()
        assertThatQuery("SELECT * FROM table_test")
            .using(customValidator())
            .isValidAgainst(expected)
    }

    private fun customValidator(): JsonValidator<String> {
        return validator {
            templatedValidator<String>("custom_notempty", comparator { actual, expected ->
                if (actual == null || actual.isEmpty()) {
                    throw ValueMatcherException("Value is null or empty", expected, actual)
                }
                true
            })
        }
    }

    class CustomPostgreSQLMapper : PostgresMapper() {
        override fun getValueFromColumn(value: Any): Any = when (value) {
            is Float -> java.lang.Double.valueOf(value.toString())
            is Number -> value
            is Boolean -> value
            is UUID -> value.toString()
            is PGobject -> value.value.takeIfIsJson() ?: throw DbAssertException(
                "Unable to parse pg object to json"
            )
            is PGInterval -> value.value
            else -> value.toString().trim { it <= ' ' } + " CUSTOM"
        }
    }
}
