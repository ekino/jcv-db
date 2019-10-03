package com.ekino.oss.jcv.db.assertj.exception

/**
 * A runtime exception wrapper to handle silently json parsing exceptions.
 */
class JsonParseException(message: String, cause: Throwable) : RuntimeException(message, cause)
