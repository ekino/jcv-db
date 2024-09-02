package com.ekino.oss.jcv.db.jdbc.util

import com.ekino.oss.jcv.db.config.DatabaseType
import com.ekino.oss.jcv.db.config.DatabaseType.Companion.getDatabaseTypeByProductName
import com.ekino.oss.jcv.db.jdbc.mapper.MSSQLMapper
import com.ekino.oss.jcv.db.jdbc.mapper.MySQLMapper
import com.ekino.oss.jcv.db.jdbc.mapper.PostgresMapper
import com.ekino.oss.jcv.db.mapper.DefaultMapper
import com.ekino.oss.jcv.db.mapper.TypeMapper
import com.ekino.oss.jcv.db.model.RowModel
import com.ekino.oss.jcv.db.model.TableModel
import java.sql.Connection
import java.sql.ResultSet

class QueryConverter(private val connection: Connection) {

    private val dbType = connection.metaData.databaseProductName

    companion object {
        val defaultMappers: Map<DatabaseType, TypeMapper> = mapOf(
            DatabaseType.POSTGRESQL to PostgresMapper(),
            DatabaseType.MYSQL to MySQLMapper(),
            DatabaseType.MSSQL to MSSQLMapper()
        )
    }

    fun fromQueryToTableModel(query: String): TableModel {
        return connection.use {
            it.createStatement().use { statement ->
                statement.executeQuery(query).use(::buildTableModel)
            }
        }
    }

    private fun buildTableModel(resultSet: ResultSet): TableModel {
        val rows = mutableSetOf<RowModel>()
        while (resultSet.next()) {
            (1..resultSet.metaData.columnCount).associate {
                resultSet.metaData.getColumnName(it) to resultSet.getObject(it)
            }
                .let(::RowModel)
                .let(rows::add)
        }
        return TableModel(rows)
    }

    fun getMapperByType(): TypeMapper = getMapperByDbType()

    private fun getMapperByDbType() = defaultMappers
        .filter { it.key == getDatabaseTypeByProductName(dbType) }
        .map { it.value }
        .firstOrNull() ?: DefaultMapper()
}
