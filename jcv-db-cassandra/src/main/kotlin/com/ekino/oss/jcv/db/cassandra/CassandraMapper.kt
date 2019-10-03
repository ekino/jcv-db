package com.ekino.oss.jcv.db.cassandra

import com.datastax.oss.driver.api.core.data.CqlDuration
import com.datastax.oss.driver.internal.core.data.DefaultTupleValue
import com.datastax.oss.driver.internal.core.data.DefaultUdtValue
import com.ekino.oss.jcv.db.mapper.TypeMapper
import org.json.JSONArray
import org.json.JSONObject
import java.net.Inet4Address
import java.time.Instant
import java.time.LocalDate
import java.time.LocalTime
import java.util.UUID
import kotlin.collections.ArrayList
import kotlin.collections.LinkedHashMap
import kotlin.collections.LinkedHashSet

open class CassandraMapper : TypeMapper {

    override fun getValueFromColumn(value: Any): Any? {
        return when (value) {
            is Float -> java.lang.Double.valueOf(value.toString())
            is UUID -> value.toString()

            is LocalDate -> value.toString()
            is CqlDuration -> value.toString()
            is LocalTime -> value.toString()
            is Instant -> value.toString()

            is DefaultUdtValue -> handleCustomType(value)
            is Inet4Address -> handleInetAdress(value)
            is DefaultTupleValue -> handleTupleValue(value)
            is ArrayList<*> -> handleIterable(value)
            is LinkedHashSet<*> -> handleIterable(value)
            is LinkedHashMap<*, *> -> handleLinkedHashMap(value)
            else -> value
        }
    }

    protected fun handleLinkedHashMap(value: LinkedHashMap<*, *>) = value.map { it.key.toString() to getValueFromColumn(it.value) }.toMap().toJSONObject()

    protected fun handleCustomType(value: DefaultUdtValue) = value.type.fieldNames.map { it.asInternal() to getValueFromColumn(value.getObject(it) ?: "null") }.toMap().toJSONObject()

    protected fun handleInetAdress(value: Inet4Address) = value.toString().removePrefix("/")

    protected fun handleTupleValue(value: DefaultTupleValue): JSONArray {
        val resultList = mutableListOf<Any?>()
        for (i in 0 until value.size()) {
            resultList.add(getValueFromColumn(value.getObject(i) ?: "null"))
        }
        return resultList.toJSONArray()
    }

    protected fun handleIterable(value: Iterable<*>) = value.map { getValueFromColumn(it!!) }.toJSONArray()

    private fun Map<String, Any?>.toJSONObject() = JSONObject(this)

    private fun List<Any?>.toJSONArray() = JSONArray(this)
}
