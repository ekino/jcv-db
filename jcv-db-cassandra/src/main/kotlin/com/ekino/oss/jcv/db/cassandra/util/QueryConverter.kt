package com.ekino.oss.jcv.db.cassandra.util

import com.datastax.oss.driver.api.core.CqlSession
import com.datastax.oss.driver.api.core.cql.ResultSet
import com.ekino.oss.jcv.db.exception.DbAssertException
import com.ekino.oss.jcv.db.model.RowModel
import com.ekino.oss.jcv.db.model.TableModel
import java.net.InetSocketAddress

class QueryConverter(private val dataSource: CassandraDataSource?) {
    fun fromQueryToTableModel(query: String): TableModel {
        return buildCqlSession().use { session ->
            val resultSet = session.execute(query)
            fromResultSetToTableModel(resultSet)
        }
    }

    private fun buildCqlSession(): CqlSession {
        dataSource ?: throw DbAssertException("You have to defined a valida datasource")

        val builder = CqlSession
            .builder()
            .withLocalDatacenter(dataSource.datacenter)
            .addContactPoint(InetSocketAddress.createUnresolved(dataSource.hostname, dataSource.port))

        if (dataSource.password != null && dataSource.username != null) {
            builder
                .withAuthCredentials(dataSource.username, dataSource.password)
        }
        return builder.build()
    }

    private fun fromResultSetToTableModel(resultSet: ResultSet): TableModel {
        return resultSet
            .map { element ->
                element.columnDefinitions
                    .mapIndexed { index, columnDefinition ->
                        columnDefinition.name.asCql(true) to element.getObject(index)
                    }
                    .toMap()
                    .let(::RowModel)
            }
            .toSet()
            .let(::TableModel)
    }
}
