package com.ekino.oss.jcv.db.assertj.util

import com.ekino.oss.jcv.core.JsonComparator
import com.ekino.oss.jcv.core.JsonValidator
import com.ekino.oss.jcv.db.assertj.DbComparatorAssertJ
import com.ekino.oss.jcv.db.assertj.mapper.AssertJBaseMapper
import com.ekino.oss.jcv.db.config.DBValidators
import com.ekino.oss.jcv.db.config.DatabaseType
import org.assertj.db.type.Table
import org.skyscreamer.jsonassert.JSONCompareMode

class DbComparatorBuilder {

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
    private var customMapper: Pair<DatabaseType, AssertJBaseMapper>? = null

    companion object {
        @JvmStatic
        fun create() = DbComparatorBuilder(
            JSONCompareMode.STRICT,
            DBValidators.defaultDBValidators()
        )
    }

    fun mode(mode: JSONCompareMode): DbComparatorBuilder {
        this.mode = mode
        return this
    }

    fun <T : JsonValidator<*>> validators(validators: List<T>): DbComparatorBuilder {
        this.validators = validators
        return this
    }

    fun <T : JsonValidator<*>> validators(vararg validators: T): DbComparatorBuilder {
        return validators(validators.toList())
    }

    fun mapper(mapper: Pair<DatabaseType, AssertJBaseMapper>): DbComparatorBuilder {
        this.customMapper = mapper
        return this
    }

    fun build(table: Table) = DbComparatorAssertJ(
        table,
        TableConverter(customMapper),
        JsonComparator(
            mode,
            validators
        )
    )
}
