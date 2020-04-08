package com.ekino.oss.jcv.db.jdbc.mapper

import com.ekino.oss.jcv.db.mapper.geometry.GeometryMapper.fromByteArrayToGeometryType
import com.ekino.oss.jcv.db.util.takeIfIsJson
import java.math.BigDecimal
import java.sql.Time
import java.sql.Timestamp
import java.util.UUID

open class MySQLMapper : JDBCMapper() {

    override fun mapNumberType(value: Number): Number = when (value) {
        is Float -> java.lang.Double.valueOf(value.toString())
        is BigDecimal -> value.toDouble()
        else -> value
    }

    override fun defaultMapper(value: Any): Any = when (value) {
        is Timestamp -> value.toInstant().toString()
        is Time -> value.toLocalTime().toString()
        else -> value.toString()
    }

    override fun mapString(value: String) = value.takeIfIsJson() ?: value

    override fun mapByteArray(value: ByteArray) = fromByteArrayToGeometryType(value) ?: value

    override fun mapUUID(value: UUID) = value.toString()
}
