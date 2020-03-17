package com.ekino.oss.jcv.db.example.jcvdbmongoexample.service;

import org.bson.Document;
import org.bson.codecs.BsonTypeClassMap;
import org.bson.codecs.Codec;
import org.bson.codecs.DocumentCodec;
import org.bson.codecs.configuration.CodecProvider;
import org.bson.codecs.configuration.CodecRegistry;

public class DocumentCodecProvider implements CodecProvider {
    private final BsonTypeClassMap bsonTypeClassMap;

    public DocumentCodecProvider(final BsonTypeClassMap bsonTypeClassMap) {
        this.bsonTypeClassMap = bsonTypeClassMap;
    }

    @Override
    public <T> Codec<T> get(final Class<T> clazz, final CodecRegistry registry) {
        if (clazz == Document.class) {
            // construct DocumentCodec with a CodecRegistry and a BsonTypeClassMap
            return (Codec<T>) new DocumentCodec(registry, bsonTypeClassMap);
        }

        return null;
    }
}
