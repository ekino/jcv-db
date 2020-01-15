package com.ekino.oss.jcv.db.util

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isNull
import com.ekino.oss.jcv.db.mapper.TypeMapper
import com.ekino.oss.jcv.db.model.RowModel
import com.ekino.oss.jcv.db.model.TableModel
import com.ekino.oss.jcv.db.util.JsonConverter.formatInput
import org.json.JSONArray
import org.json.JSONObject
import org.junit.jupiter.api.Test
import java.io.BufferedReader

class JsonConverterTest {

    @Test
    fun `Should convert string to json object`() {
        assertThat("""{ "id" : 1 }""".takeIfIsJson().toString()).isEqualTo(JSONObject("""{ "id" : 1 }""").toString())
    }

    @Test
    fun `Should convert string to json array`() {
        assertThat("""[{ "id" : 1 }]""".takeIfIsJson().toString()).isEqualTo(JSONArray("""[{ "id" : 1 }]""").toString())
    }

    @Test
    fun `Should not convert non json string to json object`() {
        assertThat("""[a""".takeIfIsJson()).isNull()
    }

    @Test
    fun `Should load file and return a json array`() {
        val expectedResult = JSONArray("""[{"id":1}]""")
        val inputString = this::class.java.classLoader.getResourceAsStream("test-file.json")!!.bufferedReader().use(BufferedReader::readText)
        assertThat(formatInput(inputString).toString()).isEqualTo(expectedResult.toString())
    }

    @Test
    fun `Should get an error when trying to load a json object`() {
        val expectedResult = JSONArray("""[{"id":1}]""")
        val inputString = this::class.java.classLoader.getResourceAsStream("test-file-object.json")!!.bufferedReader().use(BufferedReader::readText)
        assertThat(formatInput(inputString).toString()).isEqualTo(expectedResult.toString())
    }

    @Test
    fun `Should get an error when trying to load an invalid json`() {
        val inputString = this::class.java.classLoader.getResourceAsStream("test-file-invalid.json")!!.bufferedReader().use(BufferedReader::readText)
        assertThat(formatInput(inputString)).isNull()
    }

    @Test
    fun `Should convert table model to Json Array with default mapper`() {
        val table = TableModel(
            mutableSetOf(
                RowModel(
                    mutableMapOf(
                        "id" to 1
                    )
                )
            )
        )
        assertThat(table.getTableModelAsJson().toString()).isEqualTo(JSONArray("""[{ "id" : 1 }]""").toString())
    }

    @Test
    fun `Should convert table model to Json Array with custom mapper`() {
        val table = TableModel(
            mutableSetOf(
                RowModel(
                    mutableMapOf(
                        "id" to 1
                    )
                )
            )
        )

        class CustomMapper : TypeMapper {
            override fun getValueFromColumn(value: Any) = "$value CUSTOM"
        }

        assertThat(table.getTableModelAsJson(CustomMapper()).toString()).isEqualTo(JSONArray("""[{ "id" : "1 CUSTOM" }]""").toString())
    }
}
