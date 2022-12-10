package com.github.basdxz.vbuffers.layout.impl;

import com.github.basdxz.vbuffers.layout.Attribute;
import com.github.basdxz.vbuffers.layout.Layout;
import com.github.basdxz.vbuffers.layout.Stride;
import lombok.*;

import java.util.*;

@Getter
public class LayoutStride implements Stride {
    protected final List<Attribute> attributeList;
    protected final Map<String, Attribute> attributeMap;
    protected final int sizeBytes;

    public LayoutStride(Layout.Stride annotation) {
        val attributes = annotation.value();
        if (attributes.length == 0)
            throw new IllegalArgumentException("Layout must have at least one attribute");

        // Becomes the total size at the end of the loop
        var offsetBytes = 0;
        val attributeList = new ArrayList<Attribute>(attributes.length);
        val attributeMap = new HashMap<String, Attribute>(attributes.length);
        for (val attribute : attributes) {
            val bufferAttribute = new LayoutAttribute(attribute, offsetBytes);
            attributeList.add(bufferAttribute);
            attributeMap.put(attribute.name(), bufferAttribute);
            offsetBytes += bufferAttribute.sizeBytes();
        }

        // Made unmodifiable as both are exposed via getters
        this.attributeList = Collections.unmodifiableList(attributeList);
        this.attributeMap = Collections.unmodifiableMap(attributeMap);
        this.sizeBytes = offsetBytes;
    }
}
