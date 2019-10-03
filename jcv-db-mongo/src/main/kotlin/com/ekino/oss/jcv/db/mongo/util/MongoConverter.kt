package com.ekino.oss.jcv.db.mongo.util

import com.ekino.oss.jcv.db.model.RowModel
import com.ekino.oss.jcv.db.model.TableModel
import org.bson.Document

class MongoConverter {

    fun convertContentToTableModel(collection: List<Document>) =
        TableModel(collection.map { convertDocumentToRowModel(it) }.toMutableSet())

    private fun convertDocumentToRowModel(document: Document) = RowModel(document.keys.map { it to document[it]!! }.toMap().toMutableMap())
}
