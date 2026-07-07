package com.github.moaxcp.verybinary.uint64;

import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.util.List;

import static com.github.moaxcp.verybinary.Builders.struct;
import static com.github.moaxcp.verybinary.ByteArray.ba;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class SetUint64TypeTest {
  @Test
  void set() {
    var struct = struct()
        .uint64()
        .build();

    struct.set(0, BigInteger.valueOf(2));

    assertThat(struct.getByteArray()).isEqualTo(ba().uint64(2));
  }

  @Test
  void set_position_negative() {
    var struct = struct()
        .uint64()
        .build();

    assertThatThrownBy(() -> struct.set(-1, BigInteger.valueOf(2)))
        .isInstanceOf(IndexOutOfBoundsException.class)
        .hasMessage("Index -1 out of bounds for length 1");
  }

  @Test
  void set_position_greater_than_length() {
    var struct = struct()
        .uint64()
        .build();

    assertThatThrownBy(() -> struct.set(2, BigInteger.valueOf(2)))
        .isInstanceOf(IndexOutOfBoundsException.class)
        .hasMessage("Index 2 out of bounds for length 1");
  }

  @Test
  void set_not_allocated() {
    var struct = struct()
        .allocated()
        .uint64()
        .build();

    assertThatThrownBy(() -> struct.set(0, BigInteger.valueOf(2)))
        .isInstanceOf(ArrayIndexOutOfBoundsException.class)
        .hasMessage("Index 0 out of bounds for length 0");
  }

  @Test
  void set_constant() {
    var struct = struct()
        .primitive().constant(BigInteger.valueOf(5)).uint64()
        .build();

    assertThatThrownBy(() -> struct.set(0, BigInteger.valueOf(2)))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Uint64Type at position 0 is constant value: 2 constant: 5");
  }

  @Test
  void setArray() {
    var struct = struct()
        .uint64()
        .uint64Array(0)
        .uint64()
        .fromBytes(ba().uint64(1, 2, 3))
        .build();

    struct.set(1, 0, BigInteger.valueOf(5));

    assertThat((BigInteger) struct.get(0)).isEqualTo(BigInteger.ONE);
    assertThat((BigInteger) struct.get(1, 0)).isEqualTo(BigInteger.valueOf(5));
    assertThat(struct.getByteArray()).isEqualTo(ba().uint64(1, 5, 3));
  }

  @Test
  void setArray_negative() {
    var struct = struct()
        .uint64()
        .uint64Array(0)
        .uint64()
        .fromBytes(ba().uint64(1, 2, 3))
        .build();

    assertThatThrownBy(() -> struct.set(1, -1, BigInteger.valueOf(5)))
        .isInstanceOf(IndexOutOfBoundsException.class)
        .hasMessage("Uint64ListType at position 1 index: -1 length: 1");
  }

  @Test
  void setArray_greater_than_length() {
    var struct = struct()
        .uint64()
        .uint64Array(0)
        .uint64()
        .fromBytes(ba().uint64(1, 2, 3))
        .build();

    assertThatThrownBy(() -> struct.set(1, 2, BigInteger.valueOf(5)))
        .isInstanceOf(ArrayIndexOutOfBoundsException.class)
        .hasMessage("Uint64ListType at position 1 index: 2 length: 1");

    assertThat(struct.getUint64(0)).isEqualTo(BigInteger.ONE);
    assertThat(struct.getUint64(1, 0)).isEqualTo(BigInteger.valueOf(2));
    assertThat(struct.getByteArray()).isEqualTo(ba().uint64(1, 2, 3));
  }

  @Test
  void setArray_not_allocated() {
    var struct = struct()
        .allocated()
        .uint64()
        .uint64Array(0)
        .build();

    assertThatThrownBy(() -> struct.set(1, 0, BigInteger.valueOf(5)))
        .isInstanceOf(IndexOutOfBoundsException.class)
        .hasMessage("allocated: 0, index: 0, length: 8");
  }

  @Test
  void setArray_index_0_not_array() {
    var struct = struct()
        .uint64()
        .build();

    assertThatThrownBy(() -> struct.set(0, 0, BigInteger.valueOf(5)))
        .isInstanceOf(ClassCastException.class);
  }

  @Test
  void setArray_constant_value_and_length() {
    var struct = struct()
        .primitive().constant(List.of(BigInteger.valueOf(5), BigInteger.valueOf(5), BigInteger.valueOf(5), BigInteger.valueOf(5), BigInteger.valueOf(5))).uint64()
        .build();

    assertThatThrownBy(() -> struct.set(0, 3, BigInteger.valueOf(2)))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Uint64ListType at position 0 is constant index: 3 value: 2 constant: [5, 5, 5, 5, 5]");
  }

  @Test
  void setArray_constant_value() {
    var struct = struct()
        .uint64()
        .primitive().constant(List.of(BigInteger.valueOf(5), BigInteger.valueOf(5), BigInteger.valueOf(5), BigInteger.valueOf(5), BigInteger.valueOf(5))).uint64()
        .fromBytes(ba().uint64(2, 5, 5))
        .build();

    assertThatThrownBy(() -> struct.set(1, 1, BigInteger.valueOf(2)))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Uint64ListType at position 1 is constant index: 1 value: 2 constant: [5, 5, 5, 5, 5]");
  }

  @Test
  void setArray_constant_value_same() {
    var struct = struct()
        .uint64()
        .primitive().constant(List.of(BigInteger.valueOf(5), BigInteger.valueOf(5), BigInteger.valueOf(5), BigInteger.valueOf(5), BigInteger.valueOf(5))).uint64()
        .fromBytes(ba().uint64(2, 5, 5))
        .build();

    struct.set(1, 1, BigInteger.valueOf(5));

    assertThat(struct.getByteArray()).isEqualTo(ba().uint64(2, 5, 5));
  }

  @Test
  void setArray_set_length_field_without_adding_to_array() {
    var struct = struct()
        .uint64()
        .uint64Array(0)
        .build();

    struct.set(0, BigInteger.valueOf(2));

    assertThat(struct.getByteArray()).isEqualTo(ba().uint64(2, 0, 0));
  }
}
