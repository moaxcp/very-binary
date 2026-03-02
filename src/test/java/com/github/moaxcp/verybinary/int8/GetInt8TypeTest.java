package com.github.moaxcp.verybinary.int8;

import com.github.moaxcp.verybinary.Int8ArrayType;
import com.github.moaxcp.verybinary.Int8Type;
import org.junit.jupiter.api.Test;

import static com.github.moaxcp.verybinary.Builders.struct;
import static com.github.moaxcp.verybinary.ByteArray.ba;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class GetInt8TypeTest {
  @Test
  void getWrapper() {
    var struct = struct()
        .int8()
        .build();

    assertThatThrownBy(() -> ((Int8Type) struct.getType(0)).get(struct))
        .isInstanceOf(UnsupportedOperationException.class)
        .hasMessage("get(Pointer) not supported for Int8Type. Use getInt8(Pointer) instead.");
  }

  @Test
  void getInt8() {
    var struct = struct()
        .int8()
        .build();

    struct.setInt8(0, (byte) 2);

    assertThat(struct.getInt8(0)).isEqualTo((byte) 2);
  }

  @Test
  void getInt8_position_negative() {
    var struct = struct()
        .int8()
        .build();

    assertThatThrownBy(() -> struct.getInt8(-1))
        .isInstanceOf(IndexOutOfBoundsException.class)
        .hasMessage("Index -1 out of bounds for length 1");
  }

  @Test
  void getInt8_position_greater_than_length() {
    var struct = struct()
        .int8()
        .build();

    assertThatThrownBy(() -> struct.getInt8(2))
        .isInstanceOf(IndexOutOfBoundsException.class)
        .hasMessage("Index 2 out of bounds for length 1");
  }

  @Test
  void getInt8Allocated() {
    var struct = struct()
        .int8()
        .build();

    assertThat(struct.getInt8(0)).isEqualTo((byte) 0);
    assertThat(struct.getByteArray()).isEqualTo(ba().int8(0));
  }

  @Test
  void getInt8NotAllocated() {
    var struct = struct()
        .allocated()
        .int8()
        .build();

    assertThatThrownBy(() -> struct.getInt8(0))
        .isInstanceOf(IndexOutOfBoundsException.class)
        .hasMessage("allocated: 0, index: 0, length: 1");
  }

  @Test
  void getInt8_constant() {
    var struct = struct()
        .primitive().constant((byte) 5).int8()
        .build();

    assertThat(struct.getInt8(0)).isEqualTo((byte) 5);
  }

  @Test
  void getInt8_index_0_not_array() {
    var struct = struct()
        .int8()
        .fromBytes(ba().int8(1))
        .build();

    assertThatThrownBy(() -> struct.getInt8(0, 0))
        .isInstanceOf(ClassCastException.class);
  }

  @Test
  void getArrayWrapper() {
    var struct = struct()
        .int8()
        .int8Array(0)
        .build();

    assertThatThrownBy(() -> ((Int8ArrayType) struct.getType(1)).get(struct, 0))
        .isInstanceOf(UnsupportedOperationException.class)
        .hasMessage("get(Pointer, long) not supported for Int8ArrayType. Use getInt8(Pointer, long) instead.");
  }

  @Test
  void getInt8Array() {
    var struct = struct()
        .int8()
        .int8Array(0)
        .fromBytes(ba().int8(2, 1, 2))
        .build();

    assertThat(struct.getInt8(1, 0)).isEqualTo((byte) 1);
    assertThat(struct.getInt8(1, 1)).isEqualTo((byte) 2);
  }

  @Test
  void getInt8Array_negative() {
    var struct = struct()
        .int8()
        .int8Array(0)
        .build();

    assertThatThrownBy(() -> struct.getInt8(1, -1))
        .isInstanceOf(ArrayIndexOutOfBoundsException.class)
        .hasMessage("Int8ArrayType at position 1 index: -1 length: 0");
  }

  @Test
  void getInt8Array_greater_than_length() {
    var struct = struct()
        .int8()
        .int8Array(0)
        .fromBytes(ba().int8(2, 1, 2))
        .build();

    assertThatThrownBy(() -> struct.getInt8(1, 2))
        .isInstanceOf(ArrayIndexOutOfBoundsException.class)
        .hasMessage("Int8ArrayType at position 1 index: 2 length: 2");
  }

  @Test
  void getInt8Array_not_allocated() {
    var struct = struct()
        .allocated()
        .int8()
        .int8Array(0)
        .build();

    assertThatThrownBy(() -> struct.getInt8(1, 0))
        .isInstanceOf(IndexOutOfBoundsException.class)
        .hasMessage("allocated: 0, index: 0, length: 1");
  }

  @Test
  void getInt8Array_constant() {
    var struct = struct()
        .primitive().constant(new byte[]{5, 5, 5, 5, 5}).int8()
        .build();

    assertThat(struct.getInt8(0, 3)).isEqualTo((byte) 5);
  }
}
