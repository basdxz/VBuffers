# VBuffers

## Ven's Buffer Interface API

VBuffers is a robust and versatile Java API designed to offer advanced buffer management and manipulation capabilities, catering to the needs of various applications that require efficient data handling and organization.

### Key Features

-   **Flexible Layouts**: Utilize Java Interfaces and Annotations to create powerful and adaptable buffer layouts.
-   **Rich Collections API**: Benefit from an extensive built-in collections API, including Indexed, Iterables, Streams, and more.
-   **User-defined Getters and Setters**: Customize Getters and Setters while taking advantage of runtime-generated glue code for seamless integration.
-   **Custom Type Adapters**: Implement user-defined type adapters to enable tailored byte serialization and deserialization for specific use cases.
-   **Nested Type Handling**: Manage nested types effectively, emulating the power and organization of C structs.
-   **Customizable Attributes**: Configure alignment, padding, or overlapping attributes to suit specific requirements.
-   **Memory Implementation Agnostic**: Ensure compatibility with various memory implementations, such as NIO and Unsafe, for a flexible solution.
-   **Attribute Aliasing**: Enable aliasing between interchangeable attributes based on name and type for more efficient data handling.
-   **Advanced Copy Operations**: Perform masked, shuffled, and swizzled copies across the same or different buffers for sophisticated data manipulation.

VBuffers provides a comprehensive solution for developers seeking efficient and customizable buffer management in Java applications, ensuring that data handling remains streamlined and adaptable to various requirements.

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
| Internally Swizzling | Essential | ✴️   | ❌    | ❌    | ❌     |
| Externally Swizzling | Essential | ✴️   | ❌    | ❌    | ❌     |
| Different Backings   | High      | ❌    | ❌    | ❌    | ❌     |
| SSE/AVX acceleration | Neat      | ❌    | ❌    | ❌    | ❌     |

### Correctness

- ByteBuffer navigation constraints
- JetBrains method contracts
- Static analysis for nullity
- Static analysis for bounds
- Comprehensive exceptions (tell what and where was wrong)
