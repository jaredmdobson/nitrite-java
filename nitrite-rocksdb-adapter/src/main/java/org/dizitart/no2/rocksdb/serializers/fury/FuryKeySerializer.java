package org.dizitart.no2.rocksdb.serializers.fury;


import com.esotericsoftware.kryo.kryo5.Kryo;
import com.esotericsoftware.kryo.kryo5.Serializer;
import com.esotericsoftware.kryo.kryo5.io.Input;
import com.esotericsoftware.kryo.kryo5.io.Output;
import org.apache.fury.Fury;

/**
 * @since 4.0
 * @author Anindya Chatterjee

public abstract class FuryKeySerializer<T> extends Serializer<T> {
    public abstract void writeKey(Fury kryo, Output output, T object);
    public abstract T readKey(Fury kryo, Input input, Class<T> type);

    public boolean registerToFury() {
        return false;
    }
}
 */