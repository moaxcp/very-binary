package com.github.moaxcp.verybinary.uint32;

import com.github.moaxcp.verybinary.Uint32ArrayType;
import com.github.moaxcp.verybinary.Uint32Type;
import org.junit.jupiter.api.Test;

import static com.github.moaxcp.verybinary.Builders.struct;
import static com.github.moaxcp.verybinary.ByteArray.ba;
import static com.github.moaxcp.verybinary.Expression.constant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class GetUint32TypeTest {

  @Test
  void getWrapper() {
    var struct = struct()
        .uint32()
        .build();

    assertThatThrownBy(() -> ((Uint32Type) struct.getType(0)).get(struct))
        .isInstanceOf(UnsupportedOperationException.class)
        .hasMessage("get(Pointer) not supported for Uint32Type. Use getUint32(Pointer) instead.");
  }

  @Test
  void getUint32() {
    var struct = struct()
        .uint32()
        .build();

    struct.setUint32(0, 2L);

    assertThat(struct.getUint32(0)).isEqualTo(2L);
  }

  @Test
  void getUint32_position_negative() {
    var struct = struct()
        .uint32()
        .build();

    assertThatThrownBy(() -> struct.getUint32(-1))
        .isInstanceOf(IndexOutOfBoundsException.class)
        .hasMessage("Index -1 out of bounds for length 1");
  }

  @Test
  void getUint32_position_greater_than_length() {
    var struct = struct()
        .uint32()
        .build();

    assertThatThrownBy(() -> struct.getUint32(2))
        .isInstanceOf(IndexOutOfBoundsException.class)
        .hasMessage("Index 2 out of bounds for length 1");
  }

  @Test
  void getUint32Allocated() {
    var struct = struct()
        .uint32()
        .build();

    assertThat(struct.getUint32(0)).isEqualTo(0L);
    assertThat(struct.getByteArray()).isEqualTo(ba().uint32(0));
  }

  @Test
  void getUint32NotAllocated() {
    var struct = struct()
        .allocated()
        .uint32()
        .build();

    assertThatThrownBy(() -> struct.getUint32(0))
        .isInstanceOf(IndexOutOfBoundsException.class)
        .hasMessage("allocated: 0, index: 0, length: 4");
  }

  @Test
  void getUint32_constant() {
    var struct = struct()
        .primitive().constant(5L).uint32()
        .build();

    assertThat(struct.getUint32(0)).isEqualTo(5L);
  }

  @Test
  void getUint32_index_0_not_array() {
    var struct = struct()
        .uint32()
        .fromBytes(ba().uint32(1))
        .build();

    assertThat(struct.getUint32(0, 0)).isEqualTo(1L);
  }

  @Test
  void getUint32_index_1_not_array() {
    var struct = struct()
        .uint32()
        .fromBytes(ba().uint32(1))
        .build();

    assertThatThrownBy(() -> struct.getUint32(0, 1))
        .isInstanceOf(ArrayIndexOutOfBoundsException.class)
        .hasMessage("Uint32Type at position 0 index: 1 length: 1");
  }

  @Test
  void getArrayWrapper() {
    var struct = struct()
        .uint32()
        .uint32Array(0)
        .build();

    assertThatThrownBy(() -> ((Uint32ArrayType) struct.getType(1)).get(struct, 0))
        .isInstanceOf(UnsupportedOperationException.class)
        .hasMessage("get(Pointer, long) not supported for Uint32ArrayType. Use getUint32(Pointer, long) instead.");
  }

  @Test
  void getUint32Array() {
    var struct = struct()
        .uint32()
        .uint32Array(0)
        .fromBytes(ba().uint32(2, 1, 2))
        .build();

    assertThat(struct.getUint32(1, 0)).isEqualTo(1L);
    assertThat(struct.getUint32(1, 1)).isEqualTo(2L);
  }

  @Test
  void getUint32Array_negative() {
    var struct = struct()
        .uint32()
        .uint32Array(0)
        .build();

    assertThatThrownBy(() -> struct.getUint32(1, -1))
        .isInstanceOf(ArrayIndexOutOfBoundsException.class)
        .hasMessage("Uint32ArrayType at position 1 index: -1 length: 0");
  }

  @Test
  void getUint32Array_greater_than_length() {
    var struct = struct()
        .uint32()
        .uint32Array(0)
        .fromBytes(ba().uint32(2, 1, 2))
        .build();

    assertThatThrownBy(() -> struct.getUint32(1, 2))
        .isInstanceOf(ArrayIndexOutOfBoundsException.class)
        .hasMessage("Uint32ArrayType at position 1 index: 2 length: 2");
  }

  @Test
  void getUint32Array_not_allocated() {
    var struct = struct()
        .allocated()
        .uint32()
        .uint32Array(0)
        .build();

    assertThatThrownBy(() -> struct.getUint32(1, 0))
        .isInstanceOf(IndexOutOfBoundsException.class)
        .hasMessage("allocated: 0, index: 0, length: 4");
  }

  @Test
  void getUint32Array_constant() {
    var struct = struct()
        .primitive().constant(5L).lengthExpression(constant(5)).uint32()
        .build();

    assertThat(struct.getUint32(0, 3)).isEqualTo(5L);
  }
}
