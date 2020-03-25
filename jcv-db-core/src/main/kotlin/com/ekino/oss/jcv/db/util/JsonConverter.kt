package com.ekino.oss.jcv.db.util

import com.ekino.oss.jcv.core.JsonComparator
import com.fasterxml.jackson.databind.ObjectMapper
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import org.json.JSONTokener
import org.skyscreamer.jsonassert.JSONCompare
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import kotlin.test.fail

object JsonConverter {

    fun compareJsonAndLogResult(actual: JSONArray, expected: JSONArray, jsonComparator: JsonComparator) {
        JSONCompare.compareJSON(expected, actual, jsonComparator)
            .takeUnless { it.passed() }
            ?.also { fail("${it.message}\nActual: $actual") }
    }

    fun formatInput(input: String) = when (val json = input.takeIfIsJson()) {
        is JSONObject -> JSONArray().put(json)
        is JSONArray -> json
        else -> null
    }

    @Throws(IOException::class)
    fun loadFileAsString(inputStream: InputStream) = inputStream.bufferedReader().use(BufferedReader::readText)
}

fun Any.toJsonObject() = JSONObject(ObjectMapper().writeValueAsString(this))
fun Any.toJsonArray() = JSONArray(ObjectMapper().writeValueAsString(this))

fun String.takeIfIsJson() = try {
    when (val json = JSONTokener(this).nextValue()) {
        is String -> null
        is JSONObject, is JSONArray -> json
        else -> null
    }
} catch (e: JSONException) {
    null
}
