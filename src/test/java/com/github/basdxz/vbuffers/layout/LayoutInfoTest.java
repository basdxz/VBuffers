package com.github.basdxz.vbuffers.layout;

import com.github.basdxz.vbuffers.sample.XYZBuffer;
import lombok.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.github.basdxz.vbuffers.sample.TestConstants.*;

@DisplayName("Layout Info")
class LayoutInfoTest {
    @Test
    @DisplayName("XYZ Buffer")
    void xyzBuffer() {
        // Create the layout info
        val layoutInfo = new LayoutInfo<>(XYZBuffer.class);

        // Validate the layout info
        Assertions.assertEquals(XYZBuffer.class, layoutInfo.type());

        // Validate the stride
        val stride = layoutInfo.stride();

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

        // Validate methods
        val methods = layoutInfo.methods();

        Assertions.assertEquals(6, methods.size());
    }
}
