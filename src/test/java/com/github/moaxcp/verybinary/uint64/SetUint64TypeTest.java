package com.github.moaxcp.verybinary.uint64;

import org.junit.jupiter.api.Test;

import java.math.BigInteger;

import static com.github.moaxcp.verybinary.Builders.struct;
import static com.github.moaxcp.verybinary.ByteArray.ba;
import static com.github.moaxcp.verybinary.Expression.constant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class SetUint64TypeTest {
  @Test
  void setUint64() {
    var struct = struct()
        .uint64()
        .build();

    struct.setUint64(0, BigInteger.valueOf(2));

    assertThat(struct.getByteArray()).isEqualTo(ba().uint64(2));
  }

  @Test
  void setUint64_position_negative() {
    var struct = struct()
        .uint64()
        .build();

    assertThatThrownBy(() -> struct.setUint64(-1, BigInteger.valueOf(2)))
        .isInstanceOf(IndexOutOfBoundsException.class)
        .hasMessage("Index -1 out of bounds for length 1");
  }

  @Test
  void setUint64_position_greater_than_length() {
    var struct = struct()
        .uint64()
        .build();

    assertThatThrownBy(() -> struct.setUint64(2, BigInteger.valueOf(2)))
        .isInstanceOf(IndexOutOfBoundsException.class)
        .hasMessage("Index 2 out of bounds for length 1");
  }

  @Test
  void setUint64_not_allocated() {
    var struct = struct()
        .allocated()
        .uint64()
        .build();

    assertThatThrownBy(() -> struct.setUint64(0, BigInteger.valueOf(2)))
        .isInstanceOf(ArrayIndexOutOfBoundsException.class)
        .hasMessage("Index 0 out of bounds for length 0");
  }

  @Test
  void setUint64_constant() {
    var struct = struct()
        .primitive().constant(BigInteger.valueOf(5)).uint64()
        .build();

    assertThatThrownBy(() -> struct.setUint64(0, BigInteger.valueOf(2)))
        .isInstanceOf(UnsupportedOperationException.class)
        .hasMessage("Uint64Type at position 0 is constant index: 0 value: 2 constant: 5");
  }

  @Test
  void setUint64Array() {
    var struct = struct()
        .uint64()
        .uint64Array(0)
        .uint64()
        .fromBytes(ba().uint64(1, 2, 3))
        .build();

    struct.setUint64(1, 0, BigInteger.valueOf(5));

    assertThat(struct.getUint64(0)).isEqualTo(BigInteger.ONE);
    assertThat(struct.getUint64(1, 0)).isEqualTo(BigInteger.valueOf(5));
    assertThat(struct.getByteArray()).isEqualTo(ba().uint64(1, 5, 3));
  }

  @Test
  void setUint64Array_negative() {
    var struct = struct()
        .uint64()
        .uint64Array(0)
        .uint64()
        .fromBytes(ba().uint64(1, 2, 3))
        .build();

    assertThatThrownBy(() -> struct.setUint64(1, -1, BigInteger.valueOf(5)))
        .isInstanceOf(IndexOutOfBoundsException.class)
        .hasMessage("Uint64Type at position 1 index: -1 length: 1");
  }

  @Test
  void setUint64Array_greater_than_length() {
    var struct = struct()
        .uint64()
        .uint64Array(0)
        .uint64()
        .fromBytes(ba().uint64(1, 2, 3))
        .build();

    assertThatThrownBy(() -> struct.setUint64(1, 2, BigInteger.valueOf(5)))
        .isInstanceOf(ArrayIndexOutOfBoundsException.class)
        .hasMessage("Uint64Type at position 1 index: 2 length: 1");

    assertThat(struct.getUint64(0)).isEqualTo(BigInteger.ONE);
    assertThat(struct.getUint64(1, 0)).isEqualTo(BigInteger.valueOf(2));
    assertThat(struct.getByteArray()).isEqualTo(ba().uint64(1, 2, 3));
  }

  @Test
  void setUint64Array_not_allocated() {
    var struct = struct()
        .allocated()
        .uint64()
        .uint64Array(0)
        .build();

    assertThatThrownBy(() -> struct.setUint64(1, 0, BigInteger.valueOf(5)))
        .isInstanceOf(IndexOutOfBoundsException.class)
        .hasMessage("allocated: 0, index: 0, length: 8");
  }

  @Test
  void setUint64Array_index_0_not_array() {
    var struct = struct()
        .uint64()
        .build();

    struct.setUint64(0, 0, BigInteger.valueOf(5));

    assertThat(struct.getByteArray()).isEqualTo(ba().uint64(5));
  }

  @Test
  void setUint64Array_index_1_not_array() {
    var struct = struct()
        .uint64()
        .build();

    assertThatThrownBy(() -> struct.setUint64(0, 1, BigInteger.valueOf(5)))
        .isInstanceOf(ArrayIndexOutOfBoundsException.class)
        .hasMessage("Uint64Type at position 0 index: 1 length: 1");
  }

  @Test
  void setUint64Array_constant_value_and_length() {
    var struct = struct()
        .primitive().constant(BigInteger.valueOf(5)).lengthExpression(constant(5)).uint64()
        .build();

    assertThatThrownBy(() -> struct.setUint64(0, 3, BigInteger.valueOf(2)))
        .isInstanceOf(UnsupportedOperationException.class)
        .hasMessage("Uint64Type at position 0 is constant index: 3 value: 2 constant: 5");
  }

  @Test
  void setUint64Array_constant_value() {
    var struct = struct()
        .uint64()
        .primitive().constant(BigInteger.valueOf(5)).lengthField(0).uint64()
        .fromBytes(ba().uint64(2, 5, 5))
        .build();

    struct.setUint64(1, 1, BigInteger.valueOf(2));

    assertThat(struct.getByteArray()).isEqualTo(ba().uint64(2, 5, 2));
  }

  @Test
  void setUint64Array_constant_value_same() {
    var struct = struct()
        .uint64()
        .primitive().constant(BigInteger.valueOf(5)).lengthField(0).uint64()
        .fromBytes(ba().uint64(2, 5, 5))
        .build();

    struct.setUint64(1, 1, BigInteger.valueOf(5));

    assertThat(struct.getByteArray()).isEqualTo(ba().uint64(2, 5, 5));
  }

  @Test
  void setUint64Array_set_length_field_without_adding_to_array() {
    var struct = struct()
        .uint64()
        .uint64Array(0)
        .build();

    struct.setUint64(0, BigInteger.valueOf(2));

    assertThat(struct.getByteArray()).isEqualTo(ba().uint64(2, 0, 0));
  }
}
