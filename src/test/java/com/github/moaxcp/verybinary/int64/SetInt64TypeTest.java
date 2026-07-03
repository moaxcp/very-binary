package com.github.moaxcp.verybinary.int64;

import com.github.moaxcp.verybinary.Int64ListType;
import com.github.moaxcp.verybinary.Int64Type;
import org.junit.jupiter.api.Test;

import static com.github.moaxcp.verybinary.Builders.struct;
import static com.github.moaxcp.verybinary.ByteArray.ba;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class SetInt64TypeTest {
  @Test
  void setWrapper() {
    var struct = struct()
        .int64()
        .build();

    assertThatThrownBy(() -> ((Int64Type) struct.getType(0)).set(struct, Long.valueOf(2L)))
        .isInstanceOf(UnsupportedOperationException.class)
        .hasMessage("set(Pointer, Long) not supported for Int64Type. Use set(Pointer, long) instead.");
  }

  @Test
  void setInt64() {
    var struct = struct()
        .int64()
        .build();

    struct.setInt64(0, 2L);

    assertThat(struct.getByteArray()).isEqualTo(ba().int64(2));
  }

  @Test
  void setInt64_position_negative() {
    var struct = struct()
        .int64()
        .build();

    assertThatThrownBy(() -> struct.setInt64(-1, 2L))
        .isInstanceOf(IndexOutOfBoundsException.class)
        .hasMessage("Index -1 out of bounds for length 1");
  }

  @Test
  void setInt64_position_greater_than_length() {
    var struct = struct()
        .int64()
        .build();

    assertThatThrownBy(() -> struct.setInt64(2, 2L))
        .isInstanceOf(IndexOutOfBoundsException.class)
        .hasMessage("Index 2 out of bounds for length 1");
  }

  @Test
  void setInt64_not_allocated() {
    var struct = struct()
        .allocated()
        .int64()
        .build();

    assertThatThrownBy(() -> struct.setInt64(0, 2L))
        .isInstanceOf(ArrayIndexOutOfBoundsException.class)
        .hasMessage("Index 0 out of bounds for length 0");
  }

  @Test
  void setInt64_constant() {
    var struct = struct()
        .primitive().constant(5L).int64()
        .build();

    assertThatThrownBy(() -> struct.setInt64(0, 2L))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Int64Type at position 0 is constant value: 2 constant: 5");
  }

  @Test
  void setArrayWrapper() {
    var struct = struct()
        .int64()
        .int64Array(0)
        .build();

    assertThatThrownBy(() -> ((Int64ListType) struct.getType(1)).set(struct, 0, Long.valueOf(2L)))
        .isInstanceOf(UnsupportedOperationException.class)
        .hasMessage("set(Pointer, long, Long) not supported for Int64ListType. Use set(Pointer, long, long) instead.");
  }

  @Test
  void setInt64Array() {
    var struct = struct()
        .int64()
        .int64Array(0)
        .int64()
        .fromBytes(ba().int64(1, 2, 3))
        .build();

    struct.setInt64(1, 0, 5L);

    assertThat(struct.getInt64(0)).isEqualTo(1L);
    assertThat(struct.getInt64(1, 0)).isEqualTo(5L);
    assertThat(struct.getByteArray()).isEqualTo(ba().int64(1, 5, 3));
  }

  @Test
  void setInt64Array_negative() {
    var struct = struct()
        .int64()
        .int64Array(0)
        .int64()
        .fromBytes(ba().int64(1, 2, 3))
        .build();

    assertThatThrownBy(() -> struct.setInt64(1, -1, 5L))
        .isInstanceOf(IndexOutOfBoundsException.class)
        .hasMessage("Int64ListType at position 1 index: -1 length: 1");
  }

  @Test
  void setInt64Array_greater_than_length() {
    var struct = struct()
        .int64()
        .int64Array(0)
        .int64()
        .fromBytes(ba().int64(1, 2, 3))
        .build();

    assertThatThrownBy(() -> struct.setInt64(1, 2, 5L))
        .isInstanceOf(ArrayIndexOutOfBoundsException.class)
        .hasMessage("Int64ListType at position 1 index: 2 length: 1");

    assertThat(struct.getInt64(0)).isEqualTo(1L);
    assertThat(struct.getInt64(1, 0)).isEqualTo(2L);
    assertThat(struct.getByteArray()).isEqualTo(ba().int64(1, 2, 3));
  }

  @Test
  void setInt64Array_not_allocated() {
    var struct = struct()
        .allocated()
        .int64()
        .int64Array(0)
        .build();

    assertThatThrownBy(() -> struct.setInt64(1, 0, 2L))
        .isInstanceOf(IndexOutOfBoundsException.class)
        .hasMessage("allocated: 0, index: 0, length: 8");
  }

  @Test
  void setInt64Array_index_0_not_array() {
    var struct = struct()
        .int64()
        .build();

    assertThatThrownBy(() -> struct.setInt64(0, 0, 2L))
        .isInstanceOf(ClassCastException.class);
  }

  @Test
  void setInt64Array_constant_value_and_length() {
    var struct = struct()
        .primitive().constant(new long[]{5, 5, 5, 5, 5}).int64()
        .build();

    assertThatThrownBy(() -> struct.setInt64(0, 3, 2L))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Int64ListType at position 0 is constant index: 3 value: 2 constant: 5");
  }

  @Test
  void setInt64Array_constant_value() {
    var struct = struct()
        .int64()
        .primitive().constant(new long[]{5, 5}).int64()
        .fromBytes(ba().int64(2, 5, 5))
        .build();

    assertThatThrownBy(() -> struct.setInt64(1, 1, 2))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Int64ListType at position 1 is constant index: 1 value: 2 constant: 5");
  }

  @Test
  void setInt64Array_constant_value_same() {
    var struct = struct()
        .int64()
        .primitive().constant(new long[]{5, 5, 5, 5, 5}).int64()
        .fromBytes(ba().int64(2, 5, 5))
        .build();

    struct.setInt64(1, 1, 5L);

    assertThat(struct.getByteArray()).isEqualTo(ba().int64(2, 5, 5));
  }

  @Test
  void setInt64Array_set_length_field_without_adding_to_array() {
    var struct = struct()
        .int64()
        .int64Array(0)
        .build();

    struct.setInt64(0, 2);

    assertThat(struct.getByteArray()).isEqualTo(ba().int64(2, 0, 0));
  }
}
