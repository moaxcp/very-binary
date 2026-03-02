package com.github.moaxcp.verybinary.int8;

import com.github.moaxcp.verybinary.Int8ArrayType;
import org.junit.jupiter.api.Test;

import static com.github.moaxcp.verybinary.Builders.struct;
import static com.github.moaxcp.verybinary.ByteArray.ba;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class AddInt8TypeTest {
  @Test
  void addInt8Wrapper() {
    var struct = struct()
        .int8()
        .int8Array(0)
        .build();

    assertThatThrownBy(() -> ((Int8ArrayType) struct.getType(1)).add(struct, Byte.valueOf((byte) 1)))
        .isInstanceOf(UnsupportedOperationException.class)
        .hasMessage("add(Pointer, Byte) not supported for Int8ArrayType. Use add(Pointer, byte) instead.");
  }

  @Test
  void addInt8Wrapper_with_index() {
    var struct = struct()
        .int8()
        .int8Array(0)
        .build();

    assertThatThrownBy(() -> ((Int8ArrayType) struct.getType(1)).add(struct, 0, Byte.valueOf((byte) 1)))
        .isInstanceOf(UnsupportedOperationException.class)
        .hasMessage("add(Pointer, long, Byte) not supported for Int8ArrayType. Use add(Pointer, long, byte) instead.");
  }

  @Test
  void addInt8() {
    var struct = struct()
        .int8()
        .int8Array(0)
        .fromBytes(ba().int8(2, 1, 2))
        .build();

    struct.addInt8(1, (byte) 3);
    struct.addInt8(1, (byte) 4);

    assertThat(struct.getByteArray()).isEqualTo(ba().int8(4, 1, 2, 3, 4));
  }

  @Test
  void addInt8_position_negative() {
    var struct = struct()
        .int8()
        .int8Array(0)
        .fromBytes(ba().int8(2, 1, 2))
        .build();

    assertThatThrownBy(() -> struct.addInt8(-1, (byte) 3))
        .isInstanceOf(IndexOutOfBoundsException.class)
        .hasMessage("Index -1 out of bounds for length 2");
  }

  @Test
  void addInt8_position_greater_than_length() {
    var struct = struct()
        .int8()
        .int8Array(0)
        .fromBytes(ba().int8(2, 1, 2))
        .build();

    assertThatThrownBy(() -> struct.addInt8(3, (byte) 3))
        .isInstanceOf(IndexOutOfBoundsException.class)
        .hasMessage("Index 3 out of bounds for length 2");
  }

  @Test
  void addInt8_not_allocated() {
    var struct = struct()
        .allocated()
        .int8()
        .int8Array(0)
        .build();
    assertThatThrownBy(() -> struct.addInt8(1, (byte) 3))
        .isInstanceOf(IndexOutOfBoundsException.class)
        .hasMessage("allocated: 0, index: 0, length: 1");
  }

  @Test
  void addInt8Array_constant() {
    var struct = struct()
        .primitive().constant(new byte[]{5, 5, 5, 5, 5}).int8()
        .build();

    assertThatThrownBy(() -> struct.addInt8(0, (byte) 3))
        .isInstanceOf(IllegalStateException.class)
        .hasMessage("Int8ArrayType at position 0 is constant length: 5 index: 5");
  }

  @Test
  void addInt8_not_array() {
    var struct = struct()
        .int8()
        .fromBytes(ba().int8(1))
        .build();
    assertThatThrownBy(() -> struct.addInt8(0, (byte) 3))
        .isInstanceOf(ClassCastException.class);
  }

  @Test
  void addInt8_with_index() {
    var struct = struct()
        .int8()
        .int8Array(0)
        .fromBytes(ba().int8(2, 1, 2))
        .build();

    struct.addInt8(1, 0, (byte) 3);
    struct.addInt8(1, 1, (byte) 4);

    assertThat(struct.getByteArray()).isEqualTo(ba().int8(4, 3, 4, 1, 2));
  }

  @Test
  void addInt8_with_index_negative() {
    var struct = struct()
        .int8()
        .int8Array(0)
        .fromBytes(ba().int8(2, 1, 2))
        .build();

    assertThatThrownBy(() -> struct.addInt8(1, -1, (byte) 3))
        .isInstanceOf(ArrayIndexOutOfBoundsException.class)
        .hasMessage("Int8ArrayType at position 1 index: -1 new length: 3");
  }

  @Test
  void addInt8_with_index_greater_than_length() {
    var struct = struct()
        .int8()
        .int8Array(0)
        .fromBytes(ba().int8(2, 1, 2))
        .build();

    assertThatThrownBy(() -> struct.addInt8(1, 3, (byte) 3))
        .isInstanceOf(ArrayIndexOutOfBoundsException.class)
        .hasMessage("Int8ArrayType at position 1 index: 3 new length: 3");
  }

  @Test
  void addInt8_with_index_not_allocated() {
    var struct = struct()
        .allocated()
        .int8()
        .int8Array(0)
        .build();

    assertThatThrownBy(() -> struct.addInt8(1, 0, (byte) 3))
        .isInstanceOf(IndexOutOfBoundsException.class)
        .hasMessage("allocated: 0, index: 0, length: 1");
  }

  @Test
  void addInt8_with_index_0_not_array() {
    var struct = struct()
        .int8()
        .fromBytes(ba().int8(1))
        .build();

    assertThatThrownBy(() -> struct.addInt8(0, 0, (byte) 3))
        .isInstanceOf(ClassCastException.class);
  }

  @Test
  void addInt8Array_with_index_constant() {
    var struct = struct()
        .primitive().constant(new byte[]{5, 5, 5, 5, 5}).int8()
        .build();

    assertThatThrownBy(() -> struct.addInt8(0, 3, (byte) 3))
        .isInstanceOf(IllegalStateException.class)
        .hasMessage("Int8ArrayType at position 0 is constant length: 5 index: 3");
  }
}
