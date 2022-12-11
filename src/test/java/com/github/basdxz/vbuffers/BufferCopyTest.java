package com.github.basdxz.vbuffers;

import com.github.basdxz.vbuffers.instance.BufferInstance;
import com.github.basdxz.vbuffers.sample.ZYXBuffer;
import lombok.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.nio.ByteBuffer;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BufferCopyTest {
    @Test
    @DisplayName("Buffer to Buffer")
    void bufferToBuffer() {
        val bufferA = BufferInstance.newBuffer(ZYXBuffer.class, ByteBuffer::allocateDirect, 1000);
        val bufferB = BufferInstance.newBuffer(ZYXBuffer.class, ByteBuffer::allocateDirect, 1000);

        // Fill buffer A
        for (var i = 0; i < 1000; i++) {
            bufferA.x(i + 1)
                   .y(i + 2)
                   .z(i + 3);
            bufferA.v$next();
        }
        bufferA.v$flip();

        // Copy buffer A to buffer B
        bufferB.v$put(bufferA);
        bufferA.v$flip();
        bufferB.v$flip();

        // Read buffer B
        for (var i = 0; i < 1000; i++) {
            assertEquals(i + 1, bufferB.x());
            assertEquals(i + 2, bufferB.y());
            assertEquals(i + 3, bufferB.z());
            bufferB.v$next();
        }
        bufferB.v$flip();
    }
}
