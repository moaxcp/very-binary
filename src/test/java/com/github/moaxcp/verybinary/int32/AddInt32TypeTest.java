package com.github.moaxcp.verybinary.int32;

import com.github.moaxcp.verybinary.Int32ArrayType;
import org.junit.jupiter.api.Test;

import static com.github.moaxcp.verybinary.Builders.struct;
import static com.github.moaxcp.verybinary.ByteArray.ba;
import static com.github.moaxcp.verybinary.Expression.constant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class AddInt32TypeTest {
  @Test
  void addInt32Wrapper() {
    var struct = struct()
        .int32()
        .int32Array(0)
        .build();

    assertThatThrownBy(() -> ((Int32ArrayType) struct.getType(1)).add(struct, Integer.valueOf(1)))
        .isInstanceOf(UnsupportedOperationException.class)
        .hasMessage("add(Pointer, Integer) not supported for Int32ArrayType. Use add(Pointer, int) instead.");
  }

  @Test
  void addInt32Wrapper_with_index() {
    var struct = struct()
        .int32()
        .int32Array(0)
        .build();

    assertThatThrownBy(() -> ((Int32ArrayType) struct.getType(1)).add(struct, 0, Integer.valueOf(1)))
        .isInstanceOf(UnsupportedOperationException.class)
        .hasMessage("add(Pointer, long, Integer) not supported for Int32ArrayType. Use add(Pointer, long, int) instead.");
  }

  @Test
  void addInt32() {
    var struct = struct()
        .int32()
        .int32Array(0)
        .fromBytes(ba().int32(2, 1, 2))
        .build();

    struct.addInt32(1, 3);
    struct.addInt32(1, 4);

    assertThat(struct.getByteArray()).isEqualTo(ba().int32(4, 1, 2, 3, 4));
  }

  @Test
  void addInt32_position_negative() {
    var struct = struct()
        .int32()
        .int32Array(0)
        .fromBytes(ba().int32(2, 1, 2))
        .build();

    assertThatThrownBy(() -> struct.addInt32(-1, 3))
        .isInstanceOf(IndexOutOfBoundsException.class)
        .hasMessage("Index -1 out of bounds for length 2");
  }

  @Test
  void addInt32_position_greater_than_length() {
    var struct = struct()
        .int32()
        .int32Array(0)
        .fromBytes(ba().int32(2, 1, 2))
        .build();

    assertThatThrownBy(() -> struct.addInt32(3, 3))
        .isInstanceOf(IndexOutOfBoundsException.class)
        .hasMessage("Index 3 out of bounds for length 2");
  }

  @Test
  void addInt32_not_allocated() {
    var struct = struct()
        .allocated()
        .int32()
        .int32Array(0)
        .build();
    assertThatThrownBy(() -> struct.addInt32(1, 3))
        .isInstanceOf(IndexOutOfBoundsException.class)
        .hasMessage("allocated: 0, index: 0, length: 4");
  }

  @Test
  void addInt32Array_constant() {
    var struct = struct()
        .primitive().constant(5).lengthExpression(constant(5)).int32()
        .build();

    assertThatThrownBy(() -> struct.addInt32(0, 3))
        .isInstanceOf(IllegalStateException.class)
        .hasMessage("Int32ArrayType at position 0 is constant length: 5 index: 5");
  }

  @Test
  void addInt32_not_array() {
    var struct = struct()
        .int32()
        .fromBytes(ba().int32(1))
        .build();
    assertThatThrownBy(() -> struct.addInt32(0, 3))
        .isInstanceOf(ArrayIndexOutOfBoundsException.class)
        .hasMessage("Int32ArrayType cannot add to non-array type at position 0 index: 1 length: 1");
  }

  @Test
  void addInt32_with_index() {
    var struct = struct()
        .int32()
        .int32Array(0)
        .fromBytes(ba().int32(2, 1, 2))
        .build();

    struct.addInt32(1, 0, 3);
    struct.addInt32(1, 1, 4);

    assertThat(struct.getByteArray()).isEqualTo(ba().int32(4, 3, 4, 1, 2));
  }

  @Test
  void addInt32_with_index_negative() {
    var struct = struct()
        .int32()
        .int32Array(0)
        .fromBytes(ba().int32(2, 1, 2))
        .build();

    assertThatThrownBy(() -> struct.addInt32(1, -1, 3))
        .isInstanceOf(ArrayIndexOutOfBoundsException.class)
        .hasMessage("Int32ArrayType at position 1 index: -1 new length: 3");
  }

  @Test
  void addInt32_with_index_greater_than_length() {
    var struct = struct()
        .int32()
        .int32Array(0)
        .fromBytes(ba().int32(2, 1, 2))
        .build();

    assertThatThrownBy(() -> struct.addInt32(1, 3, 3))
        .isInstanceOf(ArrayIndexOutOfBoundsException.class)
        .hasMessage("Int32ArrayType at position 1 index: 3 new length: 3");
  }

  @Test
  void addInt32_with_index_not_allocated() {
    var struct = struct()
        .allocated()
        .int32()
        .int32Array(0)
        .build();

    assertThatThrownBy(() -> struct.addInt32(1, 0, 3))
        .isInstanceOf(IndexOutOfBoundsException.class)
        .hasMessage("allocated: 0, index: 0, length: 4");
  }

  @Test
  void addInt32_with_index_0_not_array() {
    var struct = struct()
        .int32()
        .fromBytes(ba().int32(1))
        .build();

    assertThatThrownBy(() -> struct.addInt32(0, 0, 3))
        .isInstanceOf(ArrayIndexOutOfBoundsException.class)
        .hasMessage("Int32ArrayType cannot add to non-array type at position 0 index: 0 length: 1");
  }

  @Test
  void addInt32_with_index_1_not_array() {
    var struct = struct()
        .int32()
        .fromBytes(ba().int32(1))
        .build();

    assertThatThrownBy(() -> struct.addInt32(0, 1, 3))
        .isInstanceOf(ArrayIndexOutOfBoundsException.class)
        .hasMessage("Int32ArrayType cannot add to non-array type at position 0 index: 1 length: 1");
  }

  @Test
  void addInt32Array_with_index_constant() {
    var struct = struct()
        .primitive().constant(5).lengthExpression(constant(5)).int32()
        .build();

    assertThatThrownBy(() -> struct.addInt32(0, 3, 3))
        .isInstanceOf(IllegalStateException.class)
        .hasMessage("Int32ArrayType at position 0 is constant length: 5 index: 3");
  }
}
