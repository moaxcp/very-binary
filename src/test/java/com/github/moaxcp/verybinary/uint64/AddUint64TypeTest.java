package com.github.moaxcp.verybinary.uint64;

import org.junit.jupiter.api.Test;

import java.math.BigInteger;

import static com.github.moaxcp.verybinary.Builders.struct;
import static com.github.moaxcp.verybinary.ByteArray.ba;
import static com.github.moaxcp.verybinary.Expression.constant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class AddUint64TypeTest {
  @Test
  void addUint64() {
    var struct = struct()
        .uint64()
        .uint64Array(0)
        .fromBytes(ba().uint64(2, 1, 2))
        .build();

    struct.addUint64(1, BigInteger.valueOf(3));
    struct.addUint64(1, BigInteger.valueOf(4));

    assertThat(struct.getByteArray()).isEqualTo(ba().uint64(4, 1, 2, 3, 4));
  }

  @Test
  void addUint64_position_negative() {
    var struct = struct()
        .uint64()
        .uint64Array(0)
        .fromBytes(ba().uint64(2, 1, 2))
        .build();

    assertThatThrownBy(() -> struct.addUint64(-1, BigInteger.valueOf(3)))
        .isInstanceOf(IndexOutOfBoundsException.class)
        .hasMessage("Index -1 out of bounds for length 2");
  }

  @Test
  void addUint64_position_greater_than_length() {
    var struct = struct()
        .uint64()
        .uint64Array(0)
        .fromBytes(ba().uint64(2, 1, 2))
        .build();

    assertThatThrownBy(() -> struct.addUint64(3, BigInteger.valueOf(3)))
        .isInstanceOf(IndexOutOfBoundsException.class)
        .hasMessage("Index 3 out of bounds for length 2");
  }

  @Test
  void addUint64_not_allocated() {
    var struct = struct()
        .allocated()
        .uint64()
        .uint64Array(0)
        .build();
    assertThatThrownBy(() -> struct.addUint64(1, BigInteger.valueOf(3)))
        .isInstanceOf(IndexOutOfBoundsException.class)
        .hasMessage("allocated: 0, index: 0, length: 8");
  }

  @Test
  void addUint64Array_constant() {
    var struct = struct()
        .primitive().constant(BigInteger.valueOf(5)).lengthExpression(constant(5)).uint64()
        .build();

    assertThatThrownBy(() -> struct.addUint64(0, BigInteger.valueOf(3)))
        .isInstanceOf(IllegalStateException.class)
        .hasMessage("Uint64ArrayType at position 0 is constant length: 5 index: 5");
  }

  @Test
  void addUint64_not_array() {
    var struct = struct()
        .uint64()
        .fromBytes(ba().uint64(1))
        .build();
    assertThatThrownBy(() -> struct.addUint64(0, BigInteger.valueOf(3)))
        .isInstanceOf(ArrayIndexOutOfBoundsException.class)
        .hasMessage("Uint64Type cannot add to non-array type at position 0 index: 1 length: 1");
  }

  @Test
  void addUint64_with_index() {
    var struct = struct()
        .uint64()
        .uint64Array(0)
        .fromBytes(ba().uint64(2, 1, 2))
        .build();

    struct.addUint64(1, 0, BigInteger.valueOf(3));
    struct.addUint64(1, 1, BigInteger.valueOf(4));

    assertThat(struct.getByteArray()).isEqualTo(ba().uint64(4, 3, 4, 1, 2));
  }

  @Test
  void addUint64_with_index_negative() {
    var struct = struct()
        .uint64()
        .uint64Array(0)
        .fromBytes(ba().uint64(2, 1, 2))
        .build();

    assertThatThrownBy(() -> struct.addUint64(1, -1, BigInteger.valueOf(3)))
        .isInstanceOf(ArrayIndexOutOfBoundsException.class)
        .hasMessage("Uint64ArrayType at position 1 index: -1 new length: 3");
  }

  @Test
  void addUint64_with_index_greater_than_length() {
    var struct = struct()
        .uint64()
        .uint64Array(0)
        .fromBytes(ba().uint64(2, 1, 2))
        .build();

    assertThatThrownBy(() -> struct.addUint64(1, 3, BigInteger.valueOf(3)))
        .isInstanceOf(ArrayIndexOutOfBoundsException.class)
        .hasMessage("Uint64ArrayType at position 1 index: 3 new length: 3");
  }

  @Test
  void addUint64_with_index_not_allocated() {
    var struct = struct()
        .allocated()
        .uint64()
        .uint64Array(0)
        .build();

    assertThatThrownBy(() -> struct.addUint64(1, 0, BigInteger.valueOf(3)))
        .isInstanceOf(IndexOutOfBoundsException.class)
        .hasMessage("allocated: 0, index: 0, length: 8");
  }

  @Test
  void addUint64_with_index_0_not_array() {
    var struct = struct()
        .uint64()
        .fromBytes(ba().uint64(1))
        .build();

    assertThatThrownBy(() -> struct.addUint64(0, 0, BigInteger.valueOf(3)))
        .isInstanceOf(ArrayIndexOutOfBoundsException.class)
        .hasMessage("Uint64Type cannot add to non-array type at position 0 index: 0 length: 1");
  }

  @Test
  void addUint64_with_index_1_not_array() {
    var struct = struct()
        .uint64()
        .fromBytes(ba().uint64(1))
        .build();

    assertThatThrownBy(() -> struct.addUint64(0, 1, BigInteger.valueOf(3)))
        .isInstanceOf(ArrayIndexOutOfBoundsException.class)
        .hasMessage("Uint64ArrayType cannot add to non-array type at position 0 index: 1 length: 1");
  }

  @Test
  void addUint64Array_with_index_constant() {
    var struct = struct()
        .primitive().constant(BigInteger.valueOf(5)).lengthExpression(constant(5)).uint64()
        .build();

    assertThatThrownBy(() -> struct.addUint64(0, 3, BigInteger.valueOf(3)))
        .isInstanceOf(IllegalStateException.class)
        .hasMessage("Uint64ArrayType at position 0 is constant length: 5 index: 3");
  }
}
