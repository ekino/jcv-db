package com.ekino.oss.jcv.db.example.jcvdbassertjdbexample.service;

import com.ekino.oss.jcv.db.assertj.DbComparatorAssertJ;
import com.ekino.oss.jcv.db.example.jcvdbassertjdbexample.dto.CurrencyDto;
import org.apache.commons.io.IOUtils;
import org.assertj.db.type.Source;
import org.assertj.db.type.Table;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CurrencyServiceTest {

    @Autowired
    public CurrencyService currencyService;

    @Test
    @DisplayName("Should create a currency")
    void shouldCreateCurrency() {
        CurrencyDto currency = currencyService.createCurrency("USD", "USD-label");

        assertThat(currency.getCode()).isEqualTo("USD");
        assertThat(currency.getLabel()).isEqualTo("USD-label");
        assertThat(currency.getId()).isNotNull();

        assertThatTable("currency")
                .isValidAgainst(loadJson("expected_create_currency.json"));
    }

    private static DbComparatorAssertJ assertThatTable(String tableName) {
        return DbComparatorAssertJ.assertThatTable(
                new Table(new Source("jdbc:postgresql://localhost:5432/jcv-db-test", "jcv-db-test", "jcv-db-test"), tableName));
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
