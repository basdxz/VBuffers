package com.github.basdxz.vbuffers.helper;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Byte Helper")
public class ByteHelperTest {
    @Test
    @DisplayName("byte to bytes")
    public void byteToBytes() {
        byte[] bytes = new byte[1];
        ByteHelper.byteToBytes(bytes, 0, (byte) 0x12);
        assertEquals(0x12, bytes[0]);
    }

    @Test
    @DisplayName("bytes to byte")
    public void bytesToByte() {
        byte[] bytes = new byte[]{0x12};
        assertEquals(0x12, ByteHelper.bytesToByte(bytes, 0));
    }

    @Test
    @DisplayName("boolean to bytes")
    public void booleanToBytes() {
        byte[] bytes = new byte[1];
        ByteHelper.booleanToBytes(bytes, 0, true);
        assertEquals(1, bytes[0]);
    }

    @Test
    @DisplayName("bytes to boolean")
    public void bytesToBoolean() {
        byte[] bytes = new byte[]{1};
        assertTrue(ByteHelper.bytesToBoolean(bytes, 0));
    }

    @Test
    @DisplayName("char to bytes")
    public void charToBytes() {
        byte[] bytes = new byte[2];
        ByteHelper.charToBytes(bytes, 0, (char) 0x1234);
        assertEquals(0x12, bytes[0]);
        assertEquals(0x34, bytes[1]);
    }

    @Test
    @DisplayName("bytes to char")
    public void bytesToChar() {
        byte[] bytes = new byte[]{0x12, 0x34};
        assertEquals(0x1234, ByteHelper.bytesToChar(bytes, 0));
    }

    @Test
    @DisplayName("short to bytes")
    public void shortToBytes() {
        byte[] bytes = new byte[2];
        ByteHelper.shortToBytes(bytes, 0, (short) 0x1234);
        assertEquals(0x12, bytes[0]);
        assertEquals(0x34, bytes[1]);
    }

    @Test
    @DisplayName("bytes to short")
    public void bytesToShort() {
        byte[] bytes = new byte[]{0x12, 0x34};
        assertEquals(0x1234, ByteHelper.bytesToShort(bytes, 0));
    }

    @Test
    @DisplayName("int to bytes")
    public void intToBytes() {
        byte[] bytes = new byte[4];
        ByteHelper.intToBytes(bytes, 0, 0x12345678);
        assertEquals(0x12, bytes[0]);
        assertEquals(0x34, bytes[1]);
        assertEquals(0x56, bytes[2]);
        assertEquals(0x78, bytes[3]);
    }

    @Test
    @DisplayName("bytes to int")
    public void bytesToInt() {
        byte[] bytes = new byte[]{0x12, 0x34, 0x56, 0x78};
        assertEquals(0x12345678, ByteHelper.bytesToInt(bytes, 0));
    }

    @Test
    @DisplayName("long to bytes")
    public void longToBytes() {
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

    @Test
    @DisplayName("bytes to long")
    public void bytesToLong() {
        byte[] bytes = new byte[]{0x12, 0x34, 0x56, 0x78, (byte) 0x90, (byte) 0xab, (byte) 0xcd, (byte) 0xef};
        assertEquals(0x1234567890abcdefL, ByteHelper.bytesToLong(bytes, 0));
    }

    @Test
    @DisplayName("float to bytes")
    public void floatToBytes() {
        byte[] bytes = new byte[4];
        ByteHelper.floatToBytes(bytes, 0, 12345678F);
        assertEquals(0x4b, bytes[0]);
        assertEquals(0x3c, bytes[1]);
        assertEquals(0x61, bytes[2]);
        assertEquals(0x4e, bytes[3]);
    }

    @Test
    @DisplayName("bytes to float")
    public void bytesToFloat() {
        byte[] bytes = new byte[]{0x4b, 0x3c, 0x61, 0x4e};
        assertEquals(12345678F, ByteHelper.bytesToFloat(bytes, 0));
    }

    @Test
    @DisplayName("double to bytes")
    public void doubleToBytes() {
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

    @Test
    @DisplayName("bytes to double")
    public void bytesToDouble() {
        byte[] bytes = new byte[]{0x43, 0x11, (byte) 0x8B, 0x54, (byte) 0xF2, 0x6E, (byte) 0xBC, 0x1C};
        assertEquals(1234567891234567L, ByteHelper.bytesToDouble(bytes, 0));
    }

    @Test
    @DisplayName("Primitive Size Bytes")
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

    @Test
    @DisplayName("Boxed Size Bytes")
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
}