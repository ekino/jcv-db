package com.ekino.oss.jcv.db.config.comparator

import com.ekino.oss.jcv.core.JsonValueComparator
import org.json.JSONArray
import org.skyscreamer.jsonassert.ValueMatcherException

class JsonArrayComparator : JsonValueComparator<Any> {

    override fun hasCorrectValue(actual: Any?, expected: Any?): Boolean {
        if (actual.takeIfInstanceOf<JSONArray>()) {
            return true
        }
        throw ValueMatcherException("Value is not a valid json array", expected.toString(), actual.toString())
    }

    private inline fun <reified T> Any?.takeIfInstanceOf(): Boolean {
        return this is T
    }
}
