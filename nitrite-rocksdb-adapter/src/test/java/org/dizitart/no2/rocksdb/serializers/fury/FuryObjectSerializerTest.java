package org.dizitart.no2.rocksdb.serializers.fury;

import org.dizitart.no2.rocksdb.serializers.kyro.KryoObjectSerializer;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class FuryObjectSerializerTest {

    @Test
    public void testEncode() {
        assertEquals(10, (new FuryObjectSerializer()).<Object>encode("object").length);
        assertEquals(1, (new FuryObjectSerializer()).encode(null).length);
    }

    @Test
    public void testEncodeKey() {
        assertEquals(1, (new FuryObjectSerializer()).encodeKey(null).length);
        assertEquals(10, (new FuryObjectSerializer()).<Object>encodeKey("object").length);
    }
}

