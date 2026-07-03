package com.github.moaxcp.verybinary.int16;

import com.github.moaxcp.verybinary.Int16ListType;
import org.junit.jupiter.api.Test;

import static com.github.moaxcp.verybinary.Builders.struct;
import static com.github.moaxcp.verybinary.ByteArray.ba;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class RemoveInt16TypeTest {
  @Test
  void removeInt16() {
    var struct = struct()
        .int16()
        .int16Array(0)
        .fromBytes(ba().int16(2, 1, 2))
        .build();

    struct.removeAll(1);

    assertThat(struct.getByteArray()).isEqualTo(ba().int16(0));
  }

  @Test
  void removeInt16_position_negative() {
    var struct = struct()
        .int16()
        .int16Array(0)
        .build();

    assertThatThrownBy(() -> struct.removeAll(-1))
        .isInstanceOf(IndexOutOfBoundsException.class)
        .hasMessage("Index -1 out of bounds for length 2");
  }

  @Test
  void removeInt16_position_greater_than_length() {
    var struct = struct()
        .int16()
        .int16Array(0)
        .build();

    assertThatThrownBy(() -> struct.removeAll(2))
        .isInstanceOf(IndexOutOfBoundsException.class)
        .hasMessage("Index 2 out of bounds for length 2");
  }

  @Test
  void removeInt16_not_allocated() {
    var struct = struct()
        .allocated()
        .int16()
        .int16Array(0)
        .build();

    assertThatThrownBy(() -> struct.removeAll(1))
        .isInstanceOf(IndexOutOfBoundsException.class)
        .hasMessage("allocated: 0, index: 0, length: 2");
  }

  @Test
  void removeInt16_not_array() {
    var struct = struct()
        .int16()
        .build();

    assertThatThrownBy(() -> struct.removeAll(0))
        .isInstanceOf(UnsupportedOperationException.class)
        .hasMessage("Cannot remove element from fixed length Int16Type at position 0");
  }

  @Test
  void removeInt16_with_index_0() {
    var struct = struct()
        .int16()
        .int16Array(0)
        .build();

    struct.addInt16(1, (short) 1);
    struct.addInt16(1, (short) 2);
    struct.remove(1, 0);

    assertThat(((Int16ListType) struct.getType(1)).getInt16(struct, 0)).isEqualTo((short) 2);

    assertThat(struct.getByteArray()).isEqualTo(ba().int16(1, 2));
  }

  @Test
  void removeInt16_with_index_1() {
    var struct = struct()
        .int16()
        .int16Array(0)
        .build();
    struct.addInt16(1, (short) 1);
    struct.addInt16(1, (short) 2);

    struct.remove(1, 1);

    assertThat(struct.getByteArray()).isEqualTo(ba().int16(1, 1));
  }

  @Test
  void removeInt16_with_index_negative() {
    var struct = struct()
        .int16()
        .int16Array(0)
        .fromBytes(ba().int16(2, 1, 2))
        .build();

    assertThatThrownBy(() -> struct.remove(1, -1))
        .isInstanceOf(ArrayIndexOutOfBoundsException.class)
        .hasMessage("Int16ListType at position 1 index: -1 length: 2");
  }

  @Test
  void removeInt16_with_index_greater_than_length() {
    var struct = struct()
        .int16()
        .int16Array(0)
        .fromBytes(ba().int16(2, 1, 2))
        .build();

    assertThatThrownBy(() -> struct.remove(1, 2))
        .isInstanceOf(ArrayIndexOutOfBoundsException.class)
        .hasMessage("Int16ListType at position 1 index: 2 length: 2");
  }

  @Test
  void removeInt16_with_index_not_array() {
    var struct = struct()
        .int16()
        .fromBytes(ba().int16(1))
        .build();

    assertThatThrownBy(() -> struct.remove(0, 0))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Field at postion 0 is not a ArrayValueType or ListValueType");
  }

  @Test
  void removeInt16Array_fixed_length() {
    var struct = struct()
        .primitive().constant(new short[]{5, 5, 5, 5, 5}).int16()
        .build();

    assertThatThrownBy(() -> struct.removeAll(0))
        .isInstanceOf(UnsupportedOperationException.class)
        .hasMessage("Cannot remove element from fixed length Int16ListType at position 0");
  }

  @Test
  void removeInt16Array_fixed_length_with_index() {
    var struct = struct()
        .primitive().constant(new short[]{5, 5, 5, 5, 5}).int16()
        .build();

    assertThatThrownBy(() -> struct.remove(0, 3))
        .isInstanceOf(UnsupportedOperationException.class)
        .hasMessage("Cannot remove element from fixed length array Int16ListType at position 0 index: 3");
  }
}
