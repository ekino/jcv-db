package com.ekino.oss.jcv.db.assertj.mapper

import com.ekino.oss.jcv.db.exception.DbAssertException
import com.ekino.oss.jcv.db.model.Circle
import com.ekino.oss.jcv.db.model.DefaultGeometricModel
import com.ekino.oss.jcv.db.model.EuclideanLine
import com.ekino.oss.jcv.db.model.Point
import com.ekino.oss.jcv.db.util.takeIfIsJson
import com.ekino.oss.jcv.db.util.toJsonArray
import com.ekino.oss.jcv.db.util.toJsonObject
import org.assertj.db.type.Value
import org.postgresql.geometric.PGbox
import org.postgresql.geometric.PGcircle
import org.postgresql.geometric.PGline
import org.postgresql.geometric.PGlseg
import org.postgresql.geometric.PGpath
import org.postgresql.geometric.PGpoint
import org.postgresql.geometric.PGpolygon
import org.postgresql.util.PGInterval
import org.postgresql.util.PGobject
import java.math.BigDecimal
import java.sql.Timestamp
import java.util.UUID

open class PostgresMapper : AssertJBaseMapper() {

    override fun mapUUIDType(value: Value) = (value.value as UUID).toString()

    override fun mapBooleanType(value: Value): Any = value.value

    override fun mapNumberType(value: Value): Any {
        return when (val valueOfValue = value.value) {
            is Float -> java.lang.Double.valueOf(valueOfValue.toString())
            is BigDecimal -> valueOfValue.toDouble()
            else -> value.value
        }
    }

    override fun mapTextType(value: Value) = value.value.toString().trim { it <= ' ' }

    override fun mapNotIdentifiedType(value: Value): Any? {
        return when (val valueOfValue = value.value) {
            is PGInterval -> valueOfValue.value
            is PGcircle -> Circle(
                valueOfValue.radius,
                convertPGPointToPointModel(valueOfValue.center)
            ).toJsonObject()
            is PGpoint -> convertPGPointToPointModel(valueOfValue).toJsonObject()
            is PGlseg -> DefaultGeometricModel(valueOfValue.point.map {
                convertPGPointToPointModel(
                    it
                )
            }.toList()).toJsonArray()
            is PGline -> EuclideanLine(
                valueOfValue.a,
                valueOfValue.b,
                valueOfValue.c
            ).toJsonObject()
            is PGpolygon -> DefaultGeometricModel(valueOfValue.points.map {
                convertPGPointToPointModel(
                    it
                )
            }.toList()).toJsonArray()
            is PGpath -> DefaultGeometricModel(valueOfValue.points.map {
                convertPGPointToPointModel(
                    it
                )
            }.toList()).toJsonArray()
            is PGbox -> DefaultGeometricModel(valueOfValue.point.map {
                convertPGPointToPointModel(
                    it
                )
            }.toList()).toJsonArray()
            is PGobject -> valueOfValue.value.takeIfIsJson() ?: throw DbAssertException(
                "Unable to parse pg object to json"
            )
            else -> value.value
        }
    }

    override fun mapTimeType(value: Value) = value.value.toString().trim { it <= ' ' }
    override fun mapDateTimeType(value: Value) = (value.value as Timestamp).toInstant().toString()
    override fun mapDateType(value: Value) = value.value.toString().trim { it <= ' ' }

    private fun convertPGPointToPointModel(point: PGpoint) = Point(point.x, point.y)
}
