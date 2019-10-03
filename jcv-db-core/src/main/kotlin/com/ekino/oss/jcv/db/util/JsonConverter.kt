package com.ekino.oss.jcv.db.util

import com.ekino.oss.jcv.core.JsonComparator
import com.fasterxml.jackson.databind.ObjectMapper
import com.ekino.oss.jcv.db.exception.DbAssertException
import com.ekino.oss.jcv.db.mapper.DefaultMapper
import com.ekino.oss.jcv.db.mapper.TypeMapper
import com.ekino.oss.jcv.db.model.RowModel
import com.ekino.oss.jcv.db.model.TableModel
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import org.json.JSONTokener
import org.skyscreamer.jsonassert.JSONCompare
import org.skyscreamer.jsonassert.JSONCompareResult
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import kotlin.test.fail

object JsonConverter {

    fun getTableModelAsJson(tableModel: TableModel, typeMapper: TypeMapper? = null): JSONArray {
        val mapper = typeMapper ?: DefaultMapper()
        return JSONArray(tableModel.rows.map {
            convertRowModelToJSONObject(
                it,
                mapper
            )
        })
    }

    private fun convertRowModelToJSONObject(row: RowModel, mapper: TypeMapper) = row.cells
        .map { entry -> entry.key.toLowerCase() to getValueFromMapper(entry.value, mapper) }.toMap()
        .let { map -> JSONObject(map) }

    private fun getValueFromMapper(value: Any?, mapper: TypeMapper) = value?.let { mapper.getValueFromColumn(it) } ?: JSONObject.NULL

    fun compareJsonAndLogResult(actual: JSONArray, expected: JSONArray, jsonComparator: JsonComparator) {
        JSONCompare.compareJSON(expected, actual, jsonComparator)
            .takeUnless { it.passed() }
            ?.also { failWithMessage(it, expected, actual) }
    }

    private fun failWithMessage(result: JSONCompareResult, expected: JSONArray, actual: JSONArray) {
        var failureMessage: String? = result.message
        if (failureMessage != null) {
            failureMessage = failureMessage.replace(" ; ".toRegex(), "\n")
        }

        failureMessage = ("""================ Expected JSON ================
                ${expected.toString(4)}
                ================= Actual JSON =================
                ${actual.toString(4)}
                ================= Error List ==================
                $failureMessage""".trimIndent())
        fail(failureMessage)
    }

    @Throws(IOException::class)
    fun loadJson(inputStream: InputStream): JSONArray {
        val fileContent = fromInputStreamToString(inputStream).takeIfIsJson()
        if (fileContent is JSONArray) {
            return fileContent
        } else {
            throw DbAssertException("Unable to load JSON Array from input stream")
        }
    }

    @Throws(IOException::class)
    private fun fromInputStreamToString(inputStream: InputStream) = inputStream.bufferedReader().use(BufferedReader::readText)
}

fun Any.toJsonObject() = JSONObject(ObjectMapper().writeValueAsString(this))
fun Any.toJsonArray() = JSONArray(ObjectMapper().writeValueAsString(this))

fun String.takeIfIsJson(): Any? {
    try {
        val jsonObject = JSONTokener(this).nextValue()
        if (jsonObject is String && jsonObject == this) {
            return null
        }
        return jsonObject
    } catch (e: JSONException) {
        return null
    }
}
