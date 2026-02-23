package com.github.moaxcp.verybinary.int64;

import com.github.moaxcp.verybinary.Int64ArrayType;
import org.junit.jupiter.api.Test;

import static com.github.moaxcp.verybinary.Builders.struct;
import static com.github.moaxcp.verybinary.ByteArray.ba;
import static com.github.moaxcp.verybinary.Expression.constant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class AddInt64TypeTest {
  @Test
  void addInt64Wrapper() {
    var struct = struct()
        .int64()
        .int64Array(0)
        .build();

    assertThatThrownBy(() -> ((Int64ArrayType) struct.getType(1)).add(struct, Long.valueOf(1L)))
        .isInstanceOf(UnsupportedOperationException.class)
        .hasMessage("add(Pointer, Long) not supported for Int64ArrayType. Use add(Pointer, long) instead.");
  }

  @Test
  void addInt64Wrapper_with_index() {
    var struct = struct()
        .int64()
        .int64Array(0)
        .build();

    assertThatThrownBy(() -> ((Int64ArrayType) struct.getType(1)).add(struct, 0, Long.valueOf(1L)))
        .isInstanceOf(UnsupportedOperationException.class)
        .hasMessage("add(Pointer, long, Long) not supported for Int64ArrayType. Use add(Pointer, long, long) instead.");
  }

  @Test
  void addInt64() {
    var struct = struct()
        .int64()
        .int64Array(0)
        .fromBytes(ba().int64(2, 1, 2))
        .build();

    struct.addInt64(1, 3L);
    struct.addInt64(1, 4L);

    assertThat(struct.getByteArray()).isEqualTo(ba().int64(4, 1, 2, 3, 4));
  }

  @Test
  void addInt64_position_negative() {
    var struct = struct()
        .int64()
        .int64Array(0)
        .fromBytes(ba().int64(2, 1, 2))
        .build();

    assertThatThrownBy(() -> struct.addInt64(-1, 3L))
        .isInstanceOf(IndexOutOfBoundsException.class)
        .hasMessage("Index -1 out of bounds for length 2");
  }

  @Test
  void addInt64_position_greater_than_length() {
    var struct = struct()
        .int64()
        .int64Array(0)
        .fromBytes(ba().int64(2, 1, 2))
        .build();

    assertThatThrownBy(() -> struct.addInt64(3, 3L))
        .isInstanceOf(IndexOutOfBoundsException.class)
        .hasMessage("Index 3 out of bounds for length 2");
  }

  @Test
  void addInt64_not_allocated() {
    var struct = struct()
        .allocated()
        .int64()
        .int64Array(0)
        .build();
    assertThatThrownBy(() -> struct.addInt64(1, 3L))
        .isInstanceOf(IndexOutOfBoundsException.class)
        .hasMessage("allocated: 0, index: 0, length: 8");
  }

  @Test
  void addInt64Array_constant() {
    var struct = struct()
        .primitive().constant(5L).lengthExpression(constant(5)).int64()
        .build();

    assertThatThrownBy(() -> struct.addInt64(0, 3L))
        .isInstanceOf(IllegalStateException.class)
        .hasMessage("Int64ArrayType at position 0 is constant length: 5 index: 5");
  }

  @Test
  void addInt64_not_array() {
    var struct = struct()
        .int64()
        .fromBytes(ba().int64(1))
        .build();
    assertThatThrownBy(() -> struct.addInt64(0, 3L))
        .isInstanceOf(ArrayIndexOutOfBoundsException.class)
        .hasMessage("Int64ArrayType cannot add to non-array type at position 0 index: 1 length: 1");
  }

  @Test
  void addInt64_with_index() {
    var struct = struct()
        .int64()
        .int64Array(0)
        .fromBytes(ba().int64(2, 1, 2))
        .build();

    struct.addInt64(1, 0, 3L);
    struct.addInt64(1, 1, 4L);

    assertThat(struct.getByteArray()).isEqualTo(ba().int64(4, 3, 4, 1, 2));
  }

  @Test
  void addInt64_with_index_negative() {
    var struct = struct()
        .int64()
        .int64Array(0)
        .fromBytes(ba().int64(2, 1, 2))
        .build();

    assertThatThrownBy(() -> struct.addInt64(1, -1, 3L))
        .isInstanceOf(ArrayIndexOutOfBoundsException.class)
        .hasMessage("Int64ArrayType at position 1 index: -1 new length: 3");
  }

  @Test
  void addInt64_with_index_greater_than_length() {
    var struct = struct()
        .int64()
        .int64Array(0)
        .fromBytes(ba().int64(2, 1, 2))
        .build();

    assertThatThrownBy(() -> struct.addInt64(1, 3, 3L))
        .isInstanceOf(ArrayIndexOutOfBoundsException.class)
        .hasMessage("Int64ArrayType at position 1 index: 3 new length: 3");
  }

  @Test
  void addInt64_with_index_not_allocated() {
    var struct = struct()
        .allocated()
        .int64()
        .int64Array(0)
        .build();

    assertThatThrownBy(() -> struct.addInt64(1, 0, 3L))
        .isInstanceOf(IndexOutOfBoundsException.class)
        .hasMessage("allocated: 0, index: 0, length: 8");
  }

  @Test
  void addInt64_with_index_0_not_array() {
    var struct = struct()
        .int64()
        .fromBytes(ba().int64(1))
        .build();

    assertThatThrownBy(() -> struct.addInt64(0, 0, 3L))
        .isInstanceOf(ArrayIndexOutOfBoundsException.class)
        .hasMessage("Int64ArrayType cannot add to non-array type at position 0 index: 0 length: 1");
  }

  @Test
  void addInt64_with_index_1_not_array() {
    var struct = struct()
        .int64()
        .fromBytes(ba().int64(1))
        .build();

    assertThatThrownBy(() -> struct.addInt64(0, 1, 3L))
        .isInstanceOf(ArrayIndexOutOfBoundsException.class)
        .hasMessage("Int64ArrayType cannot add to non-array type at position 0 index: 1 length: 1");
  }

  @Test
  void addInt64Array_with_index_constant() {
    var struct = struct()
        .primitive().constant(5L).lengthExpression(constant(5)).int64()
        .build();

    assertThatThrownBy(() -> struct.addInt64(0, 3, 3L))
        .isInstanceOf(IllegalStateException.class)
        .hasMessage("Int64ArrayType at position 0 is constant length: 5 index: 3");
  }
}
