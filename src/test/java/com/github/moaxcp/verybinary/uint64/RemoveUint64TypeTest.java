package com.github.moaxcp.verybinary.uint64;

import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.util.List;

import static com.github.moaxcp.verybinary.Builders.struct;
import static com.github.moaxcp.verybinary.ByteArray.ba;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class RemoveUint64TypeTest {
  @Test
  void removeUint64() {
    var struct = struct()
        .uint64()
        .uint64Array(0)
        .fromBytes(ba().uint64(2, 1, 2))
        .build();

    struct.remove(1);

    assertThat(struct.getByteArray()).isEqualTo(ba().int64(0));
  }

  @Test
  void removeUint64_position_negative() {
    var struct = struct()
        .uint64()
        .uint64Array(0)
        .build();

    assertThatThrownBy(() -> struct.remove(-1))
        .isInstanceOf(IndexOutOfBoundsException.class)
        .hasMessage("Index -1 out of bounds for length 2");
  }

  @Test
  void removeUint64_position_greater_than_length() {
    var struct = struct()
        .uint64()
        .uint64Array(0)
        .build();

    assertThatThrownBy(() -> struct.remove(2))
        .isInstanceOf(IndexOutOfBoundsException.class)
        .hasMessage("Index 2 out of bounds for length 2");
  }

  @Test
  void removeUint64_not_allocated() {
    var struct = struct()
        .allocated()
        .uint64()
        .uint64Array(0)
        .build();

    assertThatThrownBy(() -> struct.remove(1))
        .isInstanceOf(IndexOutOfBoundsException.class)
        .hasMessage("allocated: 0, index: 0, length: 8");
  }

  @Test
  void removeUint64_not_array() {
    var struct = struct()
        .uint64()
        .build();

    assertThatThrownBy(() -> struct.remove(0))
        .isInstanceOf(UnsupportedOperationException.class)
        .hasMessage("Cannot remove element from fixed length Uint64Type at position 0");
  }

  @Test
  void removeUint64_with_index_0() {
    var struct = struct()
        .uint64()
        .uint64Array(0)
        .build();

    struct.add(1, BigInteger.ONE);
    struct.add(1, BigInteger.valueOf(2));
    struct.remove(1, 0);

    assertThat(struct.getByteArray()).isEqualTo(ba().int64(1, 2));
  }

  @Test
  void removeUint64_with_index_1() {
    var struct = struct()
        .uint64()
        .uint64Array(0)
        .build();
    struct.add(1, BigInteger.ONE);
    struct.add(1, BigInteger.valueOf(2));

    struct.remove(1, 1);

    assertThat(struct.getByteArray()).isEqualTo(ba().int64(1, 1));
  }

  @Test
  void removeUint64_with_index_negative() {
    var struct = struct()
        .uint64()
        .uint64Array(0)
        .fromBytes(ba().uint64(2, 1, 2))
        .build();

    assertThatThrownBy(() -> struct.remove(1, -1))
        .isInstanceOf(ArrayIndexOutOfBoundsException.class)
        .hasMessage("Uint64ListType at position 1 index: -1 length: 2");
  }

  @Test
  void removeUint64_with_index_greater_than_length() {
    var struct = struct()
        .uint64()
        .uint64Array(0)
        .fromBytes(ba().uint64(2, 1, 2))
        .build();

    assertThatThrownBy(() -> struct.remove(1, 2))
        .isInstanceOf(ArrayIndexOutOfBoundsException.class)
        .hasMessage("Uint64ListType at position 1 index: 2 length: 2");
  }

  @Test
  void removeUint64_with_index_not_array() {
    var struct = struct()
        .uint64()
        .fromBytes(ba().uint64(1))
        .build();

    assertThatThrownBy(() -> struct.remove(0, 0))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Field at postion 0 is not a ArrayValueType or ListValueType");
  }

  @Test
  void removeUint64Array_fixed_length() {
    var struct = struct()
        .primitive().constant(List.of(BigInteger.valueOf(5), BigInteger.valueOf(5), BigInteger.valueOf(5), BigInteger.valueOf(5), BigInteger.valueOf(5))).uint64()
        .build();

    assertThatThrownBy(() -> struct.remove(0))
        .isInstanceOf(UnsupportedOperationException.class)
        .hasMessage("Cannot remove element from fixed length Uint64ListType at position 0");
  }

  @Test
  void removeUint64Array_fixed_length_with_index() {
    var struct = struct()
        .primitive().constant(List.of(BigInteger.valueOf(5), BigInteger.valueOf(5), BigInteger.valueOf(5), BigInteger.valueOf(5), BigInteger.valueOf(5))).uint64()
        .build();

    assertThatThrownBy(() -> struct.remove(0, 3))
        .isInstanceOf(UnsupportedOperationException.class)
        .hasMessage("Cannot remove element from fixed length array Uint64ListType at position 0 index: 3");
  }
}
