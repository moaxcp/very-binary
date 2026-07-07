package com.github.moaxcp.verybinary.uint64;

import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.util.List;

import static com.github.moaxcp.verybinary.Builders.struct;
import static com.github.moaxcp.verybinary.ByteArray.ba;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class GetUint64TypeTest {
  @Test
  void getUint64() {
    var struct = struct()
        .uint64()
        .build();

    struct.set(0, BigInteger.valueOf(2));

    assertThat(struct.getUint64(0)).isEqualTo(BigInteger.valueOf(2));
  }

  @Test
  void getUint64_position_negative() {
    var struct = struct()
        .uint64()
        .build();

    assertThatThrownBy(() -> struct.getUint64(-1))
        .isInstanceOf(IndexOutOfBoundsException.class)
        .hasMessage("Index -1 out of bounds for length 1");
  }

  @Test
  void getUint64_position_greater_than_length() {
    var struct = struct()
        .uint64()
        .build();

    assertThatThrownBy(() -> struct.getUint64(2))
        .isInstanceOf(IndexOutOfBoundsException.class)
        .hasMessage("Index 2 out of bounds for length 1");
  }

  @Test
  void getUint64Allocated() {
    var struct = struct()
        .uint64()
        .build();

    assertThat(struct.getUint64(0)).isEqualTo(BigInteger.ZERO);
    assertThat(struct.getByteArray()).isEqualTo(ba().uint64(0));
  }

  @Test
  void getUint64NotAllocated() {
    var struct = struct()
        .allocated()
        .uint64()
        .build();

    assertThatThrownBy(() -> struct.getUint64(0))
        .isInstanceOf(IndexOutOfBoundsException.class)
        .hasMessage("allocated: 0, index: 0, length: 8");
  }

  @Test
  void getUint64_constant() {
    var struct = struct()
        .primitive().constant(BigInteger.valueOf(5)).uint64()
        .build();

    assertThat(struct.getUint64(0)).isEqualTo(BigInteger.valueOf(5));
  }

  @Test
  void getUint64_index_0_not_array() {
    var struct = struct()
        .uint64()
        .fromBytes(ba().uint64(1))
        .build();

    assertThatThrownBy(() -> struct.getUint64(0, 0))
        .isInstanceOf(ClassCastException.class);
  }

  @Test
  void getUint64Array() {
    var struct = struct()
        .uint64()
        .uint64Array(0)
        .fromBytes(ba().uint64(2, 1, 2))
        .build();

    assertThat(struct.getUint64(1, 0)).isEqualTo(BigInteger.ONE);
    assertThat(struct.getUint64(1, 1)).isEqualTo(BigInteger.valueOf(2));
  }

  @Test
  void getUint64Array_negative() {
    var struct = struct()
        .uint64()
        .uint64Array(0)
        .build();

    assertThatThrownBy(() -> struct.getUint64(1, -1))
        .isInstanceOf(ArrayIndexOutOfBoundsException.class)
        .hasMessage("Uint64ListType at position 1 index: -1 length: 0");
  }

  @Test
  void getUint64Array_greater_than_length() {
    var struct = struct()
        .uint64()
        .uint64Array(0)
        .fromBytes(ba().uint64(1, 2))
        .build();

    assertThatThrownBy(() -> struct.getUint64(1, 1))
        .isInstanceOf(ArrayIndexOutOfBoundsException.class)
        .hasMessage("Uint64ListType at position 1 index: 1 length: 1");
  }

  @Test
  void getUint64Array_not_allocated() {
    var struct = struct()
        .allocated()
        .uint64()
        .uint64Array(0)
        .build();

    assertThatThrownBy(() -> struct.getUint64(1, 0))
        .isInstanceOf(IndexOutOfBoundsException.class)
        .hasMessage("allocated: 0, index: 0, length: 8");
  }

  @Test
  void getUint64Array_constant() {
    var struct = struct()
        .primitive().constant(List.of(BigInteger.valueOf(5), BigInteger.valueOf(5), BigInteger.valueOf(5), BigInteger.valueOf(5), BigInteger.valueOf(5))).uint64()
        .build();

    assertThat(struct.getUint64(0, 3)).isEqualTo(BigInteger.valueOf(5));
  }
}
