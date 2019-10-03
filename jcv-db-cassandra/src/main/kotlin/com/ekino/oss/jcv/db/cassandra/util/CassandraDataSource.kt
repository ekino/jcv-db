package com.ekino.oss.jcv.db.cassandra.util

data class CassandraDataSource(val datacenter: String, val hostname: String, val port: Int, val username: String? = null, val password: String? = null)
