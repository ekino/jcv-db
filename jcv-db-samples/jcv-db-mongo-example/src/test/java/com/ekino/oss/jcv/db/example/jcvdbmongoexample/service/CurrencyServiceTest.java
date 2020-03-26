package com.ekino.oss.jcv.db.example.jcvdbmongoexample.service;

import com.ekino.oss.jcv.db.example.jcvdbmongoexample.dto.CurrencyDto;
import com.ekino.oss.jcv.db.mongo.DbComparatorMongo;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.ServerAddress;
import org.apache.commons.io.IOUtils;
import org.bson.BsonType;
import org.bson.codecs.BsonTypeClassMap;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.jsr310.LocalDateCodec;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CurrencyServiceTest {

    @Autowired
    public CurrencyService currencyService;

    @Autowired
    public MongoTemplate mongoTemplate;

    @BeforeEach
    public void cleanMongoDb() {
        mongoTemplate.getDb().drop();
    }

    @Test
    @DisplayName("Should create a currency")
    void shouldCreateCurrency() {
        CurrencyDto currency = currencyService.createCurrency("USD", "USD-label");

        assertThat(currency.getCode()).isEqualTo("USD");
        assertThat(currency.getLabel()).isEqualTo("USD-label");
        assertThat(currency.getId()).isNotNull();


        Map<BsonType, Class<?>> replacements = new HashMap<BsonType, Class<?>>();
        replacements.put(BsonType.DATE_TIME, Instant.class);
        BsonTypeClassMap bsonTypeClassMap = new BsonTypeClassMap(replacements);

        CodecRegistry codecRegistry = CodecRegistries.fromRegistries(
            CodecRegistries.fromCodecs(new LocalDateCodec()),
            CodecRegistries.fromProviders(new DocumentCodecProvider(bsonTypeClassMap)),
            MongoClient.getDefaultCodecRegistry());

        MongoClientOptions options = MongoClientOptions.builder()
                .codecRegistry(codecRegistry).build();

        MongoClient client = new MongoClient(new ServerAddress("localhost", 27017), options);

        DbComparatorMongo.assertThatCollection(client.getDatabase("jcv-db-mongo-test").getCollection("currencies").find())
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

