package com.github.moaxcp.verybinary.float32;

import com.github.moaxcp.verybinary.Float32ListType;
import org.junit.jupiter.api.Test;

import static com.github.moaxcp.verybinary.Builders.struct;
import static com.github.moaxcp.verybinary.ByteArray.ba;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class AddFloat32TypeTest {
  @Test
  void addFloat32Wrapper() {
    var struct = struct()
        .float32()
        .float32Array(0)
        .build();

    assertThatThrownBy(() -> ((Float32ListType) struct.getType(1)).add(struct, Float.valueOf(1.0f)))
        .isInstanceOf(UnsupportedOperationException.class)
        .hasMessage("add(Pointer, Float) not supported for Float32ArrayType. Use add(Pointer, float) instead.");
  }

  @Test
  void addFloat32Wrapper_with_index() {
    var struct = struct()
        .float32()
        .float32Array(0)
        .build();

    assertThatThrownBy(() -> ((Float32ListType) struct.getType(1)).add(struct, 0, Float.valueOf(1.0f)))
        .isInstanceOf(UnsupportedOperationException.class)
        .hasMessage("add(Pointer, long, Float) not supported for Float32ArrayType. Use add(Pointer, long, float) instead.");
  }

  @Test
  void addFloat32() {
    var struct = struct()
        .float32()
        .float32Array(0)
        .fromBytes(ba().float32(2, 4, 3))
        .build();

    struct.addFloat32(1, 3.0f);
    struct.addFloat32(1, 2.0f);

    assertThat(struct.getByteArray()).isEqualTo(ba().float32(4, 4, 3, 3, 2));
  }

  @Test
  void addFloat32_position_negative() {
    var struct = struct()
        .float32()
        .float32Array(0)
        .fromBytes(ba())
        .build();

    assertThatThrownBy(() -> struct.addFloat32(-1, 3.0f))
        .isInstanceOf(IndexOutOfBoundsException.class)
        .hasMessage("Index -1 out of bounds for length 2");
  }

  @Test
  void addFloat32_position_greater_than_length() {
    var struct = struct()
        .float32()
        .float32Array(0)
        .fromBytes(ba())
        .build();

    assertThatThrownBy(() -> struct.addFloat32(3, 3.0f))
        .isInstanceOf(IndexOutOfBoundsException.class)
        .hasMessage("Index 3 out of bounds for length 2");
  }

  @Test
  void addFloat32_not_allocated() {
    var struct = struct()
        .allocated()
        .float32()
        .float32Array(0)
        .build();
    assertThatThrownBy(() -> struct.addFloat32(1, 3.0f))
        .isInstanceOf(IndexOutOfBoundsException.class)
        .hasMessage("allocated: 0, index: 0, length: 4");
  }

  @Test
  void addFloat32Array_constant() {
    var struct = struct()
        .primitive().constant(new float[]{3.0f, 3.0f, 3.0f}).float32()
        .build();

    assertThatThrownBy(() -> struct.addFloat32(0, 5.0f))
        .isInstanceOf(IllegalStateException.class)
        .hasMessage("Float32ArrayType at position 0 is constant length: 3 index: 3");
  }

  @Test
  void addFloat32_not_array() {
    var struct = struct()
        .float32()
        .fromBytes(ba())
        .build();
    assertThatThrownBy(() -> struct.addFloat32(0, 3.0f))
        .isInstanceOf(ClassCastException.class);
  }

  @Test
  void addFloat32_with_index() {
    var struct = struct()
        .float32()
        .float32Array(0)
        .fromBytes(ba().float32(2).float32(2).float32(3))
        .build();

    struct.addFloat32(1, 0, 1.5f);
    struct.addFloat32(1, 1, 2.5f);

    assertThat(struct.getByteArray()).isEqualTo(ba().float32(4).float32(1.5f).float32(2.5f).float32(2f).float32(3f));
  }

  @Test
  void addFloat32_with_index_negative() {
    var struct = struct()
        .float32()
        .float32Array(0)
        .fromBytes(ba().float32(2).float32(2).float32(3))
        .build();

    assertThatThrownBy(() -> struct.addFloat32(1, -1, 3.0f))
        .isInstanceOf(ArrayIndexOutOfBoundsException.class)
        .hasMessage("Float32ArrayType at position 1 index: -1 new length: 3");
  }

  @Test
  void addFloat32_with_index_greater_than_length() {
    var struct = struct()
        .float32()
        .float32Array(0)
        .fromBytes(ba().float64(2).float64(2).float64(3))
        .build();

    assertThatThrownBy(() -> struct.addFloat32(1, 3, 3.0f))
        .isInstanceOf(ArrayIndexOutOfBoundsException.class)
        .hasMessage("Float32ArrayType at position 1 index: 3 new length: 3");
  }

  @Test
  void addFloat32_with_index_not_allocated() {
    var struct = struct()
        .allocated()
        .float32()
        .float32Array(0)
        .build();

    assertThatThrownBy(() -> struct.addFloat32(1, 0, 3.0f))
        .isInstanceOf(IndexOutOfBoundsException.class)
        .hasMessage("allocated: 0, index: 0, length: 4");
  }

  @Test
  void addFloat32_with_index_0_not_array() {
    var struct = struct()
        .float32()
        .fromBytes(ba())
        .build();

    assertThatThrownBy(() -> struct.addFloat32(0, 0, 3.0f))
        .isInstanceOf(ClassCastException.class);
  }

  @Test
  void addFloat32_with_index_1_not_array() {
    var struct = struct()
        .float32()
        .fromBytes(ba())
        .build();

    assertThatThrownBy(() -> struct.addFloat32(0, 1, 3.0f))
        .isInstanceOf(ClassCastException.class);
  }

  @Test
  void addFloat32Array_with_index_constant() {
    var struct = struct()
        .primitive().constant(new float[]{3.0f, 3.0f, 3.0f}).float32()
        .build();

    assertThatThrownBy(() -> struct.addFloat32(0, 2, 4.0f))
        .isInstanceOf(IllegalStateException.class)
        .hasMessage("Float32ArrayType at position 0 is constant length: 3 index: 2");
  }
}
