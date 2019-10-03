package com.ekino.oss.jcv.db.mapper.geometry

import com.ekino.oss.jcv.db.model.DefaultGeometricModel
import com.ekino.oss.jcv.db.util.toJsonArray
import com.ekino.oss.jcv.db.util.toJsonObject
import org.locationtech.jts.geom.Coordinate
import org.locationtech.jts.geom.Geometry
import org.locationtech.jts.geom.GeometryCollection
import org.locationtech.jts.geom.GeometryFactory
import org.locationtech.jts.geom.LineString
import org.locationtech.jts.geom.MultiLineString
import org.locationtech.jts.geom.MultiPoint
import org.locationtech.jts.geom.MultiPolygon
import org.locationtech.jts.geom.Point
import org.locationtech.jts.geom.Polygon
import org.locationtech.jts.geom.PrecisionModel
import org.locationtech.jts.io.ByteOrderValues
import org.locationtech.jts.io.InputStreamInStream
import org.locationtech.jts.io.ParseException
import org.locationtech.jts.io.WKBReader
import org.locationtech.jts.io.WKTReader
import java.io.ByteArrayInputStream
import com.ekino.oss.jcv.db.model.Point as ModelPoint

object GeometryMapper {

    @JvmStatic
    fun fromByteArrayToGeometryType(bytes: ByteArray) = readByteArray(bytes)?.let { handleGeometryType(it) }

    @JvmStatic
    fun fromWKTStringToGeometryType(wktString: String): Any? {
        return readWktString(wktString)?.let { handleGeometryType(it) }
    }

    private fun handleGeometryType(geometry: Geometry): Any? = when (geometry) {
        is Point -> convertPoint(geometry.coordinate).toJsonObject()
        is LineString, is Polygon, is MultiPoint -> convertGeometry(geometry).toJsonArray()
        is MultiPolygon, is MultiLineString -> convertGeometryCollection(geometry as GeometryCollection).toJsonArray()
        else -> null
    }

    private fun convertGeometryCollection(geometryCollection: GeometryCollection): List<DefaultGeometricModel> {
        val geometries = mutableListOf<DefaultGeometricModel>()
        for (i in 0 until geometryCollection.numGeometries) {
            geometries.add(convertGeometry(geometryCollection.getGeometryN(i)))
        }
        return geometries.toList()
    }

    private fun convertGeometry(geometry: Geometry) = DefaultGeometricModel(geometry.coordinates.map { convertPoint(it) }.toList())

    private fun convertPoint(coordinate: Coordinate): ModelPoint {
        return ModelPoint(coordinate.getX(), coordinate.getY(), coordinate.getZ().takeIf { !it.isNaN() })
    }

    private fun readWktString(wktString: String) = try {
        WKTReader().read(wktString)
    } catch (e: ParseException) {
        null
    }

    private fun readByteArray(bytes: ByteArray): Geometry? {
        val byteOrder = ByteOrderValues.LITTLE_ENDIAN
        val inputStream = ByteArrayInputStream(bytes)

        // SRID
        val sridBytes = ByteArray(4)
        inputStream.read(sridBytes)
        val srid = ByteOrderValues.getInt(sridBytes, byteOrder)

        // Geometry
        val geometryFactory = GeometryFactory(PrecisionModel(PrecisionModel.maximumPreciseValue), srid)
        val wkbReader = WKBReader(geometryFactory)

        return try {
            wkbReader.read(InputStreamInStream(inputStream))
        } catch (e: ParseException) {
            null
        }
    }
}
