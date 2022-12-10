package com.github.basdxz.vbuffers.layout.impl;

import com.github.basdxz.vbuffers.helper.SizeBytes;
import com.github.basdxz.vbuffers.layout.Attribute;
import com.github.basdxz.vbuffers.layout.Layout;
import lombok.*;

@Getter
public class LayoutAttribute implements Attribute {
    protected final String name;
    protected final Class<?> type;
    protected final int sizeBytes;
    protected final int offsetBytes;

    public LayoutAttribute(Layout.Attribute annotation, int offsetBytes) {
        this.name = annotation.name();
        this.type = annotation.type();
        this.sizeBytes = SizeBytes.of(this.type);
        this.offsetBytes = offsetBytes;
    }
}
