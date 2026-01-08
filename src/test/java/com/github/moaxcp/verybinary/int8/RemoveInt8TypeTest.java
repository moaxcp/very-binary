package com.github.moaxcp.verybinary.int8;

import com.github.moaxcp.verybinary.Int8Type;
import org.junit.jupiter.api.Test;

import static com.github.moaxcp.verybinary.Builders.struct;
import static com.github.moaxcp.verybinary.ByteArray.ba;
import static com.github.moaxcp.verybinary.Expression.constant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class RemoveInt8TypeTest {
  @Test
  void removeInt8() {
    var struct = struct()
        .int8()
        .int8Array(0)
        .fromBytes(ba().int8(2, 1, 2))
        .build();

    struct.removeAll(1);

    assertThat(struct.getByteArray()).isEqualTo(ba().int8(0));
  }

  @Test
  void removeInt8_position_negative() {
    var struct = struct()
        .int8()
        .int8Array(0)
        .build();

    assertThatThrownBy(() -> struct.removeAll(-1))
        .isInstanceOf(IndexOutOfBoundsException.class)
        .hasMessage("Index -1 out of bounds for length 2");
  }

  @Test
  void removeInt8_position_greater_than_length() {
    var struct = struct()
        .int8()
        .int8Array(0)
        .build();

    assertThatThrownBy(() -> struct.removeAll(2))
        .isInstanceOf(IndexOutOfBoundsException.class)
        .hasMessage("Index 2 out of bounds for length 2");
  }

  @Test
  void removeInt8_not_allocated() {
    var struct = struct()
        .allocated()
        .int8()
        .int8Array(0)
        .build();

    assertThatThrownBy(() -> struct.removeAll(1))
        .isInstanceOf(IndexOutOfBoundsException.class)
        .hasMessage("allocated: 0, index: 0, length: 1");
  }

  @Test
  void removeInt8_not_array() {
    var struct = struct()
        .int8()
        .build();

    assertThatThrownBy(() -> struct.removeAll(0))
        .isInstanceOf(ArrayIndexOutOfBoundsException.class)
        .hasMessage("Int8Type cannot remove from non-array type at position 0");
  }

  @Test
  void removeInt8_with_index_0() {
    var struct = struct()
        .int8()
        .int8Array(0)
        .build();

    struct.addInt8(1, (byte) 1);
    struct.addInt8(1, (byte) 2);
    struct.remove(1, 0);

    assertThat(((Int8Type) struct.getType(1)).getInt8(struct, 0)).isEqualTo((byte) 2);

    assertThat(struct.getByteArray()).isEqualTo(ba().int8(1, 2));
  }

  @Test
  void removeInt8_with_index_1() {
    var struct = struct()
        .int8()
        .int8Array(0)
        .build();
    struct.addInt8(1, (byte) 1);
    struct.addInt8(1, (byte) 2);

    struct.remove(1, 1);

    assertThat(struct.getByteArray()).isEqualTo(ba().int8(1, 1));
  }

  @Test
  void removeInt8_with_index_negative() {
    var struct = struct()
        .int8()
        .int8Array(0)
        .fromBytes(ba().int8(2, 1, 2))
        .build();

    assertThatThrownBy(() -> struct.remove(1, -1))
        .isInstanceOf(ArrayIndexOutOfBoundsException.class)
        .hasMessage("Int8Type at position 1 index: -1 length: 2");
  }

  @Test
  void removeInt8_with_index_greater_than_length() {
    var struct = struct()
        .int8()
        .int8Array(0)
        .fromBytes(ba().int8(2, 1, 2))
        .build();

    assertThatThrownBy(() -> struct.remove(1, 2))
        .isInstanceOf(ArrayIndexOutOfBoundsException.class)
        .hasMessage("Int8Type at position 1 index: 2 length: 2");
  }

  @Test
  void removeInt8_with_index_not_array() {
    var struct = struct()
        .int8()
        .fromBytes(ba().int8(1))
        .build();

    assertThatThrownBy(() -> struct.remove(0, 0))
        .isInstanceOf(ArrayIndexOutOfBoundsException.class)
        .hasMessage("Int8Type cannot remove from non-array type at position 0");
  }

  @Test
  void removeInt8Array_fixed_length() {
    var struct = struct()
        .primitive().constant((byte) 5).lengthExpression(constant(5)).int8()
        .build();

    assertThatThrownBy(() -> struct.removeAll(0))
        .isInstanceOf(UnsupportedOperationException.class)
        .hasMessage("Cannot remove fixed length array Int8Type at position 0");
  }

  @Test
  void removeInt8Array_fixed_length_with_index() {
    var struct = struct()
        .primitive().constant((byte) 5).lengthExpression(constant(5)).int8()
        .build();

    assertThatThrownBy(() -> struct.remove(0, 3))
        .isInstanceOf(UnsupportedOperationException.class)
        .hasMessage("Cannot remove element from fixed length array Int8Type at position 0 index: 3");
  }
}
