package com.github.basdxz.vbuffers.layout;

import com.github.basdxz.vbuffers.helper.ByteHelper;
import lombok.*;

@Getter
public class Attribute {
    protected final String name;
    protected final Class<?> type;
    protected final int sizeBytes;
    protected final int offsetBytes;

    public Attribute(Layout.Attribute annotation, int offsetBytes) {
        this.name = annotation.name();
        this.type = annotation.type();
        this.sizeBytes = ByteHelper.sizeBytes(this.type);
        this.offsetBytes = offsetBytes;
    }
}
