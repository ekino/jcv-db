package com.ekino.oss.jcv.db.model

import com.ekino.oss.jcv.db.mapper.DefaultMapper
import com.ekino.oss.jcv.db.mapper.TypeMapper
import org.json.JSONArray
import org.json.JSONObject

data class TableModel(val rows: MutableSet<RowModel> = mutableSetOf()) {
    fun addRow(row: RowModel) = rows.add(row)

    fun getTableModelAsJson(typeMapper: TypeMapper? = null): JSONArray {
        val mapper = typeMapper ?: DefaultMapper()
        return JSONArray(
            this.rows.map {
                convertRowModelToJSONObject(
                    it,
                    mapper
                )
            }
        )
    }

    private fun convertRowModelToJSONObject(row: RowModel, mapper: TypeMapper) = row.cells
        .map { entry -> entry.key.toLowerCase() to getValueFromMapper(entry.value, mapper) }.toMap()
        .let { map -> JSONObject(map) }

    private fun getValueFromMapper(value: Any?, mapper: TypeMapper) = value?.let { mapper.getValueFromColumn(it) } ?: JSONObject.NULL
}

data class RowModel(var cells: MutableMap<String, Any?> = mutableMapOf())
