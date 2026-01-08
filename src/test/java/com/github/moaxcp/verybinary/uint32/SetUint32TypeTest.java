package com.github.moaxcp.verybinary.uint32;

import com.github.moaxcp.verybinary.Uint32Type;
import org.junit.jupiter.api.Test;

import static com.github.moaxcp.verybinary.Builders.struct;
import static com.github.moaxcp.verybinary.ByteArray.ba;
import static com.github.moaxcp.verybinary.Expression.constant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class SetUint32TypeTest {

  @Test
  void setWrapper() {
    var struct = struct()
        .uint32()
        .build();

    assertThatThrownBy(() -> ((Uint32Type) struct.getType(0)).set(struct, Long.valueOf(2)))
        .isInstanceOf(UnsupportedOperationException.class)
        .hasMessage("set(Pointer, Long) not supported for Uint32Type. Use set(Pointer, long) instead.");
  }

  @Test
  void setUint32() {
    var struct = struct()
        .uint32()
        .build();

    struct.setUint32(0, 2L);

    assertThat(struct.getByteArray()).isEqualTo(ba().uint32(2));
  }

  @Test
  void setUint32_position_negative() {
    var struct = struct()
        .uint32()
        .build();

    assertThatThrownBy(() -> struct.setUint32(-1, 2L))
        .isInstanceOf(IndexOutOfBoundsException.class)
        .hasMessage("Index -1 out of bounds for length 1");
  }

  @Test
  void setUint32_position_greater_than_length() {
    var struct = struct()
        .uint32()
        .build();

    assertThatThrownBy(() -> struct.setUint32(2, 2L))
        .isInstanceOf(IndexOutOfBoundsException.class)
        .hasMessage("Index 2 out of bounds for length 1");
  }

  @Test
  void setUint32_not_allocated() {
    var struct = struct()
        .allocated()
        .uint32()
        .build();

    assertThatThrownBy(() -> struct.setUint32(0, 2L))
        .isInstanceOf(ArrayIndexOutOfBoundsException.class)
        .hasMessage("Index 0 out of bounds for length 0");
  }

  @Test
  void setUint32_constant() {
    var struct = struct()
        .primitive().constant(5L).uint32()
        .build();

    assertThatThrownBy(() -> struct.setUint32(0, 2L))
        .isInstanceOf(UnsupportedOperationException.class)
        .hasMessage("Uint32Type at position 0 is constant index: 0 value: 2 constant: 5");
  }

  @Test
  void setArrayWrapper() {
    var struct = struct()
        .uint32()
        .uint32Array(0)
        .build();

    assertThatThrownBy(() -> ((Uint32Type) struct.getType(1)).set(struct, 0, Long.valueOf(2)))
        .isInstanceOf(UnsupportedOperationException.class)
        .hasMessage("set(Pointer, long, Long) not supported for Uint32Type. Use set(Pointer, long, long) instead.");
  }

  @Test
  void setUint32Array() {
    var struct = struct()
        .uint32()
        .uint32Array(0)
        .uint32()
        .fromBytes(ba().uint32(1, 2, 3))
        .build();

    struct.setUint32(1, 0, 5L);

    assertThat(struct.getUint32(0)).isEqualTo(1L);
    assertThat(struct.getUint32(1, 0)).isEqualTo(5L);
    assertThat(struct.getByteArray()).isEqualTo(ba().uint32(1, 5, 3));
  }

  @Test
  void setUint32Array_negative() {
    var struct = struct()
        .uint32()
        .uint32Array(0)
        .uint32()
        .fromBytes(ba().uint32(1, 2, 3))
        .build();

    assertThatThrownBy(() -> struct.setUint32(1, -1, 5L))
        .isInstanceOf(IndexOutOfBoundsException.class)
        .hasMessage("Uint32Type at position 1 index: -1 length: 1");
  }

  @Test
  void setUint32Array_greater_than_length() {
    var struct = struct()
        .uint32()
        .uint32Array(0)
        .uint32()
        .fromBytes(ba().uint32(1, 2, 3))
        .build();

    assertThatThrownBy(() -> struct.setUint32(1, 2, 5L))
        .isInstanceOf(ArrayIndexOutOfBoundsException.class)
        .hasMessage("Uint32Type at position 1 index: 2 length: 1");

    assertThat(struct.getUint32(0)).isEqualTo(1L);
    assertThat(struct.getUint32(1, 0)).isEqualTo(2L);
    assertThat(struct.getByteArray()).isEqualTo(ba().uint32(1, 2, 3));
  }

  @Test
  void setUint32Array_not_allocated() {
    var struct = struct()
        .allocated()
        .uint32()
        .uint32Array(0)
        .build();

    assertThatThrownBy(() -> struct.setUint32(1, 0, 2L))
        .isInstanceOf(IndexOutOfBoundsException.class)
        .hasMessage("allocated: 0, index: 0, length: 4");
  }

  @Test
  void setUint32Array_index_0_not_array() {
    var struct = struct()
        .uint32()
        .build();

    struct.setUint32(0, 0, 2L);

    assertThat(struct.getByteArray()).isEqualTo(ba().uint32(2));
  }

  @Test
  void setUint32Array_index_1_not_array() {
    var struct = struct()
        .uint32()
        .build();

    assertThatThrownBy(() -> struct.setUint32(0, 1, 2L))
        .isInstanceOf(ArrayIndexOutOfBoundsException.class)
        .hasMessage("Uint32Type at position 0 index: 1 length: 1");
  }

  @Test
  void setUint32Array_constant_value_and_length() {
    var struct = struct()
        .primitive().constant(5L).lengthExpression(constant(5)).uint32()
        .build();

    assertThatThrownBy(() -> struct.setUint32(0, 3, 2L))
        .isInstanceOf(UnsupportedOperationException.class)
        .hasMessage("Uint32Type at position 0 is constant index: 3 value: 2 constant: 5");
  }

  @Test
  void setUint32Array_constant_value() {
    var struct = struct()
        .uint32()
        .primitive().constant(5L).lengthField(0).uint32()
        .fromBytes(ba().uint32(2, 5, 5))
        .build();

    struct.setUint32(1, 1, 2);

    assertThat(struct.getByteArray()).isEqualTo(ba().uint32(2, 5, 2));
  }

  @Test
  void setUint32Array_constant_value_same() {
    var struct = struct()
        .uint32()
        .primitive().constant(5L).lengthField(0).uint32()
        .fromBytes(ba().uint32(2, 5, 5))
        .build();

    struct.setUint32(1, 1, 5L);

    assertThat(struct.getByteArray()).isEqualTo(ba().uint32(2, 5, 5));
  }

  @Test
  void setUint32Array_set_length_field_without_adding_to_array() {
    var struct = struct()
        .uint32()
        .uint32Array(0)
        .build();

    struct.setUint32(0, 2);

    assertThat(struct.getByteArray()).isEqualTo(ba().uint32(2, 0, 0));
  }
}
