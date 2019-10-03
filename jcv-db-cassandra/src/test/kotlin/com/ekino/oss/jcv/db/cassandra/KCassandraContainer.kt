package com.ekino.oss.jcv.db.cassandra

import org.testcontainers.containers.CassandraContainer

class KCassandraContainer(imageName: String) : CassandraContainer<KCassandraContainer>(imageName)
