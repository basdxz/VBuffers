package com.github.basdxz.vbuffers.attribute.impl;

import com.github.basdxz.vbuffers.VBuffer;
import com.github.basdxz.vbuffers.attribute.Attribute;
import com.github.basdxz.vbuffers.attribute.Layout;
import com.github.basdxz.vbuffers.helper.SizeBytes;
import lombok.*;

import java.util.HashMap;
import java.util.Map;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class AttributeProvider {
    public static Map<String, Attribute> fromLayout(Class<? extends VBuffer<?>> layout) {
        val layoutAnnotation = layout.getAnnotation(Layout.class);
        if (layoutAnnotation == null)
            throw new IllegalArgumentException("Layout annotation not found on " + layout.getName());
        val attributeAnnotations = layoutAnnotation.value();
        if (attributeAnnotations == null || attributeAnnotations.length == 0)
            throw new IllegalArgumentException("Layout annotation on " + layout.getName() + " has no attributes");
        var offset = 0;
        val attributes = new HashMap<String, Attribute>(attributeAnnotations.length);
        for (val attributeAnnotation : attributeAnnotations) {
            attributes.put(attributeAnnotation.name(), new LayoutAttribute(attributeAnnotation, offset));
            offset += SizeBytes.of(attributeAnnotation.type());
        }
        return attributes;
    }
}
