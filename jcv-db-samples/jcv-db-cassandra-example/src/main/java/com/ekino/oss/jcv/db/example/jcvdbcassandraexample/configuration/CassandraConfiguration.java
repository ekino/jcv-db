package com.ekino.oss.jcv.db.example.jcvdbcassandraexample.configuration;

import com.datastax.driver.core.policies.ConstantReconnectionPolicy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.cassandra.config.AbstractCassandraConfiguration;
import org.springframework.data.cassandra.config.CassandraClusterFactoryBean;
import org.springframework.data.cassandra.config.SchemaAction;
import org.springframework.data.cassandra.core.cql.keyspace.CreateKeyspaceSpecification;
import org.springframework.data.cassandra.core.cql.keyspace.DropKeyspaceSpecification;
import org.springframework.data.cassandra.core.cql.keyspace.KeyspaceOption;

import java.util.Collections;
import java.util.List;

@Configuration
public class CassandraConfiguration extends AbstractCassandraConfiguration {

    @Value("${spring.data.cassandra.contactpoints}") private String contactPoints;
    @Value("${spring.data.cassandra.port}") private int port;
    @Value("${spring.data.cassandra.keyspace-name}") private String keyspace;

    @Override protected String getKeyspaceName() {
        return keyspace;
    }
    @Override protected String getContactPoints() {
        return contactPoints;
    }
    @Override protected int getPort() {
        return port;
    }
    @Override public SchemaAction getSchemaAction() {
        return SchemaAction.CREATE_IF_NOT_EXISTS;
    }
    @Override
    public String[] getEntityBasePackages() {
        return new String[]{"com.ekino.oss.jcv.db.example.jcvdbcassandraexample.model"};
    }

    @Override
    public CassandraClusterFactoryBean cluster() {
        CassandraClusterFactoryBean cluster=new CassandraClusterFactoryBean();

        cluster.setJmxReportingEnabled(false);
        cluster.setContactPoints(contactPoints);
        cluster.setPort(port);
        cluster.setKeyspaceCreations(getKeyspaceCreations());
        cluster.setReconnectionPolicy(new ConstantReconnectionPolicy(1000));

        return cluster;
    }

    @Override
    protected List<CreateKeyspaceSpecification> getKeyspaceCreations() {

        CreateKeyspaceSpecification specification = CreateKeyspaceSpecification.createKeyspace(keyspace)
                .ifNotExists()
                .with(KeyspaceOption.DURABLE_WRITES, true);

        return Collections.singletonList(specification);
    }

    @Override
    protected List<DropKeyspaceSpecification> getKeyspaceDrops() {
        return Collections.singletonList(DropKeyspaceSpecification.dropKeyspace(keyspace));
    }
}
