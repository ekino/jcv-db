package com.ekino.oss.jcv.db.example.jcvdbcassandraexample.service;

import com.ekino.oss.jcv.db.cassandra.DbComparatorCassandra;
import com.ekino.oss.jcv.db.cassandra.util.CassandraDataSource;
import com.ekino.oss.jcv.db.example.jcvdbcassandraexample.dto.CurrencyDto;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.cassandra.core.CassandraAdminOperations;
import org.springframework.data.cassandra.core.cql.CqlIdentifier;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CurrencyServiceTest {

    @Autowired
    public CurrencyService currencyService;

    @Autowired
    private CassandraAdminOperations adminTemplate;

    @AfterEach
    public void dropTable() {
        adminTemplate.dropTable(CqlIdentifier.of("currency"));
    }

    @Test
    @DisplayName("Should create a currency")
    void shouldCreateCurrency() {
        CurrencyDto currency = currencyService.createCurrency("USD", "USD-label");

        assertThat(currency.getCode()).isEqualTo("USD");
        assertThat(currency.getLabel()).isEqualTo("USD-label");
        assertThat(currency.getId()).isNotNull();

        DbComparatorCassandra
                .assertThatQuery("select * from jcvdb.currency")
                .using(new CassandraDataSource("datacenter1", "127.0.0.1", 9042, null, null))
                .isValidAgainst(loadJson("expected_create_currency.json"));
    }

    private static String loadJson(String filename) {
        try {
            return IOUtils.resourceToString(
                    Paths.get("/service/currencies", filename).toString(),
                    StandardCharsets.UTF_8
            );
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}

