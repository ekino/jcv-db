package com.ekino.oss.jcv.db.util

import com.ekino.oss.jcv.core.JsonComparator
import com.ekino.oss.jcv.db.exception.DbAssertException
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
            ?.also { fail(it.fail("", expected, actual).message) }
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
