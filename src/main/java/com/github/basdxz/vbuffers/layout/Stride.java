package com.github.basdxz.vbuffers.layout;

import lombok.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Getter
public class Stride {
    protected final int sizeBytes;
    protected final Map<String, Attribute> attributes;

    public Stride(Layout.Stride annotation) {
        val attributeAnnotations = annotation.value();
        if (attributeAnnotations.length == 0)
            throw new IllegalArgumentException("Layout must have at least one attribute");

        // Becomes the total size at the end of the loop
        var offsetBytes = 0;
        val attributes = new HashMap<String, Attribute>();
        for (val attribute : attributeAnnotations) {
            val bufferAttribute = new Attribute(attribute, offsetBytes);
            attributes.put(attribute.name(), bufferAttribute);
            offsetBytes += bufferAttribute.sizeBytes();
        }

        this.sizeBytes = offsetBytes;
        // Made unmodifiable as it is exposed via getter
        this.attributes = Collections.unmodifiableMap(attributes);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this)
            return true;
        if (!(obj instanceof Stride other))
            return false;
        return sizeBytes == other.sizeBytes && attributes.equals(other.attributes);
    }
}
