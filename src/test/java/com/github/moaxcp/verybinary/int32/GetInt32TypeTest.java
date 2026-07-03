package com.github.moaxcp.verybinary.int32;

import com.github.moaxcp.verybinary.Int32ListType;
import com.github.moaxcp.verybinary.Int32Type;
import org.junit.jupiter.api.Test;

import static com.github.moaxcp.verybinary.Builders.struct;
import static com.github.moaxcp.verybinary.ByteArray.ba;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class GetInt32TypeTest {
  @Test
  void getWrapper() {
    var struct = struct()
        .int32()
        .build();

    assertThatThrownBy(() -> ((Int32Type) struct.getType(0)).get(struct))
        .isInstanceOf(UnsupportedOperationException.class)
        .hasMessage("get(Pointer) not supported for Int32Type. Use getInt32(Pointer) instead.");
  }

  @Test
  void getInt32() {
    var struct = struct()
        .int32()
        .build();

    struct.setInt32(0, 2);

    assertThat(struct.getInt32(0)).isEqualTo(2);
  }

  @Test
  void getInt32_position_negative() {
    var struct = struct()
        .int32()
        .build();

    assertThatThrownBy(() -> struct.getInt32(-1))
        .isInstanceOf(IndexOutOfBoundsException.class)
        .hasMessage("Index -1 out of bounds for length 1");
  }

  @Test
  void getInt32_position_greater_than_length() {
    var struct = struct()
        .int32()
        .build();

    assertThatThrownBy(() -> struct.getInt32(2))
        .isInstanceOf(IndexOutOfBoundsException.class)
        .hasMessage("Index 2 out of bounds for length 1");
  }

  @Test
  void getInt32Allocated() {
    var struct = struct()
        .int32()
        .build();

    assertThat(struct.getInt32(0)).isEqualTo(0);
    assertThat(struct.getByteArray()).isEqualTo(ba().int32(0));
  }

  @Test
  void getInt32NotAllocated() {
    var struct = struct()
        .allocated()
        .int32()
        .build();

    assertThatThrownBy(() -> struct.getInt32(0))
        .isInstanceOf(IndexOutOfBoundsException.class)
        .hasMessage("allocated: 0, index: 0, length: 4");
  }

  @Test
  void getInt32_constant() {
    var struct = struct()
        .primitive().constant(5).int32()
        .build();

    assertThat(struct.getInt32(0)).isEqualTo(5);
  }

  @Test
  void getInt32_index_0_not_array() {
    var struct = struct()
        .int32()
        .fromBytes(ba().int32(1))
        .build();

    assertThatThrownBy(() -> struct.getInt32(0, 0))
        .isInstanceOf(ClassCastException.class);
  }

  @Test
  void getArrayWrapper() {
    var struct = struct()
        .int32()
        .int32Array(0)
        .build();

    assertThatThrownBy(() -> ((Int32ListType) struct.getType(1)).get(struct, 0))
        .isInstanceOf(UnsupportedOperationException.class)
        .hasMessage("get(Pointer, long) not supported for Int32ListType. Use getInt32(Pointer, long) instead.");
  }

  @Test
  void getInt32Array() {
    var struct = struct()
        .int32()
        .int32Array(0)
        .fromBytes(ba().int32(2, 1, 2))
        .build();

    assertThat(struct.getInt32(1, 0)).isEqualTo(1);
    assertThat(struct.getInt32(1, 1)).isEqualTo(2);
  }

  @Test
  void getInt32Array_negative() {
    var struct = struct()
        .int32()
        .int32Array(0)
        .build();

    assertThatThrownBy(() -> struct.getInt32(1, -1))
        .isInstanceOf(ArrayIndexOutOfBoundsException.class)
        .hasMessage("Int32ListType at position 1 index: -1 length: 0");
  }

  @Test
  void getInt32Array_greater_than_length() {
    var struct = struct()
        .int32()
        .int32Array(0)
        .fromBytes(ba().int32(2, 1, 2))
        .build();

    assertThatThrownBy(() -> struct.getInt32(1, 2))
        .isInstanceOf(ArrayIndexOutOfBoundsException.class)
        .hasMessage("Int32ListType at position 1 index: 2 length: 2");
  }

  @Test
  void getInt32Array_not_allocated() {
    var struct = struct()
        .allocated()
        .int32()
        .int32Array(0)
        .build();

    assertThatThrownBy(() -> struct.getInt32(1, 0))
        .isInstanceOf(IndexOutOfBoundsException.class)
        .hasMessage("allocated: 0, index: 0, length: 4");
  }

  @Test
  void getInt32Array_constant() {
    var struct = struct()
        .primitive().constant(new int[]{5, 5, 5, 5, 5}).int32()
        .build();

    assertThat(struct.getInt32(0, 3)).isEqualTo(5);
  }
}
