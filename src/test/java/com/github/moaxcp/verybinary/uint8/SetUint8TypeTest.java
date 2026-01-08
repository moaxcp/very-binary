package com.github.moaxcp.verybinary.uint8;

import org.junit.jupiter.api.Test;

import static com.github.moaxcp.verybinary.Builders.struct;
import static com.github.moaxcp.verybinary.ByteArray.ba;
import static com.github.moaxcp.verybinary.Expression.constant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class SetUint8TypeTest {

  @Test
  void setWrapper() {
    var struct = struct()
        .uint8()
        .build();

    assertThatThrownBy(() -> ((com.github.moaxcp.verybinary.Uint8Type) struct.getType(0)).set(struct, Short.valueOf((short) 2)))
        .isInstanceOf(UnsupportedOperationException.class)
        .hasMessage("set(Pointer, Short) not supported for Uint8Type. Use set(Pointer, short) instead.");
  }

  @Test
  void setUint8() {
    var struct = struct()
        .uint8()
        .build();

    struct.setUint8(0, (byte) 2);

    assertThat(struct.getByteArray()).isEqualTo(ba().uint8(2));
  }

  @Test
  void setUint8_position_negative() {
    var struct = struct()
        .uint8()
        .build();

    assertThatThrownBy(() -> struct.setUint8(-1, (byte) 2))
        .isInstanceOf(IndexOutOfBoundsException.class)
        .hasMessage("Index -1 out of bounds for length 1");
  }

  @Test
  void setUint8_position_greater_than_length() {
    var struct = struct()
        .uint8()
        .build();

    assertThatThrownBy(() -> struct.setUint8(2, (byte) 2))
        .isInstanceOf(IndexOutOfBoundsException.class)
        .hasMessage("Index 2 out of bounds for length 1");
  }

  @Test
  void setUint8_not_allocated() {
    var struct = struct()
        .allocated()
        .uint8()
        .build();

    assertThatThrownBy(() -> struct.setUint8(0, (byte) 2))
        .isInstanceOf(ArrayIndexOutOfBoundsException.class)
        .hasMessage("Index 0 out of bounds for length 0");
  }

  @Test
  void setUint8_constant() {
    var struct = struct()
        .primitive().constant((short) 5).uint8()
        .build();

    assertThatThrownBy(() -> struct.setUint8(0, (short) 2))
        .isInstanceOf(UnsupportedOperationException.class)
        .hasMessage("Uint8Type at position 0 is constant index: 0 value: 2 constant: 5");
  }

  @Test
  void setArrayWrapper() {
    var struct = struct()
        .uint8()
        .uint8Array(0)
        .build();

    assertThatThrownBy(() -> ((com.github.moaxcp.verybinary.Uint8Type) struct.getType(1)).set(struct, 0, Short.valueOf((short) 2)))
        .isInstanceOf(UnsupportedOperationException.class)
        .hasMessage("set(Pointer, long, Short) not supported for Uint8Type. Use set(Pointer, long, short) instead.");
  }

  @Test
  void setUint8Array() {
    var struct = struct()
        .uint8()
        .uint8Array(0)
        .uint8()
        .fromBytes(ba().uint8(1, 2, 3))
        .build();

    struct.setUint8(1, 0, (byte) 5);

    assertThat(struct.getUint8(0)).isEqualTo((byte) 1);
    assertThat(struct.getUint8(1, 0)).isEqualTo((byte) 5);
    assertThat(struct.getByteArray()).isEqualTo(ba().uint8(1, 5, 3));
  }

  @Test
  void setUint8Array_negative() {
    var struct = struct()
        .uint8()
        .uint8Array(0)
        .uint8()
        .fromBytes(ba().uint8(1, 2, 3))
        .build();

    assertThatThrownBy(() -> struct.setUint8(1, -1, (byte) 5))
        .isInstanceOf(IndexOutOfBoundsException.class)
        .hasMessage("Uint8Type at position 1 index: -1 length: 1");
  }

  @Test
  void setUint8Array_greater_than_length() {
    var struct = struct()
        .uint8()
        .uint8Array(0)
        .uint8()
        .fromBytes(ba().uint8(1, 2, 3))
        .build();

    assertThatThrownBy(() -> struct.setUint8(1, 2, (byte) 5))
        .isInstanceOf(ArrayIndexOutOfBoundsException.class)
        .hasMessage("Uint8Type at position 1 index: 2 length: 1");

    assertThat(struct.getUint8(0)).isEqualTo((byte) 1);
    assertThat(struct.getUint8(1, 0)).isEqualTo((byte) 2);
    assertThat(struct.getByteArray()).isEqualTo(ba().uint8(1, 2, 3));
  }

  @Test
  void setUint8Array_not_allocated() {
    var struct = struct()
        .allocated()
        .uint8()
        .uint8Array(0)
        .build();

    assertThatThrownBy(() -> struct.setUint8(1, 0, (byte) 2))
        .isInstanceOf(IndexOutOfBoundsException.class)
        .hasMessage("allocated: 0, index: 0, length: 1");
  }

  @Test
  void setUint8Array_index_0_not_array() {
    var struct = struct()
        .uint8()
        .build();

    struct.setUint8(0, 0, (byte) 2);

    assertThat(struct.getByteArray()).isEqualTo(ba().uint8(2));
  }

  @Test
  void setUint8Array_index_1_not_array() {
    var struct = struct()
        .uint8()
        .build();

    assertThatThrownBy(() -> struct.setUint8(0, 1, (byte) 2))
        .isInstanceOf(ArrayIndexOutOfBoundsException.class)
        .hasMessage("Uint8Type at position 0 index: 1 length: 1");
  }

  @Test
  void setUint8Array_constant_value_and_length() {
    var struct = struct()
        .primitive().constant((short) 5).lengthExpression(constant(5)).uint8()
        .build();

    assertThatThrownBy(() -> struct.setUint8(0, 3, (short) 2))
        .isInstanceOf(UnsupportedOperationException.class)
        .hasMessage("Uint8Type at position 0 is constant index: 3 value: 2 constant: 5");
  }

  @Test
  void setUint8Array_constant_value() {
    var struct = struct()
        .uint8()
        .primitive().constant((short) 5).lengthField(0).uint8()
        .fromBytes(ba().uint8(2, 5, 5))
        .build();

    struct.setUint8(1, 1, 2);

    assertThat(struct.getByteArray()).isEqualTo(ba().uint8(2, 5, 2));
  }

  @Test
  void setUint8Array_constant_value_same() {
    var struct = struct()
        .uint8()
        .primitive().constant((short) 5).lengthField(0).uint8()
        .fromBytes(ba().uint8(2, 5, 5))
        .build();

    struct.setUint8(1, 1, (short) 5);

    assertThat(struct.getByteArray()).isEqualTo(ba().uint8(2, 5, 5));
  }

  @Test
  void setUint8Array_set_length_field_without_adding_to_array() {
    var struct = struct()
        .uint8()
        .uint8Array(0)
        .build();

    struct.setUint8(0, 2);

    assertThat(struct.getByteArray()).isEqualTo(ba().uint8(2, 0, 0));
  }
}
