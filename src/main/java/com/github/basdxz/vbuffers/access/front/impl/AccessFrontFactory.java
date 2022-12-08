package com.github.basdxz.vbuffers.access.front.impl;

import com.github.basdxz.vbuffers.access.back.SetterBack;
import com.github.basdxz.vbuffers.access.back.impl.AccessorBacks;
import com.github.basdxz.vbuffers.access.front.AccessFront;
import com.github.basdxz.vbuffers.access.front.SetterFront;
import lombok.*;

import java.lang.reflect.Method;
import java.nio.ByteBuffer;

public class AccessFrontFactory {
    public boolean isSupported(Method method) {
        if (!method.isAnnotationPresent(SetterFront.Access.class))
            return false;
        if (method.getReturnType() != void.class)
            return false;
        return method.getParameterTypes().length == 1;
    }

    @SuppressWarnings("unchecked")
    public AccessFront create(Method method) {
        val setter = (SetterBack<Object>) AccessorBacks.setter(method.getParameterTypes()[0]);
        return (SetterFront) args -> {
            setter.put((ByteBuffer) args[0], (int) args[1], args[2]);
            return null;
        };
    }
}
