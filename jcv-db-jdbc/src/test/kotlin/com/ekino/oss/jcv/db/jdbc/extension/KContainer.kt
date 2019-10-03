package com.ekino.oss.jcv.db.jdbc.extension

import org.testcontainers.containers.MSSQLServerContainer
import org.testcontainers.containers.MySQLContainer
import org.testcontainers.containers.PostgreSQLContainer

class KPostgreSQLContainer(imageName: String) : PostgreSQLContainer<KPostgreSQLContainer>(imageName)

class KMySQLContainer(imageName: String) : MySQLContainer<KMySQLContainer>(imageName)

class KMSSQLContainer(imageName: String) : MSSQLServerContainer<KMSSQLContainer>(imageName)
