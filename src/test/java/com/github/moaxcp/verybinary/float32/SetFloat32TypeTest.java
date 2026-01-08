package com.github.moaxcp.verybinary.float32;

import com.github.moaxcp.verybinary.Float32Type;
import org.junit.jupiter.api.Test;

import static com.github.moaxcp.verybinary.Builders.struct;
import static com.github.moaxcp.verybinary.ByteArray.ba;
import static com.github.moaxcp.verybinary.Expression.constant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class SetFloat32TypeTest {
  @Test
  void setWrapper() {
    var struct = struct()
        .float32()
        .build();

    assertThatThrownBy(() -> ((Float32Type) struct.getType(0)).set(struct, Float.valueOf(2.0f)))
        .isInstanceOf(UnsupportedOperationException.class)
        .hasMessage("set(Pointer, Float) not supported for Float32Type. Use set(Pointer, float) instead.");
  }

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
  void setFloat32_not_allocated() {
    var struct = struct()
        .allocated()
        .float32()
        .build();

    assertThatThrownBy(() -> struct.setFloat32(0, 2.0f))
        .isInstanceOf(ArrayIndexOutOfBoundsException.class)
        .hasMessage("Index 0 out of bounds for length 0");
  }

  @Test
  void setFloat32_constant() {
    var struct = struct()
        .primitive().constant(3.0f).float32()
        .build();

    assertThatThrownBy(() -> struct.setFloat32(0, 2.0f))
        .isInstanceOf(UnsupportedOperationException.class)
        .hasMessage("Float32Type at position 0 is constant index: 0 value: 2.0 constant: 3.0");
  }

  @Test
  void setArrayWrapper() {
    var struct = struct()
        .float32()
        .float32Array(0)
        .build();

    assertThatThrownBy(() -> ((Float32Type) struct.getType(1)).set(struct, 0, Float.valueOf(2.0f)))
        .isInstanceOf(UnsupportedOperationException.class)
        .hasMessage("set(Pointer, long, Float) not supported for Float32Type. Use set(Pointer, long, float) instead.");
  }

  @Test
  void setFloat32Array() {
    var struct = struct()
        .float32()
        .float32Array(0)
        .float32()
        .fromBytes(ba().float32(1, 4, 3))
        .build();

    struct.setFloat32(1, 0, 2.0f);

    assertThat(struct.getFloat32(0)).isEqualTo(1.0f);
    assertThat(struct.getFloat32(1, 0)).isEqualTo(2.0f);
    assertThat(struct.getByteArray()).isEqualTo(ba().float32(1, 2, 3));
  }

  @Test
  void setFloat32Array_negative() {
    var struct = struct()
        .float32()
        .float32Array(0)
        .float32()
        .fromBytes(ba().float32(1, 4))
        .build();

    assertThatThrownBy(() -> struct.setFloat32(1, -1, 2.0f))
        .isInstanceOf(IndexOutOfBoundsException.class)
        .hasMessage("Float32Type at position 1 index: -1 length: 1");
  }

  @Test
  void setFloat32Array_greater_than_length() {
    var struct = struct()
        .float32()
        .float32Array(0)
        .float32()
        .fromBytes(ba().float32(1, 4, 2))
        .build();

    assertThatThrownBy(() -> struct.setFloat32(1, 2, 2.0f))
        .isInstanceOf(ArrayIndexOutOfBoundsException.class)
        .hasMessage("Float32Type at position 1 index: 2 length: 1");
  }

  @Test
  void setFloat32Array_not_allocated() {
    var struct = struct()
        .allocated()
        .float32()
        .float32Array(0)
        .build();

    assertThatThrownBy(() -> struct.setFloat32(1, 0, 2.0f))
        .isInstanceOf(IndexOutOfBoundsException.class)
        .hasMessage("allocated: 0, index: 0, length: 4");
  }

  @Test
  void setFloat32Array_index_0_not_array() {
    var struct = struct()
        .float32()
        .build();

    struct.setFloat32(0, 0, 2.0f);

    assertThat(struct.getByteArray()).isEqualTo(ba().float32(2));
  }

  @Test
  void setFloat32Array_index_1_not_array() {
    var struct = struct()
        .float32()
        .build();

    assertThatThrownBy(() -> struct.setFloat32(0, 1, 2.0f))
        .isInstanceOf(ArrayIndexOutOfBoundsException.class)
        .hasMessage("Float32Type at position 0 index: 1 length: 1");
  }

  @Test
  void setFloat32Array_constant_value_and_length() {
    var struct = struct()
        .primitive().constant(3.0f).lengthExpression(constant(3)).float32()
        .build();

    assertThatThrownBy(() -> struct.setFloat32(0, 2, 2.0f))
        .isInstanceOf(UnsupportedOperationException.class)
        .hasMessage("Float32Type at position 0 is constant index: 2 value: 2.0 constant: 3.0");
  }

  @Test
  void setFloat32Array_constant_value() {
    var struct = struct()
        .float32()
        .primitive().constant(3.0f).lengthField(0).float32()
        .fromBytes(ba().float32(2).float32(3).float32(3))
        .build();

    struct.setFloat32(1, 1, 2.0f);

    assertThat(struct.getByteArray()).isEqualTo(ba().float32(2).float32(3).float32(2));
  }

  @Test
  void setFloat32Array_constant_value_same() {
    var struct = struct()
        .float32()
        .primitive().constant(3.0f).lengthField(0).float32()
        .fromBytes(ba().float32(2, 4, 3))
        .build();

    struct.setFloat32(1, 1, 3.0f);

    assertThat(struct.getByteArray()).isEqualTo(ba().float32(2, 4, 3));
  }

  @Test
  void setFloat32Array_set_length_field_without_adding_to_array() {
    var struct = struct()
        .float32()
        .float32Array(0)
        .build();

    struct.setFloat32(0, 2.0f);

    assertThat(struct.getByteArray()).isEqualTo(ba().float32(2).float32(0, 0));
  }
}
