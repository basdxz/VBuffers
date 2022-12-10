package com.github.basdxz.vbuffers.old;

import com.github.basdxz.vbuffers.helper.ByteHelper;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ByteHelperTest {
    // Test converting byte to bytes
    @Test
    public void testByteToBytes() {
        byte[] bytes = new byte[1];
        ByteHelper.byteToBytes(bytes, 0, (byte) 0x12);
        assertEquals(0x12, bytes[0]);
    }

    // Test converting bytes to byte
    @Test
    public void testBytesToByte() {
        byte[] bytes = new byte[]{0x12};
        assertEquals(0x12, ByteHelper.bytesToByte(bytes, 0));
    }

    // Test converting boolean to bytes
    @Test
    public void testBooleanToBytes() {
        byte[] bytes = new byte[1];
        ByteHelper.booleanToBytes(bytes, 0, true);
        assertEquals(1, bytes[0]);
    }

    // Test converting bytes to boolean
    @Test
    public void testBytesToBoolean() {
        byte[] bytes = new byte[]{1};
        assertTrue(ByteHelper.bytesToBoolean(bytes, 0));
    }

    // Test converting char to bytes
    @Test
    public void testCharToBytes() {
        byte[] bytes = new byte[2];
        ByteHelper.charToBytes(bytes, 0, (char) 0x1234);
        assertEquals(0x12, bytes[0]);
        assertEquals(0x34, bytes[1]);
    }

    // Test converting bytes to char
    @Test
    public void testBytesToChar() {
        byte[] bytes = new byte[]{0x12, 0x34};
        assertEquals(0x1234, ByteHelper.bytesToChar(bytes, 0));
    }

    // Test converting short to bytes
    @Test
    public void testShortToBytes() {
        byte[] bytes = new byte[2];
        ByteHelper.shortToBytes(bytes, 0, (short) 0x1234);
        assertEquals(0x12, bytes[0]);
        assertEquals(0x34, bytes[1]);
    }

    // Test converting bytes to short
    @Test
    public void testBytesToShort() {
        byte[] bytes = new byte[]{0x12, 0x34};
        assertEquals(0x1234, ByteHelper.bytesToShort(bytes, 0));
    }

    // Test converting int to bytes
    @Test
    public void testIntToBytes() {
        byte[] bytes = new byte[4];
        ByteHelper.intToBytes(bytes, 0, 0x12345678);
        assertEquals(0x12, bytes[0]);
        assertEquals(0x34, bytes[1]);
        assertEquals(0x56, bytes[2]);
        assertEquals(0x78, bytes[3]);
    }

    // Test converting bytes to int
    @Test
    public void testBytesToInt() {
        byte[] bytes = new byte[]{0x12, 0x34, 0x56, 0x78};
        assertEquals(0x12345678, ByteHelper.bytesToInt(bytes, 0));
    }

    // Test converting long to bytes
    @Test
    public void testLongToBytes() {
        byte[] bytes = new byte[8];
        ByteHelper.longToBytes(bytes, 0, 0x1234567890ABCDEFL);
        assertEquals(0x12, bytes[0]);
        assertEquals(0x34, bytes[1]);
        assertEquals(0x56, bytes[2]);
        assertEquals(0x78, bytes[3]);
        assertEquals((byte) 0x90, bytes[4]);
        assertEquals((byte) 0xAB, bytes[5]);
        assertEquals((byte) 0xCD, bytes[6]);
        assertEquals((byte) 0xEF, bytes[7]);
    }

    // Test converting bytes to long
    @Test
    public void testBytesToLong() {
        byte[] bytes = new byte[]{0x12, 0x34, 0x56, 0x78, (byte) 0x90, (byte) 0xab, (byte) 0xcd, (byte) 0xef};
        assertEquals(0x1234567890abcdefL, ByteHelper.bytesToLong(bytes, 0));
    }

    // Test converting float to bytes
    @Test
    public void testFloatToBytes() {
        byte[] bytes = new byte[4];
        ByteHelper.floatToBytes(bytes, 0, 12345678F);
        assertEquals(0x4b, bytes[0]);
        assertEquals(0x3c, bytes[1]);
        assertEquals(0x61, bytes[2]);
        assertEquals(0x4e, bytes[3]);
    }

    // Test converting bytes to float
    @Test
    public void testBytesToFloat() {
        byte[] bytes = new byte[]{0x4b, 0x3c, 0x61, 0x4e};
        assertEquals(12345678F, ByteHelper.bytesToFloat(bytes, 0));
    }

    // Test converting double to bytes
    @Test
    public void testDoubleToBytes() {
        byte[] bytes = new byte[8];
        ByteHelper.doubleToBytes(bytes, 0, 1234567891234567L);
        assertEquals(0x43, bytes[0]);
        assertEquals(0x11, bytes[1]);
        assertEquals((byte) 0x8B, bytes[2]);
        assertEquals(0x54, bytes[3]);
        assertEquals((byte) 0xF2, bytes[4]);
        assertEquals(0x6E, bytes[5]);
        assertEquals((byte) 0xBC, bytes[6]);
        assertEquals(0x1C, bytes[7]);
    }

    // Test converting bytes to double
    @Test
    public void testBytesToDouble() {
        byte[] bytes = new byte[]{0x43, 0x11, (byte) 0x8B, 0x54, (byte) 0xF2, 0x6E, (byte) 0xBC, 0x1C};
        assertEquals(1234567891234567L, ByteHelper.bytesToDouble(bytes, 0));
    }

    @Test
    public void boxedSizeBytes() {
        assertEquals(Byte.BYTES, ByteHelper.sizeBytes(Byte.class));
        assertEquals(Byte.BYTES, ByteHelper.sizeBytes(Boolean.class));
        assertEquals(Short.BYTES, ByteHelper.sizeBytes(Short.class));
        assertEquals(Character.BYTES, ByteHelper.sizeBytes(Character.class));
        assertEquals(Integer.BYTES, ByteHelper.sizeBytes(Integer.class));
        assertEquals(Long.BYTES, ByteHelper.sizeBytes(Long.class));
        assertEquals(Float.BYTES, ByteHelper.sizeBytes(Float.class));
        assertEquals(Double.BYTES, ByteHelper.sizeBytes(Double.class));
    }

    @Test
    public void primitiveSizeBytes() {
        assertEquals(Byte.BYTES, ByteHelper.sizeBytes(byte.class));
        assertEquals(Byte.BYTES, ByteHelper.sizeBytes(boolean.class));
        assertEquals(Short.BYTES, ByteHelper.sizeBytes(short.class));
        assertEquals(Character.BYTES, ByteHelper.sizeBytes(char.class));
        assertEquals(Integer.BYTES, ByteHelper.sizeBytes(int.class));
        assertEquals(Long.BYTES, ByteHelper.sizeBytes(long.class));
        assertEquals(Float.BYTES, ByteHelper.sizeBytes(float.class));
        assertEquals(Double.BYTES, ByteHelper.sizeBytes(double.class));
    }
}