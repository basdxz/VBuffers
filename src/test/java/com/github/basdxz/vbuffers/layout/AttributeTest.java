package com.github.basdxz.vbuffers.layout;

import com.github.basdxz.vbuffers.sample.XYZBuffer;
import lombok.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Attribute")
class AttributeTest {

    @Test
    @DisplayName("XYZ Buffer")
    void xyzBuffer() {
        // Get the annotations from the sample interface
        val attributeAnnotations = XYZBuffer.class.getAnnotationsByType(Layout.Stride.class)[0].value();
        val xAttAnnotation = attributeAnnotations[0];
        val yAttAnnotation = attributeAnnotations[1];
        val zAttAnnotation = attributeAnnotations[2];

        // Create the attributes
        val xAttribute = new Attribute(xAttAnnotation, 0);
        val yAttribute = new Attribute(yAttAnnotation, 4);
        val zAttribute = new Attribute(zAttAnnotation, 8);

        // Validate the attributes
        Assertions.assertEquals(XYZBuffer.x, xAttribute.name());
        Assertions.assertEquals(Integer.class, xAttribute.type());
        Assertions.assertEquals(4, xAttribute.sizeBytes());
        Assertions.assertEquals(0, xAttribute.offsetBytes());

        Assertions.assertEquals(XYZBuffer.y, yAttribute.name());
        Assertions.assertEquals(Integer.class, yAttribute.type());
        Assertions.assertEquals(4, yAttribute.sizeBytes());
        Assertions.assertEquals(4, yAttribute.offsetBytes());

        Assertions.assertEquals(XYZBuffer.z, zAttribute.name());
        Assertions.assertEquals(Integer.class, zAttribute.type());
        Assertions.assertEquals(4, zAttribute.sizeBytes());
        Assertions.assertEquals(8, zAttribute.offsetBytes());
    }
}