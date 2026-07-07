package com.github.moaxcp.verybinary.int32;

import org.junit.jupiter.api.Test;

import static com.github.moaxcp.verybinary.Builders.struct;
import static com.github.moaxcp.verybinary.ByteArray.ba;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class RemoveInt32TypeTest {
  @Test
  void removeInt32() {
    var struct = struct()
        .int32()
        .int32Array(0)
        .fromBytes(ba().int32(2, 1, 2))
        .build();

    struct.remove(1);

    assertThat(struct.getByteArray()).isEqualTo(ba().int32(0));
  }

  @Test
  void removeInt32_position_negative() {
    var struct = struct()
        .int32()
        .int32Array(0)
        .build();

    assertThatThrownBy(() -> struct.remove(-1))
        .isInstanceOf(IndexOutOfBoundsException.class)
        .hasMessage("Index -1 out of bounds for length 2");
  }

  @Test
  void removeInt32_position_greater_than_length() {
    var struct = struct()
        .int32()
        .int32Array(0)
        .build();

    assertThatThrownBy(() -> struct.remove(2))
        .isInstanceOf(IndexOutOfBoundsException.class)
        .hasMessage("Index 2 out of bounds for length 2");
  }

  @Test
  void removeInt32_not_allocated() {
    var struct = struct()
        .allocated()
        .int32()
        .int32Array(0)
        .build();

    assertThatThrownBy(() -> struct.remove(1))
        .isInstanceOf(IndexOutOfBoundsException.class)
        .hasMessage("allocated: 0, index: 0, length: 4");
  }

  @Test
  void removeInt32_not_array() {
    var struct = struct()
        .int32()
        .build();

    assertThatThrownBy(() -> struct.remove(0))
        .isInstanceOf(UnsupportedOperationException.class)
        .hasMessage("Cannot remove element from fixed length Int32Type at position 0");
  }

  @Test
  void removeInt32_with_index_0() {
    var struct = struct()
        .int32()
        .int32Array(0)
        .build();

    struct.addInt32(1, 1);
    struct.addInt32(1, 2);
    struct.remove(1, 0);

    assertThat(struct.getByteArray()).isEqualTo(ba().int32(1, 2));
  }

  @Test
  void removeInt32_with_index_1() {
    var struct = struct()
        .int32()
        .int32Array(0)
        .build();
    struct.addInt32(1, 1);
    struct.addInt32(1, 2);

    struct.remove(1, 1);

    assertThat(struct.getByteArray()).isEqualTo(ba().int32(1, 1));
  }

  @Test
  void removeInt32_with_index_negative() {
    var struct = struct()
        .int32()
        .int32Array(0)
        .fromBytes(ba().int32(2, 1, 2))
        .build();

    assertThatThrownBy(() -> struct.remove(1, -1))
        .isInstanceOf(ArrayIndexOutOfBoundsException.class)
        .hasMessage("Int32ListType at position 1 index: -1 length: 2");
  }

  @Test
  void removeInt32_with_index_greater_than_length() {
    var struct = struct()
        .int32()
        .int32Array(0)
        .fromBytes(ba().int32(2, 1, 2))
        .build();

    assertThatThrownBy(() -> struct.remove(1, 2))
        .isInstanceOf(ArrayIndexOutOfBoundsException.class)
        .hasMessage("Int32ListType at position 1 index: 2 length: 2");
  }

  @Test
  void removeInt32_with_index_not_array() {
    var struct = struct()
        .int32()
        .fromBytes(ba().int32(1))
        .build();

    assertThatThrownBy(() -> struct.remove(0, 0))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Field at postion 0 is not a ArrayValueType or ListValueType");
  }

  @Test
  void removeInt32Array_fixed_length() {
    var struct = struct()
        .primitive().constant(new int[]{5, 5, 5, 5, 5}).int32()
        .build();

    assertThatThrownBy(() -> struct.remove(0))
        .isInstanceOf(UnsupportedOperationException.class)
        .hasMessage("Cannot remove element from fixed length Int32ListType at position 0");
  }

  @Test
  void removeInt32Array_fixed_length_with_index() {
    var struct = struct()
        .primitive().constant(new int[]{5, 5, 5, 5, 5}).int32()
        .build();

    assertThatThrownBy(() -> struct.remove(0, 3))
        .isInstanceOf(UnsupportedOperationException.class)
        .hasMessage("Cannot remove element from fixed length array Int32ListType at position 0 index: 3");
  }
}
