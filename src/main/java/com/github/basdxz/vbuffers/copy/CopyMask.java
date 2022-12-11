package com.github.basdxz.vbuffers.copy;

import lombok.*;

import java.util.List;

@Getter
public class CopyMask {
    protected final List<String> attributeNames;

    public CopyMask(String... attributeNames) {
        if (attributeNames.length == 0)
            throw new IllegalArgumentException("Must have at least one attribute name");
        this.attributeNames = List.of(attributeNames);
    }
}
