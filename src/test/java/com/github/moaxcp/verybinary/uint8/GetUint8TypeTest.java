package com.github.moaxcp.verybinary.uint8;

import org.junit.jupiter.api.Test;

import static com.github.moaxcp.verybinary.Builders.struct;
import static com.github.moaxcp.verybinary.ByteArray.ba;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class GetUint8TypeTest {

  @Test
  void getWrapper() {
    var struct = struct()
        .uint8()
        .build();

    assertThatThrownBy(() -> ((com.github.moaxcp.verybinary.Uint8Type) struct.getType(0)).get(struct))
        .isInstanceOf(UnsupportedOperationException.class)
        .hasMessage("get(Pointer) not supported for Uint8Type. Use getUint8(Pointer) instead.");
  }

  @Test
  void getUint8() {
    var struct = struct()
        .uint8()
        .build();

    struct.setUint8(0, (byte) 2);

    assertThat(struct.getUint8(0)).isEqualTo((byte) 2);
  }

  @Test
  void getUint8_position_negative() {
    var struct = struct()
        .uint8()
        .build();

    assertThatThrownBy(() -> struct.getUint8(-1))
        .isInstanceOf(IndexOutOfBoundsException.class)
        .hasMessage("Index -1 out of bounds for length 1");
  }

  @Test
  void getUint8_position_greater_than_length() {
    var struct = struct()
        .uint8()
        .build();

    assertThatThrownBy(() -> struct.getUint8(2))
        .isInstanceOf(IndexOutOfBoundsException.class)
        .hasMessage("Index 2 out of bounds for length 1");
  }

  @Test
  void getUint8Allocated() {
    var struct = struct()
        .uint8()
        .build();

    assertThat(struct.getUint8(0)).isEqualTo((byte) 0);
    assertThat(struct.getByteArray()).isEqualTo(ba().uint8(0));
  }

  @Test
  void getUint8NotAllocated() {
    var struct = struct()
        .allocated()
        .uint8()
        .build();

    assertThatThrownBy(() -> struct.getUint8(0))
        .isInstanceOf(IndexOutOfBoundsException.class)
        .hasMessage("allocated: 0, index: 0, length: 1");
  }

  @Test
  void getUint8_constant() {
    var struct = struct()
        .primitive().constant((short) 5).uint8()
        .build();

    assertThat(struct.getUint8(0)).isEqualTo((short) 5);
  }

  @Test
  void getUint8_index_0_not_array() {
    var struct = struct()
        .uint8()
        .fromBytes(ba().uint8(1))
        .build();

    assertThatThrownBy(() -> struct.getUint8(0, 0))
        .isInstanceOf(ClassCastException.class);
  }

  @Test
  void getArrayWrapper() {
    var struct = struct()
        .uint8()
        .uint8Array(0)
        .build();

    assertThatThrownBy(() -> ((com.github.moaxcp.verybinary.Uint8ListType) struct.getType(1)).get(struct, 0))
        .isInstanceOf(UnsupportedOperationException.class)
        .hasMessage("get(Pointer, long) not supported for Uint8ListType. Use getUint8(Pointer, long) instead.");
  }

  @Test
  void getUint8Array() {
    var struct = struct()
        .uint8()
        .uint8Array(0)
        .fromBytes(ba().uint8(2, 1, 2))
        .build();

    assertThat(struct.getUint8(1, 0)).isEqualTo((byte) 1);
    assertThat(struct.getUint8(1, 1)).isEqualTo((byte) 2);
  }

  @Test
  void getUint8Array_negative() {
    var struct = struct()
        .uint8()
        .uint8Array(0)
        .build();

    assertThatThrownBy(() -> struct.getUint8(1, -1))
        .isInstanceOf(ArrayIndexOutOfBoundsException.class)
        .hasMessage("Uint8ListType at position 1 index: -1 length: 0");
  }

  @Test
  void getUint8Array_greater_than_length() {
    var struct = struct()
        .uint8()
        .uint8Array(0)
        .fromBytes(ba().uint8(2, 1, 2))
        .build();

    assertThatThrownBy(() -> struct.getUint8(1, 2))
        .isInstanceOf(ArrayIndexOutOfBoundsException.class)
        .hasMessage("Uint8ListType at position 1 index: 2 length: 2");
  }

  @Test
  void getUint8Array_not_allocated() {
    var struct = struct()
        .allocated()
        .uint8()
        .uint8Array(0)
        .build();

    assertThatThrownBy(() -> struct.getUint8(1, 0))
        .isInstanceOf(IndexOutOfBoundsException.class)
        .hasMessage("allocated: 0, index: 0, length: 1");
  }

  @Test
  void getUint8Array_constant() {
    var struct = struct()
        .primitive().constant(new short[]{5, 5, 5, 5, 5}).uint8()
        .build();

    assertThat(struct.getUint8(0, 3)).isEqualTo((byte) 5);
  }
}
