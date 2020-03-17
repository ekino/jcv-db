package com.ekino.oss.jcv.db.example.jcvdbmongoexample.repository;

import com.ekino.oss.jcv.db.example.jcvdbmongoexample.model.Currency;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.UUID;

public interface CurrencyRepository extends MongoRepository<Currency, UUID> {
}
