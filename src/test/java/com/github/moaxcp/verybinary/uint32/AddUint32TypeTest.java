package com.github.moaxcp.verybinary.uint32;

import com.github.moaxcp.verybinary.Uint32ArrayType;
import org.junit.jupiter.api.Test;

import static com.github.moaxcp.verybinary.Builders.struct;
import static com.github.moaxcp.verybinary.ByteArray.ba;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class AddUint32TypeTest {

  @Test
  void addUint32Wrapper() {
    var struct = struct()
        .uint32()
        .uint32Array(0)
        .build();

    assertThatThrownBy(() -> ((Uint32ArrayType) struct.getType(1)).add(struct, Long.valueOf(1)))
        .isInstanceOf(UnsupportedOperationException.class)
        .hasMessage("add(Pointer, Long) not supported for Uint32ArrayType. Use add(Pointer, long) instead.");
  }

  @Test
  void addUint32Wrapper_with_index() {
    var struct = struct()
        .uint32()
        .uint32Array(0)
        .build();

    assertThatThrownBy(() -> ((Uint32ArrayType) struct.getType(1)).add(struct, 0, Long.valueOf(1)))
        .isInstanceOf(UnsupportedOperationException.class)
        .hasMessage("add(Pointer, long, Long) not supported for Uint32ArrayType. Use add(Pointer, long, long) instead.");
  }

  @Test
  void addUint32() {
    var struct = struct()
        .uint32()
        .uint32Array(0)
        .fromBytes(ba().uint32(2, 1, 2))
        .build();

    struct.addUint32(1, 3L);
    struct.addUint32(1, 4L);

    assertThat(struct.getByteArray()).isEqualTo(ba().uint32(4, 1, 2, 3, 4));
  }

  @Test
  void addUint32_position_negative() {
    var struct = struct()
        .uint32()
        .uint32Array(0)
        .fromBytes(ba().uint32(2, 1, 2))
        .build();

    assertThatThrownBy(() -> struct.addUint32(-1, 3L))
        .isInstanceOf(IndexOutOfBoundsException.class)
        .hasMessage("Index -1 out of bounds for length 2");
  }

  @Test
  void addUint32_position_greater_than_length() {
    var struct = struct()
        .uint32()
        .uint32Array(0)
        .fromBytes(ba().uint32(2, 1, 2))
        .build();

    assertThatThrownBy(() -> struct.addUint32(3, 3L))
        .isInstanceOf(IndexOutOfBoundsException.class)
        .hasMessage("Index 3 out of bounds for length 2");
  }

  @Test
  void addUint32_not_allocated() {
    var struct = struct()
        .allocated()
        .uint32()
        .uint32Array(0)
        .build();
    assertThatThrownBy(() -> struct.addUint32(1, 3L))
        .isInstanceOf(IndexOutOfBoundsException.class)
        .hasMessage("allocated: 0, index: 0, length: 4");
  }

  @Test
  void addUint32Array_constant() {
    var struct = struct()
        .primitive().constant(new long[]{5, 5, 5, 5, 5}).uint32()
        .build();

    assertThatThrownBy(() -> struct.addUint32(0, 3L))
        .isInstanceOf(IllegalStateException.class)
        .hasMessage("Uint32ArrayType at position 0 is constant length: 5 index: 5");
  }

  @Test
  void addUint32_not_array() {
    var struct = struct()
        .uint32()
        .fromBytes(ba().uint32(1))
        .build();
    assertThatThrownBy(() -> struct.addUint32(0, 3L))
        .isInstanceOf(ClassCastException.class);
  }

  @Test
  void addUint32_with_index() {
    var struct = struct()
        .uint32()
        .uint32Array(0)
        .fromBytes(ba().uint32(2, 1, 2))
        .build();

    struct.addUint32(1, 0, 3L);
    struct.addUint32(1, 1, 4L);

    assertThat(struct.getByteArray()).isEqualTo(ba().uint32(4, 3, 4, 1, 2));
  }

  @Test
  void addUint32_with_index_negative() {
    var struct = struct()
        .uint32()
        .uint32Array(0)
        .fromBytes(ba().uint32(2, 1, 2))
        .build();

    assertThatThrownBy(() -> struct.addUint32(1, -1, 3L))
        .isInstanceOf(ArrayIndexOutOfBoundsException.class)
        .hasMessage("Uint32ArrayType at position 1 index: -1 new length: 3");
  }

  @Test
  void addUint32_with_index_greater_than_length() {
    var struct = struct()
        .uint32()
        .uint32Array(0)
        .fromBytes(ba().uint32(2, 1, 2))
        .build();

    assertThatThrownBy(() -> struct.addUint32(1, 3, 3L))
        .isInstanceOf(ArrayIndexOutOfBoundsException.class)
        .hasMessage("Uint32ArrayType at position 1 index: 3 new length: 3");
  }

  @Test
  void addUint32_with_index_not_allocated() {
    var struct = struct()
        .allocated()
        .uint32()
        .uint32Array(0)
        .build();

    assertThatThrownBy(() -> struct.addUint32(1, 0, 3L))
        .isInstanceOf(IndexOutOfBoundsException.class)
        .hasMessage("allocated: 0, index: 0, length: 4");
  }

  @Test
  void addUint32_with_index_0_not_array() {
    var struct = struct()
        .uint32()
        .fromBytes(ba().uint32(1))
        .build();

    assertThatThrownBy(() -> struct.addUint32(0, 0, 3L))
        .isInstanceOf(ClassCastException.class);
  }

  @Test
  void addUint32Array_with_index_constant() {
    var struct = struct()
        .primitive().constant(new long[]{5, 5, 5, 5, 5}).uint32()
        .build();

    assertThatThrownBy(() -> struct.addUint32(0, 3, 3L))
        .isInstanceOf(IllegalStateException.class)
        .hasMessage("Uint32ArrayType at position 0 is constant length: 5 index: 3");
  }
}
