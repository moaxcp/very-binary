package com.github.moaxcp.verybinary.uint8;

import org.junit.jupiter.api.Test;

import static com.github.moaxcp.verybinary.Builders.struct;
import static com.github.moaxcp.verybinary.ByteArray.ba;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class AddUint8TypeTest {

  @Test
  void addUint8Wrapper() {
    var struct = struct()
        .uint8()
        .uint8Array(0)
        .build();

    assertThatThrownBy(() -> ((com.github.moaxcp.verybinary.Uint8ArrayType) struct.getType(1)).add(struct, Short.valueOf((short) 1)))
        .isInstanceOf(UnsupportedOperationException.class)
        .hasMessage("add(Pointer, Short) not supported for Uint8ArrayType. Use add(Pointer, short) instead.");
  }

  @Test
  void addUint8Wrapper_with_index() {
    var struct = struct()
        .uint8()
        .uint8Array(0)
        .build();

    assertThatThrownBy(() -> ((com.github.moaxcp.verybinary.Uint8ArrayType) struct.getType(1)).add(struct, 0, Short.valueOf((short) 1)))
        .isInstanceOf(UnsupportedOperationException.class)
        .hasMessage("add(Pointer, long, Short) not supported for Uint8ArrayType. Use add(Pointer, long, short) instead.");
  }

  @Test
  void addUint8() {
    var struct = struct()
        .uint8()
        .uint8Array(0)
        .fromBytes(ba().uint8(2, 1, 2))
        .build();

    struct.addUint8(1, (byte) 3);
    struct.addUint8(1, (byte) 4);

    assertThat(struct.getByteArray()).isEqualTo(ba().uint8(4, 1, 2, 3, 4));
  }

  @Test
  void addUint8_position_negative() {
    var struct = struct()
        .uint8()
        .uint8Array(0)
        .fromBytes(ba().uint8(2, 1, 2))
        .build();

    assertThatThrownBy(() -> struct.addUint8(-1, (byte) 3))
        .isInstanceOf(IndexOutOfBoundsException.class)
        .hasMessage("Index -1 out of bounds for length 2");
  }

  @Test
  void addUint8_position_greater_than_length() {
    var struct = struct()
        .uint8()
        .uint8Array(0)
        .fromBytes(ba().uint8(2, 1, 2))
        .build();

    assertThatThrownBy(() -> struct.addUint8(3, (byte) 3))
        .isInstanceOf(IndexOutOfBoundsException.class)
        .hasMessage("Index 3 out of bounds for length 2");
  }

  @Test
  void addUint8_not_allocated() {
    var struct = struct()
        .allocated()
        .uint8()
        .uint8Array(0)
        .build();
    assertThatThrownBy(() -> struct.addUint8(1, (byte) 3))
        .isInstanceOf(IndexOutOfBoundsException.class)
        .hasMessage("allocated: 0, index: 0, length: 1");
  }

  @Test
  void addUint8Array_constant() {
    var struct = struct()
        .primitive().constant(new short[]{5, 5, 5, 5, 5}).uint8()
        .build();

    assertThatThrownBy(() -> struct.addUint8(0, (byte) 3))
        .isInstanceOf(IllegalStateException.class)
        .hasMessage("Uint8ArrayType at position 0 is constant length: 5 index: 5");
  }

  @Test
  void addUint8_not_array() {
    var struct = struct()
        .uint8()
        .fromBytes(ba().uint8(1))
        .build();
    assertThatThrownBy(() -> struct.addUint8(0, (byte) 3))
        .isInstanceOf(ClassCastException.class);
  }

  @Test
  void addUint8_with_index() {
    var struct = struct()
        .uint8()
        .uint8Array(0)
        .fromBytes(ba().uint8(2, 1, 2))
        .build();

    struct.addUint8(1, 0, (byte) 3);
    struct.addUint8(1, 1, (byte) 4);

    assertThat(struct.getByteArray()).isEqualTo(ba().uint8(4, 3, 4, 1, 2));
  }

  @Test
  void addUint8_with_index_negative() {
    var struct = struct()
        .uint8()
        .uint8Array(0)
        .fromBytes(ba().uint8(2, 1, 2))
        .build();

    assertThatThrownBy(() -> struct.addUint8(1, -1, (byte) 3))
        .isInstanceOf(ArrayIndexOutOfBoundsException.class)
        .hasMessage("Uint8ArrayType at position 1 index: -1 new length: 3");
  }

  @Test
  void addUint8_with_index_greater_than_length() {
    var struct = struct()
        .uint8()
        .uint8Array(0)
        .fromBytes(ba().uint8(2, 1, 2))
        .build();

    assertThatThrownBy(() -> struct.addUint8(1, 3, (byte) 3))
        .isInstanceOf(ArrayIndexOutOfBoundsException.class)
        .hasMessage("Uint8ArrayType at position 1 index: 3 new length: 3");
  }

  @Test
  void addUint8_with_index_not_allocated() {
    var struct = struct()
        .allocated()
        .uint8()
        .uint8Array(0)
        .build();

    assertThatThrownBy(() -> struct.addUint8(1, 0, (byte) 3))
        .isInstanceOf(IndexOutOfBoundsException.class)
        .hasMessage("allocated: 0, index: 0, length: 1");
  }

  @Test
  void addUint8_with_index_0_not_array() {
    var struct = struct()
        .uint8()
        .fromBytes(ba().uint8(1))
        .build();

    assertThatThrownBy(() -> struct.addUint8(0, 0, (byte) 3))
        .isInstanceOf(ClassCastException.class);
  }

  @Test
  void addUint8Array_with_index_constant() {
    var struct = struct()
        .primitive().constant(new short[]{5, 5, 5, 5, 5}).uint8()
        .build();

    assertThatThrownBy(() -> struct.addUint8(0, 3, (short) 3))
        .isInstanceOf(IllegalStateException.class)
        .hasMessage("Uint8ArrayType at position 0 is constant length: 5 index: 3");
  }
}
