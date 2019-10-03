package com.ekino.oss.jcv.db

import com.ekino.oss.jcv.core.JsonComparator
import com.ekino.oss.jcv.core.validator.Validators
import com.ekino.oss.jcv.db.exception.DbAssertException
import com.ekino.oss.jcv.db.model.RowModel
import com.ekino.oss.jcv.db.model.TableModel
import com.ekino.oss.jcv.db.util.JsonConverter
import com.ekino.oss.jcv.db.util.JsonConverter.getTableModelAsJson
import com.ekino.oss.jcv.db.util.takeIfIsJson
import org.json.JSONArray
import org.skyscreamer.jsonassert.JSONCompareMode
import java.io.IOException
import java.io.InputStream

class DbComparatorAssert(private val actualJson: JSONArray, private val jsonComparator: JsonComparator) {

    companion object {
        @JvmStatic
        fun assertThatTableModel(tableModel: TableModel): DbComparatorAssert {
            val actualJson = getTableModelAsJson(tableModel)
            return DbComparatorAssert(
                actualJson,
                JsonComparator(JSONCompareMode.NON_EXTENSIBLE, Validators.defaultValidators())
            )
        }

        @JvmStatic
        fun assertThatRowModel(rowModel: RowModel): DbComparatorAssert {
            val actualJson = getTableModelAsJson(TableModel(mutableSetOf(rowModel)))
            return DbComparatorAssert(
                actualJson,
                JsonComparator(JSONCompareMode.NON_EXTENSIBLE, Validators.defaultValidators())
            )
        }
    }

    fun isValidAgainst(input: String) = input.takeIfIsJson()?.let { compareActualAndExcepted(it as JSONArray) } ?: throw DbAssertException(
        "Unable to parse pg object to json"
    )

    @Throws(IOException::class)
    fun isValidAgainst(inputStream: InputStream) = compareActualAndExcepted(JsonConverter.loadJson(inputStream))

    private fun compareActualAndExcepted(expected: JSONArray) {
        JsonConverter.compareJsonAndLogResult(actualJson, expected, jsonComparator)
    }
}
