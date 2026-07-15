package com.github.moaxcp.verybinary.bool;

import org.junit.jupiter.api.Test;

import static com.github.moaxcp.verybinary.Builders.struct;
import static com.github.moaxcp.verybinary.ByteArray.ba;
import static com.github.moaxcp.verybinary.math.Expression.constant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class RemoveBoolTypeTest {

  @Test
  void remove() {
    var struct = struct()
        .int8()
        .boolArray(0)
        .fromBytes(new byte[] {2, 1, 0})
        .build();

    struct.remove(1);

    assertThat(struct.getByteArray()).isEqualTo(ba().int8(0));
  }

  @Test
  void remove_position_negative() {
    var struct = struct()
        .int8()
        .boolArray(0)
        .build();

    assertThatThrownBy(() -> struct.remove(-1))
        .isInstanceOf(IndexOutOfBoundsException.class)
        .hasMessage("Index -1 out of bounds for length 2");
  }

  @Test
  void remove_position_greater_than_length() {
    var struct = struct()
        .int8()
        .boolArray(0)
        .build();

    assertThatThrownBy(() -> struct.remove(2))
        .isInstanceOf(IndexOutOfBoundsException.class)
        .hasMessage("Index 2 out of bounds for length 2");
  }

  @Test
  void remove_not_allocated() {
    var struct = struct()
        .allocated()
        .int8()
        .boolArray(0)
        .build();

    assertThatThrownBy(() -> struct.remove(1))
        .isInstanceOf(IndexOutOfBoundsException.class)
        .hasMessage("allocated: 0, index: 0, length: 1");
  }

  @Test
  void remove_not_array() {
    var struct = struct()
        .bool()
        .build();

    assertThatThrownBy(() -> struct.remove(0))
        .isInstanceOf(UnsupportedOperationException.class)
        .hasMessage("Cannot remove element from fixed length BoolType at position 0");
  }

  @Test
  void remove_index_0() {
    var struct = struct()
        .int8()
        .boolArray(0)
        .build();

    struct.addBool(1, true);
    struct.addBool(1, false);
    struct.remove(1, 0);

    assertThat(struct.getByteArray()).isEqualTo(ba().int8(1).bool(false));
  }

  @Test
  void remove_index_1() {
    var struct = struct()
        .int8()
        .boolArray(0)
        .build();
    struct.addBool(1, true);
    struct.addBool(1, false);

    struct.remove(1, 1);

    assertThat(struct.getByteArray()).isEqualTo(ba().int8(1).bool(true));
  }

  @Test
  void remove_index_negative() {
    var struct = struct()
        .int8()
        .boolArray(0)
        .fromBytes(new byte[] {2, 1, 0})
        .build();

    assertThatThrownBy(() -> struct.remove(1, -1))
        .isInstanceOf(ArrayIndexOutOfBoundsException.class)
        .hasMessage("BoolListType at position 1 index: -1 length: 2");
  }

  @Test
  void remove_index_greater_than_length() {
    var struct = struct()
        .int8()
        .boolArray(0)
        .fromBytes(new byte[] {2, 1, 0})
        .build();

    assertThatThrownBy(() -> struct.remove(1, 2))
        .isInstanceOf(ArrayIndexOutOfBoundsException.class)
        .hasMessage("BoolListType at position 1 index: 2 length: 2");
  }

  @Test
  void remove_index_not_array() {
    var struct = struct()
        .bool()
        .fromBytes(new byte[] {1})
        .build();

    assertThatThrownBy(() -> struct.remove(0, 0))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Field at postion 0 is not a ArrayValueType or ListValueType");
  }

  @Test
  void remove_fixed_length() {
    var struct = struct()
        .primitive().constant(new boolean[]{true, true, true, true, true}).bool()
        .build();

    assertThatThrownBy(() -> struct.remove(0))
        .isInstanceOf(UnsupportedOperationException.class)
        .hasMessage("Cannot remove element from fixed length BoolListType at position 0");
  }

  @Test
  void remove_index_fixed_length() {
    var struct = struct()
        .primitive().constant(new boolean[]{true, true, true, true, true}).bool()
        .build();

    assertThatThrownBy(() -> struct.remove(0, 3))
        .isInstanceOf(UnsupportedOperationException.class)
        .hasMessage("Cannot remove element from fixed length array BoolListType at position 0 index: 3");
  }

  @Test
  void remove_index_length() {
    var struct = struct()
        .int8()
        .boolArray(0)
        .build();

    struct.addBool(1, true, true, true, true);
    struct.remove(1, 2, 2);

    assertThat(struct.getByteArray()).isEqualTo(ba().int8(2).bool(true, true));
  }

  @Test
  void remove_index_length_negative() {
    var struct = struct()
        .int8()
        .boolArray(0)
        .build();

    struct.addBool(1, true, true, true, true);

    assertThatThrownBy(() -> struct.remove(1, 2, -1))
        .isInstanceOf(ArrayIndexOutOfBoundsException.class)
        .hasMessage("BoolListType at position 1 length: 4 start: 2 end: 0");
  }

  @Test
  void remove_index_length_greater_than_length() {
    var struct = struct()
        .int8()
        .boolArray(0)
        .build();

    struct.addBool(1, true, true, true, true);

    assertThatThrownBy(() -> struct.remove(1, 2, 5))
        .isInstanceOf(ArrayIndexOutOfBoundsException.class)
        .hasMessage("BoolListType at position 1 length: 4 start: 2 end: 6");
  }

  @Test
  void remove_index_length_fixed() {
    var struct = struct()
        .boolArray(constant(5))
        .build();

    assertThatThrownBy(() -> struct.remove(0, 2, 2))
        .isInstanceOf(UnsupportedOperationException.class)
        .hasMessage("Cannot remove element from fixed length array BoolListType at position 0 index: 2");
  }
}
