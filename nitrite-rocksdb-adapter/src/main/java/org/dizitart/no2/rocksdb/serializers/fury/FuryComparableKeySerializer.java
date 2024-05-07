package org.dizitart.no2.rocksdb.serializers.fury;


import com.esotericsoftware.kryo.kryo5.Kryo;
import com.esotericsoftware.kryo.kryo5.io.Input;
import com.esotericsoftware.kryo.kryo5.io.Output;
import org.dizitart.no2.rocksdb.serializers.kyro.KryoKeySerializer;

/**
 * @since 4.0
 * @author Anindya Chatterjee

//TODO: Verify if needed
public abstract class FuryComparableKeySerializer<T extends Comparable<?>> extends FuryKeySerializer<T> {
    private static final String NULL = "null";

    protected abstract void writeKeyInternal(Kryo kryo, Output output, T object);
    protected abstract T readKeyInternal(Kryo kryo, String input, Class<T> type);

    @Override
    public void write(Kryo kryo, Output output, T object) {
        kryo.writeObject(output, object);
    }

    @Override
    public T read(Kryo kryo, Input input, Class<? extends T> type) {
        return kryo.readObject(input, type);
    }

    @Override
    public void writeKey(Kryo kryo, Output output, T object) {
        if (object == null) {
            output.writeString(NULL);
        } else {
            writeKeyInternal(kryo, output, object);
        }
    }

    @Override
    public T readKey(Kryo kryo, Input input, Class<T> type) {
        String value = input.readString();
        if (NULL.equals(value)) return null;
        return readKeyInternal(kryo, value, type);
    }
} */
