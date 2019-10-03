package com.ekino.oss.jcv.db.assertj.util

import com.ekino.oss.jcv.db.assertj.mapper.AssertJBaseMapper
import com.ekino.oss.jcv.db.assertj.mapper.MSSQLMapper
import com.ekino.oss.jcv.db.assertj.mapper.MySQLMapper
import com.ekino.oss.jcv.db.assertj.mapper.PostgresMapper
import com.ekino.oss.jcv.db.config.DatabaseType
import com.ekino.oss.jcv.db.exception.DbAssertException
import com.ekino.oss.jcv.db.mapper.TypeMapper
import com.ekino.oss.jcv.db.model.RowModel
import com.ekino.oss.jcv.db.model.TableModel
import com.ekino.oss.jcv.db.util.ConverterUtil.addCustomMapperToDefaultOne
import com.ekino.oss.jcv.db.util.ConverterUtil.getMapperFromMapByKey
import org.assertj.db.type.Source
import org.assertj.db.type.Table
import java.sql.Connection
import java.sql.DriverManager
import java.util.HashMap
import javax.sql.DataSource

class TableConverter(customMapper: Pair<DatabaseType, AssertJBaseMapper>? = null) {

    private val mappers: Map<DatabaseType, TypeMapper> = customMapper?.let { addCustomMapperToDefaultOne(it, defaultMappers) } ?: defaultMappers

    companion object {
        val defaultMappers: Map<DatabaseType, AssertJBaseMapper> = mapOf(
            DatabaseType.POSTGRESQL to PostgresMapper(),
            DatabaseType.MYSQL to MySQLMapper(),
            DatabaseType.MSSQL to MSSQLMapper()
        )
    }

    fun convertTableToTableModel(table: Table): TableModel {
        val tableModel = TableModel()
        table.rowsList.forEach {
            val rowModel = RowModel()
            val cells = HashMap<String, Any?>()

            it.columnsNameList.forEach { columnName -> cells[columnName] = it.getColumnValue(columnName) }
            rowModel.cells = cells
            tableModel.addRow(rowModel)
        }
        return tableModel
    }

    fun getMapperByDbType(source: Source?, dataSource: DataSource?): TypeMapper {
        val connection = getConnection(source, dataSource) ?: throw DbAssertException(
            "No connection found"
        )
        return getMapperFromMapByKey(connection.metaData.databaseProductName, mappers)
    }

    private fun getConnection(source: Source?, dataSource: DataSource?): Connection? {
        if (dataSource == null && source == null) {
            throw NullPointerException("connection and dataSource must be not null")
        }

        return when {
            dataSource != null -> dataSource.connection
            source != null -> DriverManager.getConnection(source.url, source.user, source.password)
            else -> null
        }
    }
}
