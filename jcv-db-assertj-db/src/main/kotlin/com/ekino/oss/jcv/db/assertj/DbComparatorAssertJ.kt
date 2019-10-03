package com.ekino.oss.jcv.db.assertj

import com.ekino.oss.jcv.core.JsonComparator
import com.ekino.oss.jcv.core.JsonValidator
import com.ekino.oss.jcv.db.assertj.exception.JsonParseException
import com.ekino.oss.jcv.db.assertj.mapper.AssertJBaseMapper
import com.ekino.oss.jcv.db.assertj.util.DbComparatorBuilder
import com.ekino.oss.jcv.db.assertj.util.TableConverter
import com.ekino.oss.jcv.db.config.DatabaseType
import com.ekino.oss.jcv.db.exception.DbAssertException
import com.ekino.oss.jcv.db.util.JsonConverter
import com.ekino.oss.jcv.db.util.JsonConverter.compareJsonAndLogResult
import com.ekino.oss.jcv.db.util.JsonConverter.getTableModelAsJson
import com.ekino.oss.jcv.db.util.takeIfIsJson
import org.assertj.core.api.AbstractAssert
import org.assertj.db.type.Table
import org.json.JSONArray
import org.json.JSONException
import org.skyscreamer.jsonassert.JSONCompareMode
import java.io.IOException
import java.io.InputStream
import java.util.Objects

class DbComparatorAssertJ(
    actualTable: Table,
    private val tableConverter: TableConverter,
    private val jsonComparator: JsonComparator
) : AbstractAssert<DbComparatorAssertJ, Table>(actualTable, DbComparatorAssertJ::class.java) {

    companion object {

        @JvmStatic
        fun assertThatTable(table: Table) = DbComparatorBuilder.create().build(table)
    }

    fun using(comparator: JsonComparator) = DbComparatorAssertJ(actual, tableConverter, comparator)

    fun <T : JsonValidator<*>> using(mode: JSONCompareMode, vararg validators: T) = using(mode, validators.toList())

    fun <T : JsonValidator<*>> using(mode: JSONCompareMode, validators: List<T>) = DbComparatorAssertJ(actual, tableConverter, JsonComparator(mode, validators))

    fun <T : JsonValidator<*>> using(mode: JSONCompareMode, mapper: Pair<DatabaseType, AssertJBaseMapper>, validators: List<T>) =
        DbComparatorAssertJ(
            actual,
            TableConverter(mapper),
            JsonComparator(mode, validators)
        )

    fun <T : JsonValidator<*>> using(vararg validators: T) = using(validators.toList())

    fun <T : JsonValidator<*>> using(validators: List<T>) = using(JSONCompareMode.STRICT, validators)

    fun using(mapper: Pair<DatabaseType, AssertJBaseMapper>) = DbComparatorAssertJ(
        actual,
        TableConverter(mapper),
        jsonComparator
    )

    fun isValidAgainst(input: String) = input.takeIfIsJson()?.let { compareActualAndExcepted(it as JSONArray) } ?: throw DbAssertException(
        "Unable to parse pg object to json"
    )

    @Throws(IOException::class)
    fun isValidAgainst(inputStream: InputStream) = compareActualAndExcepted(JsonConverter.loadJson(inputStream))

    private fun compareActualAndExcepted(expected: JSONArray) {
        isNotNull

        Objects.requireNonNull<Any>(jsonComparator, "Json comparator definition is missing")

        val actualJson = getTableModelAsJson(tableConverter.convertTableToTableModel(actual), tableConverter.getMapperByDbType(actual.source, actual.dataSource))

        try {
            compareJsonAndLogResult(actualJson, expected, jsonComparator)
        } catch (e: JSONException) {
            throw JsonParseException("Error with provided JSON Strings", e)
        }
    }
}
