package com.ekino.oss.jcv.db.cassandra.util

import com.ekino.oss.jcv.core.JsonComparator
import com.ekino.oss.jcv.core.JsonValidator
import com.ekino.oss.jcv.db.cassandra.CassandraMapper
import com.ekino.oss.jcv.db.cassandra.DbComparatorCassandra
import com.ekino.oss.jcv.db.config.DBValidators
import org.skyscreamer.jsonassert.JSONCompareMode

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
    private var datasource: CassandraDataSource? = null
    private var customMapper: CassandraMapper? = null

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

    fun datasource(datasource: CassandraDataSource): DBComparatorBuilder {
        this.datasource = datasource
        return this
    }

    fun mapper(mapper: CassandraMapper): DBComparatorBuilder {
        this.customMapper = mapper
        return this
    }

    fun build(query: String) = DbComparatorCassandra(
        QueryConverter(datasource),
        JsonComparator(
            JSONCompareMode.STRICT,
            DBValidators.defaultDBValidators()
        ),
        customMapper ?: CassandraMapper(),
        query
    )
}
