package com.ekino.oss.jcv.db.example.jcvdbjdbcexample.service;

import com.ekino.oss.jcv.db.example.jcvdbjdbcexample.dto.CurrencyDto;
import com.ekino.oss.jcv.db.jdbc.DbComparatorJDBC;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.sql.DriverManager;
import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CurrencyServiceTest {

    @Autowired
    public CurrencyService currencyService;

    @Test
    @DisplayName("Should create a currency")
    void shouldCreateCurrency() throws SQLException {
        CurrencyDto currency = currencyService.createCurrency("USD", "USD-label");

        assertThat(currency.getCode()).isEqualTo("USD");
        assertThat(currency.getLabel()).isEqualTo("USD-label");
        assertThat(currency.getId()).isNotNull();

        assertThatQuery("SELECT * FROM currency")
                .isValidAgainst(loadJson("expected_create_currency.json"));
    }

    private static DbComparatorJDBC assertThatQuery(String query) throws SQLException {
        return DbComparatorJDBC.assertThatQuery(query)
                    .using(DriverManager.getConnection("jdbc:postgresql://localhost:5432/jcv-db-test", "jcv-db-test", "jcv-db-test"));
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
