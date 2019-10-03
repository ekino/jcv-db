package com.ekino.oss.jcv.db.util

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isInstanceOf
import com.ekino.oss.jcv.db.config.DatabaseType
import com.ekino.oss.jcv.db.mapper.DefaultMapper
import com.ekino.oss.jcv.db.mapper.TypeMapper
import com.ekino.oss.jcv.db.util.ConverterUtil.getMapperFromMapByKey
import com.ekino.oss.jcv.db.util.ConverterUtil.addCustomMapperToDefaultOne
import org.junit.jupiter.api.Test

class ConverterUtilTest {

    @Test
    fun `Should add custom mapper to existing default mapper`() {
        val m1 = CustomMapper()
        val m2 = CustomMapper()
        val m3 = CustomMapper()
        val map = mapOf(DatabaseType.POSTGRESQL to m1, DatabaseType.MYSQL to m2)

        assertThat(addCustomMapperToDefaultOne(DatabaseType.MSSQL to m3, map)).isEqualTo(mapOf(DatabaseType.POSTGRESQL to m1, DatabaseType.MYSQL to m2, DatabaseType.MSSQL to m3))
    }

    @Test
    fun `Should merge two maps with same keys`() {
        val m1 = CustomMapper()
        val m2 = CustomMapper()
        val m3 = CustomMapper()
        val map = mapOf(DatabaseType.POSTGRESQL to m1, DatabaseType.MYSQL to m2)

        assertThat(addCustomMapperToDefaultOne(DatabaseType.MYSQL to m3, map)).isEqualTo(mapOf(DatabaseType.POSTGRESQL to m1, DatabaseType.MYSQL to m3))
    }

    @Test
    fun `Should get mapper by key`() {
        val m1 = CustomMapper()
        val m2 = CustomMapper()
        val map = mapOf(DatabaseType.MSSQL to m1, DatabaseType.POSTGRESQL to m2)
        assertThat(getMapperFromMapByKey("Microsoft SQL Server", map)).isEqualTo(m1)
    }

    @Test
    fun `Should get mapper by non existent key`() {
        val m1 = CustomMapper()
        val m2 = CustomMapper()
        val map = mapOf(DatabaseType.MSSQL to m1, DatabaseType.POSTGRESQL to m2)
        assertThat(getMapperFromMapByKey("MySQL", map)).isInstanceOf(DefaultMapper::class)
    }

    private class CustomMapper : TypeMapper {
        override fun getValueFromColumn(value: Any) = value
    }
}
