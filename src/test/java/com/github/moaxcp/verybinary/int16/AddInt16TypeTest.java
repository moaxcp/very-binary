package com.github.moaxcp.verybinary.int16;

import com.github.moaxcp.verybinary.Int16ArrayType;
import org.junit.jupiter.api.Test;

import static com.github.moaxcp.verybinary.Builders.struct;
import static com.github.moaxcp.verybinary.ByteArray.ba;
import static com.github.moaxcp.verybinary.Expression.constant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class AddInt16TypeTest {
  @Test
  void addInt16Wrapper() {
    var struct = struct()
        .int16()
        .int16Array(0)
        .build();

    assertThatThrownBy(() -> ((Int16ArrayType) struct.getType(1)).add(struct, Short.valueOf((short) 1)))
        .isInstanceOf(UnsupportedOperationException.class)
        .hasMessage("add(Pointer, Short) not supported for Int16ArrayType. Use add(Pointer, short) instead.");
  }

  @Test
  void addInt16Wrapper_with_index() {
    var struct = struct()
        .int16()
        .int16Array(0)
        .build();

    assertThatThrownBy(() -> ((Int16ArrayType) struct.getType(1)).add(struct, 0, Short.valueOf((short) 1)))
        .isInstanceOf(UnsupportedOperationException.class)
        .hasMessage("add(Pointer, long, Short) not supported for Int16ArrayType. Use add(Pointer, long, short) instead.");
  }

  @Test
  void addInt16() {
    var struct = struct()
        .int16()
        .int16Array(0)
        .fromBytes(ba().int16(2, 1, 2))
        .build();

    struct.addInt16(1, (short) 3);
    struct.addInt16(1, (short) 4);

    assertThat(struct.getByteArray()).isEqualTo(ba().int16(4, 1, 2, 3, 4));
  }

  @Test
  void addInt16_position_negative() {
    var struct = struct()
        .int16()
        .int16Array(0)
        .fromBytes(ba().int16(2, 1, 2))
        .build();

    assertThatThrownBy(() -> struct.addInt16(-1, (short) 3))
        .isInstanceOf(IndexOutOfBoundsException.class)
        .hasMessage("Index -1 out of bounds for length 2");
  }

  @Test
  void addInt16_position_greater_than_length() {
    var struct = struct()
        .int16()
        .int16Array(0)
        .fromBytes(ba().int16(2, 1, 2))
        .build();

    assertThatThrownBy(() -> struct.addInt16(3, (short) 3))
        .isInstanceOf(IndexOutOfBoundsException.class)
        .hasMessage("Index 3 out of bounds for length 2");
  }

  @Test
  void addInt16_not_allocated() {
    var struct = struct()
        .allocated()
        .int16()
        .int16Array(0)
        .build();
    assertThatThrownBy(() -> struct.addInt16(1, (short) 3))
        .isInstanceOf(IndexOutOfBoundsException.class)
        .hasMessage("allocated: 0, index: 0, length: 2");
  }

  @Test
  void addInt16Array_constant() {
    var struct = struct()
        .primitive().constant((short) 5).lengthExpression(constant(5)).int16()
        .build();

    assertThatThrownBy(() -> struct.addInt16(0, (short) 3))
        .isInstanceOf(IllegalStateException.class)
        .hasMessage("Int16ArrayType at position 0 is constant length: 5 index: 5");
  }

  @Test
  void addInt16_not_array() {
    var struct = struct()
        .int16()
        .fromBytes(ba().int16(1))
        .build();
    assertThatThrownBy(() -> struct.addInt16(0, (short) 3))
        .isInstanceOf(ArrayIndexOutOfBoundsException.class)
        .hasMessage("Int16ArrayType cannot add to non-array type at position 0 index: 1 length: 1");
  }

  @Test
  void addInt16_with_index() {
    var struct = struct()
        .int16()
        .int16Array(0)
        .fromBytes(ba().int16(2, 1, 2))
        .build();

    struct.addInt16(1, 0, (short) 3);
    struct.addInt16(1, 1, (short) 4);

    assertThat(struct.getByteArray()).isEqualTo(ba().int16(4, 3, 4, 1, 2));
  }

  @Test
  void addInt16_with_index_negative() {
    var struct = struct()
        .int16()
        .int16Array(0)
        .fromBytes(ba().int16(2, 1, 2))
        .build();

    assertThatThrownBy(() -> struct.addInt16(1, -1, (short) 3))
        .isInstanceOf(ArrayIndexOutOfBoundsException.class)
        .hasMessage("Int16ArrayType at position 1 index: -1 new length: 3");
  }

  @Test
  void addInt16_with_index_greater_than_length() {
    var struct = struct()
        .int16()
        .int16Array(0)
        .fromBytes(ba().int16(2, 1, 2))
        .build();

    assertThatThrownBy(() -> struct.addInt16(1, 3, (short) 3))
        .isInstanceOf(ArrayIndexOutOfBoundsException.class)
        .hasMessage("Int16ArrayType at position 1 index: 3 new length: 3");
  }

  @Test
  void addInt16_with_index_not_allocated() {
    var struct = struct()
        .allocated()
        .int16()
        .int16Array(0)
        .build();

    assertThatThrownBy(() -> struct.addInt16(1, 0, (short) 3))
        .isInstanceOf(IndexOutOfBoundsException.class)
        .hasMessage("allocated: 0, index: 0, length: 2");
  }

  @Test
  void addInt16_with_index_0_not_array() {
    var struct = struct()
        .int16()
        .fromBytes(ba().int16(1))
        .build();

    assertThatThrownBy(() -> struct.addInt16(0, 0, (short) 3))
        .isInstanceOf(ArrayIndexOutOfBoundsException.class)
        .hasMessage("Int16ArrayType cannot add to non-array type at position 0 index: 0 length: 1");
  }

  @Test
  void addInt16_with_index_1_not_array() {
    var struct = struct()
        .int16()
        .fromBytes(ba().int16(1))
        .build();

    assertThatThrownBy(() -> struct.addInt16(0, 1, (short) 3))
        .isInstanceOf(ArrayIndexOutOfBoundsException.class)
        .hasMessage("Int16ArrayType cannot add to non-array type at position 0 index: 1 length: 1");
  }

  @Test
  void addInt16Array_with_index_constant() {
    var struct = struct()
        .primitive().constant((short) 5).lengthExpression(constant(5)).int16()
        .build();

    assertThatThrownBy(() -> struct.addInt16(0, 3, (short) 3))
        .isInstanceOf(IllegalStateException.class)
        .hasMessage("Int16ArrayType at position 0 is constant length: 5 index: 3");
  }
}
