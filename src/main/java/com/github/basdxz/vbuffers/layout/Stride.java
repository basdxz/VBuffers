package com.github.basdxz.vbuffers.layout;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

public interface Stride {
    int sizeBytes();

    Map<String, Attribute> attributes();

    List<Method> methods();
}
