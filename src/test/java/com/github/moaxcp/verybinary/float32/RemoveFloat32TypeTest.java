package com.github.moaxcp.verybinary.float32;

import com.github.moaxcp.verybinary.Float32ArrayType;
import org.junit.jupiter.api.Test;

import static com.github.moaxcp.verybinary.Builders.struct;
import static com.github.moaxcp.verybinary.ByteArray.ba;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class RemoveFloat32TypeTest {
  @Test
  void removeFloat32() {
    var struct = struct()
        .float32()
        .float32Array(0)
        .fromBytes(ba().float32(2).float32(2).float32(3))
        .build();

    struct.removeAll(1);

    assertThat(struct.getByteArray()).isEqualTo(ba().float32(0));
  }

  @Test
  void removeFloat32_position_negative() {
    var struct = struct()
        .float32()
        .float32Array(0)
        .build();

    assertThatThrownBy(() -> struct.removeAll(-1))
        .isInstanceOf(IndexOutOfBoundsException.class)
        .hasMessage("Index -1 out of bounds for length 2");
  }

  @Test
  void removeFloat32_position_greater_than_length() {
    var struct = struct()
        .float32()
        .float32Array(0)
        .build();

    assertThatThrownBy(() -> struct.removeAll(2))
        .isInstanceOf(IndexOutOfBoundsException.class)
        .hasMessage("Index 2 out of bounds for length 2");
  }

  @Test
  void removeFloat32_not_allocated() {
    var struct = struct()
        .allocated()
        .float32()
        .float32Array(0)
        .build();

    assertThatThrownBy(() -> struct.removeAll(1))
        .isInstanceOf(IndexOutOfBoundsException.class)
        .hasMessage("allocated: 0, index: 0, length: 4");
  }

  @Test
  void removeFloat32_not_array() {
    var struct = struct()
        .float32()
        .build();

    assertThatThrownBy(() -> struct.removeAll(0))
        .isInstanceOf(UnsupportedOperationException.class)
        .hasMessage("Cannot remove element from fixed length Float32Type at position 0");
  }

  @Test
  void removeFloat32_with_index_0() {
    var struct = struct()
        .float32()
        .float32Array(0)
        .build();

    struct.addFloat32(1, 3.0f);
    struct.addFloat32(1, 2.0f);
    struct.remove(1, 0);

    assertThat(((Float32ArrayType) struct.getType(1)).getFloat32(struct, 0)).isEqualTo(2.0f);

    assertThat(struct.getByteArray()).isEqualTo(ba().float32(1f).float32(2.0f));
  }

  @Test
  void removeFloat32_with_index_1() {
    var struct = struct()
        .float32()
        .float32Array(0)
        .build();
    struct.addFloat32(1, 3.0f);
    struct.addFloat32(1, 2.0f);

    struct.remove(1, 1);

    assertThat(struct.getByteArray()).isEqualTo(ba().float32(1f).float32(3.0f));
  }

  @Test
  void removeFloat32_with_index_negative() {
    var struct = struct()
        .float32()
        .float32Array(0)
        .fromBytes(ba().float32(2).float32(2).float32(3))
        .build();

    assertThatThrownBy(() -> struct.remove(1, -1))
        .isInstanceOf(ArrayIndexOutOfBoundsException.class)
        .hasMessage("Float32ArrayType at position 1 index: -1 length: 2");
  }

  @Test
  void removeFloat32_with_index_greater_than_length() {
    var struct = struct()
        .float32()
        .float32Array(0)
        .fromBytes(ba().float32(2).float32(2).float32(3))
        .build();

    assertThatThrownBy(() -> struct.remove(1, 2))
        .isInstanceOf(ArrayIndexOutOfBoundsException.class)
        .hasMessage("Float32ArrayType at position 1 index: 2 length: 2");
  }

  @Test
  void removeFloat32_with_index_not_array() {
    var struct = struct()
        .float32()
        .fromBytes(ba().float32(2))
        .build();

    assertThatThrownBy(() -> struct.remove(0, 0))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Field at postion 0 is not a ArrayValueType or ListValueType");
  }

  @Test
  void removeFloat32Array_fixed_length() {
    var struct = struct()
        .primitive().constant(new float[]{3, 3, 3}).float32()
        .build();

    assertThatThrownBy(() -> struct.removeAll(0))
        .isInstanceOf(UnsupportedOperationException.class)
        .hasMessage("Cannot remove element from fixed length Float32ArrayType at position 0");
  }

  @Test
  void removeFloat32Array_fixed_length_with_index() {
    var struct = struct()
        .primitive().constant(new float[]{3, 3, 3}).float32()
        .build();

    assertThatThrownBy(() -> struct.remove(0, 2))
        .isInstanceOf(UnsupportedOperationException.class)
        .hasMessage("Cannot remove element from fixed length array Float32ArrayType at position 0 index: 2");
  }
}
