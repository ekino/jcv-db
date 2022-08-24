package com.ekino.oss.jcv.db.mongo

import com.ekino.oss.jcv.db.mongo.util.DBComparatorBuilder
import com.mongodb.BasicDBObject
import com.mongodb.client.FindIterable
import com.mongodb.client.MongoClients
import com.mongodb.client.MongoDatabase
import com.mongodb.client.model.Filters.eq
import com.mongodb.client.model.Sorts.descending
import org.bson.Document
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test

class MongoAssertDB {

    companion object {
        const val ADDRESS = "localhost"
        const val PORT = 27017

        @BeforeAll
        @JvmStatic
        fun setUpMongoData() {
            val mongoClient = MongoClients.create("mongodb://$ADDRESS:$PORT")

            val document1 = Document("name", "test-mongo-db")
            val document2 = Document("name", "test-mongo-db-2")
            val document3 = Document("name", "test-mongo-db-3")
                .append(
                    "json-object",
                    Document("name", "test-json-object-field").append(
                        "json-object",
                        Document("name", "test-json-object-field")
                    )
                )
            val document4 = Document("name", "test-mongo-db-4")
                .append("json-array", listOf(Document("name", "test-name").append("null-field", null)))

            mongoClient.getDatabase("test").getCollection("testCollection").insertOne(document1)
            mongoClient.getDatabase("test").getCollection("testCollection").insertOne(document2)
            mongoClient.getDatabase("test").getCollection("testCollection").insertOne(document3)
            mongoClient.getDatabase("test").getCollection("testCollection").insertOne(document4)
        }
    }

    private fun assertThatDocument(query: (MongoDatabase) -> Document?): DbComparatorMongo {
        return DBComparatorBuilder
            .create()
            .database(MongoClients.create("mongodb://$ADDRESS:$PORT").getDatabase("test"))
            .buildWithDocument(query)
    }

    private fun assertThatCollection(query: (MongoDatabase) -> FindIterable<Document>): DbComparatorMongo {
        return DBComparatorBuilder
            .create()
            .database(MongoClients.create("mongodb://$ADDRESS:$PORT").getDatabase("test"))
            .buildWithCollection(query)
    }

    @Test
    fun `Should assert mongo collection`() {

        val expected = // language=json
            """
                [
                  {
                    "name": "test-mongo-db",
                    "_id": "{#mongo_id#}"
                  },
                  {
                    "name": "test-mongo-db-2",
                    "_id": "{#mongo_id#}"
                  },
                  {
                    "name": "test-mongo-db-3",
                    "json-object": { 
                      "name": "test-json-object-field",
                      "json-object": { "name": "test-json-object-field" }
                    },
                    "_id": "{#mongo_id#}"
                  },
                  {
                    "name": "test-mongo-db-4",
                    "json-array": [
                      { 
                        "name": "test-name",
                        "null-field": null
                      }
                    ],
                    "_id": "{#mongo_id#}"
                  }
                ]
            """.trimIndent()

        assertThatCollection { database: MongoDatabase -> database.getCollection("testCollection").find() }.isValidAgainst(expected)
    }

    @Test
    fun `Should assert single document`() {

        val expected = // language=json
            """
              {
                "name": "test-mongo-db",
                "_id": "{#mongo_id#}"
              }
            """.trimIndent()

        assertThatDocument { database: MongoDatabase -> database.getCollection("testCollection").find(eq("name", "test-mongo-db")).first() }.isValidAgainst(expected)
    }

    @Test
    fun `Should validate document with json object field`() {

        val expected = // language=json
            """
                [
                  {
                    "name": "test-mongo-db-3",
                    "_id": "{#mongo_id#}",
                    "json-object": {
                      "name": "test-json-object-field",
                      "json-object": {
                        "name": "test-json-object-field"
                      }
                    }
                  }
                ]
            """.trimIndent()

        assertThatDocument { database: MongoDatabase -> database.getCollection("testCollection").find(eq("name", "test-mongo-db-3")).first() }.isValidAgainst(expected)
    }

    @Test
    fun `Should validate document with json array field`() {

        val expected = // language=json
            """
                [
                  {
                    "name": "test-mongo-db-4",
                    "json-array": [
                      {
                        "name": "test-name",
                        "null-field": null
                      }
                    ],
                    "_id": "{#mongo_id#}"
                  }
                ]
            """.trimIndent()

        assertThatDocument { database: MongoDatabase -> database.getCollection("testCollection").find(eq("name", "test-mongo-db-4")).first() }.isValidAgainst(expected)
    }

    @Test
    fun `Should validate document with json object validator`() {

        val expected = // language=json
            """
                [
                  {
                    "name": "test-mongo-db-3",
                    "_id": "{#mongo_id#}",
                    "json-object": {
                      "name": "test-json-object-field",
                      "json-object": "{#json_object#}"
                    }
                  }
                ]
            """.trimIndent()

        assertThatDocument { database: MongoDatabase -> database.getCollection("testCollection").find(eq("name", "test-mongo-db-3")).first() }.isValidAgainst(expected)
    }

    @Test
    fun `Should validate document with json array validator`() {

        val expected = // language=json
            """
                [
                  {
                    "name": "test-mongo-db-4",
                    "json-array": "{#json_array#}",
                    "_id": "{#mongo_id#}"
                  }
                ]
            """.trimIndent()

        assertThatDocument { database: MongoDatabase -> database.getCollection("testCollection").find(eq("name", "test-mongo-db-4")).first() }.isValidAgainst(expected)
    }

    @Test
    fun `Should assert request with field restriction`() {

        val expected = // language=json
            """
                [
                  {
                    "name": "test-mongo-db",
                    "_id": "{#mongo_id#}"
                  },
                  {
                    "name": "test-mongo-db-2",
                    "_id": "{#mongo_id#}"
                  },
                  {
                    "name": "test-mongo-db-3",
                    "_id": "{#mongo_id#}"
                  },
                  {
                    "name": "test-mongo-db-4",
                    "_id": "{#mongo_id#}"
                  }
                ]
            """.trimIndent()

        assertThatCollection { database: MongoDatabase -> database.getCollection("testCollection").find().projection(BasicDBObject("name", true)) }.isValidAgainst(expected)
    }

    @Test
    fun `Should assert request with ordering`() {

        val expected = // language=json
            """
                [
                  {
                    "name": "test-mongo-db-4",
                    "json-array": [
                      { 
                        "name": "test-name",
                        "null-field": null
                      }
                    ],
                    "_id": "{#mongo_id#}"
                  },
                  {
                    "name": "test-mongo-db-3",
                    "json-object": { 
                      "name": "test-json-object-field",
                      "json-object": { "name": "test-json-object-field" }
                    },
                    "_id": "{#mongo_id#}"
                  },
                  {
                    "name": "test-mongo-db-2",
                    "_id": "{#mongo_id#}"
                  },
                  {
                    "name": "test-mongo-db",
                    "_id": "{#mongo_id#}"
                  }
                ]
            """.trimIndent()

        assertThatCollection { database: MongoDatabase -> database.getCollection("testCollection").find().sort(descending("name")) }.isValidAgainst(expected)
    }
}
