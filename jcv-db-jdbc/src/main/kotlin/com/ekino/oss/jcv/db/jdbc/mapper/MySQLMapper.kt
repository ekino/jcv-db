package com.ekino.oss.jcv.db.jdbc.mapper

import com.ekino.oss.jcv.db.mapper.geometry.GeometryMapper.fromByteArrayToGeometryType
import com.ekino.oss.jcv.db.util.takeIfIsJson
import java.util.UUID

open class MySQLMapper : JDBCMapper() {

    override fun mapNumberType(value: Number): Number = when (value) {
        is Float -> java.lang.Double.valueOf(value.toString())
        else -> value
    }

    override fun mapString(value: String) = value.takeIfIsJson() ?: value

    override fun mapByteArray(value: ByteArray) = fromByteArrayToGeometryType(value) ?: value

    override fun mapUUID(value: UUID) = value.toString()
}
