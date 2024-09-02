package com.ekino.oss.jcv.db.config

enum class DatabaseType(private val productName: String) {
    POSTGRESQL("PostgreSQL"),
    MYSQL("MySQL"),
    MSSQL("Microsoft SQL Server");

    companion object {
        fun getDatabaseTypeByProductName(name: String) = entries.find { it.productName == name }
    }
}
