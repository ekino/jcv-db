package com.ekino.oss.jcv.db.jdbc

import com.ekino.oss.jcv.core.JsonComparator
import com.ekino.oss.jcv.core.JsonValidator
import com.ekino.oss.jcv.db.exception.DbAssertException
import com.ekino.oss.jcv.db.jdbc.mapper.JDBCMapper
import com.ekino.oss.jcv.db.jdbc.util.DBComparatorBuilder
import com.ekino.oss.jcv.db.jdbc.util.QueryConverter
import com.ekino.oss.jcv.db.util.JsonConverter
import com.ekino.oss.jcv.db.util.JsonConverter.compareJsonAndLogResult
import com.ekino.oss.jcv.db.util.takeIfIsJson
import org.json.JSONArray
import org.skyscreamer.jsonassert.JSONCompareMode
import java.io.IOException
import java.io.InputStream
import java.sql.Connection

class DbComparatorJDBC constructor(
    private val query: String,
    private val queryConverter: QueryConverter,
    private val jsonComparator: JsonComparator,
    private val customMapper: JDBCMapper? = null
) {

    companion object {
        @JvmStatic
        fun assertThatQuery(query: String): DbComparatorJDBC = DBComparatorBuilder.create().build(query)
    }

    fun isValidAgainst(input: String) = input.takeIfIsJson()?.let { compareActualAndExcepted(it as JSONArray) } ?: throw DbAssertException(
        "Unable to parse string to json array"
    )

    @Throws(IOException::class)
    fun isValidAgainst(inputStream: InputStream) = compareActualAndExcepted(JsonConverter.loadJson(inputStream))

    fun using(comparator: JsonComparator) =
        DbComparatorJDBC(query, queryConverter, comparator, customMapper)

    fun <T : JsonValidator<*>> using(mode: JSONCompareMode, vararg validators: T) = using(mode, validators.toList())

    fun <T : JsonValidator<*>> using(mode: JSONCompareMode, validators: List<T>) =
        DbComparatorJDBC(query, queryConverter, JsonComparator(mode, validators), customMapper)

    fun <T : JsonValidator<*>> using(mode: JSONCompareMode, connection: Connection, validators: List<T>) =
        DbComparatorJDBC(query, QueryConverter(connection), JsonComparator(mode, validators), customMapper)

    fun <T : JsonValidator<*>> using(vararg validators: T) = using(validators.toList())

    fun <T : JsonValidator<*>> using(validators: List<T>) = using(JSONCompareMode.STRICT, validators)

    fun using(connection: Connection) = DbComparatorJDBC(
        query,
        QueryConverter(connection),
        jsonComparator,
        customMapper
    )

    fun using(mapper: JDBCMapper) =
        DbComparatorJDBC(
            query,
            QueryConverter(queryConverter.connection),
            jsonComparator,
            mapper
        )

    private fun compareActualAndExcepted(expected: JSONArray) {
        val actualJson = queryConverter.fromQueryToTableModel(query).getTableModelAsJson(customMapper ?: queryConverter.getMapperByType())
        compareJsonAndLogResult(actualJson, expected, jsonComparator)
    }
}
