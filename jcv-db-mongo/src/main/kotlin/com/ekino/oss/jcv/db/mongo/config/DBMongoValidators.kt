package com.ekino.oss.jcv.db.mongo.config

import com.ekino.oss.jcv.core.comparator.RegexComparator
import com.ekino.oss.jcv.core.validator.validators
import com.ekino.oss.jcv.db.config.DBValidators

object DBMongoValidators {

    private const val mongoDefaultIdRegex = "^[a-f\\d]{24}$"

    @JvmStatic
    fun defaultDBValidators() = validators {
        +DBValidators.defaultDBValidators()
        +templatedValidator("mongo_id", RegexComparator(mongoDefaultIdRegex.toRegex().toPattern()))
    }
}
