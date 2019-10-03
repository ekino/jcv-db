package com.ekino.oss.jcv.db.assertj.mapper

import com.ekino.oss.jcv.db.exception.DbAssertException
import com.ekino.oss.jcv.db.mapper.TypeMapper
import org.assertj.db.type.Value
import org.assertj.db.type.ValueType

abstract class AssertJBaseMapper : TypeMapper {

    override fun getValueFromColumn(value: Any): Any? {
        return when ((value as Value).valueType) {
            ValueType.UUID -> mapUUIDType(value)
            ValueType.BOOLEAN -> mapBooleanType(value)
            ValueType.NUMBER -> mapNumberType(value)
            ValueType.TEXT -> mapTextType(value)
            ValueType.NOT_IDENTIFIED -> mapNotIdentifiedType(value)
            ValueType.BYTES -> mapBytesType(value)
            ValueType.TIME -> mapTimeType(value)
            ValueType.DATE_TIME -> mapDateTimeType(value)
            ValueType.DATE -> mapDateType(value)
            else -> throw DbAssertException("Value of column is null")
        }
    }

    open fun mapUUIDType(value: Value): Any = value.value
    open fun mapBooleanType(value: Value): Any = value.value
    open fun mapNumberType(value: Value): Any = value.value
    open fun mapTextType(value: Value): Any = value.value
    open fun mapNotIdentifiedType(value: Value): Any? = value.value
    open fun mapBytesType(value: Value): Any = value.value
    open fun mapTimeType(value: Value): Any = value.value
    open fun mapDateTimeType(value: Value): Any = value.value
    open fun mapDateType(value: Value): Any = value.value
}
