package com.ekino.oss.jcv.db

import com.ekino.oss.jcv.core.JsonComparator
import com.ekino.oss.jcv.core.validator.Validators
import com.ekino.oss.jcv.db.exception.DbAssertException
import com.ekino.oss.jcv.db.model.RowModel
import com.ekino.oss.jcv.db.model.TableModel
import com.ekino.oss.jcv.db.util.JsonConverter
import org.json.JSONArray
import org.skyscreamer.jsonassert.JSONCompareMode
import java.io.InputStream

class DbComparatorAssert(private val actualJson: JSONArray, private val jsonComparator: JsonComparator) {

    companion object {
        @JvmStatic
        fun assertThatTableModel(tableModel: TableModel): DbComparatorAssert {
            val actualJson = tableModel.getTableModelAsJson()
            return DbComparatorAssert(
                actualJson,
                JsonComparator(JSONCompareMode.NON_EXTENSIBLE, Validators.defaultValidators())
            )
        }

        @JvmStatic
        fun assertThatRowModel(rowModel: RowModel): DbComparatorAssert {
            val actualJson = TableModel(mutableSetOf(rowModel)).getTableModelAsJson()
            return DbComparatorAssert(
                actualJson,
                JsonComparator(JSONCompareMode.NON_EXTENSIBLE, Validators.defaultValidators())
            )
        }
    }

    fun isValidAgainst(input: String) = JsonConverter.formatInput(input)?.let { compareActualAndExcepted(it) } ?: throw DbAssertException(
        "Unable to parse expected result from string to json"
    )

    fun isValidAgainst(inputStream: InputStream) = isValidAgainst(JsonConverter.loadFileAsString(inputStream))

    private fun compareActualAndExcepted(expected: JSONArray) {
        JsonConverter.compareJsonAndLogResult(actualJson, expected, jsonComparator)
    }
}
