package com.ekino.oss.jcv.db.mongo

import assertk.assertAll
import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isFalse
import assertk.assertions.isSuccess
import assertk.tableOf
import com.ekino.oss.jcv.core.JsonComparator
import com.ekino.oss.jcv.db.mongo.config.DBMongoValidators.defaultDBValidators
import org.junit.jupiter.api.Test
import org.skyscreamer.jsonassert.JSONCompare
import org.skyscreamer.jsonassert.JSONCompareMode
import org.skyscreamer.jsonassert.JSONCompareResult

class ValidatorsTest {

    @Test
    fun `mongo id validator`() {
        assertThat {
            compare(
                """{"field_name": "5d8253faef748a2c0e53ebfd"}""",
                """{"field_name": "{#mongo_id#}"}"""
            )
        }.isSuccess()
    }

    @Test
    fun `validator errors`() {
        tableOf("actual", "expected", "error")
            .row(
                """{"field_name": "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"}""",
                """{"field_name": "{#mongo_id#}"}""",
                """
                field_name: Value does not match pattern /^[a-f\d]{24}$/
                Expected: {#mongo_id#}
                     got: aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa

                """.trimIndent()
            )
            .forAll { actual, expected, error ->
                compare(
                    actualJson = actual,
                    expectedJson = expected
                ) {
                    assertAll {
                        assertThat(it.passed()).isFalse()
                        assertThat(it.message).isEqualTo(error)
                    }
                }
            }
    }

    private fun compare(actualJson: String, expectedJson: String, body: (JSONCompareResult) -> Unit = {}) = body.invoke(
        JSONCompare.compareJSON(
            expectedJson,
            actualJson,
            JsonComparator(
                JSONCompareMode.NON_EXTENSIBLE,
                defaultDBValidators().toTypedArray().toList()
            )
        )
    )
}
