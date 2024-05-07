package org.dizitart.no2.rocksdb.serializers.fury;

import lombok.extern.slf4j.Slf4j;
import org.apache.fury.Fury;
import org.apache.fury.ThreadLocalFury;
import org.apache.fury.ThreadSafeFury;
import org.apache.fury.config.Language;
import org.dizitart.no2.exceptions.NitriteIOException;
import org.dizitart.no2.rocksdb.serializers.ObjectSerializer;

import java.util.Arrays;

import static org.dizitart.no2.rocksdb.Constants.DB_NULL;

/**
 * This class is a custom serializer using Fury.
 * <p>
 * WARNING: Changing serializers on an existing database can lead to data corruption or loss.
 * If you need to change the serializer, consider creating a new database or ensure you have a
 * reliable backup and migration strategy.
 * </p>
 */
@Slf4j(topic = "nitrite-rocksdb")
public class FuryObjectSerializer implements ObjectSerializer {



    private final ThreadSafeFury fury = new ThreadLocalFury(classLoader -> {
        Fury f = Fury.builder().withLanguage(Language.JAVA)
                .withClassLoader(classLoader)
                .requireClassRegistration(false)
                .build();

        // Fury already has java time serializers
        // https://github.com/apache/incubator-fury/blob/main/java/fury-core/src/main/java/org/apache/fury/serializer/TimeSerializers.java
        FuryNitriteSerializers.registerAll(f);
        return f;
    });

    public FuryObjectSerializer() {

    }

    @Override
    public <T> byte[] encode(T object) {
        if (object == null) return DB_NULL;

        try {
            return fury.serialize(object);
        } catch (Exception e) {
            throw new NitriteIOException("Failed to serialize object", e);
        }
    }

    @Override
    public <T> byte[] encodeKey(T object) {
        return encode(object);
    }


    @Override
    @SuppressWarnings("unchecked")
    public <T> T decode(byte[] bytes, Class<T> type) {
        if (Arrays.equals(bytes, DB_NULL)) return null;

        try {
            return (T) fury.deserialize(bytes);
        } catch (Exception e) {
            throw new NitriteIOException("Failed to deserialize object", e);
        }
    }

    @Override
    public <T> T decodeKey(byte[] bytes, Class<T> type) {
        return decode(bytes, type);
    }

}
