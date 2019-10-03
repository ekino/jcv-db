package com.ekino.oss.jcv.db.jdbc.util

import com.ekino.oss.jcv.db.config.DatabaseType
import com.ekino.oss.jcv.db.config.DatabaseType.Companion.getDatabaseTypeByProductName
import com.ekino.oss.jcv.db.exception.DbAssertException
import com.ekino.oss.jcv.db.jdbc.mapper.MSSQLMapper
import com.ekino.oss.jcv.db.jdbc.mapper.MySQLMapper
import com.ekino.oss.jcv.db.jdbc.mapper.PostgresMapper
import com.ekino.oss.jcv.db.mapper.DefaultMapper
import com.ekino.oss.jcv.db.mapper.TypeMapper
import com.ekino.oss.jcv.db.model.RowModel
import com.ekino.oss.jcv.db.model.TableModel
import java.sql.Connection

class QueryConverter(val connection: Connection? = null, val customMapper: Pair<DatabaseType, TypeMapper>? = null) {

    private val mappers: Map<DatabaseType, TypeMapper> = customMapper?.let { defaultMappers.plus(customMapper) } ?: defaultMappers

    companion object {
        val defaultMappers: Map<DatabaseType, TypeMapper> = mapOf(
            DatabaseType.POSTGRESQL to PostgresMapper(),
            DatabaseType.MYSQL to MySQLMapper(),
            DatabaseType.MSSQL to MSSQLMapper()
        )
    }

    fun fromQueryToTableModel(query: String): TableModel {
        val statement = connection?.createStatement() ?: throw DbAssertException("You have to define a connection")
        val resultSet = statement.executeQuery(query)

        val rows = mutableSetOf<RowModel>()
        while (resultSet.next()) {
            val numberColumn = resultSet.metaData.columnCount
            val cells = mutableMapOf<String, Any?>()
            for (index in 1..numberColumn) {
                cells[resultSet.metaData.getColumnName(index)] = resultSet.getObject(index)
            }
            rows.add(RowModel(cells))
        }
        return TableModel(rows)
    }

    fun getMapperByType(): TypeMapper = getMapperByDbType(connection?.metaData?.databaseProductName ?: throw DbAssertException("You have to define a connection"))

    private fun getMapperByDbType(dbType: String) = mappers
        .filter { it.key == getDatabaseTypeByProductName(dbType) }
        .map { it.value }
        .firstOrNull() ?: DefaultMapper()
}
