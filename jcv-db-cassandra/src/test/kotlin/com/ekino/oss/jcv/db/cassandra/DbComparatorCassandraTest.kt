package com.ekino.oss.jcv.db.cassandra

import com.datastax.oss.driver.api.querybuilder.QueryBuilder
import com.ekino.oss.jcv.db.cassandra.util.CassandraDataSource
import com.ekino.oss.jcv.db.cassandra.util.DBComparatorBuilder
import org.junit.jupiter.api.Test

class DbComparatorCassandraTest {

    companion object {
        const val CASSANDRA_DC = "local"
        const val CASSANDRA_IP_ADDRESS = "localhost"
        const val CASSANDRA_PORT = 9042
    }

    private fun assertThatQuery(query: String) = DBComparatorBuilder
        .create()
        .datasource(
            CassandraDataSource(
                CASSANDRA_DC,
                CASSANDRA_IP_ADDRESS,
                CASSANDRA_PORT
            )
        )

        .build(query)

    @Test
    fun `Should test cassandra type`() {

        val expected = // language=json
            """[{
            "bigint_field": 9223372036854775806,
            "float_field": 12345.679,
            "smallint_field": 32766,
            "tuple_field": [
                "fruit",
                1
            ],
            "custom_type_field": {
                "test_field_text": "test",
                "test_field_int": 1
            },
            "list_udt_field": [{
                "test_field_text": "test",
                "test_field_int": 1
            }],
            "list_field": [
                1,
                2,
                3,
                4
            ],
            "timestamp_field": "1970-01-19T00:25:01.239Z",
            "varint_field": 1,
            "map_field": {
                "legume": 2,
                "fruit": 1
            },
            "tinyint_field": 100,
            "date_field": "9999-12-31",
            "set_field": [
                1,
                2,
                3,
                4
            ],
            "time_field": "00:11:12",
            "duration_field": "89h9m9s",
            "inet_field": "127.0.0.1",
            "text_field": "text field",
            "decimal_field": 12345.67898,
            "int_field": 2147483646,
            "id": "28f4dcde-a221-4133-8d72-4115c4d24038",
            "varchar_field": "varchar field",
            "double_field": 12345.67898,
            "ascii_field": "A",
            "boolean_field": true,
            "complex_type": [
              {
                "test_field_int": 1,
                "test_type": [{
                  "test_field_int": 1,
                  "test_field_text": "test"
                }]
              }
            ]  ,
            "column_null": null
        }]
            """.trimIndent()

        assertThatQuery("select * from cassandratest.cassandra_table_type")
            .isValidAgainst(expected)
    }

    @Test
    fun `Should test cassandra type with cql builder`() {
        val expected = // language=json
            """[{
            "bigint_field": 9223372036854775806,
            "float_field": 12345.679,
            "smallint_field": 32766,
            "tuple_field": [
                "fruit",
                1
            ],
            "custom_type_field": {
                "test_field_text": "test",
                "test_field_int": 1
            },
            "list_udt_field": [{
                "test_field_text": "test",
                "test_field_int": 1
            }],
            "list_field": [
                1,
                2,
                3,
                4
            ],
            "timestamp_field": "1970-01-19T00:25:01.239Z",
            "varint_field": 1,
            "map_field": {
                "legume": 2,
                "fruit": 1
            },
            "tinyint_field": 100,
            "date_field": "9999-12-31",
            "set_field": [
                1,
                2,
                3,
                4
            ],
            "time_field": "00:11:12",
            "duration_field": "89h9m9s",
            "inet_field": "127.0.0.1",
            "text_field": "text field",
            "decimal_field": 12345.67898,
            "int_field": 2147483646,
            "id": "28f4dcde-a221-4133-8d72-4115c4d24038",
            "varchar_field": "varchar field",
            "double_field": 12345.67898,
            "ascii_field": "A",
            "boolean_field": true,
            "complex_type": [
              {
                "test_field_int": 1,
                "test_type": [{
                  "test_field_int": 1,
                  "test_field_text": "test"
                }]
              }
            ]  ,
            "column_null": null
        }]
            """.trimIndent()

        DbComparatorCassandra.assertThatQuery(QueryBuilder.selectFrom("cassandratest", "cassandra_table_type").all())
            .using(
                CassandraDataSource(
                    CASSANDRA_DC,
                    CASSANDRA_IP_ADDRESS,
                    CASSANDRA_PORT
                )
            )

            .isValidAgainst(expected)
    }

    @Test
    fun `Should test column restriction with cql query`() {
        val expected = // language=json
            """[
                  {
                      "criteria_number": 1,
                      "content": "content 1",
                      "id": "d8a32fff-13a5-4db6-9c2c-111bee329575"
                  },
                  {
                      "criteria_number": 2,
                      "content": "content 1",
                      "id": "607bd08a-269f-42a0-90e9-bc8e4da11357"
                  },
                  {
                      "criteria_number": 3,
                      "content": "content 1",
                      "id": "dbeb9161-cf55-4008-a233-9df257a418c3"
                  }
            ]
            """.trimIndent()

        DbComparatorCassandra.assertThatQuery("SELECT * FROM cassandratest.table_test WHERE content = 'content 1';")
            .using(
                CassandraDataSource(
                    CASSANDRA_DC,
                    CASSANDRA_IP_ADDRESS,
                    CASSANDRA_PORT
                )
            )

            .isValidAgainst(expected)
    }

    @Test
    fun `Should test column ordering with cql query`() {
        val expected = // language=json
            """[
                  {
                      "criteria_number": 3,
                      "content": "content 1",
                      "id": "dbeb9161-cf55-4008-a233-9df257a418c3"
                  },
                  {
                      "criteria_number": 2,
                      "content": "content 1",
                      "id": "607bd08a-269f-42a0-90e9-bc8e4da11357"
                  },
                  {
                      "criteria_number": 1,
                      "content": "content 1",
                      "id": "d8a32fff-13a5-4db6-9c2c-111bee329575"
                  }
            ]
            """.trimIndent()

        DbComparatorCassandra.assertThatQuery("SELECT * FROM cassandratest.table_test WHERE content = 'content 1' ORDER BY criteria_number DESC;")
            .using(
                CassandraDataSource(
                    CASSANDRA_DC,
                    CASSANDRA_IP_ADDRESS,
                    CASSANDRA_PORT
                )
            )

            .isValidAgainst(expected)
    }
}
