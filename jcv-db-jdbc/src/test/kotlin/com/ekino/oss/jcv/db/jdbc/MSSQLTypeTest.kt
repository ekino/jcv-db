package com.ekino.oss.jcv.db.jdbc

import com.ekino.oss.jcv.core.JsonValidator
import com.ekino.oss.jcv.core.validator.comparator
import com.ekino.oss.jcv.core.validator.validator
import com.ekino.oss.jcv.db.jdbc.mapper.MSSQLMapper
import com.ekino.oss.jcv.db.jdbc.util.DBComparatorBuilder
import com.ekino.oss.jcv.db.util.takeIfIsJson
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test
import org.skyscreamer.jsonassert.ValueMatcherException
import java.sql.DriverManager
import java.util.UUID

class MSSQLTypeTest {

    companion object {
        const val JDBC_URL = "jdbc:sqlserver://localhost:1433;encrypt=true;trustServerCertificate=true;"
        const val USERNAME = "SA"
        const val PASSWORD = "A_Str0ng_Required_Password"
    }

    private fun assertThatQuery(query: String) = DBComparatorBuilder
        .create()
        .connection(DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD))
        .build(query)

    @Test
    fun `Should test types for mysql database`() {
        val expected = // language=json
            """
                [
                  {
                    "id": "AAF7393D-3EB1-4D4A-B022-208D582B3B3F",
                    "column_bigint": 9223372036854775806,
                    "column_int": 2147483646,
                    "column_smallint": 32766,
                    "column_tinyint": 254,
                    "column_decimal": 12345.67898,
                    "column_numeric": 12345.67898,
                    "column_money": 922337203685456.5808,
                    "column_smallmoney": 214736.3648,
                    "column_float": 98472983.329043,
                    "column_real": 4.079324,
                    "column_date": "9999-12-31",
                    "column_datetime2": "9999-12-31 23:59:59.0",
                    "column_datetime": "9999-12-31 23:59:59.0",
                    "column_datetimeoffset": "9999-12-31 23:59:59 +00:00",
                    "column_smalldatetime": "2079-06-06 00:00:00.0",
                    "column_time": "00:11:12",
                    "column_char": "c",
                    "column_text": "text",
                    "column_varchar": "varchar",
                    "column_point": {
                      "x": 3,
                      "y": 4,
                      "z": 7
                    },
                    "column_multipoint": [
                      { "x": 2, "y": 3 },
                      { "x": 7, "y": 8, "z": 9.5 }
                    ],
                    "column_polygon": [
                      { "x": 0, "y": 0 },
                      { "x": 0, "y": 3 },
                      { "x": 3, "y": 3 },
                      { "x": 3, "y": 0 },
                      { "x": 0, "y": 0 },
                      { "x": 1, "y": 1 },
                      { "x": 1, "y": 2 },
                      { "x": 2, "y": 1 },
                      { "x": 1, "y": 1 }
                    ],
                    "column_multipolygon": [
                      [
                        { "x": 0, "y": 0 },
                        { "x": 0, "y": 3 },
                        { "x": 3, "y": 3 },
                        { "x": 3, "y": 0 },
                        { "x": 0, "y": 0 },
                        { "x": 1, "y": 1 },
                        { "x": 1, "y": 2 },
                        { "x": 2, "y": 1 },
                        { "x": 1, "y": 1 }
                      ],
                      [
                        { "x": 9, "y": 9 },
                        { "x": 9, "y": 10 },
                        { "x": 10, "y": 9 },
                        { "x": 9, "y": 9 }
                      ]
                    ],
                    "column_multilinestring": [
                      [
                          { "x": 0, "y": 2 },
                          { "x": 1, "y": 1 }
                      ],
                      [
                          { "x": 1, "y": 0 },
                          { "x": 1, "y": 1 }
                      ]
                    ],
                    "column_linestring": [
                      { "x": 1, "y": 1 },
                      { "x": 2, "y": 4 },
                      { "x": 3, "y": 9 }
                    ],
                    "column_null": null
                  }
                ]
            """.trimIndent()
        assertThatQuery("SELECT * FROM mssql_type_table")
            .isValidAgainst(expected)
    }

    @Test
    fun `Should assert several rows of table`() {
        val expected = // language=json
            """
            [
              {
                "id": "642A00A5-18F2-47CF-9E5C-0161156FF0A0",
                "criteria_number": 3,
                "content": "content 4"
              },
              {
                "id": "63A36810-C582-42EC-9AB6-322F15F0C3B9",
                "criteria_number": 2,
                "content": "content 3"
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
                    "id": "642A00A5-18F2-47CF-9E5C-0161156FF0A0",
                    "content": "content 4"
                  },
                  {
                    "id": "F9B5B79D-606E-4251-8967-12DBC5611C70",
                    "content": "content 2"
                  },
                  {
                    "id": "63A36810-C582-42EC-9AB6-322F15F0C3B9",
                    "content": "content 3"
                  },
                  {
                    "id": "A849D2F7-7814-4D37-989E-A7D6EE4B5E05",
                    "content": "content 1"
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
                  "id": "642A00A5-18F2-47CF-9E5C-0161156FF0A0",
                  "criteria_number": 3,
                  "content": "content 4"
                },
                {
                  "id": "63A36810-C582-42EC-9AB6-322F15F0C3B9",
                  "criteria_number": 2,
                  "content": "content 3"
                },
                {
                  "id": "F9B5B79D-606E-4251-8967-12DBC5611C70",
                  "criteria_number": 1,
                  "content": "content 2"
                },
                {
                  "id": "A849D2F7-7814-4D37-989E-A7D6EE4B5E05",
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
                  "id": "63A36810-C582-42EC-9AB6-322F15F0C3B9",
                  "criteria_number": 2,
                  "content": "content 3"
                },
                {
                  "id": "642A00A5-18F2-47CF-9E5C-0161156FF0A0",
                  "criteria_number": 3,
                  "content": "content 4"
                },
                {
                  "id": "F9B5B79D-606E-4251-8967-12DBC5611C70",
                  "criteria_number": 1,
                  "content": "content 2"
                },
                {
                  "id": "A849D2F7-7814-4D37-989E-A7D6EE4B5E05",
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
    fun `Should valid table against json with custom mapper`() {
        val expected = // language=json
            """
              [
                {
                  "id": "642A00A5-18F2-47CF-9E5C-0161156FF0A0 CUSTOM",
                  "criteria_number": 3,
                  "content": "content 4 CUSTOM"
                },
                {
                  "id": "F9B5B79D-606E-4251-8967-12DBC5611C70 CUSTOM",
                  "criteria_number": 1,
                  "content": "content 2 CUSTOM"
                },
                {
                  "id": "63A36810-C582-42EC-9AB6-322F15F0C3B9 CUSTOM",
                  "criteria_number": 2,
                  "content": "content 3 CUSTOM"
                },
                {
                  "id": "A849D2F7-7814-4D37-989E-A7D6EE4B5E05 CUSTOM",
                  "criteria_number": 0,
                  "content": "content 1 CUSTOM"
                }
              ]
            """.trimIndent()
        assertThatQuery("SELECT * FROM table_test")
            .using(CustomMSSQLMapper())
            .isValidAgainst(expected)
    }

    @Test
    fun `Should valid table against json with custom validator`() {
        val expected = // language=json
            """
              [
                {
                  "id": "642A00A5-18F2-47CF-9E5C-0161156FF0A0",
                  "criteria_number": 3,
                  "content": "{#custom_notempty#}"
                },
                {
                  "id": "F9B5B79D-606E-4251-8967-12DBC5611C70",
                  "criteria_number": 1,
                  "content": "{#custom_notempty#}"
                },
                {
                  "id": "63A36810-C582-42EC-9AB6-322F15F0C3B9",
                  "criteria_number": 2,
                  "content": "{#custom_notempty#}"
                },
                {
                  "id": "A849D2F7-7814-4D37-989E-A7D6EE4B5E05",
                  "criteria_number": 0,
                  "content": "{#custom_notempty#}"
                }
              ]
            """.trimIndent()
        assertThatQuery("SELECT * FROM table_test")
            .using(customValidator())
            .isValidAgainst(expected)
    }

    @Test
    fun `Should valid single row against json object`() {
        val expected = // language=json
            """
              {
                "id": "642A00A5-18F2-47CF-9E5C-0161156FF0A0",
                "criteria_number": 3,
                "content": "content 4"
              }
            """.trimIndent()

        assertThatQuery("SELECT * FROM table_test WHERE id='642A00A5-18F2-47CF-9E5C-0161156FF0A0'")
            .isValidAgainst(expected)
    }

    private fun customValidator(): JsonValidator<String> {
        return validator {
            templatedValidator<String>(
                "custom_notempty",
                comparator { actual, expected ->
                    if (actual == null || actual.isEmpty()) {
                        throw ValueMatcherException("Value is null or empty", expected, actual)
                    }
                    true
                }
            )
        }
    }

    class CustomMSSQLMapper : MSSQLMapper() {
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
