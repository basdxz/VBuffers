package com.github.basdxz.vbuffers;

import lombok.*;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.junit.jupiter.api.Test;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.ReadOnlyBufferException;
import java.util.List;
import java.util.function.IntFunction;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

// Iterator for read/writing
@NoArgsConstructor
public final class BufferProviderTest {
    private static final int[] SAMPLE_VALUES = new int[]{12, 100, 340, 4300};

    private static final int BUFFER_SIZE = 1000;

    private static final int CONSTANT_1 = 1;
    private static final int CONSTANT_2 = 2;
    private static final int CONSTANT_3 = 3;//make these more interesting numbers

    private static final int SIZE_A = 10;
    private static final int SIZE_B = 20;
    private static final int SIZE_C = 30;

    @Test
    public void test0() {
        val buffer = VBufferHandler.newBuffer(LayoutA.class, ByteBuffer::allocate);
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
        val buffer = VBufferHandler.newBuffer(LayoutA.class, capacity -> {
            val allocation = ByteBuffer.allocate(capacity);
            intBackingBox[0] = allocation.asIntBuffer();
            return allocation;
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
        val buffer = VBufferHandler.newBuffer(LayoutB.class, ByteBuffer::allocateDirect);

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

    // Buffer treated as an array for reads and writes
    // https://www.baeldung.com/java-bytebuffer
    @Test
    public void test3() {
        val buffer = VBufferHandler.newBuffer(LayoutB.class, ByteBuffer::allocateDirect, BUFFER_SIZE);
        val positions = newRangeList(BUFFER_SIZE, Vector3f::new);
        val normals = newRangeList(BUFFER_SIZE, Vector3f::new);
        val colors = IntStream.range(0, BUFFER_SIZE)
                              .mapToObj(Vector4f::new)
                              .toList();
        val textures = IntStream.range(0, BUFFER_SIZE)
                                .mapToObj(Vector2f::new)
                                .toList();

        while (buffer.v$hasRemaining()) {
            val index = buffer.v$position();
            buffer.position(positions.get(index))
                  .normal(normals.get(index))
                  .color(colors.get(index))
                  .texture(textures.get(index));
            buffer.v$oldNext();
        }
        buffer.v$clear();

        while (buffer.v$hasRemaining()) {
            val index = buffer.v$position();
            assertEquals(positions.get(index), buffer.position());
            assertEquals(normals.get(index), buffer.normal());
            assertEquals(colors.get(index), buffer.color());
            assertEquals(textures.get(index), buffer.texture());
            buffer.v$position(index + 1);
        }
    }

    // Flip/Compact demonstration
    // https://www.happycoders.eu/java/bytebuffer-flip-compact
    @Test
    public void test4() {
        val middleOfB = SIZE_B / 2;

        // Create the test buffer
        val buffer = VBufferHandler.newBuffer(LayoutA.class, ByteBuffer::allocateDirect, BUFFER_SIZE);

        // Write CONSTANT_1 to the buffer SIZE_A times
        for (var i = 0; i < SIZE_A; i++) {
            buffer.position(CONSTANT_1)
                  .normal(CONSTANT_1)
                  .color(CONSTANT_1)
                  .texture(CONSTANT_1);
            buffer.v$oldNext();
        }
        // Write CONSTANT_2 to the buffer SIZE_B times
        for (var i = 0; i < SIZE_B; i++) {
            buffer.position(CONSTANT_2)
                  .normal(CONSTANT_2)
                  .color(CONSTANT_2)
                  .texture(CONSTANT_2);
            buffer.v$oldNext();
        }
        // Flip the buffer
        buffer.v$flip();

        // Read the CONSTANT_1 values from the buffer
        for (var i = 0; i < SIZE_A; i++) {
            assertEquals(CONSTANT_1, buffer.position());
            assertEquals(CONSTANT_1, buffer.normal());
            assertEquals(CONSTANT_1, buffer.color());
            assertEquals(CONSTANT_1, buffer.texture());
            buffer.v$oldNext();
        }
        // Read half of the CONSTANT_2 values from the buffer
        for (var i = 0; i < middleOfB; i++) {
            assertEquals(CONSTANT_2, buffer.position());
            assertEquals(CONSTANT_2, buffer.normal());
            assertEquals(CONSTANT_2, buffer.color());
            assertEquals(CONSTANT_2, buffer.texture());
            buffer.v$oldNext();
        }
        // Compact the buffer
        buffer.v$compact();

        // Write constant C to the buffer SIZE_C times
        for (var i = 0; i < SIZE_C; i++) {
            buffer.position(CONSTANT_3)
                  .normal(CONSTANT_3)
                  .color(CONSTANT_3)
                  .texture(CONSTANT_3);
            buffer.v$oldNext();
        }
        buffer.v$flip();

        // Read the rest of the CONSTANT_2 values from the buffer
        for (var i = 0; i < middleOfB; i++) {
            assertEquals(CONSTANT_2, buffer.position());
            assertEquals(CONSTANT_2, buffer.normal());
            assertEquals(CONSTANT_2, buffer.color());
            assertEquals(CONSTANT_2, buffer.texture());
            buffer.v$oldNext();
        }
        // Read the CONSTANT_3 values from the buffer
        for (var i = 0; i < SIZE_C; i++) {
            assertEquals(CONSTANT_3, buffer.position());
            assertEquals(CONSTANT_3, buffer.normal());
            assertEquals(CONSTANT_3, buffer.color());
            assertEquals(CONSTANT_3, buffer.texture());
            buffer.v$oldNext();
        }
    }

    @Test
    public void testCopy() {
        // Create the test buffer
        val buffer = VBufferHandler.newBuffer(LayoutB.class, ByteBuffer::allocateDirect, 10);

        //  Create the test data
        val position = new Vector3f(55F, 994F, -1515F);
        val normal = new Vector3f(35F, 300F, -105F);
        val color = new Vector4f(7777F, 0F, -1F, 1000F);
        val texture = new Vector2f(-642F, 0.66F);

        // Write the test data at position 3 in the buffer
        buffer.v$position(3);
        buffer.position(position)
              .normal(normal)
              .color(color)
              .texture(texture);

        // Copy the test data to position 9 in the buffer
        buffer.v$copyStride(3, 9);

        // Read the test data from position 9 in the buffer
        buffer.v$position(9);
        assertEquals(position, buffer.position());
        assertEquals(normal, buffer.normal());
        assertEquals(color, buffer.color());
        assertEquals(texture, buffer.texture());
    }

    @Test
    public void duplicate() {
        // Create the test buffer
        val buffer = VBufferHandler.newBuffer(LayoutA.class, ByteBuffer::allocateDirect, 10);

        // Fill buffer with numbers 0 to 9
        for (var i = 0; i < 10; i++) {
            buffer.position(i)
                  .normal(i)
                  .color(i)
                  .texture(i);
            buffer.v$oldNext();
        }

        // Duplicate the buffer
        val duplicateBuffer = buffer.v$duplicateView();
        // Flip the duplicate buffer
        duplicateBuffer.v$flip();

        // Read the duplicate buffer
        for (var i = 0; i < 10; i++) {
            assertEquals(i, duplicateBuffer.position());
            assertEquals(i, duplicateBuffer.normal());
            assertEquals(i, duplicateBuffer.color());
            assertEquals(i, duplicateBuffer.texture());
            duplicateBuffer.v$oldNext();
        }
    }

    @Test
    public void slice() {
        // Create the test buffer
        val buffer = VBufferHandler.newBuffer(LayoutA.class, ByteBuffer::allocateDirect, 10);

        // Fill buffer with numbers 0 to 9
        for (var i = 0; i < 10; i++) {
            buffer.position(i)
                  .normal(i)
                  .color(i)
                  .texture(i);
            buffer.v$oldNext();
        }

        // Slice the buffer from index 3 with a length of 7
        val sliceBuffer = buffer.v$sliceView(3, 7);

        // Read the slice buffer
        for (var i = 0; i < 7; i++) {
            assertEquals(i + 3, sliceBuffer.position());
            assertEquals(i + 3, sliceBuffer.normal());
            assertEquals(i + 3, sliceBuffer.color());
            assertEquals(i + 3, sliceBuffer.texture());
            sliceBuffer.v$oldNext();
        }
    }

    // Read-Only
    public void readOnly() {
        // Create the test buffer
        val buffer = VBufferHandler.newBuffer(LayoutA.class, ByteBuffer::allocateDirect, 10);

        // Fill buffer with numbers 0 to 9
        for (var i = 0; i < 10; i++) {
            buffer.position(i)
                  .normal(i)
                  .color(i)
                  .texture(i);
            buffer.v$oldNext();
        }

        // Flip the buffer
        buffer.v$flip();

        // Create a read-only buffer
        val readOnlyBuffer = buffer.v$asReadOnlyView();

        // Read the read-only buffer
        for (var i = 0; i < 10; i++) {
            assertEquals(i, readOnlyBuffer.position());
            assertEquals(i, readOnlyBuffer.normal());
            assertEquals(i, readOnlyBuffer.color());
            assertEquals(i, readOnlyBuffer.texture());
            readOnlyBuffer.v$oldNext();
        }

        // Try to write to the read-only buffer
        assertThrows(ReadOnlyBufferException.class, () -> {
            readOnlyBuffer.position(0)
                          .normal(0)
                          .color(0)
                          .texture(0);
        });
    }

    @Test
    public void iteration() {
        // Create the test buffer
        val buffer = VBufferHandler.newBuffer(LayoutA.class, ByteBuffer::allocateDirect, 10);

        // Fill buffer with numbers 0 to 9
        var value = 0;
        for (val stride : buffer) {
            stride.position(value)
                  .normal(value)
                  .color(value)
                  .texture(value);
            value++;
        }

        // Read the buffer
        value = 0;
        for (val stride : buffer) {
            assertEquals(value, stride.position());
            assertEquals(value, stride.normal());
            assertEquals(value, stride.color());
            assertEquals(value, stride.texture());
            value++;
        }
    }

    // Stream
    @Test
    public void stream() {
        val buffer = VBufferHandler.newBuffer(LayoutB.class, ByteBuffer::allocateDirect, 10);
    }

    // Buffer to Buffer transfers
    @Test
    public void bufferToBuffer() {
        val buffer = VBufferHandler.newBuffer(LayoutB.class, ByteBuffer::allocateDirect, 10);
    }

    @NotNull
    private static <T> List<T> newRangeList(int size, IntFunction<T> mapper) {
        return newRangeList(0, size, mapper);
    }

    @NotNull
    private static <T> List<T> newRangeList(int start, int size, IntFunction<T> mapper) {
        return IntStream.range(start, start + size)
                        .mapToObj(mapper)
                        .toList();
    }
}