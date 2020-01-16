package com.ekino.oss.jcv.db.cassandra

import com.datastax.oss.driver.api.querybuilder.select.Select
import com.ekino.oss.jcv.core.JsonComparator
import com.ekino.oss.jcv.core.JsonValidator
import com.ekino.oss.jcv.db.cassandra.util.CassandraDataSource
import com.ekino.oss.jcv.db.cassandra.util.DBComparatorBuilder
import com.ekino.oss.jcv.db.cassandra.util.QueryConverter
import com.ekino.oss.jcv.db.exception.DbAssertException
import com.ekino.oss.jcv.db.util.JsonConverter
import com.ekino.oss.jcv.db.util.JsonConverter.compareJsonAndLogResult
import com.ekino.oss.jcv.db.util.takeIfIsJson
import org.json.JSONArray
import org.skyscreamer.jsonassert.JSONCompareMode
import java.io.IOException
import java.io.InputStream

class DbComparatorCassandra(
    private val queryConverter: QueryConverter,
    private val jsonComparator: JsonComparator,
    private val mapper: CassandraMapper,
    private val query: String
) {

    companion object {

        @JvmStatic
        fun assertThatQuery(query: String) = DBComparatorBuilder.create().build(query)

        @JvmStatic
        fun assertThatQuery(select: Select) = DBComparatorBuilder.create().build(select.asCql())
    }

    fun isValidAgainst(input: String) = input.takeIfIsJson()?.let { compareActualAndExcepted(it as JSONArray) } ?: throw DbAssertException(
        "Unable to parse expected result from string to json array"
    )

    @Throws(IOException::class)
    fun isValidAgainst(inputStream: InputStream) = compareActualAndExcepted(JsonConverter.loadJson(inputStream))

    private fun compareActualAndExcepted(expected: JSONArray) {
        val actualJson = queryConverter.fromQueryToTableModel(query).getTableModelAsJson(mapper)
        compareJsonAndLogResult(actualJson, expected, jsonComparator)
    }

    fun <T : JsonValidator<*>> using(vararg validators: T) = using(validators.toList())

    fun <T : JsonValidator<*>> using(validators: List<T>) = using(JSONCompareMode.STRICT, validators)

    fun <T : JsonValidator<*>> using(mode: JSONCompareMode, validators: List<T>) = using(mode, validators, mapper, queryConverter)

    fun <T : JsonValidator<*>> using(mode: JSONCompareMode, vararg validators: T) = using(mode, validators.toList())

    fun using(mapper: CassandraMapper) = using(jsonComparator, mapper, queryConverter)

    fun using(dataSource: CassandraDataSource) = using(jsonComparator, mapper,
        QueryConverter(dataSource)
    )

    fun using(comparator: JsonComparator, mapper: CassandraMapper, queryConverter: QueryConverter) =
        DbComparatorCassandra(queryConverter, comparator, mapper, query)

    fun <T : JsonValidator<*>> using(mode: JSONCompareMode, validators: List<T>, mapper: CassandraMapper, queryConverter: QueryConverter) =
        DbComparatorCassandra(
            queryConverter,
            JsonComparator(mode, validators),
            mapper,
            query
        )

    fun <T : JsonValidator<*>> using(mode: JSONCompareMode, validators: List<T>, mapper: CassandraMapper, dataSource: CassandraDataSource) =
        DbComparatorCassandra(QueryConverter(dataSource), JsonComparator(mode, validators), mapper, query)
}
