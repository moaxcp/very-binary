package com.github.moaxcp.verybinary.int32;

import com.github.moaxcp.verybinary.Int32Type;
import org.junit.jupiter.api.Test;

import static com.github.moaxcp.verybinary.Builders.struct;
import static com.github.moaxcp.verybinary.ByteArray.ba;
import static com.github.moaxcp.verybinary.Expression.constant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class SetInt32TypeTest {
  @Test
  void setWrapper() {
    var struct = struct()
        .int32()
        .build();

    assertThatThrownBy(() -> ((Int32Type) struct.getType(0)).set(struct, Integer.valueOf(2)))
        .isInstanceOf(UnsupportedOperationException.class)
        .hasMessage("set(Pointer, Integer) not supported for Int32Type. Use set(Pointer, int) instead.");
  }

  @Test
  void setInt32() {
    var struct = struct()
        .int32()
        .build();

    struct.setInt32(0, 2);

    assertThat(struct.getByteArray()).isEqualTo(ba().int32(2));
  }

  @Test
  void setInt32_position_negative() {
    var struct = struct()
        .int32()
        .build();

    assertThatThrownBy(() -> struct.setInt32(-1, 2))
        .isInstanceOf(IndexOutOfBoundsException.class)
        .hasMessage("Index -1 out of bounds for length 1");
  }

  @Test
  void setInt32_position_greater_than_length() {
    var struct = struct()
        .int32()
        .build();

    assertThatThrownBy(() -> struct.setInt32(2, 2))
        .isInstanceOf(IndexOutOfBoundsException.class)
        .hasMessage("Index 2 out of bounds for length 1");
  }

  @Test
  void setInt32_not_allocated() {
    var struct = struct()
        .allocated()
        .int32()
        .build();

    assertThatThrownBy(() -> struct.setInt32(0, 2))
        .isInstanceOf(ArrayIndexOutOfBoundsException.class)
        .hasMessage("Index 0 out of bounds for length 0");
  }

  @Test
  void setInt32_constant() {
    var struct = struct()
        .primitive().constant(5).int32()
        .build();

    assertThatThrownBy(() -> struct.setInt32(0, 2))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Int32Type at position 0 is constant index: 0 value: 2 constant: 5");
  }

  @Test
  void setArrayWrapper() {
    var struct = struct()
        .int32()
        .int32Array(0)
        .build();

    assertThatThrownBy(() -> ((Int32Type) struct.getType(1)).set(struct, 0, Integer.valueOf(2)))
        .isInstanceOf(UnsupportedOperationException.class)
        .hasMessage("set(Pointer, long, Integer) not supported for Int32Type. Use set(Pointer, long, int) instead.");
  }

  @Test
  void setInt32Array() {
    var struct = struct()
        .int32()
        .int32Array(0)
        .int32()
        .fromBytes(ba().int32(1, 2, 3))
        .build();

    struct.setInt32(1, 0, 5);

    assertThat(struct.getInt32(0)).isEqualTo(1);
    assertThat(struct.getInt32(1, 0)).isEqualTo(5);
    assertThat(struct.getByteArray()).isEqualTo(ba().int32(1, 5, 3));
  }

  @Test
  void setInt32Array_negative() {
    var struct = struct()
        .int32()
        .int32Array(0)
        .int32()
        .fromBytes(ba().int32(1, 2, 3))
        .build();

    assertThatThrownBy(() -> struct.setInt32(1, -1, 5))
        .isInstanceOf(IndexOutOfBoundsException.class)
        .hasMessage("Int32Type at position 1 index: -1 length: 1");
  }

  @Test
  void setInt32Array_greater_than_length() {
    var struct = struct()
        .int32()
        .int32Array(0)
        .int32()
        .fromBytes(ba().int32(1, 2, 3))
        .build();

    assertThatThrownBy(() -> struct.setInt32(1, 2, 5))
        .isInstanceOf(ArrayIndexOutOfBoundsException.class)
        .hasMessage("Int32Type at position 1 index: 2 length: 1");
  }

  @Test
  void setInt32Array_not_allocated() {
    var struct = struct()
        .allocated()
        .int32()
        .int32Array(0)
        .build();

    assertThatThrownBy(() -> struct.setInt32(1, 0, 2))
        .isInstanceOf(IndexOutOfBoundsException.class)
        .hasMessage("allocated: 0, index: 0, length: 4");
  }

  @Test
  void setInt32Array_index_0_not_array() {
    var struct = struct()
        .int32()
        .build();

    struct.setInt32(0, 0, 2);

    assertThat(struct.getByteArray()).isEqualTo(ba().int32(2));
  }

  @Test
  void setInt32Array_index_1_not_array() {
    var struct = struct()
        .int32()
        .build();

    assertThatThrownBy(() -> struct.setInt32(0, 1, 2))
        .isInstanceOf(ArrayIndexOutOfBoundsException.class)
        .hasMessage("Int32Type at position 0 index: 1 length: 1");
  }

  @Test
  void setInt32Array_constant_value_and_length() {
    var struct = struct()
        .primitive().constant(5).lengthExpression(constant(5)).int32()
        .build();

    assertThatThrownBy(() -> struct.setInt32(0, 3, 2))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Int32Type at position 0 is constant index: 3 value: 2 constant: 5");
  }

  @Test
  void setInt32Array_constant_value() {
    var struct = struct()
        .int32()
        .primitive().constant(5).lengthField(0).int32()
        .fromBytes(ba().int32(2, 5, 5))
        .build();

    assertThatThrownBy(() -> struct.setInt32(1, 1, 2))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Int32Type at position 1 is constant index: 1 value: 2 constant: 5");
  }

  @Test
  void setInt32Array_constant_value_same() {
    var struct = struct()
        .int32()
        .primitive().constant(5).lengthField(0).int32()
        .fromBytes(ba().int32(2, 5, 5))
        .build();

    struct.setInt32(1, 1, 5);

    assertThat(struct.getByteArray()).isEqualTo(ba().int32(2, 5, 5));
  }

  @Test
  void setInt32Array_set_length_field_without_adding_to_array() {
    var struct = struct()
        .int32()
        .int32Array(0)
        .build();

    struct.setInt32(0, 2);

    assertThat(struct.getByteArray()).isEqualTo(ba().int32(2, 0, 0));
  }
}
