# VBuffers

## Ven's Buffer Interface API

### Goals

- Powerful layouts based on Java Interfaces and Annotations
- Extensive built-in collections API: Indexed, Iterables, Streams, etc.
- User defined Getters and Setters, while providing runtime generation of the glue code
- User defined type adapters, allowing for custom byte serialization and deserialization
- Handling of nested types, aiming to match the power of C structs
- Custom alignment, padding or overlapping attributes
- Agnostic to the underlying memory implementation: NIO, Unsafe, etc.
- Aliasing between interchangeable attributes by name and type
- Masked, Shuffled and Swizzled copies across the same or different buffers

### Functionality

#### Layout

| Name       | Priority  | Impl | Test | Docs | Bench |
|------------|-----------|------|------|------|-------|
| Definition | Essential | ✴️   | ❌    | ❌    | ❌     |
| Stride     | Essential | ✴️   | ❌    | ❌    | ❌     |
| Attribute  | Essential | ✴️   | ❌    | ❌    | ❌     |
| Accessors  | Essential | ✴️   | ❌    | ❌    | ❌     |

<br>

#### Attribute

| Name                      | Priority  | Impl | Test | Docs | Bench |
|---------------------------|-----------|------|------|------|-------|
| Definition                | Essential | ✴️   | ❌    | ❌    | ❌     |
| Name Aliasing             | High      | ❌    | ❌    | ❌    | ❌     |
| Type Aliasing             | Neat      | ❌    | ❌    | ❌    | ❌     |
| Different I/O Class Types | Neat      | ❌    | ❌    | ❌    | ❌     |

<br>

#### Type handling

| Name                     | Priority  | Impl | Test | Docs | Bench |
|--------------------------|-----------|------|------|------|-------|
| Definition               | Essential | ✴️   | ❌    | ❌    | ❌     |
| Type Accessors           | Essential | ✴️   | ✴️   | ❌    | ❌     |
| Primitives/Boxed Support | Essential | ✴️   | ✴️   | ❌    | ❌     |
| JOML Support             | High      | ✴️   | ✴️   | ❌    | ❌     |
| Nested VBuffers Support  | Neat      | ❌    | ❌    | ❌    | ❌     |
| JUnion Support           | Neat      | ❌    | ❌    | ❌    | ❌     |

<br>

#### Backing

| Name                | Priority  | Impl | Test | Docs | Bench |
|---------------------|-----------|------|------|------|-------|
| ByteBuffer/Java NIO | Essential | ✴️   | ✴️   | ❌    | ❌     |
| Abstract Definition | High      | ❌    | ❌    | ❌    | ❌     |
| MemorySegment       | High      | ❌    | ❌    | ❌    | ❌     |
| Java Unsafe         | Neat      | ❌    | ❌    | ❌    | ❌     |
| Disk/Filesystem     | Neat      | ❌    | ❌    | ❌    | ❌     |

<br>

#### Instance creation

| Name           | Priority  | Impl | Test | Docs | Bench |
|----------------|-----------|------|------|------|-------|
| Dynamic Proxy  | Essential | ✴️   | ✴️   | ❌    | ❌     |
| Object Web ASM | High      | ❌    | ❌    | ❌    | ❌     |

Dynamic Proxies make for great prototyping tools but are not suitable for production use.
They are slow, have a lot of overhead and are generally more clunky.

ASM will take longer to implement but will be much faster and more efficient.

<br>

#### NIO Based API

| Name           | Priority | Impl | Test | Docs | Bench |
|----------------|----------|------|------|------|-------|
| Navigation     | High     | ✴️   | ❌    | ❌    | ❌     |
| Slice View     | High     | ✴️   | ✴️   | ❌    | ❌     |
| Flip           | Neat     | ✴️   | ✴️   | ❌    | ❌     |
| Compact        | Neat     | ✴️   | ✴️   | ❌    | ❌     |
| Duplicate View | Neat     | ✴️   | ✴️   | ❌    | ❌     |
| Read-Only View | Neat     | ✴️   | ✴️   | ❌    | ❌     |

<br>

#### Collections

| Name             | Priority  | Impl | Test | Docs | Bench |
|------------------|-----------|------|------|------|-------|
| Basic Array      | Essential | ✴️   | ✴️   | ❌    | ❌     |
| Singleton        | Essential | ✴️   | ❌    | ❌    | ❌     |
| Iterators        | High      | ✴️   | ✴️   | ❌    | ❌     |
| Streams          | High      | ✴️   | ✴️   | ❌    | ❌     |
| Parallel Streams | Neat      | ✴️   | ✴️   | ❌    | ❌     |

<br>

#### Copy

| Name                 | Priority  | Impl | Test | Docs | Bench |
|----------------------|-----------|------|------|------|-------|
| Internally 1:1       | Essential | ✴️   | ✴️   | ❌    | ❌     |
| Externally 1:1       | Essential | ✴️   | ❌    | ❌    | ❌     |
| Internally Swizzling | Essential | ❌    | ❌    | ❌    | ❌     |
| Externally Swizzling | Essential | ❌    | ❌    | ❌    | ❌     |
| Different Backings   | High      | ❌    | ❌    | ❌    | ❌     |
| SSE/AVX acceleration | Neat      | ❌    | ❌    | ❌    | ❌     |

### Correctness

- ByteBuffer navigation constraints
- JetBrains method contracts
- Static analysis for nullity
- Static analysis for bounds
- Comprehensive exceptions (tell what and where was wrong)