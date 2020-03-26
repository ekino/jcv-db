package com.ekino.oss.jcv.db.example.jcvdbcassandraexample.repository;

import com.ekino.oss.jcv.db.example.jcvdbcassandraexample.model.Currency;
import org.springframework.data.cassandra.repository.CassandraRepository;

import java.util.UUID;

public interface CurrencyRepository extends CassandraRepository<Currency, UUID> {
}
