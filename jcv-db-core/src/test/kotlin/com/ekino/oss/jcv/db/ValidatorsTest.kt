package com.ekino.oss.jcv.db

import assertk.assertAll
import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isFalse
import assertk.assertions.isSuccess
import assertk.tableOf
import com.ekino.oss.jcv.core.JsonComparator
import com.ekino.oss.jcv.db.config.DBValidators.defaultDBValidators
import org.junit.jupiter.api.Test
import org.skyscreamer.jsonassert.JSONCompare
import org.skyscreamer.jsonassert.JSONCompareMode
import org.skyscreamer.jsonassert.JSONCompareResult

class ValidatorsTest {

    @Test
    fun `json object validator`() {
        assertThat(
            runCatching {
                compare(
                    """{"field_name": {}}""",
                    """{"field_name": "{#json_object#}"}"""
                )
            }
        ).isSuccess()
    }

    @Test
    fun `json array validator`() {
        assertThat(
            runCatching {
                compare(
                    """{"field_name": []}""",
                    """{"field_name": "{#json_array#}"}"""
                )
            }
        )
            .isSuccess()
    }

    @Test
    fun `validator errors`() {
        tableOf("actual", "expected", "error")
            .row(
                """{"field_name": "aaa"}""",
                """{"field_name": "{#json_array#}"}""",
                """
                field_name: Value is not a valid json array
                Expected: {#json_array#}
                     got: aaa

                """.trimIndent()
            )
            .row(
                """{"field_name": "aaa"}""",
                """{"field_name": "{#json_object#}"}""",
                """
                field_name: Value is not a valid json object
                Expected: {#json_object#}
                     got: aaa

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

    private fun compare(actualJson: String, expectedJson: String, body: (JSONCompareResult) -> Unit = {}) = body(
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
