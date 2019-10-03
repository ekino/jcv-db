package com.ekino.oss.jcv.db.config

import com.ekino.oss.jcv.core.validator.Validators.defaultValidators
import com.ekino.oss.jcv.core.validator.validators
import com.ekino.oss.jcv.db.config.comparator.JsonArrayComparator
import com.ekino.oss.jcv.db.config.comparator.JsonObjectComparator

object DBValidators {

    private const val mongoDefaultIdRegex = "^[a-f\\d]{24}$"

    @JvmStatic
    fun defaultDBValidators() = validators {
        +defaultValidators()
        +templatedValidator("json_object", JsonObjectComparator())
        +templatedValidator("json_array", JsonArrayComparator())
    }
}
