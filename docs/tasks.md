
1. [x] Phase 1 — Primitive definitions and rules
  - [x] Define constants for supported primitives and their byte widths: bool (1), int8/uint8 (1), int16/uint16 (2), int32/uint32 (4), int64/uint64 (8), float32 (4), float64 (8).
  - [x] Specify unsigned read return types (e.g., int→long, long→BigInteger or custom) and implement write-time range validation for all unsigned types — refs: req: Layers of abstraction > primitive types; plan: 4.1. Primitive definitions and rules
  - [x] Implement canonical conversions for uint32/uint64 to ensure full-range, sign-safe handling — refs: req: Layers of abstraction > primitive types; plan: 4.1. Primitive definitions and rules
  - [x] Decide and document canonical NaN handling for float32/float64; ensure bit pattern preservation on round-trips.
  - [x] Provide shared utilities for range checks and constant-width lookups used across layers — refs: req: Layers of abstraction > primitive types; plan: 4.1. Primitive definitions and rules

2. [x] Phase 2 — Serializer layer
  - [x] Implement BigEndian serializer covering all primitives (signed/unsigned, floats) with consistent method names.
  - [x] Implement LittleEndian serializer covering all primitives (signed/unsigned, floats) with consistent method names.
  - [x] Ensure all reads/writes go through the Serializer (no direct byte[] access in higher layers).
  - [x] Add cross-endian unit tests validating symmetry, correct byte order, and edge values (including unsigned bounds and NaN payloads).
  - [x] Add cross-endian unit tests for float32/float64 non-NaN values — refs: req: Layers of abstraction > primitive types; plan: 4.2. Serializer layer
  - [x] Update tests comparing bytes to assert ByteArray equality using ba() instead of getBytes() — refs: req: ByteArray; plan: 4.2. Serializer layer

3. [x] Phase 3 — ByteArray core
  - [x] Provide constructors with optional initial capacity and pluggable Serializer.
  - [x] Use long for public indices/lengths; add defensive checks against exceeding single-array addressable limits — refs: req: ByteArray; plan: 4.3. ByteArray core
  - [x] Implement capacity management with growth strategy and efficient shifting via System.arraycopy.
  - [x] Implement per-primitive APIs (pattern applies to all types): getX(index), getX(index, length), getXList(index, length). — refs: req: ByteArray; plan: 4.3. ByteArray core
  - [x] Implement per-primitive APIs (pattern applies to all types): setX(index, value), setX(index, values...), setX(index, List).
  - [x] Implement per-primitive APIs (pattern applies to all types): insert APIs: addX(index, value), addX(index, values), addX(index, List) with shifting — refs: req: ByteArray; plan: 4.3. ByteArray core
  - [x] Implement per-primitive APIs (pattern applies to all types: bool, int8, uint8, int16, uint16, int32, uint32, int64, uint64, float32, flaot64): append APIs: x(value), x(values...), x(List) using the add methods with an index of the length of the bytes. — refs: req: ByteArray; plan: 4.3. ByteArray core
  - [x] Implement per-primitive APIs (pattern applies to all types): remove APIs: removeX(index) and removeX(index, length).
  - [x] Validate per-primitive APIs (pattern applies to all types): all index/length arguments; throw descriptive out-of-range exceptions.

4. [x] Phase 4 — ByteArray listeners and shift events
   - [x] Implement listener registration and removal APIs.
   - [x] Fire notifications on structural changes: after insertions/removals.
   - [x] Include event metadata (index, length delta) in notifications.
   - [ ] Define and enforce non-reentrant listener behavior; document ordering and re-entrancy rules.

5. [ ] Phase 5 — Primitive Type classes
   - [x] Create Type classes for each primitive with known byte-width and any alignment rules.
   - [x] Compute absolute byte indices from Pointer offset and field index.
   - [x] Delegate all data access to ByteArray methods (no direct serialization logic here).
   - [ ] Expose value, array, and list operations mirroring ByteArray’s surface.
     - [ ] BoolType
     - [ ] Int8Type
     - [ ] Uint8Type
     - [ ] Int16Type
     - [ ] Uint16Type
     - [ ] Int32Type
     - [ ] Uint32Type
     - [ ] Int64Type
     - [ ] Uint64Type
     - [ ] Float32Type
     - [ ] Float64Type
     - [ ] StructType

6. [ ] Phase 6 — StructType schema
   - [x] Define ordered fields with indexes and associated Type instances.
   - [x] Support explicit padding entries via a PadType.
   - [ ] Implement total size calculation including padding.
   - [ ] Provide utilities to compute per-field byte offsets.
   - [ ] Provide builders to ergonomically assemble StructType definitions; support embedding child structs.

7. [ ] Phase 7 — Pointer abstraction
   - [ ] Define a Pointer interface exposing: getByteArray, getType, getOffset, and movement to a different offset.
   - [ ] Ensure immutability of the type association where appropriate.
   - [ ] Provide bounds-safe navigation utilities for arrays of structs.

8. [ ] Phase 8 — Struct
   - [ ] Implement Struct as a Pointer bound to a StructType.
   - [ ] Expose methods to access fields by index and, if supported, by symbolic name from the schema.
   - [ ] Ensure struct-sized inserts/removes shift bytes properly through ByteArray.
   - [ ] Ensure listener notifications propagate correctly on struct operations.

9. [ ] Phase 9 — Builders and ergonomics
   - [ ] Provide builder classes for StructType creation, including sub-builders for primitive fields and pad entries.
   - [ ] Introduce helper utilities for computed lengths or conditional fields if required by schemas.

10. [ ] Phase 10 — Testing strategy
  - [x] Add unit tests for each primitive under both endianness serializers with edge values, including unsigned boundaries — refs: req: Layers of abstraction > primitive types; plan: 4.10. Testing strategy
  - [ ] Add ByteArray tests for set/get, append, insert, remove, capacity growth, and listener notifications verifying indices/lengths.
  - [ ] Add Type tests confirming offset calculations and delegation to ByteArray.
  - [ ] Add StructType tests for total size, field offsets, and padding correctness.
  - [ ] Add Struct tests for full read/write round-trips across multiple fields, including nested structs and shifting scenarios.
  - [ ] Add property-based or fuzz tests for random sequences of inserts/removes validating invariants (lengths, contents).

11. [ ] Phase 11 — Performance and memory
   - [ ] Benchmark common operations (sequential appends, scattered inserts, large removals) and set target thresholds.
   - [ ] Optimize shifting paths; minimize temporary allocations based on benchmark feedback.
   - [ ] Document practical limits due to the single-array backing and outline plans for multi-buffer extension.

12. [ ] Phase 12 — Documentation
   - [ ] Update README and docs to explain abstractions, layering, and usage patterns (without code samples for this phase).
   - [ ] Describe listener semantics, unsigned behavior, and endianness configuration.
   - [ ] Correct the specification typo: clarify that uint64 is the 64-bit unsigned integer (not uint32).

13. [ ] Phase 13 — Dependencies and sequencing checks
   - [ ] Verify Serializer layer completion before integrating ByteArray.
   - [ ] Verify ByteArray readiness before implementing Type classes.
   - [ ] Verify Type and StructType completeness before Pointer/Struct.
   - [ ] Verify listeners are complete before exposing insert/remove features widely.
   - [ ] Defer builders requiring StructType/Type stability until those are finalized.

14. [ ] Phase 14 — Acceptance and release criteria
   - [ ] Confirm all primitives round-trip correctly under both endianness modes, including edge and unsigned values.
   - [ ] Confirm ByteArray supports required APIs with long indexing; shifting works; listeners fire with correct metadata.
   - [ ] Confirm Types compute offsets correctly and delegate operations to ByteArray.
   - [ ] Confirm StructType accurately defines size and field offsets; Struct manipulates data per schema.
   - [ ] Ensure the full test suite passes consistently and documents coverage of critical behaviors.
