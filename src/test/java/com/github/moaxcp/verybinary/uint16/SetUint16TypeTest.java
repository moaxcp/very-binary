package com.github.moaxcp.verybinary.uint16;

import com.github.moaxcp.verybinary.Uint16ArrayType;
import com.github.moaxcp.verybinary.Uint16Type;
import org.junit.jupiter.api.Test;

import static com.github.moaxcp.verybinary.Builders.struct;
import static com.github.moaxcp.verybinary.ByteArray.ba;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class SetUint16TypeTest {
  @Test
  void setWrapper() {
    var struct = struct()
        .uint16()
        .build();

    assertThatThrownBy(() -> ((Uint16Type) struct.getType(0)).set(struct, Integer.valueOf(2)))
        .isInstanceOf(UnsupportedOperationException.class)
        .hasMessage("set(Pointer, Integer) not supported for Uint16Type. Use set(Pointer, int) instead.");
  }

  @Test
  void setUint16() {
    var struct = struct()
        .uint16()
        .build();

    struct.setUint16(0, 2);

    assertThat(struct.getByteArray()).isEqualTo(ba().uint16(2));
  }

  @Test
  void setUint16_position_negative() {
    var struct = struct()
        .uint16()
        .build();

    assertThatThrownBy(() -> struct.setUint16(-1, 2))
        .isInstanceOf(IndexOutOfBoundsException.class)
        .hasMessage("Index -1 out of bounds for length 1");
  }

  @Test
  void setUint16_position_greater_than_length() {
    var struct = struct()
        .uint16()
        .build();

    assertThatThrownBy(() -> struct.setUint16(2, 2))
        .isInstanceOf(IndexOutOfBoundsException.class)
        .hasMessage("Index 2 out of bounds for length 1");
  }

  @Test
  void setUint16_not_allocated() {
    var struct = struct()
        .allocated()
        .uint16()
        .build();

    assertThatThrownBy(() -> struct.setUint16(0, 2))
        .isInstanceOf(ArrayIndexOutOfBoundsException.class)
        .hasMessage("Index 0 out of bounds for length 0");
  }

  @Test
  void setUint16_constant() {
    var struct = struct()
        .primitive().constant(5).uint16()
        .build();

    assertThatThrownBy(() -> struct.setUint16(0, 2))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Uint16Type at position 0 is constant value: 2 constant: 5");
  }

  @Test
  void setArrayWrapper() {
    var struct = struct()
        .uint16()
        .uint16Array(0)
        .build();

    assertThatThrownBy(() -> ((Uint16ArrayType) struct.getType(1)).set(struct, 0, Integer.valueOf(2)))
        .isInstanceOf(UnsupportedOperationException.class)
        .hasMessage("set(Pointer, long, Integer) not supported for Uint16ArrayType. Use set(Pointer, long, int) instead.");
  }

  @Test
  void setUint16Array() {
    var struct = struct()
        .uint16()
        .uint16Array(0)
        .uint16()
        .fromBytes(ba().uint16(1, 2, 3))
        .build();

    struct.setUint16(1, 0, 5);

    assertThat(struct.getUint16(0)).isEqualTo(1);
    assertThat(struct.getUint16(1, 0)).isEqualTo(5);
    assertThat(struct.getByteArray()).isEqualTo(ba().uint16(1, 5, 3));
  }

  @Test
  void setUint16Array_negative() {
    var struct = struct()
        .uint16()
        .uint16Array(0)
        .uint16()
        .fromBytes(ba().uint16(1, 2, 3))
        .build();

    assertThatThrownBy(() -> struct.setUint16(1, -1, 5))
        .isInstanceOf(IndexOutOfBoundsException.class)
        .hasMessage("Uint16ArrayType at position 1 index: -1 length: 1");
  }

  @Test
  void setUint16Array_greater_than_length() {
    var struct = struct()
        .uint16()
        .uint16Array(0)
        .uint16()
        .fromBytes(ba().uint16(1, 2, 3))
        .build();

    assertThatThrownBy(() -> struct.setUint16(1, 2, 5))
        .isInstanceOf(ArrayIndexOutOfBoundsException.class)
        .hasMessage("Uint16ArrayType at position 1 index: 2 length: 1");

    assertThat(struct.getUint16(0)).isEqualTo(1);
    assertThat(struct.getUint16(1, 0)).isEqualTo(2);
    assertThat(struct.getByteArray()).isEqualTo(ba().uint16(1, 2, 3));
  }

  @Test
  void setUint16Array_not_allocated() {
    var struct = struct()
        .allocated()
        .uint16()
        .uint16Array(0)
        .build();

    assertThatThrownBy(() -> struct.setUint16(1, 0, 2))
        .isInstanceOf(IndexOutOfBoundsException.class)
        .hasMessage("allocated: 0, index: 0, length: 2");
  }

  @Test
  void setUint16Array_index_0_not_array() {
    var struct = struct()
        .uint16()
        .build();

    assertThatThrownBy(() -> struct.setUint16(0, 0, 2))
        .isInstanceOf(ClassCastException.class);
  }

  @Test
  void setUint16Array_constant_value_and_length() {
    var struct = struct()
        .primitive().constant(new int[]{5, 5, 5, 5, 5}).uint16()
        .build();

    assertThatThrownBy(() -> struct.setUint16(0, 3, 2))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Uint16ArrayType at position 0 is constant index: 3 value: 2 constant: 5");
  }

  @Test
  void setUint16Array_constant_value() {
    var struct = struct()
        .uint16()
        .primitive().constant(new int[]{5, 5, 5, 5, 5}).uint16()
        .fromBytes(ba().uint16(2, 5, 5))
        .build();

    assertThatThrownBy(() -> struct.setUint16(1, 1, 2))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Uint16ArrayType at position 1 is constant index: 1 value: 2 constant: 5");
  }

  @Test
  void setUint16Array_constant_value_same() {
    var struct = struct()
        .uint16()
        .primitive().constant(new int[]{5, 5, 5, 5, 5}).uint16()
        .fromBytes(ba().uint16(2, 5, 5))
        .build();

    struct.setUint16(1, 1, 5);

    assertThat(struct.getByteArray()).isEqualTo(ba().uint16(2, 5, 5));
  }

  @Test
  void setUint16Array_set_length_field_without_adding_to_array() {
    var struct = struct()
        .uint16()
        .uint16Array(0)
        .build();

    struct.setUint16(0, 2);

    assertThat(struct.getByteArray()).isEqualTo(ba().uint16(2, 0, 0));
  }
}
