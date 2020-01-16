package com.ekino.oss.jcv.db.mongo

import com.ekino.oss.jcv.core.JsonComparator
import com.ekino.oss.jcv.core.JsonValidator
import com.ekino.oss.jcv.db.exception.DbAssertException
import com.ekino.oss.jcv.db.mongo.util.DBComparatorBuilder
import com.ekino.oss.jcv.db.mongo.util.MongoConverter
import com.ekino.oss.jcv.db.util.JsonConverter
import com.ekino.oss.jcv.db.util.JsonConverter.compareJsonAndLogResult
import com.ekino.oss.jcv.db.util.takeIfIsJson
import com.mongodb.client.FindIterable
import org.bson.Document
import org.json.JSONArray
import org.skyscreamer.jsonassert.JSONCompareMode
import java.io.IOException
import java.io.InputStream

class DbComparatorMongo(
    private val mongoConverter: MongoConverter,
    private val jsonComparator: JsonComparator,
    private val mapper: MongoMapper,
    private val collection: List<Document>
) {

    companion object {
        @JvmStatic
        fun assertThatCollection(collection: FindIterable<Document>) = DBComparatorBuilder.create().build(collection)

        @JvmStatic
        fun assertThatCollection(document: Document) = DBComparatorBuilder.create().build(document)
    }

    fun isValidAgainst(input: String) = input.takeIfIsJson()?.let { compareActualAndExcepted(it as JSONArray) } ?: throw DbAssertException(
        "Unable to parse expected result from string to json array"
    )

    @Throws(IOException::class)
    fun isValidAgainst(inputStream: InputStream) = compareActualAndExcepted(JsonConverter.loadJson(inputStream))

    private fun compareActualAndExcepted(expected: JSONArray) {
        val actualJson = mongoConverter.convertContentToTableModel(collection).getTableModelAsJson(mapper)
        compareJsonAndLogResult(actualJson, expected, jsonComparator)
    }

    fun <T : JsonValidator<*>> using(vararg validators: T) = using(validators.toList())

    fun <T : JsonValidator<*>> using(validators: List<T>) = using(JSONCompareMode.STRICT, validators)

    fun <T : JsonValidator<*>> using(mode: JSONCompareMode, validators: List<T>) = using(mode, validators, mapper)

    fun <T : JsonValidator<*>> using(mode: JSONCompareMode, vararg validators: T) = using(mode, validators.toList())

    fun using(comparator: JsonComparator) =
        DbComparatorMongo(mongoConverter, comparator, mapper, collection)

    fun <T : JsonValidator<*>> using(mode: JSONCompareMode, validators: List<T>, mapper: MongoMapper) =
        DbComparatorMongo(
            mongoConverter,
            JsonComparator(mode, validators),
            mapper,
            collection
        )
}
