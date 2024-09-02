package com.ekino.oss.jcv.db.assertj

import com.ekino.oss.jcv.core.JsonComparator
import com.ekino.oss.jcv.core.JsonValidator
import com.ekino.oss.jcv.db.assertj.exception.JsonParseException
import com.ekino.oss.jcv.db.assertj.mapper.AssertJBaseMapper
import com.ekino.oss.jcv.db.assertj.mapper.MSSQLMapper
import com.ekino.oss.jcv.db.assertj.mapper.MySQLMapper
import com.ekino.oss.jcv.db.assertj.mapper.PostgresMapper
import com.ekino.oss.jcv.db.assertj.util.DbComparatorBuilder
import com.ekino.oss.jcv.db.config.DatabaseType
import com.ekino.oss.jcv.db.exception.DbAssertException
import com.ekino.oss.jcv.db.mapper.TypeMapper
import com.ekino.oss.jcv.db.model.RowModel
import com.ekino.oss.jcv.db.model.TableModel
import com.ekino.oss.jcv.db.util.ConverterUtil
import com.ekino.oss.jcv.db.util.JsonConverter
import com.ekino.oss.jcv.db.util.JsonConverter.compareJsonAndLogResult
import com.ekino.oss.jcv.db.util.JsonConverter.formatInput
import org.assertj.core.api.AbstractAssert
import org.assertj.db.type.Source
import org.assertj.db.type.Table
import org.intellij.lang.annotations.Language
import org.json.JSONArray
import org.json.JSONException
import org.skyscreamer.jsonassert.JSONCompareMode
import java.io.InputStream
import java.sql.DriverManager
import java.util.*
import javax.sql.DataSource

class DbComparatorAssertJ(
    actualTable: Table,
    private val jsonComparator: JsonComparator,
    private val mapper: AssertJBaseMapper? = null
) : AbstractAssert<DbComparatorAssertJ, Table>(actualTable, DbComparatorAssertJ::class.java) {

    companion object {

        @JvmStatic
        fun assertThatTable(table: Table) = DbComparatorBuilder.create().build(table)
    }

    fun using(comparator: JsonComparator) = DbComparatorAssertJ(actual, comparator)

    fun <T : JsonValidator<*>> using(mode: JSONCompareMode, vararg validators: T) = using(mode, validators.toList())

    fun <T : JsonValidator<*>> using(mode: JSONCompareMode, validators: List<T>) = DbComparatorAssertJ(actual, JsonComparator(mode, validators))

    fun <T : JsonValidator<*>> using(mode: JSONCompareMode, mapper: AssertJBaseMapper, validators: List<T>) =
        DbComparatorAssertJ(
            actual,
            JsonComparator(mode, validators),
            mapper
        )

    fun <T : JsonValidator<*>> using(vararg validators: T) = using(validators.toList())

    fun <T : JsonValidator<*>> using(validators: List<T>) = using(JSONCompareMode.STRICT, validators)

    fun using(mapper: AssertJBaseMapper) = DbComparatorAssertJ(
        actual,
        jsonComparator,
        mapper
    )

    fun isValidAgainst(@Language("JSON") input: String) = formatInput(input)?.let { compareActualAndExcepted(it) } ?: throw DbAssertException(
        "Unable to parse expected result from string to json"
    )

    fun isValidAgainst(inputStream: InputStream) = isValidAgainst(JsonConverter.loadFileAsString(inputStream))

    private fun compareActualAndExcepted(expected: JSONArray) {
        isNotNull

        Objects.requireNonNull<Any>(jsonComparator, "Json comparator definition is missing")

        val actualJson = actual.convertTableToTableModel().getTableModelAsJson(mapper ?: getMapperByDbType(actual.source, actual.dataSource))

        try {
            compareJsonAndLogResult(actualJson, expected, jsonComparator)
        } catch (e: JSONException) {
            throw JsonParseException("Error with provided JSON Strings", e)
        }
    }

    private fun Table.convertTableToTableModel(): TableModel {
        return rowsList
            .map { row ->
                row.columnsNameList.associateWith {
                    row.getColumnValue(it)
                }
            }
            .map(::RowModel)
            .toSet()
            .let(::TableModel)
    }

    private fun getMapperByDbType(source: Source?, dataSource: DataSource?): TypeMapper {
        val defaultMappers: Map<DatabaseType, AssertJBaseMapper> = mapOf(
            DatabaseType.POSTGRESQL to PostgresMapper(),
            DatabaseType.MYSQL to MySQLMapper(),
            DatabaseType.MSSQL to MSSQLMapper()
        )

        val connection = when {
            dataSource != null -> dataSource.connection
            source != null -> DriverManager.getConnection(source.url, source.user, source.password)
            else -> throw NullPointerException("Database connection must be not null")
        }

        return ConverterUtil.getMapperFromMapByKey(connection.metaData.databaseProductName, defaultMappers)
    }
}
