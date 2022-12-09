package com.github.basdxz.vbuffers.layout;

import java.util.List;
import java.util.Map;

public interface Stride {
    List<Attribute> attributeList();

    Map<String, Attribute> attributeMap();

    int sizeBytes();
}
