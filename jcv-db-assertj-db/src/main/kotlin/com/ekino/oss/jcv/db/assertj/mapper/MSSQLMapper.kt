package com.ekino.oss.jcv.db.assertj.mapper

import com.ekino.oss.jcv.db.mapper.geometry.GeometryMapper.fromWKTStringToGeometryType
import com.ekino.oss.jcv.db.util.takeIfIsJson
import com.microsoft.sqlserver.jdbc.Geometry
import microsoft.sql.DateTimeOffset
import org.assertj.db.type.Value

open class MSSQLMapper : AssertJBaseMapper() {

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

    override fun mapNotIdentifiedType(value: Value): Any? {
        return when (val cellValue = value.value) {
            is DateTimeOffset -> cellValue.toString()
            else -> value.value
        }
    }

    override fun mapTimeType(value: Value) = value.value.toString().trim { it <= ' ' }
    override fun mapDateTimeType(value: Value) = value.value.toString().trim { it <= ' ' }
    override fun mapDateType(value: Value) = value.value.toString().trim { it <= ' ' }

    override fun mapBytesType(value: Value): Any = fromWKTStringToGeometryType(Geometry.STGeomFromWKB(value.value as ByteArray).asTextZM()) ?: value.value
}
