package com.github.basdxz.vbuffers.layout.impl;

import com.github.basdxz.vbuffers.helper.SizeBytes;
import com.github.basdxz.vbuffers.layout.Attribute;
import com.github.basdxz.vbuffers.layout.Layout;
import lombok.*;

@Getter
public class BufferAttribute implements Attribute {
    private final String name;
    private final Class<?> type;
    private final int sizeBytes;
    private final int offsetBytes;

    public BufferAttribute(Layout.Attribute annotation, int offsetBytes) {
        this.name = annotation.name();
        this.type = annotation.type();
        this.sizeBytes = SizeBytes.of(this.type);
        this.offsetBytes = offsetBytes;
    }
}
