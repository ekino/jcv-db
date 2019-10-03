package com.ekino.oss.jcv.db.cassandra.util

import com.datastax.oss.driver.api.core.CqlSession
import com.datastax.oss.driver.api.core.cql.ResultSet
import com.ekino.oss.jcv.db.exception.DbAssertException
import com.ekino.oss.jcv.db.model.RowModel
import com.ekino.oss.jcv.db.model.TableModel
import java.net.InetSocketAddress

class QueryConverter(private val dataSource: CassandraDataSource? = null) {
    fun fromQueryToTableModel(query: String): TableModel {
        val session = buildCqlSession(dataSource)

        val resultSet = session.execute(query)
        return fromResultSetToTableModel(resultSet)
    }

    private fun buildCqlSession(dataSource: CassandraDataSource? = null): CqlSession {
        dataSource ?: throw DbAssertException("You have to defined a valida datource")

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
        val rows = mutableSetOf<RowModel>()
        resultSet.forEach {
            val cells = mutableMapOf<String, Any?>()
            for (i in 0 until it.columnDefinitions.size()) {
                cells[it.columnDefinitions[i].name.asCql(true)] = it.getObject(i)
            }
            rows.add(RowModel(cells))
        }
        return TableModel(rows)
    }
}
