package com.ekino.oss.jcv.db.example.jcvdbjdbcexample.repository;

import com.ekino.oss.jcv.db.example.jcvdbjdbcexample.model.Currency;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CurrencyRepository extends JpaRepository<Currency, UUID> {
}
