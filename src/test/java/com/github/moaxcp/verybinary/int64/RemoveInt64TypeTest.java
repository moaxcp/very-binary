package com.github.moaxcp.verybinary.int64;

import org.junit.jupiter.api.Test;

import static com.github.moaxcp.verybinary.Builders.struct;
import static com.github.moaxcp.verybinary.ByteArray.ba;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class RemoveInt64TypeTest {
  @Test
  void removeInt64() {
    var struct = struct()
        .int64()
        .int64Array(0)
        .fromBytes(ba().int64(2, 1, 2))
        .build();

    struct.removeAll(1);

    assertThat(struct.getByteArray()).isEqualTo(ba().int64(0));
  }

  @Test
  void removeInt64_position_negative() {
    var struct = struct()
        .int64()
        .int64Array(0)
        .build();

    assertThatThrownBy(() -> struct.removeAll(-1))
        .isInstanceOf(IndexOutOfBoundsException.class)
        .hasMessage("Index -1 out of bounds for length 2");
  }

  @Test
  void removeInt64_position_greater_than_length() {
    var struct = struct()
        .int64()
        .int64Array(0)
        .build();

    assertThatThrownBy(() -> struct.removeAll(2))
        .isInstanceOf(IndexOutOfBoundsException.class)
        .hasMessage("Index 2 out of bounds for length 2");
  }

  @Test
  void removeInt64_not_allocated() {
    var struct = struct()
        .allocated()
        .int64()
        .int64Array(0)
        .build();

    assertThatThrownBy(() -> struct.removeAll(1))
        .isInstanceOf(IndexOutOfBoundsException.class)
        .hasMessage("allocated: 0, index: 0, length: 8");
  }

  @Test
  void removeInt64_not_array() {
    var struct = struct()
        .int64()
        .build();

    assertThatThrownBy(() -> struct.removeAll(0))
        .isInstanceOf(UnsupportedOperationException.class)
        .hasMessage("Cannot remove element from fixed length Int64Type at position 0");
  }

  @Test
  void removeInt64_with_index_0() {
    var struct = struct()
        .int64()
        .int64Array(0)
        .build();

    struct.addInt64(1, 1L);
    struct.addInt64(1, 2L);
    struct.remove(1, 0);

    assertThat(struct.getByteArray()).isEqualTo(ba().int64(1, 2));
  }

  @Test
  void removeInt64_with_index_1() {
    var struct = struct()
        .int64()
        .int64Array(0)
        .build();
    struct.addInt64(1, 1L);
    struct.addInt64(1, 2L);

    struct.remove(1, 1);

    assertThat(struct.getByteArray()).isEqualTo(ba().int64(1, 1));
  }

  @Test
  void removeInt64_with_index_negative() {
    var struct = struct()
        .int64()
        .int64Array(0)
        .fromBytes(ba().int64(2, 1, 2))
        .build();

    assertThatThrownBy(() -> struct.remove(1, -1))
        .isInstanceOf(ArrayIndexOutOfBoundsException.class)
        .hasMessage("Int64ArrayType at position 1 index: -1 length: 2");
  }

  @Test
  void removeInt64_with_index_greater_than_length() {
    var struct = struct()
        .int64()
        .int64Array(0)
        .fromBytes(ba().int64(2, 1, 2))
        .build();

    assertThatThrownBy(() -> struct.remove(1, 2))
        .isInstanceOf(ArrayIndexOutOfBoundsException.class)
        .hasMessage("Int64ArrayType at position 1 index: 2 length: 2");
  }

  @Test
  void removeInt64_with_index_not_array() {
    var struct = struct()
        .int64()
        .fromBytes(ba().int64(1))
        .build();

    assertThatThrownBy(() -> struct.remove(0, 0))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Field at postion 0 is not a ArrayValueType or ListValueType");
  }

  @Test
  void removeInt64Array_fixed_length() {
    var struct = struct()
        .primitive().constant(new long[]{5, 5, 5, 5, 5}).int64()
        .build();

    assertThatThrownBy(() -> struct.removeAll(0))
        .isInstanceOf(UnsupportedOperationException.class)
        .hasMessage("Cannot remove element from fixed length Int64ArrayType at position 0");
  }

  @Test
  void removeInt64Array_fixed_length_with_index() {
    var struct = struct()
        .primitive().constant(new long[]{5, 5, 5, 5, 5}).int64()
        .build();

    assertThatThrownBy(() -> struct.remove(0, 3))
        .isInstanceOf(UnsupportedOperationException.class)
        .hasMessage("Cannot remove element from fixed length array Int64ArrayType at position 0 index: 3");
  }
}
