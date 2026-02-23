package com.github.moaxcp.verybinary.int64;

import com.github.moaxcp.verybinary.Int64ArrayType;
import com.github.moaxcp.verybinary.Int64Type;
import org.junit.jupiter.api.Test;

import static com.github.moaxcp.verybinary.Builders.struct;
import static com.github.moaxcp.verybinary.ByteArray.ba;
import static com.github.moaxcp.verybinary.Expression.constant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class GetInt64TypeTest {
  @Test
  void getWrapper() {
    var struct = struct()
        .int64()
        .build();

    assertThatThrownBy(() -> ((Int64Type) struct.getType(0)).get(struct))
        .isInstanceOf(UnsupportedOperationException.class)
        .hasMessage("get(Pointer) not supported for Int64Type. Use getInt64(Pointer) instead.");
  }

  @Test
  void getInt64() {
    var struct = struct()
        .int64()
        .build();

    struct.setInt64(0, 2L);

    assertThat(struct.getInt64(0)).isEqualTo(2L);
  }

  @Test
  void getInt64_position_negative() {
    var struct = struct()
        .int64()
        .build();

    assertThatThrownBy(() -> struct.getInt64(-1))
        .isInstanceOf(IndexOutOfBoundsException.class)
        .hasMessage("Index -1 out of bounds for length 1");
  }

  @Test
  void getInt64_position_greater_than_length() {
    var struct = struct()
        .int64()
        .build();

    assertThatThrownBy(() -> struct.getInt64(2))
        .isInstanceOf(IndexOutOfBoundsException.class)
        .hasMessage("Index 2 out of bounds for length 1");
  }

  @Test
  void getInt64Allocated() {
    var struct = struct()
        .int64()
        .build();

    assertThat(struct.getInt64(0)).isEqualTo(0L);
    assertThat(struct.getByteArray()).isEqualTo(ba().int64(0));
  }

  @Test
  void getInt64NotAllocated() {
    var struct = struct()
        .allocated()
        .int64()
        .build();

    assertThatThrownBy(() -> struct.getInt64(0))
        .isInstanceOf(IndexOutOfBoundsException.class)
        .hasMessage("allocated: 0, index: 0, length: 8");
  }

  @Test
  void getInt64_constant() {
    var struct = struct()
        .primitive().constant(5L).int64()
        .build();

    assertThat(struct.getInt64(0)).isEqualTo(5L);
  }

  @Test
  void getInt64_index_0_not_array() {
    var struct = struct()
        .int64()
        .fromBytes(ba().int64(1))
        .build();

    assertThat(struct.getInt64(0, 0)).isEqualTo(1L);
  }

  @Test
  void getInt64_index_1_not_array() {
    var struct = struct()
        .int64()
        .fromBytes(ba().int64(1))
        .build();

    assertThatThrownBy(() -> struct.getInt64(0, 1))
        .isInstanceOf(ArrayIndexOutOfBoundsException.class)
        .hasMessage("Int64ArrayType at position 0 index: 1 length: 1");
  }

  @Test
  void getArrayWrapper() {
    var struct = struct()
        .int64()
        .int64Array(0)
        .build();

    assertThatThrownBy(() -> ((Int64ArrayType) struct.getType(1)).get(struct, 0))
        .isInstanceOf(UnsupportedOperationException.class)
        .hasMessage("get(Pointer, long) not supported for Int64ArrayType. Use getInt64(Pointer, long) instead.");
  }

  @Test
  void getInt64Array() {
    var struct = struct()
        .int64()
        .int64Array(0)
        .fromBytes(ba().int64(2, 1, 2))
        .build();

    assertThat(struct.getInt64(1, 0)).isEqualTo(1L);
    assertThat(struct.getInt64(1, 1)).isEqualTo(2L);
  }

  @Test
  void getInt64Array_negative() {
    var struct = struct()
        .int64()
        .int64Array(0)
        .build();

    assertThatThrownBy(() -> struct.getInt64(1, -1))
        .isInstanceOf(ArrayIndexOutOfBoundsException.class)
        .hasMessage("Int64ArrayType at position 1 index: -1 length: 0");
  }

  @Test
  void getInt64Array_greater_than_length() {
    var struct = struct()
        .int64()
        .int64Array(0)
        .fromBytes(ba().int64(2, 1, 2))
        .build();

    assertThatThrownBy(() -> struct.getInt64(1, 2))
        .isInstanceOf(ArrayIndexOutOfBoundsException.class)
        .hasMessage("Int64ArrayType at position 1 index: 2 length: 2");
  }

  @Test
  void getInt64Array_not_allocated() {
    var struct = struct()
        .allocated()
        .int64()
        .int64Array(0)
        .build();

    assertThatThrownBy(() -> struct.getInt64(1, 0))
        .isInstanceOf(IndexOutOfBoundsException.class)
        .hasMessage("allocated: 0, index: 0, length: 8");
  }

  @Test
  void getInt64Array_constant() {
    var struct = struct()
        .primitive().constant(5L).lengthExpression(constant(5)).int64()
        .build();

    assertThat(struct.getInt64(0, 3)).isEqualTo(5L);
  }
}
