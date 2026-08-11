package com.github.moaxcp.verybinary.float32;

import org.junit.jupiter.api.Test;

import static com.github.moaxcp.verybinary.Builders.struct;
import static com.github.moaxcp.verybinary.ByteArray.ba;
import static com.github.moaxcp.verybinary.list.Float32List.toFloat32List;
import static com.github.moaxcp.verybinary.math.Int8Value.int8Value;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class SetFloat32TypeTest {
  //do not test wrapper methods. SetBoolTypeTest covers the only implementations in PrimitiveType.

  @Test
  void setFloat32() {
    var struct = struct()
        .float32()
        .build();

    struct.setFloat32(0, 2.0f);

    assertThat(struct.getByteArray()).isEqualTo(ba().float32(2));
  }

  @Test
  void setFloat32_position_negative() {
    var struct = struct()
        .float32()
        .build();

    assertThatThrownBy(() -> struct.setFloat32(-1, 2.0f))
        .isInstanceOf(IndexOutOfBoundsException.class)
        .hasMessage("Index -1 out of bounds for length 1");
  }

  @Test
  void setFloat32_position_greater_than_length() {
    var struct = struct()
        .float32()
        .build();

    assertThatThrownBy(() -> struct.setFloat32(2, 2.0f))
        .isInstanceOf(IndexOutOfBoundsException.class)
        .hasMessage("Index 2 out of bounds for length 1");
  }

  @Test
  void setFloat32_constant() {
    var struct = struct()
        .basic().constant(3.0f).float32()
        .build();

    assertThatThrownBy(() -> struct.setFloat32(0, 2.0f))
        .isInstanceOf(IllegalStateException.class)
        .hasMessage("Float32Type at position 0 is constant value");
  }

  @Test
  void setFloat32_index() {
    var struct = struct()
        .float32()
        .float32List(0)
        .float32()
        .fromBytes(ba().float32(1, 4, 3))
        .build();

    struct.setFloat32(1, 0, 2.0f);

    assertThat(struct.getFloat32(0)).isEqualTo(1.0f);
    assertThat(struct.getFloat32(1, 0)).isEqualTo(2.0f);
    assertThat(struct.getByteArray()).isEqualTo(ba().float32(1, 2, 3));
  }

  @Test
  void setFloat32_index_negative() {
    var struct = struct()
        .float32()
        .float32List(0)
        .float32()
        .build();

    assertThatThrownBy(() -> struct.setFloat32(1, -1, 2.0f))
        .isInstanceOf(IndexOutOfBoundsException.class)
        .hasMessage("Float32ListType at position 1 index: -1 length: 0");
  }

  @Test
  void setFloat32_index_greater_than_length() {
    var struct = struct()
        .float32()
        .float32List(0)
        .float32()
        .fromBytes(ba().float32(1, 4, 2))
        .build();

    assertThatThrownBy(() -> struct.setFloat32(1, 2, 2.0f))
        .isInstanceOf(ArrayIndexOutOfBoundsException.class)
        .hasMessage("Float32ListType at position 1 index: 2 length: 1");
  }

  @Test
  void setFloat32_index_0_not_array() {
    var struct = struct()
        .float32()
        .build();

    assertThatThrownBy(() -> struct.setFloat32(0, 0, 2.0f))
        .isInstanceOf(ClassCastException.class);
  }

  @Test
  void setFloat32_index_constant_value_value_bad_value() {
    var struct = struct()
        .int8()
        .basic().constant(toFloat32List(new float[]{3, 3, 3})).float32()
        .build();

    assertThatThrownBy(() -> struct.setFloat32(1, 1, 2))
        .isInstanceOf(IllegalStateException.class)
        .hasMessage("Float32ListType at position 1 is constant value");
  }

  @Test
  void setFloat32Array_constant_value_and_length() {
    var struct = struct()
        .basic().constant(toFloat32List(new float[]{3, 3, 3})).float32()
        .build();

    assertThatThrownBy(() -> struct.setFloat32(0, 2, 2.0f))
        .isInstanceOf(IllegalStateException.class)
        .hasMessage("Float32ListType at position 0 is constant value");
  }

  @Test
  void setFloat32_array() {
    var struct = struct()
        .int8()
        .float32List(0)
        .build();

    struct.setInt8(0, 5);
    struct.setFloat32(1, 5.5f, 5.5f, 5.5f, 5.5f, 5.5f);

    assertThat(struct.getByteArray()).isEqualTo(ba().int8(5).float32(5.5f, 5.5f, 5.5f, 5.5f, 5.5f));
  }

  @Test
  void setFloat32_array_length_constant() {
    var struct = struct()
        .float32List(int8Value(5))
        .build();

    struct.setFloat32(0, 5.5f, 5.5f, 5.5f, 5.5f, 5.5f);

    assertThat(struct.getByteArray()).isEqualTo(ba().float32(5.5f, 5.5f, 5.5f, 5.5f, 5.5f));
  }

  @Test
  void setFloat32_array_length_field_constant() {
    var struct = struct()
        .basic().constant(5).int8()
        .float32List(0)
        .build();

    struct.setFloat32(1, 5.5f, 5.5f, 5.5f, 5.5f, 5.5f);

    assertThat(struct.getByteArray()).isEqualTo(ba().int8(5).float32(5.5f, 5.5f, 5.5f, 5.5f, 5.5f));
  }

  @Test
  void setFloat32_index_array() {
    var struct = struct()
        .int8()
        .float32List(0)
        .build();

    struct.setInt8(0, 5);
    struct.setFloat32(1, 2, 3.5f, 4.5f);

    assertThat(struct.getByteArray()).isEqualTo(ba().int8(5).float32(0, 0, 3.5f, 4.5f, 0));
  }

  @Test
  void setFloat32_index_array_length_constant() {
    var struct = struct()
        .float32List(int8Value(5))
        .build();

    struct.setFloat32(0, 2, 3.5f, 4.5f);

    assertThat(struct.getByteArray()).isEqualTo(ba().float32(0, 0, 3.5f, 4.5f, 0));
  }

  @Test
  void setFloat32_index_array_length_field_constant() {
    var struct = struct()
        .basic().constant((byte) 5).int8()
        .float32List(0)
        .build();

    struct.setFloat32(1, 2, 3.5f, 4.5f);

    assertThat(struct.getByteArray()).isEqualTo(ba().int8(5).float32(0, 0, 3.5f, 4.5f, 0));
  }

  @Test
  void setFloat32_index_array_with_index_negative() {
    var struct = struct()
        .float32List(int8Value(5))
        .build();

    assertThatThrownBy(() -> struct.setFloat32(0, -1, 2.0f, 3.0f))
        .isInstanceOf(IndexOutOfBoundsException.class)
        .hasMessage("Float32ListType at position 0 length: 5 start: -1 end: 1");
  }

  @Test
  void setFloat32_index_array_with_index_greater_than_length() {
    var struct = struct()
        .float32List(int8Value(5))
        .build();

    assertThatThrownBy(() -> struct.setFloat32(0, 5, 2.0f, 3.0f))
        .isInstanceOf(IndexOutOfBoundsException.class)
        .hasMessage("Float32ListType at position 0 length: 5 start: 5 end: 7");
  }

  @Test
  void setFloat32_index_array_constant() {
    var struct = struct()
        .basic().constant(toFloat32List(new float[]{5, 5, 5, 5, 5})).float32()
        .build();

    assertThatThrownBy(() -> struct.setFloat32(0, 2, 3.5f, 4.5f))
        .isInstanceOf(IllegalStateException.class)
        .hasMessage("Float32ListType at position 0 is constant value");
  }

  @Test
  void setFloat32Array_set_length_field_extends_array() {
    var struct = struct()
        .int8()
        .float32List(0)
        .build();

    struct.setInt8(0, 2);

    assertThat(struct.getByteArray()).isEqualTo(ba().int8(2).float32(0, 0));
  }
}
