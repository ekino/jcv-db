package com.ekino.oss.jcv.db.util

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isFailure
import assertk.assertions.isNull
import com.ekino.oss.jcv.db.mapper.TypeMapper
import com.ekino.oss.jcv.db.model.RowModel
import com.ekino.oss.jcv.db.model.TableModel
import com.ekino.oss.jcv.db.util.JsonConverter.getTableModelAsJson
import com.ekino.oss.jcv.db.util.JsonConverter.loadJson
import org.json.JSONArray
import org.json.JSONObject
import org.junit.jupiter.api.Test

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
        val expectedJsonArray = JSONArray("""[{ "id" : 1 }]""")
        assertThat(loadJson(this::class.java.classLoader.getResourceAsStream("test-file.json")!!).toString()).isEqualTo(expectedJsonArray.toString())
    }

    @Test
    fun `Should get an error when trying to load a json object`() {
        assertThat { loadJson(this::class.java.classLoader.getResourceAsStream("test-file-object.json")!!) }.isFailure()
    }

    @Test
    fun `Should get an error when trying to load an invalid json`() {
        assertThat { loadJson(this::class.java.classLoader.getResourceAsStream("test-file-invalid.json")!!) }.isFailure()
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
        assertThat(getTableModelAsJson(table).toString()).isEqualTo(JSONArray("""[{ "id" : 1 }]""").toString())
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

        assertThat(getTableModelAsJson(table, CustomMapper()).toString()).isEqualTo(JSONArray("""[{ "id" : "1 CUSTOM" }]""").toString())
    }
}
