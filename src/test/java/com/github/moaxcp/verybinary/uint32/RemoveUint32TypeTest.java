package com.github.moaxcp.verybinary.uint32;

import com.github.moaxcp.verybinary.Uint32ArrayType;
import org.junit.jupiter.api.Test;

import static com.github.moaxcp.verybinary.Builders.struct;
import static com.github.moaxcp.verybinary.ByteArray.ba;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class RemoveUint32TypeTest {

  @Test
  void removeUint32() {
    var struct = struct()
        .uint32()
        .uint32Array(0)
        .fromBytes(ba().uint32(2, 1, 2))
        .build();

    struct.removeAll(1);

    assertThat(struct.getByteArray()).isEqualTo(ba().uint32(0));
  }

  @Test
  void removeUint32_position_negative() {
    var struct = struct()
        .uint32()
        .uint32Array(0)
        .build();

    assertThatThrownBy(() -> struct.removeAll(-1))
        .isInstanceOf(IndexOutOfBoundsException.class)
        .hasMessage("Index -1 out of bounds for length 2");
  }

  @Test
  void removeUint32_position_greater_than_length() {
    var struct = struct()
        .uint32()
        .uint32Array(0)
        .build();

    assertThatThrownBy(() -> struct.removeAll(2))
        .isInstanceOf(IndexOutOfBoundsException.class)
        .hasMessage("Index 2 out of bounds for length 2");
  }

  @Test
  void removeUint32_not_allocated() {
    var struct = struct()
        .allocated()
        .uint32()
        .uint32Array(0)
        .build();

    assertThatThrownBy(() -> struct.removeAll(1))
        .isInstanceOf(IndexOutOfBoundsException.class)
        .hasMessage("allocated: 0, index: 0, length: 4");
  }

  @Test
  void removeUint32_not_array() {
    var struct = struct()
        .uint32()
        .build();

    assertThatThrownBy(() -> struct.removeAll(0))
        .isInstanceOf(UnsupportedOperationException.class)
        .hasMessage("Cannot remove element from fixed length Uint32Type at position 0");
  }

  @Test
  void removeUint32_with_index_0() {
    var struct = struct()
        .uint32()
        .uint32Array(0)
        .build();

    struct.addUint32(1, 1L);
    struct.addUint32(1, 2L);
    struct.remove(1, 0);

    assertThat(((Uint32ArrayType) struct.getType(1)).getUint32(struct, 0)).isEqualTo(2L);

    assertThat(struct.getByteArray()).isEqualTo(ba().uint32(1, 2));
  }

  @Test
  void removeUint32_with_index_1() {
    var struct = struct()
        .uint32()
        .uint32Array(0)
        .build();
    struct.addUint32(1, 1L);
    struct.addUint32(1, 2L);

    struct.remove(1, 1);

    assertThat(struct.getByteArray()).isEqualTo(ba().uint32(1, 1));
  }

  @Test
  void removeUint32_with_index_negative() {
    var struct = struct()
        .uint32()
        .uint32Array(0)
        .fromBytes(ba().uint32(2, 1, 2))
        .build();

    assertThatThrownBy(() -> struct.remove(1, -1))
        .isInstanceOf(ArrayIndexOutOfBoundsException.class)
        .hasMessage("Uint32ArrayType at position 1 index: -1 length: 2");
  }

  @Test
  void removeUint32_with_index_greater_than_length() {
    var struct = struct()
        .uint32()
        .uint32Array(0)
        .fromBytes(ba().uint32(2, 1, 2))
        .build();

    assertThatThrownBy(() -> struct.remove(1, 2))
        .isInstanceOf(ArrayIndexOutOfBoundsException.class)
        .hasMessage("Uint32ArrayType at position 1 index: 2 length: 2");
  }

  @Test
  void removeUint32_with_index_not_array() {
    var struct = struct()
        .uint32()
        .fromBytes(ba().uint32(1))
        .build();

    assertThatThrownBy(() -> struct.remove(0, 0))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Field at postion 0 is not a ArrayValueType or ListValueType");
  }

  @Test
  void removeUint32Array_fixed_length() {
    var struct = struct()
        .primitive().constant(new long[]{5, 5, 5, 5, 5}).uint32()
        .build();

    assertThatThrownBy(() -> struct.removeAll(0))
        .isInstanceOf(UnsupportedOperationException.class)
        .hasMessage("Cannot remove element from fixed length Uint32ArrayType at position 0");
  }

  @Test
  void removeUint32Array_fixed_length_with_index() {
    var struct = struct()
        .primitive().constant(new long[]{5, 5, 5, 5, 5}).uint32()
        .build();

    assertThatThrownBy(() -> struct.remove(0, 3))
        .isInstanceOf(UnsupportedOperationException.class)
        .hasMessage("Cannot remove element from fixed length array Uint32ArrayType at position 0 index: 3");
  }
}
