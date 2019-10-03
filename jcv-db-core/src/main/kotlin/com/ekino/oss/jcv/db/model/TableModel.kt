package com.ekino.oss.jcv.db.model

data class TableModel(val rows: MutableSet<RowModel> = mutableSetOf()) {
    fun addRow(row: RowModel) = rows.add(row)
}

data class RowModel(var cells: MutableMap<String, Any?> = mutableMapOf())
