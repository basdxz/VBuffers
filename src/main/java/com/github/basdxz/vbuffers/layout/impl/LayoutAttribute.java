package com.github.basdxz.vbuffers.layout.impl;

import com.github.basdxz.vbuffers.helper.SizeBytes;
import com.github.basdxz.vbuffers.layout.Attribute;
import com.github.basdxz.vbuffers.layout.Layout;
import lombok.*;

@Getter
public class LayoutAttribute implements Attribute {
    private final String name;
    private final int sizeBytes;
    private final int offsetBytes;

    public LayoutAttribute(Layout.Attribute annotation, int offsetBytes) {
        this.name = annotation.name();
        this.sizeBytes = SizeBytes.of(annotation.type());
        this.offsetBytes = offsetBytes;
    }
}
