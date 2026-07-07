package com.github.moaxcp.verybinary.uint8;

import org.junit.jupiter.api.Test;

import static com.github.moaxcp.verybinary.Builders.struct;
import static com.github.moaxcp.verybinary.ByteArray.ba;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class RemoveUint8TypeTest {

  @Test
  void removeUint8() {
    var struct = struct()
        .uint8()
        .uint8Array(0)
        .fromBytes(ba().uint8(2, 1, 2))
        .build();

    struct.remove(1);

    assertThat(struct.getByteArray()).isEqualTo(ba().uint8(0));
  }

  @Test
  void removeUint8_position_negative() {
    var struct = struct()
        .uint8()
        .uint8Array(0)
        .build();

    assertThatThrownBy(() -> struct.remove(-1))
        .isInstanceOf(IndexOutOfBoundsException.class)
        .hasMessage("Index -1 out of bounds for length 2");
  }

  @Test
  void removeUint8_position_greater_than_length() {
    var struct = struct()
        .uint8()
        .uint8Array(0)
        .build();

    assertThatThrownBy(() -> struct.remove(2))
        .isInstanceOf(IndexOutOfBoundsException.class)
        .hasMessage("Index 2 out of bounds for length 2");
  }

  @Test
  void removeUint8_not_allocated() {
    var struct = struct()
        .allocated()
        .uint8()
        .uint8Array(0)
        .build();

    assertThatThrownBy(() -> struct.remove(1))
        .isInstanceOf(IndexOutOfBoundsException.class)
        .hasMessage("allocated: 0, index: 0, length: 1");
  }

  @Test
  void removeUint8_not_array() {
    var struct = struct()
        .uint8()
        .build();

    assertThatThrownBy(() -> struct.remove(0))
        .isInstanceOf(UnsupportedOperationException.class)
        .hasMessage("Cannot remove element from fixed length Uint8Type at position 0");
  }

  @Test
  void removeUint8_with_index_0() {
    var struct = struct()
        .uint8()
        .uint8Array(0)
        .build();

    struct.addUint8(1, (byte) 1);
    struct.addUint8(1, (byte) 2);
    struct.remove(1, 0);

    assertThat(struct.getByteArray()).isEqualTo(ba().uint8(1, 2));
  }

  @Test
  void removeUint8_with_index_1() {
    var struct = struct()
        .uint8()
        .uint8Array(0)
        .build();
    struct.addUint8(1, (byte) 1);
    struct.addUint8(1, (byte) 2);

    struct.remove(1, 1);

    assertThat(struct.getByteArray()).isEqualTo(ba().uint8(1, 1));
  }

  @Test
  void removeUint8_with_index_negative() {
    var struct = struct()
        .uint8()
        .uint8Array(0)
        .fromBytes(ba().uint8(2, 1, 2))
        .build();

    assertThatThrownBy(() -> struct.remove(1, -1))
        .isInstanceOf(ArrayIndexOutOfBoundsException.class)
        .hasMessage("Uint8ListType at position 1 index: -1 length: 2");
  }

  @Test
  void removeUint8_with_index_greater_than_length() {
    var struct = struct()
        .uint8()
        .uint8Array(0)
        .fromBytes(ba().uint8(2, 1, 2))
        .build();

    assertThatThrownBy(() -> struct.remove(1, 2))
        .isInstanceOf(ArrayIndexOutOfBoundsException.class)
        .hasMessage("Uint8ListType at position 1 index: 2 length: 2");
  }

  @Test
  void removeUint8_with_index_not_array() {
    var struct = struct()
        .uint8()
        .fromBytes(ba().uint8(1))
        .build();

    assertThatThrownBy(() -> struct.remove(0, 0))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Field at postion 0 is not a ArrayValueType or ListValueType");
  }

  @Test
  void removeUint8Array_fixed_length() {
    var struct = struct()
        .primitive().constant(new short[]{5, 5, 5, 5, 5}).uint8()
        .build();

    assertThatThrownBy(() -> struct.remove(0))
        .isInstanceOf(UnsupportedOperationException.class)
        .hasMessage("Cannot remove element from fixed length Uint8ListType at position 0");
  }

  @Test
  void removeUint8Array_fixed_length_with_index() {
    var struct = struct()
        .primitive().constant(new short[]{5, 5, 5, 5, 5}).uint8()
        .build();

    assertThatThrownBy(() -> struct.remove(0, 3))
        .isInstanceOf(UnsupportedOperationException.class)
        .hasMessage("Cannot remove element from fixed length array Uint8ListType at position 0 index: 3");
  }
}
