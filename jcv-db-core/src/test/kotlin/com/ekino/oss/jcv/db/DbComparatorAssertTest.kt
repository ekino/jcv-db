package com.ekino.oss.jcv.db

import assertk.assertThat
import assertk.assertions.hasMessage
import assertk.assertions.isFailure
import com.ekino.oss.jcv.db.model.RowModel
import com.ekino.oss.jcv.db.model.TableModel
import org.json.JSONArray
import org.json.JSONObject
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class DbComparatorAssertTest {

    @Test
    @DisplayName("Should test types for postgresql database")
    fun shouldTestNumericTypesPostGreSQL() {
        val expected = // language=json
            """
            [  
                {    
                    "id": "{#uuid#}",    
                    "column_boolean": true,    
                    "column_char_varying": "char varying",    
                    "column_varchar": "varchar",    
                    "column_character": "character",    
                    "column_char": "char",    
                    "column_text": "text",    
                    "column_unlimited_varchar": "{#string_type#}",
                    "column_smallint": 123,    
                    "column_int": 12355,    
                    "column_serial": 3282,    
                    "column_float": 123.2235,    
                    "column_real": 125125.13,    
                    "column_numeric": 312,    
                    "column_date": "2019-06-07",    
                    "column_time": "17:12:28",    
                    "column_timestamp": "2019-06-07 00:00:00.0",    
                    "column_timestamptz": "2019-06-07 00:00:00.0",   
                    "column_interval": "0 years 0 mons 100 days 0 hours 0 mins 0.00 secs",    
                    "column_json": [      
                        {        
                            "key": "Hello World !"      
                        }    
                    ],    
                    "column_jsonb": {      
                        "key": "Hello World !"    
                    }  
                }
            ]
            """.trimIndent()

        val tableModel = TableModel()
        tableModel.addRow(
            RowModel(
                mutableMapOf(
                    "id" to "0840ad8f-db82-472d-9dff-bb6d64992222",
                    "column_boolean" to true,
                    "column_char_varying" to "char varying",
                    "column_varchar" to "varchar",
                    "column_character" to "character",
                    "column_char" to "char",
                    "column_text" to "text",
                    "column_unlimited_varchar" to "unlimited varcharZOEFOEZUFHHZELF",
                    "column_smallint" to 123,
                    "column_int" to 12355,
                    "column_serial" to 3282,
                    "column_float" to 123.2235,
                    "column_real" to 125125.13,
                    "column_numeric" to 312,
                    "column_date" to "2019-06-07",
                    "column_time" to "17:12:28",
                    "column_timestamp" to "2019-06-07 00:00:00.0",
                    "column_timestamptz" to "2019-06-07 00:00:00.0",
                    "column_interval" to "0 years 0 mons 100 days 0 hours 0 mins 0.00 secs",
                    "column_json" to JSONArray("[{\"key\": \"Hello World !\"}]"),
                    "column_jsonb" to JSONObject("{\"key\": \"Hello World !\"}")
                )
            )
        )
        DbComparatorAssert.assertThatTableModel(tableModel).isValidAgainst(expected)
    }

    @Test
    @DisplayName("Should test error format")
    fun shouldTestErrorFormat() {
        val expected = // language=json
            """
            [  
                {    
                    "content_test": "abcd"
                }
            ]
            """.trimIndent()

        val tableModel = TableModel()
        tableModel.addRow(
            RowModel(
                mutableMapOf(
                    "content_test" to "abcde"
                )
            )
        )

        assertThat {
            DbComparatorAssert.assertThatTableModel(tableModel).isValidAgainst(expected)
        }.isFailure().hasMessage(
            """[content_test=abcd]
Expected: a JSON object
     but none found
 ; [content_test=abcde]
Unexpected: a JSON object

Actual: [{"content_test":"abcde"}]"""
        )
    }
}
