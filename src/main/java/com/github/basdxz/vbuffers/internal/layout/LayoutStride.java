package com.github.basdxz.vbuffers.internal.layout;

import com.github.basdxz.vbuffers.layout.Attribute;
import com.github.basdxz.vbuffers.layout.Layout;
import com.github.basdxz.vbuffers.layout.Stride;
import lombok.*;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
public class LayoutStride implements Stride {
    protected final int sizeBytes;
    protected final Map<String, Attribute> attributes;
    protected final List<Method> methods;

    public LayoutStride(Class<? extends Layout<?>> layout) {
        val attributeAnnotations = layout.getAnnotation(Layout.Stride.class).value();
        if (attributeAnnotations.length == 0)
            throw new IllegalArgumentException("Layout must have at least one attribute");

        // Becomes the total size at the end of the loop
        var offsetBytes = 0;
        val attributes = new HashMap<String, Attribute>();
        for (val attribute : attributeAnnotations) {
            val bufferAttribute = new LayoutAttribute(attribute, offsetBytes);
            attributes.put(attribute.name(), bufferAttribute);
            offsetBytes += bufferAttribute.sizeBytes();
        }

        this.sizeBytes = offsetBytes;
        // Made unmodifiable as both are exposed via getters
        this.attributes = Collections.unmodifiableMap(attributes);
        this.methods = List.of(layout.getDeclaredMethods());

    }
}
