package com.github.basdxz.vbuffers;

import lombok.*;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.junit.jupiter.api.Test;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import static org.junit.jupiter.api.Assertions.assertEquals;

// New buffer of size bigger than 0
// Iterator for read/writing
@NoArgsConstructor
public final class BufferProviderTest {
    private static final int[] SAMPLE_VALUES = new int[]{12, 100, 340, 4300};

    @Test
    public void test0() {
        val buffer = BufferProvider.newBuffer(LayoutA.class, ByteBuffer::allocate);
        for (val value : SAMPLE_VALUES) {
            val position = value + 55;
            val normal = value - 73;
            val color = value + 81;
            val texture = value + 120;

            buffer.position(position)
                  .normal(normal)
                  .color(color)
                  .texture(texture);

            assertEquals(position, buffer.position());
            assertEquals(normal, buffer.normal());
            assertEquals(color, buffer.color());
            assertEquals(texture, buffer.texture());
        }
    }

    @Test
    public void test1() {
        val intBackingBox = new IntBuffer[1];
        val buffer = BufferProvider.newBuffer(LayoutA.class, capacity -> {
            val backing = ByteBuffer.allocate(capacity);
            intBackingBox[0] = backing.asIntBuffer();
            return backing;
        });
        val intBacking = intBackingBox[0];

        for (val value : SAMPLE_VALUES) {
            val position = value + 55;
            val normal = value - 73;
            val color = value + 81;
            val texture = value + 120;

            buffer.position(position)
                  .normal(normal)
                  .color(color)
                  .texture(texture);

            assertEquals(position, intBacking.get(0));
            assertEquals(normal, intBacking.get(1));
            assertEquals(color, intBacking.get(2));
            assertEquals(texture, intBacking.get(3));
        }
    }

    @Test
    public void test2() {
        //Only worked with allocate direct
        val buffer = BufferProvider.newBuffer(LayoutB.class, ByteBuffer::allocateDirect);

        val position = new Vector3f(55F, 994F, -1515F);
        val normal = new Vector3f(35F, 300F, -105F);
        val color = new Vector4f(7777F, 0F, -1F, 1000F);
        val texture = new Vector2f(-642F, 0.66F);

        buffer.position(position)
              .normal(normal)
              .color(color)
              .texture(texture);

        assertEquals(position, buffer.position());
        assertEquals(normal, buffer.normal());
        assertEquals(color, buffer.color());
        assertEquals(texture, buffer.texture());
    }
}