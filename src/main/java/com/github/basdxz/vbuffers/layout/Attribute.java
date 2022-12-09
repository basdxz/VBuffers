package com.github.basdxz.vbuffers.layout;

public interface Attribute {
    String name();

    Class<?> type();

    int sizeBytes();

    int offsetBytes();
}
