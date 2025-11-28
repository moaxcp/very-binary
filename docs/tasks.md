
1. [ ] Phase 1 — Primitive definitions and rules
  - [x] Define constants for supported primitives and their byte widths: bool (1), int8/uint8 (1), int16/uint16 (2), int32/uint32 (4), int64/uint64 (8), float32 (4), float64 (8).
  - [x] Specify unsigned read return types (e.g., int→long, long→BigInteger or custom) and implement write-time range validation for all unsigned types — refs: req: Layers of abstraction > primitive types; plan: 4.1. Primitive definitions and rules
  - [x] Implement canonical conversions for uint32/uint64 to ensure full-range, sign-safe handling — refs: req: Layers of abstraction > primitive types; plan: 4.1. Primitive definitions and rules
  - [x] Decide and document canonical NaN handling for float32/float64; ensure bit pattern preservation on round-trips.
  - [ ] Provide shared utilities for range checks and constant-width lookups used across layers.

2. [ ] Phase 2 — Serializer layer
   - [ ] Implement BigEndian serializer covering all primitives (signed/unsigned, floats) with consistent method names.
   - [ ] Implement LittleEndian serializer covering all primitives (signed/unsigned, floats) with consistent method names.
   - [ ] Ensure all reads/writes go through the Serializer (no direct byte[] access in higher layers).
   - [ ] Add cross-endian unit tests validating symmetry, correct byte order, and edge values (including unsigned bounds and NaN payloads).

3. [ ] Phase 3 — ByteArray core
   - [ ] Provide constructors with optional initial capacity and pluggable Serializer.
   - [ ] Use long for public indices/lengths; add defensive checks against exceeding single-array addressable limits.
   - [ ] Implement capacity management with growth strategy and efficient shifting via System.arraycopy.
   - [ ] Implement per-primitive APIs (pattern applies to all types): getX(index), getX(index, length), getXList(index, length).
   - [ ] Implement setX(index, value), setX(index, values...), setX(index, List).
   - [ ] Implement append APIs: x(value), x(values...), x(List) with auto-allocation.
   - [ ] Implement insert APIs: addX(index, value/array/List) with shifting.
   - [ ] Implement remove APIs: removeX(index) and removeX(index, length).
   - [ ] Validate all index/length arguments; throw descriptive out-of-range exceptions.

4. [ ] Phase 4 — ByteArray listeners and shift events
   - [ ] Implement listener registration and removal APIs.
   - [ ] Fire notifications on structural changes: before/after insertions/removals and on total length changes.
   - [ ] Include event metadata (old/new indices, length delta) in notifications.
   - [ ] Define and enforce non-reentrant listener behavior; document ordering and re-entrancy rules.

5. [ ] Phase 5 — Primitive Type classes
   - [ ] Create Type classes for each primitive with known byte-width and any alignment rules.
   - [ ] Compute absolute byte indices from Pointer offset and field index.
   - [ ] Delegate all data access to ByteArray methods (no direct serialization logic here).
   - [ ] Expose value, array, and list operations mirroring ByteArray’s surface.

6. [ ] Phase 6 — StructType schema
   - [ ] Define ordered fields with indexes and associated Type instances.
   - [ ] Support explicit padding entries via a PadType.
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
