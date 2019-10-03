package com.ekino.oss.jcv.db.config

enum class DatabaseType(private val productName: String) {
    POSTGRESQL("PostgreSQL"),
    MYSQL("MySQL"),
    MSSQL("Microsoft SQL Server");

    companion object {
        fun getDatabaseTypeByProductName(name: String) = values().find { it.productName == name }
    }
}
