Development plan for very-binary based on docs/requirements.md

1. Goal overview
- Build a small, layered binary-serialization toolkit that models C-like structs over an underlying byte array.
- Support reading and writing basicTypeInfo values with pluggable endianness, a ByteArray wrapper with long indexing and byte shifting, a type system that maps primitives and structs, and a StructType schema used by Struct instances via a Pointer abstraction.
- Provide listener hooks for value changes and structural shifts in the underlying bytes.

2. Scope and boundaries
- In scope: basicTypeInfo value support, serializers (big- and little-endian), ByteArray operations and listeners, Type layer (primitives and StructType), Pointer, Struct, and builders/utilities to define StructType schemas.
- Out of scope (for this iteration): multi-buffer backing store for addressing beyond a single byte[], persistence or IO streams, concurrency controls, code generation, and platform-specific optimizations.

3. Architecture at a glance (from lowest to highest)
- byte[]: raw storage of primitives formatted by endianness rules.
- Serializer: strategy for encoding/decoding primitives to/from byte[]. Need BigEndian and LittleEndian.
- ByteArray: wrapper over byte[] that exposes typed get/set/add/remove APIs using the configured Serializer; indexes are long; manages capacity and performs shifting on insert/remove; notifies listeners about shifts and length changes.
- Type hierarchy: per-basicTypeInfo Type classes plus StructType. Types translate logical field accesses to concrete ByteArray operations (index calculations, sizes, and sequence handling).
- Pointer: combines ByteArray, a Type, and an offset to enable typed navigation and manipulation.
- Struct: a specialized Pointer anchored at a StructType, representing one struct instance at an offset.

4. Phased implementation plan
4.1. Primitive definitions and rules
- Define supported primitives and their sizes: bool (1), int8/uint8 (1), int16/uint16 (2), int32/uint32 (4), int64/uint64 (8), float32 (4), float64 (8).
- Clarify unsigned behavior: reads return the widest Java type that can represent the full range without sign extension; writes validate bounds. Establish canonical conversions for uint32/uint64.
- Decide canonical NaN handling for floats; preserve bit patterns on round-trip.
- Establish constants for byte-width per basicTypeInfo and shared utility for range checks.

4.2. Serializer layer
- Implement Big Endian and Little Endian serializers for all primitives, including unsigned variants, using consistent method naming.
- Ensure no direct ByteArray writes bypass Serializer.
- Add cross-endian test matrix to validate symmetry and correct byte order.

4.3. ByteArray core
- Provide construction with optional initial capacity and a Serializer.
- Use long for public indices and lengths; internally guard against exceeding the maximum addressable size of a single Java array.
- Core APIs per basicTypeInfo (pattern applies to all types):
  - getX(index), getX(index, length), getXList(index, length)
  - setX(index, value), setX(index, values...), setX(index, List)
  - x(value), x(values...), x(List) — append with auto-allocation
  - addX(index, value/array/List) — insert with shifting
  - removeX(index), removeX(index, length)
- Implement capacity management and efficient shifting (System.arraycopy). Track allocated length vs. physical capacity to minimize reallocations.
- Validate index/length arguments and throw descriptive exceptions for out-of-range operations.

4.4. ByteArray listeners and shift events
- Implement listener registration/removal.
- Fire notifications on structural changes: after byte insertions/removals. Include index and delta.
- Prevent re-entrant modification hazards by defining event ordering and documentation.

4.5. Primitive Type classes
- Create Type classes per basicTypeInfo that:
  - Know their byte-width and alignment rules (if any padding rules apply).
  - Compute absolute byte indices from Pointer offset and field index.
  - Delegate all data access to ByteArray.
- Provide value, array, and list operations mirroring ByteArray’s surface.

4.6. StructType schema
- Define ordered fields with indexes and associated Type instances; support explicit padding entries via a PadType.
- Provide size calculation (including padding) and utilities to compute field offsets.
- Add builders to ergonomically assemble StructType definitions and embed child structs where necessary.

4.7. Pointer abstraction
- Specify an interface that exposes: getByteArray, getType, getOffset, and movement to a different offset. Ensure immutability of type association where appropriate.
- Provide bounds-safe navigation utilities for arrays of structs.

4.8. Struct
- Implement Struct as a Pointer bound to a StructType. Expose methods to access fields by index or symbolic name (if supported by schema), delegating to Types for all reads/writes.
- Ensure that struct-sized inserts/removes shift bytes properly through ByteArray and notify listeners.

4.9. Builders and ergonomics
- Provide builder classes for StructType creation, including sub-builders for primitives and pad entries.
- Introduce helper utilities to compute expressions for lengths or conditional fields if required by schemas.

4.10. Testing strategy
- Unit tests for each basicTypeInfo against both endianness serializers with edge values, including unsigned boundaries.
- ByteArray tests for all operations: set/get, append, insert, remove, capacity growth, and listener notifications with index/length assertions.
- Type tests to confirm offset calculus and delegation to ByteArray.
- StructType tests for size, field offsets, and padding correctness.
- Struct tests for full read/write round-trips across multiple fields, including nested structs and shifting scenarios.
- Property-based or fuzz tests for random sequences of inserts/removes validating invariants (lengths, contents).

4.11. Performance and memory
- Benchmark common operations (sequential appends, scattered inserts, large removals) and set target thresholds.
- Optimize shifting paths and minimize temporary allocations.
- Document practical limits due to the single-array backing; plan for multi-buffer extension later.

4.12. Documentation
- Update README and docs to explain the abstractions, the layering, and usage patterns without code samples for this phase.
- Describe listener semantics, unsigned behavior, and endianness configuration.

5. Dependencies and sequencing
- Serializer complete before ByteArray; ByteArray before Type; Type and StructType before Pointer/Struct; listeners ready before insert/remove features are exposed.
- Builders depend on StructType and Type stability; tests can be added alongside each phase but full integration tests wait until Struct is usable.

6. Risks and considerations
- Index type: public long vs internal int indexing — document current single-array limit and validate bounds defensively.
- Unsigned values: ensure range checks and prevent negative values from implicit narrowing; define return types for uint32/uint64 operations.
- Floating point: preserve bitwise values on writes/reads, especially NaN payloads.
- Endianness: avoid accidental platform-dependent behavior; ensure serializer is the single source of truth.
- Shifting performance: repeated inserts/removes in large arrays can be O(n). Suggest preallocation and batched operations; expose append-optimized paths.
- Listener overhead and re-entrancy: define non-reentrant listener callbacks and prevent cycles.
- Thread-safety: no implicit synchronization; document that instances are not thread-safe.
- API surface size: maintain consistency across primitives; generate tests to prevent drift.
- Spec typo: requirements list “uint32 - 64 bit unsigned integer” where uint64 is intended — confirm and correct in documentation.

7. Milestones and deliverables
- M1: Serializers implemented and validated.
- M2: ByteArray core with get/set/append and capacity management.
- M3: Insert/remove with shifting and listener notifications.
- M4: Primitive Type layer complete.
- M5: StructType with padding and builders.
- M6: Pointer and Struct functional with integration tests.
- M7: Performance pass and documentation refresh.

8. Acceptance considerations
- All primitives round-trip correctly under both endianness modes, including edge and unsigned values.
- ByteArray supports required APIs with long indexing, shifting works, and listeners fire with correct metadata.
- Types correctly compute offsets and delegate operations.
- StructType accurately defines size and field offsets; Struct manipulates data according to the schema.
- Test suite covers the behaviors above and passes consistently.
