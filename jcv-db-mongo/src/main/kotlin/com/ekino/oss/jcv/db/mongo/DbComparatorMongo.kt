package com.ekino.oss.jcv.db.mongo

import com.ekino.oss.jcv.core.JsonComparator
import com.ekino.oss.jcv.core.JsonValidator
import com.ekino.oss.jcv.db.exception.DbAssertException
import com.ekino.oss.jcv.db.mongo.util.DBComparatorBuilder
import com.ekino.oss.jcv.db.mongo.util.MongoConverter
import com.ekino.oss.jcv.db.util.JsonConverter
import com.ekino.oss.jcv.db.util.JsonConverter.compareJsonAndLogResult
import com.mongodb.client.FindIterable
import com.mongodb.client.MongoDatabase
import org.bson.Document
import org.intellij.lang.annotations.Language
import org.json.JSONArray
import org.skyscreamer.jsonassert.JSONCompareMode
import java.io.InputStream

class DbComparatorMongo(
    private val mongoConverter: MongoConverter,
    private val jsonComparator: JsonComparator,
    private val mapper: MongoMapper,
    private val mongoDatabase: MongoDatabase,
    private val query: (MongoDatabase) -> List<Document>
) {

    companion object {
        @JvmStatic
        fun assertThatCollection(query: (MongoDatabase) -> FindIterable<Document>) = DBComparatorBuilder.create().buildWithCollection(query)

        @JvmStatic
        fun assertThatDocument(query: (MongoDatabase) -> Document) = DBComparatorBuilder.create().buildWithDocument(query)
    }

    fun isValidAgainst(@Language("JSON") input: String) = JsonConverter.formatInput(input)?.let { compareActualAndExcepted(it) } ?: throw DbAssertException(
        "Unable to parse expected result from string to json"
    )

    fun isValidAgainst(inputStream: InputStream) = isValidAgainst(JsonConverter.loadFileAsString(inputStream))

    private fun compareActualAndExcepted(expected: JSONArray) {
        val actualJson = mongoConverter.convertContentToTableModel(query.invoke(mongoDatabase)).getTableModelAsJson(mapper)
        compareJsonAndLogResult(actualJson, expected, jsonComparator)
    }

    fun <T : JsonValidator<*>> using(vararg validators: T) = using(validators.toList())

    fun using(mongoDatabase: MongoDatabase) = DbComparatorMongo(
        mongoConverter,
        jsonComparator,
        mapper,
        mongoDatabase,
        query
    )

    fun <T : JsonValidator<*>> using(validators: List<T>) = using(JSONCompareMode.STRICT, validators)

    fun <T : JsonValidator<*>> using(mode: JSONCompareMode, validators: List<T>) = using(mode, validators, mapper, mongoDatabase)

    fun <T : JsonValidator<*>> using(mode: JSONCompareMode, vararg validators: T) = using(mode, validators.toList())

    fun using(comparator: JsonComparator) =
        DbComparatorMongo(mongoConverter, comparator, mapper, mongoDatabase, query)

    fun <T : JsonValidator<*>> using(mode: JSONCompareMode, validators: List<T>, mapper: MongoMapper, mongoDatabase: MongoDatabase) =
        DbComparatorMongo(
            mongoConverter,
            JsonComparator(mode, validators),
            mapper,
            mongoDatabase,
            query
        )
}
