package com.github.basdxz.vbuffers;

public @interface Layout {
    Attribute[] value();

    @interface Attribute {
        String value();

        String[] aliases() default {};

        Class<?> type();

        int sizeBytes() default -1;

        int offsetBytes() default -1;

        int orderIndex() default -1;
    }
}
