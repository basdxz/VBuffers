package com.github.basdxz.vbuffers.layout;

import com.github.basdxz.vbuffers.sample.XYZBuffer;
import lombok.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.github.basdxz.vbuffers.sample.TestConstants.*;

@DisplayName("Stride")
class StrideTest {
    @Test
    @DisplayName("XYZ Buffer")
    void xyzBuffer() {
        // Get the annotation from the sample interface
        val strideAnnotation = XYZBuffer.class.getAnnotationsByType(Layout.Stride.class)[0];

        // Create the stride
        val stride = new Stride(strideAnnotation);

        // Validate the stride
        Assertions.assertEquals(12, stride.sizeBytes());

        // Validate the attributes
        val attributes = stride.attributes();

        Assertions.assertEquals(3, attributes.size());

        val xAttribute = attributes.get(X);
        val yAttribute = attributes.get(Y);
        val zAttribute = attributes.get(Z);

        Assertions.assertNotNull(xAttribute);
        Assertions.assertNotNull(yAttribute);
        Assertions.assertNotNull(zAttribute);

        Assertions.assertEquals(X, xAttribute.name());
        Assertions.assertEquals(Integer.class, xAttribute.type());
        Assertions.assertEquals(4, xAttribute.sizeBytes());
        Assertions.assertEquals(0, xAttribute.offsetBytes());

        Assertions.assertEquals(Y, yAttribute.name());
        Assertions.assertEquals(Integer.class, yAttribute.type());
        Assertions.assertEquals(4, yAttribute.sizeBytes());
        Assertions.assertEquals(4, yAttribute.offsetBytes());

        Assertions.assertEquals(Z, zAttribute.name());
        Assertions.assertEquals(Integer.class, zAttribute.type());
        Assertions.assertEquals(4, zAttribute.sizeBytes());
        Assertions.assertEquals(8, zAttribute.offsetBytes());
    }
}