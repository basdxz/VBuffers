package com.github.basdxz.vbuffers.helper;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class BytesConverterTest {
    // Test converting byte to bytes
    @Test
    public void testByteToBytes() {
        byte[] bytes = BytesConverter.byteToBytes((byte) 0x12);
        assertArrayEquals(new byte[]{0x12}, bytes);
    }

    // Test converting bytes to byte
    @Test
    public void testBytesToByte() {
        byte b = BytesConverter.bytesToByte(new byte[]{0x12});
        assertEquals(0x12, b);
    }

    // Test converting boolean to bytes
    @Test
    public void testBooleanToBytes() {
        byte[] bytes = BytesConverter.booleanToBytes(true);
        assertArrayEquals(new byte[]{1}, bytes);
    }

    // Test converting bytes to boolean
    @Test
    public void testBytesToBoolean() {
        boolean b = BytesConverter.bytesToBoolean(new byte[]{1});
        assertTrue(b);
    }

    // Test converting char to bytes
    @Test
    public void testCharToBytes() {
        byte[] bytes = BytesConverter.charToBytes('a');
        assertArrayEquals(new byte[]{0, 'a'}, bytes);
    }

    // Test converting bytes to char
    @Test
    public void testBytesToChar() {
        char c = BytesConverter.bytesToChar(new byte[]{0, 'a'});
        assertEquals('a', c);
    }

    // Test converting short to bytes
    @Test
    public void testShortToBytes() {
        byte[] bytes = BytesConverter.shortToBytes((short) 0x1234);
        assertArrayEquals(new byte[]{0x12, 0x34}, bytes);
    }

    // Test converting bytes to short
    @Test
    public void testBytesToShort() {
        short s = BytesConverter.bytesToShort(new byte[]{0x12, 0x34});
        assertEquals(0x1234, s);
    }

    // Test converting int to bytes
    @Test
    public void testIntToBytes() {
        byte[] bytes = BytesConverter.intToBytes(0x12345678);
        assertArrayEquals(new byte[]{0x12, 0x34, 0x56, 0x78}, bytes);
    }

    // Test converting bytes to int
    @Test
    public void testBytesToInt() {
        int i = BytesConverter.bytesToInt(new byte[]{0x12, 0x34, 0x56, 0x78});
        assertEquals(0x12345678, i);
    }

    // Test converting long to bytes
    @Test
    public void testLongToBytes() {
        byte[] bytes = BytesConverter.longToBytes(0x1234567890ABCDEFL);
        assertArrayEquals(new byte[]{0x12, 0x34, 0x56, 0x78, (byte) 0x90, (byte) 0xAB, (byte) 0xCD, (byte) 0xEF}, bytes);
    }

    // Test converting bytes to long
    @Test
    public void testBytesToLong() {
        long l = BytesConverter.bytesToLong(new byte[]{0x12, 0x34, 0x56, 0x78, (byte) 0x90, (byte) 0xAB, (byte) 0xCD, (byte) 0xEF});
        assertEquals(0x1234567890ABCDEFL, l);
    }

    // Test converting float to bytes
    @Test
    public void testFloatToBytes() {
        byte[] bytes = BytesConverter.floatToBytes(12345678);
        assertArrayEquals(new byte[]{0x4b, 0x3c, 0x61, 0x4e}, bytes);
    }

    // Test converting bytes to float
    @Test
    public void testBytesToFloat() {
        float f = BytesConverter.bytesToFloat(new byte[]{0x4b, 0x3c, 0x61, 0x4e});
        assertEquals(12345678, f);
    }

    // Test converting double to bytes
    @Test
    public void testDoubleToBytes() {
        byte[] bytes = BytesConverter.doubleToBytes(1234567891234567L);
        assertArrayEquals(new byte[]{0x43, 0x11, (byte) 0x8B, 0x54, (byte) 0xF2, 0x6E, (byte) 0xBC, 0x1C}, bytes);
    }

    // Test converting bytes to double
    @Test
    public void testBytesToDouble() {
        double d = BytesConverter.bytesToDouble(new byte[]{0x43, 0x11, (byte) 0x8B, 0x54, (byte) 0xF2, 0x6E, (byte) 0xBC, 0x1C});
        assertEquals(1234567891234567L, d);
    }
}