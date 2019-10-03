package com.ekino.oss.jcv.db.util

import com.ekino.oss.jcv.db.config.DatabaseType
import com.ekino.oss.jcv.db.config.DatabaseType.Companion.getDatabaseTypeByProductName
import com.ekino.oss.jcv.db.mapper.DefaultMapper
import com.ekino.oss.jcv.db.mapper.TypeMapper

object ConverterUtil {

    fun addCustomMapperToDefaultOne(customMapper: Pair<DatabaseType, TypeMapper>, defaultMappers: Map<DatabaseType, TypeMapper>): Map<DatabaseType, TypeMapper> {
        val mappers: MutableMap<DatabaseType, TypeMapper> = defaultMappers.toMutableMap()
        mappers[customMapper.first] = customMapper.second
        return mappers
    }

    fun getMapperFromMapByKey(dbType: String, mappers: Map<DatabaseType, TypeMapper>) = mappers
        .filter { it.key == getDatabaseTypeByProductName(dbType) }
        .map { it.value }
        .firstOrNull() ?: DefaultMapper()
}
