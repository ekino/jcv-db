package com.ekino.oss.jcv.db.mongo.util

import com.ekino.oss.jcv.core.JsonComparator
import com.ekino.oss.jcv.core.JsonValidator
import com.ekino.oss.jcv.db.mongo.DbComparatorMongo
import com.ekino.oss.jcv.db.mongo.MongoMapper
import com.ekino.oss.jcv.db.mongo.config.DBMongoValidators
import com.mongodb.client.FindIterable
import org.bson.Document
import org.skyscreamer.jsonassert.JSONCompareMode

class DBComparatorBuilder {

    constructor()

    constructor(
        mode: JSONCompareMode,
        validators: List<JsonValidator<*>>
    ) {
        this.mode = mode
        this.validators = validators
    }

    private lateinit var mode: JSONCompareMode
    private lateinit var validators: List<JsonValidator<*>>
    private var customMapper: MongoMapper? = null

    companion object {
        @JvmStatic
        fun create() = DBComparatorBuilder(
            JSONCompareMode.STRICT,
            DBMongoValidators.defaultDBValidators()
        )
    }

    fun mode(mode: JSONCompareMode): DBComparatorBuilder {
        this.mode = mode
        return this
    }

    fun <T : JsonValidator<*>> validators(validators: List<T>): DBComparatorBuilder {
        this.validators = validators
        return this
    }

    fun <T : JsonValidator<*>> validators(vararg validators: T): DBComparatorBuilder {
        return validators(validators.toList())
    }

    fun mapper(mapper: MongoMapper): DBComparatorBuilder {
        this.customMapper = mapper
        return this
    }

    fun build(collection: FindIterable<Document>) = DbComparatorMongo(
        MongoConverter(),
        JsonComparator(mode, validators),
        customMapper ?: MongoMapper(),
        collection.toList()
    )

    fun build(document: Document) = DbComparatorMongo(
        MongoConverter(),
        JsonComparator(mode, validators),
        customMapper ?: MongoMapper(),
        listOf(document)
    )
}
