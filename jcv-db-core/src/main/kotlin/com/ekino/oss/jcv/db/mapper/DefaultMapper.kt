package com.ekino.oss.jcv.db.mapper

class DefaultMapper : TypeMapper {
    override fun getValueFromColumn(value: Any) = value
}
