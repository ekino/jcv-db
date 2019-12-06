package com.ekino.oss.jcv.db.jdbc.util

import com.ekino.oss.jcv.core.JsonComparator
import com.ekino.oss.jcv.core.JsonValidator
import com.ekino.oss.jcv.db.config.DBValidators
import com.ekino.oss.jcv.db.jdbc.DbComparatorJDBC
import com.ekino.oss.jcv.db.jdbc.mapper.JDBCMapper
import org.skyscreamer.jsonassert.JSONCompareMode
import java.sql.Connection

class DBComparatorBuilder {

    constructor()

    constructor(
        mode: JSONCompareMode,
        validators: List<JsonValidator<*>>
    ) {
        this.mode = mode
        this.validators = validators
    }

    private lateinit var mode: JSONCompareMode
    private lateinit var validators: List<JsonValidator<*>>
    private var connection: Connection? = null
    private var customMapper: JDBCMapper? = null

    companion object {
        @JvmStatic
        fun create() = DBComparatorBuilder(
            JSONCompareMode.STRICT,
            DBValidators.defaultDBValidators()
        )
    }

    fun mode(mode: JSONCompareMode): DBComparatorBuilder {
        this.mode = mode
        return this
    }

    fun <T : JsonValidator<*>> validators(validators: List<T>): DBComparatorBuilder {
        this.validators = validators
        return this
    }

    fun <T : JsonValidator<*>> validators(vararg validators: T): DBComparatorBuilder {
        return validators(validators.toList())
    }

    fun connection(connection: Connection): DBComparatorBuilder {
        this.connection = connection
        return this
    }

    fun mapper(mapper: JDBCMapper): DBComparatorBuilder {
        this.customMapper = mapper
        return this
    }

    fun build(query: String) = DbComparatorJDBC(
        query,
        QueryConverter(connection),
        JsonComparator(
            mode,
            validators
        ),
        customMapper
    )
}
