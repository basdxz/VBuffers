package com.github.basdxz.vbuffers;

import lombok.*;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.junit.jupiter.api.Test;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

// New buffer of size bigger than 0
// Iterator for read/writing
@NoArgsConstructor
public final class BufferProviderTest {
    private static final int[] SAMPLE_VALUES = new int[]{12, 100, 340, 4300};
    private static final int BUFFER_SIZE = 1000;

    @Test
    public void test0() {
        val buffer = BufferProvider.newBuffer(LayoutA.class, ByteBuffer::allocate);
        for (val value : SAMPLE_VALUES) {
            val position = value + 55;
            val normal = value - 73;
            val color = value + 81;
            val texture = value + 120;

            //TODO: dumb issue, rename once overlap is solved
            buffer.positionFixOverlap(position)
                  .normal(normal)
                  .color(color)
                  .texture(texture);

            assertEquals(position, buffer.positionFixOverlap());
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

            //TODO: dumb issue, rename once overlap is solved
            buffer.positionFixOverlap(position)
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

        buffer.positionFixOverlap(position)
              .normal(normal)
              .color(color)
              .texture(texture);

        assertEquals(position, buffer.positionFixOverlap());
        assertEquals(normal, buffer.normal());
        assertEquals(color, buffer.color());
        assertEquals(texture, buffer.texture());
    }

    // Buffer treated as an array for reads and writes
    // https://www.baeldung.com/java-bytebuffer
    @Test
    public void test3() {
        val buffer = BufferProvider.newBuffer(LayoutB.class, ByteBuffer::allocateDirect, BUFFER_SIZE);
        val positions = IntStream.range(0, BUFFER_SIZE)
                                 .mapToObj(Vector3f::new)
                                 .toList();
        val normals = IntStream.range(0, BUFFER_SIZE)
                               .mapToObj(Vector3f::new)
                               .toList();
        val colors = IntStream.range(0, BUFFER_SIZE)
                              .mapToObj(Vector4f::new)
                              .toList();
        val textures = IntStream.range(0, BUFFER_SIZE)
                                .mapToObj(Vector2f::new)
                                .toList();

        while (buffer.hasRemaining()) {
            val index = buffer.position();
            buffer.positionFixOverlap(positions.get(index))
                  .normal(normals.get(index))
                  .color(colors.get(index))
                  .texture(textures.get(index));
            buffer.position(index + 1);
        }
        buffer.clear();

        while (buffer.hasRemaining()) {
            val index = buffer.position();
            assertEquals(positions.get(index), buffer.positionFixOverlap());
            assertEquals(normals.get(index), buffer.normal());
            assertEquals(colors.get(index), buffer.color());
            assertEquals(textures.get(index), buffer.texture());
            buffer.position(index + 1);
        }
    }

    // Flip/Compact demonstration
    // https://www.happycoders.eu/java/bytebuffer-flip-compact
    @Test
    public void test4() {
        val buffer = BufferProvider.newBuffer(LayoutB.class, ByteBuffer::allocateDirect, 10);
    }

    // Duplicate
    @Test
    public void test5() {
        val buffer = BufferProvider.newBuffer(LayoutB.class, ByteBuffer::allocateDirect, 10);
    }

    // Slice
    @Test
    public void test6() {
        val buffer = BufferProvider.newBuffer(LayoutB.class, ByteBuffer::allocateDirect, 10);
    }

    // Read-Only
    @Test
    public void test7() {
        val buffer = BufferProvider.newBuffer(LayoutB.class, ByteBuffer::allocateDirect, 10);
    }

    // Iterate
    @Test
    public void test8() {
        val buffer = BufferProvider.newBuffer(LayoutB.class, ByteBuffer::allocateDirect, 10);
    }

    // Stream
    @Test
    public void test9() {
        val buffer = BufferProvider.newBuffer(LayoutB.class, ByteBuffer::allocateDirect, 10);
    }

    // Buffer to Buffer transfers
    @Test
    public void test10() {
        val buffer = BufferProvider.newBuffer(LayoutB.class, ByteBuffer::allocateDirect, 10);
    }
}