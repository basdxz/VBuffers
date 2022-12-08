package com.github.basdxz.vbuffers.helper;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BytesConverterTest {
    // Test converting byte to bytes
    @Test
    public void testByteToBytes() {
        byte[] bytes = new byte[1];
        BytesConverter.byteToBytes(bytes, 0, (byte) 0x12);
        assertEquals(0x12, bytes[0]);
    }

    // Test converting bytes to byte
    @Test
    public void testBytesToByte() {
        byte[] bytes = new byte[]{0x12};
        assertEquals(0x12, BytesConverter.bytesToByte(bytes, 0));
    }

    // Test converting boolean to bytes
    @Test
    public void testBooleanToBytes() {
        byte[] bytes = new byte[1];
        BytesConverter.booleanToBytes(bytes, 0, true);
        assertEquals(1, bytes[0]);
    }

    // Test converting bytes to boolean
    @Test
    public void testBytesToBoolean() {
        byte[] bytes = new byte[]{1};
        assertTrue(BytesConverter.bytesToBoolean(bytes, 0));
    }

    // Test converting char to bytes
    @Test
    public void testCharToBytes() {
        byte[] bytes = new byte[2];
        BytesConverter.charToBytes(bytes, 0, (char) 0x1234);
        assertEquals(0x12, bytes[0]);
        assertEquals(0x34, bytes[1]);
    }

    // Test converting bytes to char
    @Test
    public void testBytesToChar() {
        byte[] bytes = new byte[]{0x12, 0x34};
        assertEquals(0x1234, BytesConverter.bytesToChar(bytes, 0));
    }

    // Test converting short to bytes
    @Test
    public void testShortToBytes() {
        byte[] bytes = new byte[2];
        BytesConverter.shortToBytes(bytes, 0, (short) 0x1234);
        assertEquals(0x12, bytes[0]);
        assertEquals(0x34, bytes[1]);
    }

    // Test converting bytes to short
    @Test
    public void testBytesToShort() {
        byte[] bytes = new byte[]{0x12, 0x34};
        assertEquals(0x1234, BytesConverter.bytesToShort(bytes, 0));
    }

    // Test converting int to bytes
    @Test
    public void testIntToBytes() {
        byte[] bytes = new byte[4];
        BytesConverter.intToBytes(bytes, 0, 0x12345678);
        assertEquals(0x12, bytes[0]);
        assertEquals(0x34, bytes[1]);
        assertEquals(0x56, bytes[2]);
        assertEquals(0x78, bytes[3]);
    }

    // Test converting bytes to int
    @Test
    public void testBytesToInt() {
        byte[] bytes = new byte[]{0x12, 0x34, 0x56, 0x78};
        assertEquals(0x12345678, BytesConverter.bytesToInt(bytes, 0));
    }

    // Test converting long to bytes
    @Test
    public void testLongToBytes() {
        byte[] bytes = new byte[8];
        BytesConverter.longToBytes(bytes, 0, 0x1234567890ABCDEFL);
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
        assertEquals(0x1234567890abcdefL, BytesConverter.bytesToLong(bytes, 0));
    }

    // Test converting float to bytes
    @Test
    public void testFloatToBytes() {
        byte[] bytes = new byte[4];
        BytesConverter.floatToBytes(bytes, 0, 12345678F);
        assertEquals(0x4b, bytes[0]);
        assertEquals(0x3c, bytes[1]);
        assertEquals(0x61, bytes[2]);
        assertEquals(0x4e, bytes[3]);
    }

    // Test converting bytes to float
    @Test
    public void testBytesToFloat() {
        byte[] bytes = new byte[]{0x4b, 0x3c, 0x61, 0x4e};
        assertEquals(12345678F, BytesConverter.bytesToFloat(bytes, 0));
    }

    // Test converting double to bytes
    @Test
    public void testDoubleToBytes() {
        byte[] bytes = new byte[8];
        BytesConverter.doubleToBytes(bytes, 0, 1234567891234567L);
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
        assertEquals(1234567891234567L, BytesConverter.bytesToDouble(bytes, 0));
    }
}