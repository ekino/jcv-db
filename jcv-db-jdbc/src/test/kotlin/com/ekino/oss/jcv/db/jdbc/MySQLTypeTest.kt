package com.ekino.oss.jcv.db.jdbc

import com.ekino.oss.jcv.core.JsonValidator
import com.ekino.oss.jcv.core.validator.comparator
import com.ekino.oss.jcv.core.validator.validator
import com.ekino.oss.jcv.db.config.DatabaseType
import com.ekino.oss.jcv.db.jdbc.extension.KMySQLContainer
import com.ekino.oss.jcv.db.jdbc.mapper.MySQLMapper
import com.ekino.oss.jcv.db.jdbc.util.DBComparatorBuilder
import com.ekino.oss.jcv.db.util.takeIfIsJson
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test
import org.skyscreamer.jsonassert.ValueMatcherException
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import java.sql.DriverManager
import java.util.UUID

@Testcontainers
class MySQLTypeTest {

    companion object {
        @JvmField
        @Container
        val mySQLContainer: KMySQLContainer = KMySQLContainer("mysql:8.0")
            .withDatabaseName("mysql-text")
            .withUsername("mysql-user")
            .withPassword("mysql-password")
            .withInitScript("com/ekino/oss/jcv/db/jdbc/mysql/mysql_db_test.sql")
    }

    private fun assertThatQuery(query: String) = DBComparatorBuilder
        .create()
        .connection(DriverManager.getConnection(mySQLContainer.jdbcUrl, mySQLContainer.username, mySQLContainer.password))
        .build(query)

    @Test
    fun `Should test types for mysql database`() {
        val expected = // language=json
            """
                [
                  {
                    "column_date": "9999-12-31",
                    "column_int": 12345,
                    "column_year": "2155-01-01",
                    "column_time": "01:11:12",
                    "column_text": "text",
                    "column_char": "c",
                    "column_numeric": 123123,
                    "column_tinyint": 23,
                    "column_float": 1.24124E8,
                    "column_json": [
                      {
                        "key": "Hello World !"
                      }
                    ],
                    "column_integer": 123,
                    "column_double": 21345.22,
                    "column_mediumint": 6897654,
                    "column_enum": "enum1",
                    "column_varchar": "varchar",
                    "column_smallint": 1234,
                    "id": 1,
                    "column_timestamp": "1994-11-01 10:59:59.0",
                    "column_decimal": 7895,
                    "column_bigint": 896932589235,
                    "column_datetime": "10000-01-01 00:59:59.0",
                    "column_point": {
                        "x": 1,
                        "y": 3
                    },
                    "column_multipoint": [
                      { "x": 0, "y": 0 },
                      { "x": 1, "y": 2 },
                      { "x": 2, "y": 4 }
                    ],
                    "column_polygon": [
                      { "x": 40, "y": 40 },
                      { "x": 48, "y": 40 },
                      { "x": 52, "y": 49 },
                      { "x": 40, "y": 49 },
                      { "x": 40, "y": 40 },
                      { "x": 45, "y": 43 },
                      { "x": 44, "y": 45 },
                      { "x": 47, "y": 49 },
                      { "x": 43, "y": 47 },
                      { "x": 40, "y": 40 }
                    ],
                    "column_multipolygon": [
                      [
                        { "x": 140, "y": 140 },
                        { "x": 148, "y": 140 },
                        { "x": 152, "y": 149 },
                        { "x": 140, "y": 149 },
                        { "x": 140, "y": 140 },
                        { "x": 145, "y": 143 },
                        { "x": 144, "y": 145 },
                        { "x": 147, "y": 149 },
                        { "x": 143, "y": 147 },
                        { "x": 140, "y": 140 }
                      ]
                    ],
                    "column_multilinestring": [
                      [
                        { "x": 12, "y": 12 }, 
                        { "x": 22, "y": 22 }
                      ], 
                      [
                        { "x": 19, "y": 19 },
                        { "x": 32, "y": 18 }
                      ]
                    ],
                    "column_linestring": [
                        { "x": 0, "y": 0 },
                        { "x": 1, "y": 2 },
                        { "x": 2, "y": 4 },
                        { "x": 3, "y": 6 }
                    ],
                    "column_null": null
                  }
                ]
            """.trimIndent()

        assertThatQuery("SELECT * FROM mysql_type_table")
            .isValidAgainst(expected)
    }

    @Test
    fun `Should assert several rows of table`() {
        val expected = // language=json
            """
            [
              {
                "id": 1236,
                "criteria_number": 2,
                "content": "content 3"
              },
              {
                "id": 1237,
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
                "id": 1234,
                "content": "content 1"
              },
              {
                "id": 1235,
                "content": "content 2"
              },
              {
                "id": 1236,
                "content": "content 3"
              },
              {
                "id": 1237,
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
                  "id": 1237,
                  "criteria_number": 3,
                  "content": "content 4"
                },
                {
                  "id": 1236,
                  "criteria_number": 2,
                  "content": "content 3"
                },
                {
                  "id": 1235,
                  "criteria_number": 1,
                  "content": "content 2"
                },
                {
                  "id": 1234,
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
                  "id": 1236,
                  "criteria_number": 2,
                  "content": "content 3"
                },
                {
                  "id": 1237,
                  "criteria_number": 3,
                  "content": "content 4"
                },
                {
                  "id": 1235,
                  "criteria_number": 1,
                  "content": "content 2"
                },
                {
                  "id": 1234,
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
        val mySQLContainerSecond: KMySQLContainer = KMySQLContainer("mysql:8.0")
            .withDatabaseName("mysql-text-second")
            .withUsername("mysql-user-second")
            .withPassword("mysql-password-second")
            .withInitScript("com/ekino/oss/jcv/db/jdbc/mysql/mysql_db_test_second.sql")
        mySQLContainerSecond.start()

        val expected = // language=json
            """
              [
                {
                  "id": 1,
                  "criteria_number": 0,
                  "content": "content 1"
                }
              ]
        """.trimIndent()
        assertThatQuery("SELECT * FROM table_test_second")
            .using(DriverManager.getConnection(mySQLContainerSecond.jdbcUrl, mySQLContainerSecond.username, mySQLContainerSecond.password))
            .isValidAgainst(expected)

        mySQLContainerSecond.stop()
    }

    @Test
    fun `Should valid table against json with custom matcher`() {
        val expected = // language=json
            """
              [
                {
                  "id": 1234,
                  "criteria_number": 0,
                  "content": "content 1 CUSTOM"
                },
                {
                  "id": 1235,
                  "criteria_number": 1,
                  "content": "content 2 CUSTOM"
                },
                {
                  "id": 1236,
                  "criteria_number": 2,
                  "content": "content 3 CUSTOM"
                },
                {
                  "id": 1237,
                  "criteria_number": 3,
                  "content": "content 4 CUSTOM"
                }
              ]
        """.trimIndent()
        assertThatQuery("SELECT * FROM table_test")
            .using(DatabaseType.MYSQL to CustomMySQLMapper())
            .isValidAgainst(expected)
    }

    @Test
    fun `Should valid table against json with custom validator`() {
        val expected = // language=json
            """
              [
                {
                  "id": 1234,
                  "criteria_number": 0,
                  "content": "{#custom_notempty#}"
                },
                {
                  "id": 1235,
                  "criteria_number": 1,
                  "content": "{#custom_notempty#}"
                },
                {
                  "id": 1236,
                  "criteria_number": 2,
                  "content": "{#custom_notempty#}"
                },
                {
                  "id": 1237,
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

    class CustomMySQLMapper : MySQLMapper() {
        override fun getValueFromColumn(value: Any): Any = when (value) {
            is Float -> java.lang.Double.valueOf(value.toString())
            is Number -> value
            is Boolean -> value
            is UUID -> value.toString()
            is String -> value.takeIfIsJson() ?: "$value CUSTOM"
            else -> value.toString().trim { it <= ' ' }
        }
    }
}
