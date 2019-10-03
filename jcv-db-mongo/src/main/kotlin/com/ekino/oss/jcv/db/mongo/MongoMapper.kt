package com.ekino.oss.jcv.db.mongo

import com.ekino.oss.jcv.db.mapper.TypeMapper
import org.bson.Document
import org.bson.types.ObjectId
import org.json.JSONTokener

class MongoMapper : TypeMapper {
    override fun getValueFromColumn(value: Any): Any = when (value) {
        is ObjectId -> value.toHexString()
        is Document -> JSONTokener(value.toJson()).nextValue()
        is Collection<*> -> JSONTokener("[" + value.filterIsInstance<Document>().joinToString(",") { it.toJson() } + "]").nextValue()
        else -> value
    }
}
