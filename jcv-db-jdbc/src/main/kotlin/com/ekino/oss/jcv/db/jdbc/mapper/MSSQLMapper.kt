package com.ekino.oss.jcv.db.jdbc.mapper

import com.ekino.oss.jcv.db.mapper.geometry.GeometryMapper.fromWKTStringToGeometryType
import com.microsoft.sqlserver.jdbc.Geometry
import java.math.BigDecimal
import java.util.UUID

open class MSSQLMapper : JDBCMapper() {

    override fun mapUUID(value: UUID) = value.toString()

    override fun mapByteArray(value: ByteArray) =
        fromWKTStringToGeometryType(Geometry.STGeomFromWKB(value).asTextZM()) ?: value

    override fun mapNumberType(value: Number): Number = when (value) {
        is Float -> java.lang.Double.valueOf(value.toString())
        is BigDecimal -> value.toDouble()
        else -> value
    }
}
