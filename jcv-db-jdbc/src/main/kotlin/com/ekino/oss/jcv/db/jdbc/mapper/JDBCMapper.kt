package com.ekino.oss.jcv.db.jdbc.mapper

import com.ekino.oss.jcv.db.mapper.TypeMapper
import java.util.UUID

abstract class JDBCMapper : TypeMapper {

    override fun getValueFromColumn(value: Any): Any? {
        return when (value) {
            is Number -> mapNumberType(value)
            is Boolean -> mapBoolean(value)
            is UUID -> mapUUID(value)
            is ByteArray -> mapByteArray(value)
            is String -> mapString(value)
            else -> defaultMapper(value)
        }
    }

    open fun mapBoolean(value: Boolean): Any = value
    open fun mapUUID(value: UUID): Any = value
    open fun mapByteArray(value: ByteArray): Any = value
    open fun mapString(value: String): Any = value
    open fun mapNumberType(value: Number): Any = value
    open fun defaultMapper(value: Any): Any? = value.toString()
}
