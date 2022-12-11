package com.github.basdxz.vbuffers.instance;

import com.github.basdxz.vbuffers.copy.CopyMask;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;

import java.nio.ByteBuffer;

@SuppressWarnings({"unused", "UnusedReturnValue"})
public interface ExtendedBuffer<LAYOUT extends ExtendedBuffer<LAYOUT>> extends Buffer<LAYOUT>,
                                                                               Viewable<LAYOUT>,
                                                                               Iterable<LAYOUT>,
                                                                               Streamable<LAYOUT> {
    @Contract(pure = true)
    ByteBuffer v$backing();

    @Contract(pure = true)
    int v$capacity();

    @Contract(pure = true)
    int v$position();

    @Contract("-> this")
    LAYOUT v$next();

    @Contract("_ -> this")
    LAYOUT v$position(int newPosition);

    @Contract(pure = true)
    int v$limit();

    @Contract("_ -> this")
    LAYOUT v$limit(int limit);

    @Contract("-> this")
    LAYOUT v$mark();

    @Contract("-> this")
    LAYOUT v$reset();

    @Contract("-> this")
    LAYOUT v$clear();

    @Contract("-> this")
    LAYOUT v$flip();

    @Contract("-> this")
    LAYOUT v$rewind();

    @Contract("-> this")
    LAYOUT v$compact();

    @Contract("_, _, _-> this")
    LAYOUT v$copyStrides(int targetIndex, int sourceIndex, int length);

    @Contract("_, _-> this")
    LAYOUT v$copyStride(int targetIndex, int sourceIndex);

    @Contract("_, _ -> this")
    LAYOUT v$put(LAYOUT source, @Nullable CopyMask mask);

    @Contract("_ -> this")
    LAYOUT v$put(LAYOUT source);

    @Contract(pure = true)
    boolean v$hasRemaining();

    @Contract(pure = true)
    int v$remaining();
}
