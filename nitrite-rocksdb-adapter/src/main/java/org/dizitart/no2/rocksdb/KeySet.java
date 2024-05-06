package org.dizitart.no2.rocksdb;

import org.dizitart.no2.rocksdb.serializers.ObjectSerializer;
import org.rocksdb.ColumnFamilyHandle;
import org.rocksdb.RocksDB;
import org.rocksdb.RocksIterator;

import java.lang.ref.Cleaner;
import java.util.Iterator;

import static org.dizitart.no2.rocksdb.Constants.CLEANER;

/**
 * @since 4.0
 * @author Anindya Chatterjee
 */
class KeySet<K> implements Iterable<K> {
    private final ObjectSerializer objectSerializer;
    private final RocksDB rocksDB;
    private final ColumnFamilyHandle columnFamilyHandle;
    private final Class<?> keyType;

    public KeySet(RocksDB rocksDB, ColumnFamilyHandle columnFamilyHandle, ObjectSerializer objectSerializer, Class<?> keyType) {
        this.rocksDB = rocksDB;
        this.columnFamilyHandle = columnFamilyHandle;
        this.objectSerializer = objectSerializer;
        this.keyType = keyType;
    }

    @Override
    public Iterator<K> iterator() {
        return new KeyIterator();
    }

    private class KeyIterator implements Iterator<K>, AutoCloseable {
        private final RocksIterator rawEntryIterator;
        private final Cleaner.Cleanable cleanable;

        public KeyIterator() {
            rawEntryIterator = rocksDB.newIterator(columnFamilyHandle);
            rawEntryIterator.seekToFirst();
            cleanable = CLEANER.register(this, new CleaningAction(rawEntryIterator));
        }

        @Override
        public boolean hasNext() {
            try {
                boolean result = rawEntryIterator.isValid();
                if (!result) {
                    rawEntryIterator.close();
                }
                return result;
            } catch (AssertionError e) {
                return false;
            }
        }

        @Override
        @SuppressWarnings("unchecked")
        public K next() {
            K key = (K) objectSerializer.decodeKey(rawEntryIterator.key(), keyType);
            rawEntryIterator.next();
            return key;
        }

        @Override
        public void close() {
            cleanable.clean();
        }
    }
}
