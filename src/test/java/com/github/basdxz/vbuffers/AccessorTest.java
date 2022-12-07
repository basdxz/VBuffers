package com.github.basdxz.vbuffers;

import com.github.basdxz.vbuffers.accessor.AccessorProvider;
import lombok.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.nio.ByteBuffer;

public class AccessorTest {
    @Test
    public void test() {
        val buffer = ByteBuffer.allocate(Integer.BYTES);

        val setter = AccessorProvider.setter(Integer.class);
        val getter = AccessorProvider.getter(Integer.class);

        setter.put(buffer, 0, 5);
        Assertions.assertEquals(5, getter.get(buffer, 0, null));
    }
}
