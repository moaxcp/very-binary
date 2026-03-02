package com.github.moaxcp.verybinary.int8;

import com.github.moaxcp.verybinary.Int8ArrayType;
import com.github.moaxcp.verybinary.Int8Type;
import org.junit.jupiter.api.Test;

import static com.github.moaxcp.verybinary.Builders.struct;
import static com.github.moaxcp.verybinary.ByteArray.ba;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class SetInt8TypeTest {
  @Test
  void setWrapper() {
    var struct = struct()
        .int8()
        .build();

    ((Int8Type) struct.getType(0)).set(struct, (byte) 1);

    assertThat(struct.getByteArray()).isEqualTo(ba().int8(1));
  }

  @Test
  void setInt8() {
    var struct = struct()
        .int8()
        .build();

    struct.setInt8(0, (byte) 1);

    assertThat(struct.getByteArray()).isEqualTo(ba().int8(1));
  }

  @Test
  void setInt8_position_negative() {
    var struct = struct()
        .int8()
        .build();

    assertThatThrownBy(() -> struct.setInt8(-1, (byte) 1))
        .isInstanceOf(IndexOutOfBoundsException.class)
        .hasMessage("Index -1 out of bounds for length 1");
  }

  @Test
  void setInt8_position_greater_than_length() {
    var struct = struct()
        .int8()
        .build();

    assertThatThrownBy(() -> struct.setInt8(1, (byte) 1))
        .isInstanceOf(IndexOutOfBoundsException.class)
        .hasMessage("Index 1 out of bounds for length 1");
  }

  @Test
  void setInt8_not_allocated() {
    var struct = struct()
        .allocated()
        .int8()
        .build();

    assertThatThrownBy(() -> struct.setInt8(0, (byte) 1))
        .isInstanceOf(IndexOutOfBoundsException.class)
        .hasMessage("allocated: 0, index: 0, length: 1");
  }

  @Test
  void setInt8_constant() {
    var struct = struct()
        .primitive().constant((byte) 5).int8()
        .build();

    assertThatThrownBy(() -> struct.setInt8(0, (byte) 2))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Int8Type at position 0 is constant value: 2 constant: 5");
  }

  @Test
  void setArrayWrapper() {
    var struct = struct()
        .int8()
        .int8Array(0)
        .build();

    assertThatThrownBy(() -> ((Int8ArrayType) struct.getType(1)).set(struct, 0, Byte.valueOf((byte) 2)))
        .isInstanceOf(UnsupportedOperationException.class)
        .hasMessage("set(Pointer, long, Byte) not supported for Int8ArrayType. Use set(Pointer, long, byte) instead.");
  }

  @Test
  void setInt8Array() {
    var struct = struct()
        .int8()
        .int8Array(0)
        .int8()
        .fromBytes(ba().int8(1, 2, 3))
        .build();

    struct.setInt8(1, 0, (byte) 5);

    assertThat(struct.getInt8(0)).isEqualTo((byte) 1);
    assertThat(struct.getInt8(1, 0)).isEqualTo((byte) 5);
    assertThat(struct.getByteArray()).isEqualTo(ba().int8(1, 5, 3));
  }

  @Test
  void setInt8Array_negative() {
    var struct = struct()
        .int8()
        .int8Array(0)
        .int8()
        .fromBytes(ba().int8(1, 2, 3))
        .build();

    assertThatThrownBy(() -> struct.setInt8(1, -1, (byte) 5))
        .isInstanceOf(IndexOutOfBoundsException.class)
        .hasMessage("Int8ArrayType at position 1 index: -1 length: 1");
  }

  @Test
  void setInt8Array_greater_than_length() {
    var struct = struct()
        .int8()
        .int8Array(0)
        .int8()
        .fromBytes(ba().int8(1, 2, 3))
        .build();

    assertThatThrownBy(() -> struct.setInt8(1, 2, (byte) 5))
        .isInstanceOf(ArrayIndexOutOfBoundsException.class)
        .hasMessage("Int8ArrayType at position 1 index: 2 length: 1");

    assertThat(struct.getInt8(0)).isEqualTo((byte) 1);
    assertThat(struct.getInt8(1, 0)).isEqualTo((byte) 2);
    assertThat(struct.getByteArray()).isEqualTo(ba().int8(1, 2, 3));
  }

  @Test
  void setInt8Array_not_allocated() {
    var struct = struct()
        .allocated()
        .int8()
        .int8Array(0)
        .build();

    assertThatThrownBy(() -> struct.setInt8(1, 0, (byte) 2))
        .isInstanceOf(IndexOutOfBoundsException.class)
        .hasMessage("allocated: 0, index: 0, length: 1");
  }

  @Test
  void setInt8Array_index_0_not_array() {
    var struct = struct()
        .int8()
        .build();

    assertThatThrownBy(() -> struct.setInt8(0, 0, (byte) 2))
        .isInstanceOf(ClassCastException.class);
  }

  @Test
  void setInt8Array_constant_value_and_length() {
    var struct = struct()
        .primitive().constant(new byte[]{5, 5, 5, 5, 5}).int8()
        .build();

    assertThatThrownBy(() -> struct.setInt8(0, 3, (byte) 2))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Int8ArrayType at position 0 is constant index: 3 value: 2 constant: [5, 5, 5, 5, 5]");
  }

  @Test
  void setInt8Array_constant_value() {
    var struct = struct()
        .int8()
        .primitive().constant(new byte[]{5, 5, 5, 5, 5}).int8()
        .fromBytes(ba().int8(2, 5, 5))
        .build();

    assertThatThrownBy(() -> struct.setInt8(1, 1, 2))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Int8ArrayType at position 1 is constant index: 1 value: 2 constant: [5, 5, 5, 5, 5]");
  }

  @Test
  void setInt8Array_constant_value_same() {
    var struct = struct()
        .int8()
        .primitive().constant(new byte[]{5, 5, 5, 5, 5}).int8()
        .fromBytes(ba().int8(2, 5, 5))
        .build();

    struct.setInt8(1, 1, (byte) 5);

    assertThat(struct.getByteArray()).isEqualTo(ba().int8(2, 5, 5));
  }

  @Test
  void setInt8Array_set_length_field_adds_to_array() {
    var struct = struct()
        .int8()
        .int8Array(0)
        .build();

    struct.setInt8(0, (byte) 2);

    assertThat(struct.getArrayLength(1)).isEqualTo(2);
    assertThat(struct.getByteArray()).isEqualTo(ba().int8(2).int8(0).int8(0));
  }

  @Test
  void setInt8Array_set_length_field_removes_from_array() {
    var struct = struct()
        .int8()
        .int8Array(0)
        .fromBytes(ba().int8(3).int8(1).int8(2).int8(3))
        .build();

    struct.setInt8(0, (byte) 2);

    assertThat(struct.getArrayLength(1)).isEqualTo(2);
    assertThat(struct.getByteArray()).isEqualTo(ba().int8(2).int8(1).int8(2));
  }
}
