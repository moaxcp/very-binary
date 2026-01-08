package com.github.moaxcp.verybinary.uint16;

import com.github.moaxcp.verybinary.Uint16Type;
import org.junit.jupiter.api.Test;

import static com.github.moaxcp.verybinary.Builders.struct;
import static com.github.moaxcp.verybinary.ByteArray.ba;
import static com.github.moaxcp.verybinary.Expression.constant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class AddUint16TypeTest {
  @Test
  void addUint16Wrapper() {
    var struct = struct()
        .uint16()
        .uint16Array(0)
        .build();

    assertThatThrownBy(() -> ((Uint16Type) struct.getType(1)).add(struct, Integer.valueOf(1)))
        .isInstanceOf(UnsupportedOperationException.class)
        .hasMessage("add(Pointer, Integer) not supported for Uint16Type. Use add(Pointer, int) instead.");
  }

  @Test
  void addUint16Wrapper_with_index() {
    var struct = struct()
        .uint16()
        .uint16Array(0)
        .build();

    assertThatThrownBy(() -> ((Uint16Type) struct.getType(1)).add(struct, 0, Integer.valueOf(1)))
        .isInstanceOf(UnsupportedOperationException.class)
        .hasMessage("add(Pointer, long, Integer) not supported for Uint16Type. Use add(Pointer, long, int) instead.");
  }

  @Test
  void addUint16() {
    var struct = struct()
        .uint16()
        .uint16Array(0)
        .fromBytes(ba().uint16(2, 1, 2))
        .build();

    struct.addUint16(1, 3);
    struct.addUint16(1, 4);

    assertThat(struct.getByteArray()).isEqualTo(ba().uint16(4, 1, 2, 3, 4));
  }

  @Test
  void addUint16_position_negative() {
    var struct = struct()
        .uint16()
        .uint16Array(0)
        .fromBytes(ba().uint16(2, 1, 2))
        .build();

    assertThatThrownBy(() -> struct.addUint16(-1, 3))
        .isInstanceOf(IndexOutOfBoundsException.class)
        .hasMessage("Index -1 out of bounds for length 2");
  }

  @Test
  void addUint16_position_greater_than_length() {
    var struct = struct()
        .uint16()
        .uint16Array(0)
        .fromBytes(ba().uint16(2, 1, 2))
        .build();

    assertThatThrownBy(() -> struct.addUint16(3, 3))
        .isInstanceOf(IndexOutOfBoundsException.class)
        .hasMessage("Index 3 out of bounds for length 2");
  }

  @Test
  void addUint16_not_allocated() {
    var struct = struct()
        .allocated()
        .uint16()
        .uint16Array(0)
        .build();
    assertThatThrownBy(() -> struct.addUint16(1, 3))
        .isInstanceOf(IndexOutOfBoundsException.class)
        .hasMessage("allocated: 0, index: 0, length: 2");
  }

  @Test
  void addUint16Array_constant() {
    var struct = struct()
        .primitive().constant(5).lengthExpression(constant(5)).uint16()
        .build();

    assertThatThrownBy(() -> struct.addUint16(0, 3))
        .isInstanceOf(UnsupportedOperationException.class)
        .hasMessage("Uint16Type at position 0 is constant index: 5 value: 3 constant: 5");
  }

  @Test
  void addUint16_not_array() {
    var struct = struct()
        .uint16()
        .fromBytes(ba().uint16(1))
        .build();
    assertThatThrownBy(() -> struct.addUint16(0, 3))
        .isInstanceOf(ArrayIndexOutOfBoundsException.class)
        .hasMessage("Uint16Type cannot add to non-array type at position 0 index: 1 length: 1");
  }

  @Test
  void addUint16_with_index() {
    var struct = struct()
        .uint16()
        .uint16Array(0)
        .fromBytes(ba().uint16(2, 1, 2))
        .build();

    struct.addUint16(1, 0, 3);
    struct.addUint16(1, 1, 4);

    assertThat(struct.getByteArray()).isEqualTo(ba().uint16(4, 3, 4, 1, 2));
  }

  @Test
  void addUint16_with_index_negative() {
    var struct = struct()
        .uint16()
        .uint16Array(0)
        .fromBytes(ba().uint16(2, 1, 2))
        .build();

    assertThatThrownBy(() -> struct.addUint16(1, -1, 3))
        .isInstanceOf(ArrayIndexOutOfBoundsException.class)
        .hasMessage("Uint16Type at position 1 index: -1 new length: 3");
  }

  @Test
  void addUint16_with_index_greater_than_length() {
    var struct = struct()
        .uint16()
        .uint16Array(0)
        .fromBytes(ba().uint16(2, 1, 2))
        .build();

    assertThatThrownBy(() -> struct.addUint16(1, 3, 3))
        .isInstanceOf(ArrayIndexOutOfBoundsException.class)
        .hasMessage("Uint16Type at position 1 index: 3 new length: 3");
  }

  @Test
  void addUint16_with_index_not_allocated() {
    var struct = struct()
        .allocated()
        .uint16()
        .uint16Array(0)
        .build();
    assertThatThrownBy(() -> struct.addUint16(1, 0, 3))
        .isInstanceOf(IndexOutOfBoundsException.class)
        .hasMessage("allocated: 0, index: 0, length: 2");
  }

  @Test
  void addUint16_with_index_0_not_array() {
    var struct = struct()
        .uint16()
        .fromBytes(ba().uint16(1))
        .build();
    assertThatThrownBy(() -> struct.addUint16(0, 0, 3))
        .isInstanceOf(ArrayIndexOutOfBoundsException.class)
        .hasMessage("Uint16Type cannot add to non-array type at position 0 index: 0 length: 1");
  }

  @Test
  void addUint16_with_index_1_not_array() {
    var struct = struct()
        .uint16()
        .fromBytes(ba().uint16(1))
        .build();

    assertThatThrownBy(() -> struct.addUint16(0, 1, 3))
        .isInstanceOf(ArrayIndexOutOfBoundsException.class)
        .hasMessage("Uint16Type cannot add to non-array type at position 0 index: 1 length: 1");
  }

  @Test
  void addUint16Array_with_index_constant() {
    var struct = struct()
        .primitive().constant(5).lengthExpression(constant(5)).uint16()
        .build();

    assertThatThrownBy(() -> struct.addUint16(0, 0, 3))
        .isInstanceOf(UnsupportedOperationException.class)
        .hasMessage("Uint16Type at position 0 is constant index: 0 value: 3 constant: 5");
  }
}
