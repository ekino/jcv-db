package com.ekino.oss.jcv.db.model

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonValue

data class Point(val x: Double, val y: Double, @field:JsonInclude(JsonInclude.Include.NON_NULL) val z: Double? = null)

data class DefaultGeometricModel(@field:JsonValue val points: List<Point>)

data class Circle(val radius: Double, val center: Point?)

data class EuclideanLine(val a: Double, val b: Double, val c: Double)
