package com.ekino.oss.jcv.db.assertj.extension

import org.assertj.db.exception.AssertJDBException
import org.assertj.db.type.Source
import org.assertj.db.type.Table
import org.junit.jupiter.api.extension.BeforeEachCallback
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.api.extension.TestInstancePostProcessor
import javax.sql.DataSource

class AssertDbExtension : TestInstancePostProcessor, BeforeEachCallback {
    private var dataSource: DataSource? = null
    private var source: Source? = null

    fun withDbInformation(jdbcUrl: String, username: String, password: String) {
        source = Source(jdbcUrl, username, password)
    }

    fun table(tableName: String): Table {
        return when {
            dataSource != null -> Table(dataSource!!, tableName, arrayOf())
            source != null -> Table(source!!, tableName, arrayOf())
            else -> throw AssertJDBException("No suitable db config found")
        }
    }

    override fun postProcessTestInstance(testInstance: Any, context: ExtensionContext) {
        this.dataSource = dataSource
    }

    override fun beforeEach(context: ExtensionContext) {
        this.dataSource = dataSource
    }
}
