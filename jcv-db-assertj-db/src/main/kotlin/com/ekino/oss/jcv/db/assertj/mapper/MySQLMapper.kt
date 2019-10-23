package com.ekino.oss.jcv.db.assertj.mapper

import com.ekino.oss.jcv.db.mapper.geometry.GeometryMapper.fromByteArrayToGeometryType
import com.ekino.oss.jcv.db.util.takeIfIsJson
import org.assertj.db.type.Value
import java.sql.Time
import java.sql.Timestamp

open class MySQLMapper : AssertJBaseMapper() {

    override fun mapNumberType(value: Value): Any {
        return when (value.value) {
            is Float -> java.lang.Double.valueOf(value.value.toString())
            else -> value.value
        }
    }

    override fun mapTextType(value: Value): Any {
        val stringValue = value.value.toString()
        return stringValue.takeIfIsJson() ?: stringValue
    }

    override fun mapTimeType(value: Value) = (value.value as Time).toLocalTime().toString()
    override fun mapDateTimeType(value: Value) = (value.value as Timestamp).toInstant().toString()
    override fun mapDateType(value: Value) = value.value.toString().trim { it <= ' ' }

    override fun mapBytesType(value: Value): Any {
        return fromByteArrayToGeometryType(value.value as ByteArray) ?: value.value
    }
}
