package com.ekino.oss.jcv.db.jdbc.mapper

import com.ekino.oss.jcv.db.model.Circle
import com.ekino.oss.jcv.db.model.DefaultGeometricModel
import com.ekino.oss.jcv.db.model.EuclideanLine
import com.ekino.oss.jcv.db.model.Point
import com.ekino.oss.jcv.db.util.toJsonArray
import com.ekino.oss.jcv.db.util.toJsonObject
import org.json.JSONArray
import org.json.JSONObject
import org.json.JSONTokener
import org.postgresql.geometric.PGbox
import org.postgresql.geometric.PGcircle
import org.postgresql.geometric.PGline
import org.postgresql.geometric.PGlseg
import org.postgresql.geometric.PGpath
import org.postgresql.geometric.PGpoint
import org.postgresql.geometric.PGpolygon
import org.postgresql.util.PGInterval
import org.postgresql.util.PGobject
import java.sql.Timestamp
import java.util.UUID

open class PostgresMapper : JDBCMapper() {

    override fun defaultMapper(value: Any): Any = when (value) {
        is Timestamp -> value.toInstant().toString()
        is PGcircle -> Circle(value.radius, convertPGPointToPointModel(value.center)).toJsonObject()
        is PGpoint -> convertPGPointToPointModel(value).toJsonObject()
        is PGlseg -> convertArrayPGPointToDefaultModel(value.point).toJsonArray()
        is PGline -> EuclideanLine(value.a, value.b, value.c).toJsonObject()
        is PGpolygon -> convertArrayPGPointToDefaultModel(value.points).toJsonArray()
        is PGpath -> convertArrayPGPointToDefaultModel(value.points).toJsonArray()
        is PGbox -> convertArrayPGPointToDefaultModel(value.point).toJsonArray()
        is PGobject -> handlePgObject(value)
        is PGInterval -> value.value
        else -> value.toString()
    }

    override fun mapNumberType(value: Number): Number = when (value) {
        is Float -> java.lang.Double.valueOf(value.toString())
        else -> value
    }

    override fun mapString(value: String) = value.trim { it <= ' ' }

    override fun mapUUID(value: UUID) = value.toString()

    private fun convertPGPointToPointModel(point: PGpoint) = Point(point.x, point.y)

    private fun convertArrayPGPointToDefaultModel(points: Array<PGpoint>) =
        DefaultGeometricModel(points.map { convertPGPointToPointModel(it) }.toList())

    private fun handlePgObject(value: PGobject): Any {
        return when (val jsonObject = JSONTokener(value.value).nextValue()) {
            is JSONObject, is JSONArray -> jsonObject
            else -> value.value
        }
    }
}
