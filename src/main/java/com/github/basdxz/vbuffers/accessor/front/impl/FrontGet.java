package com.github.basdxz.vbuffers.accessor.front.impl;

import com.github.basdxz.vbuffers.accessor.front.GetterFront;

import java.lang.reflect.Method;

public class FrontGet implements GetterFront {
    @Override
    public boolean isSupported(Method method) {
        return false;
    }
}
