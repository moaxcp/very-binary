package com.github.moaxcp.verybinary.int16;

import com.github.moaxcp.verybinary.Int16Type;
import org.junit.jupiter.api.Test;

import static com.github.moaxcp.verybinary.Builders.struct;
import static com.github.moaxcp.verybinary.ByteArray.ba;
import static com.github.moaxcp.verybinary.Expression.constant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class SetInt16TypeTest {
  @Test
  void setWrapper() {
    var struct = struct()
        .int16()
        .build();

    assertThatThrownBy(() -> ((Int16Type) struct.getType(0)).set(struct, Short.valueOf((short) 2)))
        .isInstanceOf(UnsupportedOperationException.class)
        .hasMessage("set(Pointer, Short) not supported for Int16Type. Use set(Pointer, short) instead.");
  }

  @Test
  void setInt16() {
    var struct = struct()
        .int16()
        .build();

    struct.setInt16(0, (short) 2);

    assertThat(struct.getByteArray()).isEqualTo(ba().int16(2));
  }

  @Test
  void setInt16_position_negative() {
    var struct = struct()
        .int16()
        .build();

    assertThatThrownBy(() -> struct.setInt16(-1, (short) 2))
        .isInstanceOf(IndexOutOfBoundsException.class)
        .hasMessage("Index -1 out of bounds for length 1");
  }

  @Test
  void setInt16_position_greater_than_length() {
    var struct = struct()
        .int16()
        .build();

    assertThatThrownBy(() -> struct.setInt16(2, (short) 2))
        .isInstanceOf(IndexOutOfBoundsException.class)
        .hasMessage("Index 2 out of bounds for length 1");
  }

  @Test
  void setInt16_not_allocated() {
    var struct = struct()
        .allocated()
        .int16()
        .build();

    assertThatThrownBy(() -> struct.setInt16(0, (short) 2))
        .isInstanceOf(ArrayIndexOutOfBoundsException.class)
        .hasMessage("Index 0 out of bounds for length 0");
  }

  @Test
  void setInt16_constant() {
    var struct = struct()
        .primitive().constant((short) 5).int16()
        .build();

    assertThatThrownBy(() -> struct.setInt16(0, (short) 2))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Int16Type at position 0 is constant index: 0 value: 2 constant: 5");
  }

  @Test
  void setArrayWrapper() {
    var struct = struct()
        .int16()
        .int16Array(0)
        .build();

    assertThatThrownBy(() -> ((Int16Type) struct.getType(1)).set(struct, 0, Short.valueOf((short) 2)))
        .isInstanceOf(UnsupportedOperationException.class)
        .hasMessage("set(Pointer, long, Short) not supported for Int16Type. Use set(Pointer, long, short) instead.");
  }

  @Test
  void setInt16Array() {
    var struct = struct()
        .int16()
        .int16Array(0)
        .int16()
        .fromBytes(ba().int16(1, 2, 3))
        .build();

    struct.setInt16(1, 0, (short) 5);

    assertThat(struct.getInt16(0)).isEqualTo((short) 1);
    assertThat(struct.getInt16(1, 0)).isEqualTo((short) 5);
    assertThat(struct.getByteArray()).isEqualTo(ba().int16(1, 5, 3));
  }

  @Test
  void setInt16Array_negative() {
    var struct = struct()
        .int16()
        .int16Array(0)
        .int16()
        .fromBytes(ba().int16(1, 2, 3))
        .build();

    assertThatThrownBy(() -> struct.setInt16(1, -1, (short) 5))
        .isInstanceOf(IndexOutOfBoundsException.class)
        .hasMessage("Int16Type at position 1 index: -1 length: 1");
  }

  @Test
  void setInt16Array_greater_than_length() {
    var struct = struct()
        .int16()
        .int16Array(0)
        .int16()
        .fromBytes(ba().int16(1, 2, 3))
        .build();

    assertThatThrownBy(() -> struct.setInt16(1, 2, (short) 5))
        .isInstanceOf(ArrayIndexOutOfBoundsException.class)
        .hasMessage("Int16Type at position 1 index: 2 length: 1");

    assertThat(struct.getInt16(0)).isEqualTo((short) 1);
    assertThat(struct.getInt16(1, 0)).isEqualTo((short) 2);
    assertThat(struct.getByteArray()).isEqualTo(ba().int16(1, 2, 3));
  }

  @Test
  void setInt16Array_not_allocated() {
    var struct = struct()
        .allocated()
        .int16()
        .int16Array(0)
        .build();

    assertThatThrownBy(() -> struct.setInt16(1, 0, (short) 2))
        .isInstanceOf(IndexOutOfBoundsException.class)
        .hasMessage("allocated: 0, index: 0, length: 2");
  }

  @Test
  void setInt16Array_index_0_not_array() {
    var struct = struct()
        .int16()
        .build();

    struct.setInt16(0, 0, (short) 2);

    assertThat(struct.getByteArray()).isEqualTo(ba().int16(2));
  }

  @Test
  void setInt16Array_index_1_not_array() {
    var struct = struct()
        .int16()
        .build();

    assertThatThrownBy(() -> struct.setInt16(0, 1, (short) 2))
        .isInstanceOf(ArrayIndexOutOfBoundsException.class)
        .hasMessage("Int16Type at position 0 index: 1 length: 1");
  }

  @Test
  void setInt16Array_constant_value_and_length() {
    var struct = struct()
        .primitive().constant((short) 5).lengthExpression(constant(5)).int16()
        .build();

    assertThatThrownBy(() -> struct.setInt16(0, 3, (short) 2))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Int16Type at position 0 is constant index: 3 value: 2 constant: 5");
  }

  @Test
  void setInt16Array_constant_value() {
    var struct = struct()
        .int16()
        .primitive().constant((short) 5).lengthField(0).int16()
        .fromBytes(ba().int16(2, 5, 5))
        .build();

    assertThatThrownBy(() -> struct.setInt16(1, 1, 2))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Int16Type at position 1 is constant index: 1 value: 2 constant: 5");
  }

  @Test
  void setInt16Array_constant_value_same() {
    var struct = struct()
        .int16()
        .primitive().constant((short) 5).lengthField(0).int16()
        .fromBytes(ba().int16(2, 5, 5))
        .build();

    struct.setInt16(1, 1, (short) 5);

    assertThat(struct.getByteArray()).isEqualTo(ba().int16(2, 5, 5));
  }

  @Test
  void setInt16Array_set_length_field_without_adding_to_array() {
    var struct = struct()
        .int16()
        .int16Array(0)
        .build();

    struct.setInt16(0, 2);

    assertThat(struct.getByteArray()).isEqualTo(ba().int16(2, 0, 0));
  }
}
