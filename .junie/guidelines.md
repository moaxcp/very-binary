Guidelines for working with docs/tasks.md checklist

- Mark completion by changing [ ] to [x] on a task. Do not change the wording other than the checkbox when marking done.
- Keep all phases intact (numbers and titles). Do not rename or renumber phase headers. Add any new work as new task bullets under the most relevant phase.
- Traceability required: every new or modified task must reference both a requirement and a plan item. Append refs at the end of the task line using this pattern:
  - Example: "  - [ ] Implement capacity growth policy — refs: req: ByteArray; plan: 4.3. ByteArray core"
  - Use section names from docs/requirements.md and section numbers/titles from docs/plan.md.
- Formatting rules (follow existing style exactly):
  - Phase header: "N. [ ] Phase N — Title" on its own line.
  - Task under a phase: two spaces, a hyphen, a space, checkbox, a space, then the description, e.g., "  - [ ] Do something".
  - Use the same em dash (—) and sentence style as current items. Prefer imperative verb phrases; avoid trailing periods unless needed.
- When splitting or replacing an existing task, add the new tasks under the same phase and include a short note at the end: "(replaces: <old task text>)" along with the required refs.

Note: Keep docs/tasks.md focused on the checklist itself. Place all checklist usage instructions here in .junie/guidelines.md.

---

Project-specific development guide

Build/Configuration
- Toolchain: Gradle (Java Library). JDK 21+ recommended for best JUnit 5/AssertJ compatibility; older LTS may work but is untested here
- Build file: build.gradle defines dependencies and junit-platform usage. Nothing custom beyond:
  - api org.jspecify:jspecify:1.0.0
  - testImplementation org.assertj:assertj-core:3.16.0
  - testImplementation platform org.junit:junit-bom:5.10.0 and junit-jupiter
- Default Serializer: ByteArray constructors default to BigEndianSerializer; LittleEndianSerializer is available via explicit constructor overloads
- IDE: Any Gradle-aware IDE (IntelliJ IDEA recommended). No annotation processors configured

How to build
- Clean build: ./gradlew clean build
- Compile only: ./gradlew classes testClasses
- Run tests only: ./gradlew test

Testing
- Frameworks: JUnit 5 (Jupiter) + AssertJ
- Package conventions: tests live under src/test/java/com/github/moaxcp/verybinary using the same package as main sources
- Fast targeting:
  - All tests: ./gradlew test
  - Single package: ./gradlew test --tests com.github.moaxcp.verybinary
  - Single class: ./gradlew test --tests com.github.moaxcp.verybinary.ByteArrayUInt16Test
  - Single test method: ./gradlew test --tests com.github.moaxcp.verybinary.Uint32TypeTest.addUint32

Adding tests
- Prefer AssertJ’s fluent assertions (assertThat) to match existing style
- Reuse helpers exposed by main code and tests:
  - ByteArray.ba() factory for building test data
  - Builders.struct() for struct-based tests
  - Static imports for Primitive/Type builders where appropriate
- Endianness: ByteArray defaults to BigEndian; validate expectations accordingly. For raw bytes, call ByteArray.getBytes()
- Array eventing: When verifying structural shifts, register a ByteArrayListener and assert collected ShiftBytes events

Example test workflow (verified)
- Create a test class under src/test/java/com/github/moaxcp/verybinary with package com.github.moaxcp.verybinary
- Example snippet demonstrating Big Endian uint16 layout:
  - ByteArray arr = ByteArray.ba().uint16(0x00FF).uint16(0x0F0F);
  - byte[] bytes = arr.getBytes();
  - Assertions: [0]=0x00, [1]=0xFF, [2]=0x0F, [3]=0x0F
- Run: ./gradlew test --tests com.github.moaxcp.verybinary.YourNewTestClass

Guidelines and pitfalls
- ByteArray size semantics:
  - getBytes() returns the backing array; allocated tracks the logical used length as elements are written
  - Use ensure/allocate and addXxx methods to grow and to trigger ShiftBytes notifications
- Unsigned types:
  - getUint8 returns short; getUint16 returns int; getUint32 returns long. Use the specific getters/setters to avoid sign-extension issues
- Struct types:
  - StructBuilder / StructTypeBuilder support primitive fields, arrays (constant, length-field, expression), and alignment via ByteLengthListener.align
  - Removing from non-array positions throws ArrayIndexOutOfBoundsException with type-specific message; tests enforce exact messages
- Serialization:
  - BigEndianSerializer and LittleEndianSerializer implement Serializer; choose explicitly when constructing ByteArray if tests require LE
- Assertions:
  - Many tests assert exact exception messages and event sequences. Keep error text and ShiftBytes deltas stable when modifying code

Debugging tips
- To focus on failing areas quickly: ./gradlew test --tests 'com.github.moaxcp.verybinary.*TypeTest.*remove*'
- Add temporary println in tests prefixed with [DEBUG_LOG] to separate from normal output
- For byte inspections, prefer AssertJ array assertions (hasSize, containsExactly) or compare to ba() builders when convenient

Housekeeping
- Keep docs/tasks.md changes traceable per the checklist rules above
- Do not commit temporary sample tests; use them only locally while verifying guidance