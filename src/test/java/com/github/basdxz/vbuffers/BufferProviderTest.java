package com.github.basdxz.vbuffers;

import lombok.*;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.junit.jupiter.api.Test;

import java.nio.ByteBuffer;

import static org.junit.jupiter.api.Assertions.assertEquals;

@NoArgsConstructor
public final class BufferProviderTest {
    private static final int[] SAMPLE_VALUES = new int[]{12, 100, 340, 4300};

    @Test
    public void test0() {
        val backing = ByteBuffer.allocate(4 * 4);
        val buffer = BufferProvider.newBuffer(LayoutA.class, backing);
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
        val backing = ByteBuffer.allocate(4 * 4);
        val buffer = BufferProvider.newBuffer(LayoutA.class, backing);

        val intBacking = backing.asIntBuffer();
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
        val backing = ByteBuffer.allocateDirect((3 + 3 + 4 + 2) * 4);
        val buffer = BufferProvider.newBuffer(LayoutB.class, backing);

        val position = new Vector3f();
        val normal = new Vector3f();
        val color = new Vector4f();
        val texture = new Vector2f();

        buffer.position(position);

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