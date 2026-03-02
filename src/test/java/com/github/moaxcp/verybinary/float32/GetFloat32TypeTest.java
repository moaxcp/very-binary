package com.github.moaxcp.verybinary.float32;

import org.junit.jupiter.api.Test;

import static com.github.moaxcp.verybinary.Builders.struct;
import static com.github.moaxcp.verybinary.ByteArray.ba;
import static com.github.moaxcp.verybinary.Expression.constant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class GetFloat32TypeTest {
  //do not test wrapper methods. GetBoolTypeTest covers the only implementations in PrimitiveType.

  @Test
  void getFloat32() {
    var struct = struct()
        .float32()
        .build();

    struct.setFloat32(0, 2.0f);

    assertThat(struct.getFloat32(0)).isEqualTo(2.0f);
  }

  @Test
  void getFloat32_position_negative() {
    var struct = struct()
        .float32()
        .build();

    assertThatThrownBy(() -> struct.getFloat32(-1))
        .isInstanceOf(IndexOutOfBoundsException.class)
        .hasMessage("Index -1 out of bounds for length 1");
  }

  @Test
  void getFloat32_position_greater_than_length() {
    var struct = struct()
        .float32()
        .build();

    assertThatThrownBy(() -> struct.getFloat32(2))
        .isInstanceOf(IndexOutOfBoundsException.class)
        .hasMessage("Index 2 out of bounds for length 1");
  }

  @Test
  void getFloat32Allocated() {
    var struct = struct()
        .float32()
        .build();

    assertThat(struct.getFloat32(0)).isEqualTo(0.0f);
    assertThat(struct.getByteArray()).isEqualTo(ba().float32(0));
  }

  @Test
  void getFloat32NotAllocated() {
    var struct = struct()
        .allocated()
        .float32()
        .build();

    assertThatThrownBy(() -> struct.getFloat32(0))
        .isInstanceOf(IndexOutOfBoundsException.class)
        .hasMessage("allocated: 0, index: 0, length: 4");
  }

  @Test
  void getFloat32_constant() {
    var struct = struct()
        .primitive().constant(3.0f).float32()
        .build();

    assertThat(struct.getFloat32(0)).isEqualTo(3.0f);
  }

  @Test
  void getFloat32_index_0_not_array() {
    var struct = struct()
        .float32()
        .fromBytes(ba().float32(0))
        .build();

    assertThatThrownBy(() -> struct.getFloat32(0, 0))
        .isInstanceOf(ClassCastException.class);
  }

  @Test
  void getFloat32_index() {
    var struct = struct()
        .float32()
        .float32Array(0)
        .fromBytes(ba().float32(2, 4, 3))
        .build();

    assertThat(struct.getFloat32(1, 0)).isEqualTo(4.0f);
    assertThat(struct.getFloat32(1, 1)).isEqualTo(3.0f);
  }

  @Test
  void getFloat32_index_negative() {
    var struct = struct()
        .float32()
        .float32Array(0)
        .build();

    assertThatThrownBy(() -> struct.getFloat32(1, -1))
        .isInstanceOf(ArrayIndexOutOfBoundsException.class)
        .hasMessage("Float32ArrayType at position 1 index: -1 length: 0");
  }

  @Test
  void getFloat32_index_greater_than_length() {
    var struct = struct()
        .float32()
        .float32Array(0)
        .fromBytes(ba().float32(2, 4, 3))
        .build();

    assertThatThrownBy(() -> struct.getFloat32(1, 2))
        .isInstanceOf(ArrayIndexOutOfBoundsException.class)
        .hasMessage("Float32ArrayType at position 1 index: 2 length: 2");
  }

  @Test
  void getFloat32_index_not_allocated() {
    var struct = struct()
        .allocated()
        .float32()
        .float32Array(0)
        .build();

    assertThatThrownBy(() -> struct.getFloat32(1, 0))
        .isInstanceOf(IndexOutOfBoundsException.class)
        .hasMessage("allocated: 0, index: 0, length: 4");
  }

  @Test
  void getFloat32_index_constant() {
    var struct = struct()
        .primitive().constant(new float[]{3, 3, 3}).float32()
        .build();

    assertThat(struct.getFloat32(0, 2)).isEqualTo(3.0f);
  }

  @Test
  void getFloat32Array() {
    var struct = struct()
        .float32Array(constant(5))
        .fromBytes(ba().float32(5.5f, 5.5f, 5.5f, 5.5f, 5.5f))
        .build();

    assertThat(struct.getFloat32Array(0)).containsExactly(5.5f, 5.5f, 5.5f, 5.5f, 5.5f);
  }

  @Test
  void getFloat32Array_sub() {
    var struct = struct()
        .float32Array(constant(5))
        .fromBytes(ba().float32(5.5f, 5.5f, 5.5f, 5.5f, 5.5f))
        .build();

    assertThat(struct.getFloat32Array(0, 2, 2)).containsExactly(5.5f, 5.5f);
  }

  @Test
  void getFloat32Array_sub_index_negative() {
    var struct = struct()
        .float32Array(constant(5))
        .fromBytes(ba().float32(5.5f, 5.5f, 5.5f, 5.5f, 5.5f))
        .build();

    assertThatThrownBy(() -> struct.getFloat32Array(0, -2, 2))
        .isInstanceOf(ArrayIndexOutOfBoundsException.class)
        .hasMessage("Float32ArrayType at position 0 length: 5 start: -2 end: 0");
  }

  @Test
  void getFloat32Array_sub_index_over() {
    var struct = struct()
        .float32Array(constant(5))
        .fromBytes(ba().float32(5.5f, 5.5f, 5.5f, 5.5f, 5.5f))
        .build();

    assertThatThrownBy(() -> struct.getFloat32Array(0, 5, 2))
        .isInstanceOf(ArrayIndexOutOfBoundsException.class)
        .hasMessage("Float32ArrayType at position 0 length: 5 start: 5 end: 7");
  }

  @Test
  void getFloat32List() {
    var struct = struct()
        .float32Array(constant(5))
        .fromBytes(ba().float32(5.5f, 5.5f, 5.5f, 5.5f, 5.5f))
        .build();

    assertThat(struct.getFloat32List(0)).containsExactly(5.5f, 5.5f, 5.5f, 5.5f, 5.5f);
  }

  @Test
  void getFloat32List_sub() {
    var struct = struct()
        .float32Array(constant(5))
        .fromBytes(ba().float32(5.5f, 5.5f, 5.5f, 5.5f, 5.5f))
        .build();

    assertThat(struct.getFloat32List(0, 2, 2)).containsExactly(5.5f, 5.5f);
  }

  @Test
  void getFloat32List_sub_index_negative() {
    var struct = struct()
        .float32Array(constant(5))
        .fromBytes(ba().float32(5.5f, 5.5f, 5.5f, 5.5f, 5.5f))
        .build();

    assertThatThrownBy(() -> struct.getFloat32List(0, -2, 2))
        .isInstanceOf(ArrayIndexOutOfBoundsException.class)
        .hasMessage("Float32ArrayType at position 0 length: 5 start: -2 end: 0");
  }

  @Test
  void getFloat32List_sub_index_over() {
    var struct = struct()
        .float32Array(constant(5))
        .fromBytes(ba().float32(5.5f, 5.5f, 5.5f, 5.5f, 5.5f))
        .build();

    assertThatThrownBy(() -> struct.getFloat32List(0, 5, 2))
        .isInstanceOf(ArrayIndexOutOfBoundsException.class)
        .hasMessage("Float32ArrayType at position 0 length: 5 start: 5 end: 7");
  }
}
