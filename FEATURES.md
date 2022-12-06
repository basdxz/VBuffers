### Functionality

#### Layout

- [x] Layout Definition [Missing Test]
- [ ] Layout size constraints
- [ ] Layout annotation inference (allowing for fewer total annotations)

<br>

#### Attributes

- [ ] Different I/O class types
- [ ] Mutator/Accessor annotations
- [ ] Mutator/Accessor templates
- [ ] Padding/Alignment/Overlapping
- [ ] Name Aliasing
- [ ] Type Aliasing

<br>

#### Type handling

- [ ] Primitives/boxed primitives
- [ ] Extensible API
- [ ] JOML support
- [ ] JUnion support
- [ ] Type Handling for nested attributes

<br>

#### Backing

- [x] ByteBuffer/Java NIO
- [ ] Extensible API
- [ ] with Java Unsafe
- [ ] LWJGL 2 OpenGL
- [ ] MemorySegment/Foreign Memory Access
- [ ] Disk/Filesystem

<br>

#### Instance creation

- [x] Dynamic Proxy [Missing Test]
- [ ] Object Web ASM

<br>

#### Input/Output

- [x] Basic Read/Write

<br>

#### ByteBuffer-esq API

- [ ] Navigation
- [x] Flip
- [x] Compact
- [x] Duplicate View
- [x] Slice View
- [x] Read-Only View

<br>

#### Collections

- [x] Basic Array
- [x] Singleton [Missing Test]
- [x] Iterators
- [x] Streams
- [x] Parallel Streams

<br>

#### Copy

- [x] Internally 1:1
- [ ] Internally Swizzling
- [x] Externally 1:1 [Incomplete]
- [ ] Externally Swizzling
- [ ] Externally between different backings
- [ ] with SSE/AVX acceleration

### Correctness

- [ ] Using the terms Mutator/Accessor correctly [Look it up/Ask for Help]
- [ ] ByteBuffer navigation constraints
- [ ] JetBrains method contracts
- [ ] Static analysis for nullity
- [ ] Static analysis for bounds
- [ ] Comprehensive exceptions (tell what and where was wrong)

### Benchmarking

- [ ] New layout interface generation
- [ ] Layout/View creation
- [ ] Allocation/Slicing
- [ ] Iteration I/O
- [ ] Copying

### Documentation

- [ ] Currently existing tests
- [ ] VBuffer interface
- [ ] VBuffer Handler/Implementation