package com.ekino.oss.jcv.db.mongo.util

import com.ekino.oss.jcv.db.model.RowModel
import com.ekino.oss.jcv.db.model.TableModel
import org.bson.Document

class MongoConverter {

    fun convertContentToTableModel(collection: List<Document>): TableModel {
        return TableModel(collection.map(::convertDocumentToRowModel).toSet())
    }

    private fun convertDocumentToRowModel(document: Document) =
        RowModel(document.keys.associateWith { document[it]!! }.toMap())
}
