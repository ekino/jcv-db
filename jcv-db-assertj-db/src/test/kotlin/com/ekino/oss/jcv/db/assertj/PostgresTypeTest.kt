package com.ekino.oss.jcv.db.assertj

import com.ekino.oss.jcv.core.JsonValidator
import com.ekino.oss.jcv.core.validator.comparator
import com.ekino.oss.jcv.core.validator.validator
import com.ekino.oss.jcv.db.assertj.DbComparatorAssertJ.Companion.assertThatTable
import com.ekino.oss.jcv.db.assertj.extension.AssertDbExtension
import com.ekino.oss.jcv.db.assertj.extension.KPostgreSQLContainer
import com.ekino.oss.jcv.db.assertj.mapper.PostgresMapper
import org.assertj.db.type.Table
import org.assertj.db.type.Value
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.RegisterExtension
import org.skyscreamer.jsonassert.ValueMatcherException
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers

@Testcontainers
class PostgresTypeTest {

    @RegisterExtension
    @JvmField
    val assertDb = AssertDbExtension()

    companion object {
        @JvmStatic
        @Container
        val postgreContainer: KPostgreSQLContainer = KPostgreSQLContainer(
            "postgres:11.1"
        )
            .withDatabaseName("postgres-test")
            .withUsername("postgres-user")
            .withPassword("postgres-password")
            .withInitScript("com/ekino/oss/jcv/db/assertj/postgre/postgre_db_test.sql")
    }

    @BeforeEach
    fun setDbInformation() = assertDb.withDbInformation(postgreContainer.jdbcUrl, postgreContainer.username, postgreContainer.password)

    @Test
    fun `Should test most use type of postgresql database`() {
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
                    "column_timestamp": "{#date_time_format:iso_instant#}",
                    "column_timestamptz": "{#date_time_format:iso_instant#}",
                    "column_interval": "0 years 0 mons 100 days 0 hours 0 mins 0.0 secs",
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
        assertThatTable(assertDb.table("postgres_type_table"))
            .isValidAgainst(expected)
    }

    @Test
    fun `Should assert database with column restriction`() {
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
        assertThatTable(
            assertDb.table("table_test").setColumnsToCheck(
                arrayOf("id", "content")
            )
        ).isValidAgainst(expected)
    }

    @Test
    fun `Should assert database with ordering`() {
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
        assertThatTable(
            assertDb.table("table_test").setColumnsToOrder(
                arrayOf(Table.Order.desc("criteria_number"))
            )
        ).isValidAgainst(expected)
    }

    @Test
    fun `Should using custom mapper for this test`() {
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

        assertThatTable(assertDb.table("table_test"))
            .using(CustomPostgreSQLMapper())
            .isValidAgainst(expected)
    }

    @Test
    fun `Should valid json using custom validator for this test`() {
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

        assertThatTable(assertDb.table("table_test"))
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
        override fun mapTextType(value: Value) = value.value.toString().trim { it <= ' ' } + " CUSTOM"
    }
}
