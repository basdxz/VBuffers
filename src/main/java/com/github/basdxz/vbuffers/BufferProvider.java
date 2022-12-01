package com.github.basdxz.vbuffers;

import lombok.*;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

public final class BufferProvider {
    @SuppressWarnings("unchecked")
    public static <BUFFER extends VBuffer> BUFFER provide(@NonNull Class<BUFFER> layout) {
        val classLoader = BufferProvider.class.getClassLoader();
        val interfaces = new Class[]{layout};
        return (BUFFER) Proxy.newProxyInstance(classLoader, interfaces, new BufferInvocationHandler());
    }

    public static class BufferInvocationHandler implements InvocationHandler {
        protected final Map<String, Object> data = new HashMap<>();

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            val key = method.getName();
            if (args != null) {
                write(key, args[0]);
                return proxy;
            }
            return read(key);
        }

        protected void write(String key, Object value) {
            data.put(key, value);
        }

        protected Object read(String key) {
            return data.get(key);
        }
    }
}
