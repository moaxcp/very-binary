package com.github.moaxcp.verybinary.bool;

import com.github.moaxcp.verybinary.BoolArrayType;
import com.github.moaxcp.verybinary.BoolType;
import org.junit.jupiter.api.Test;

import static com.github.moaxcp.verybinary.Builders.struct;
import static com.github.moaxcp.verybinary.ByteArray.ba;
import static com.github.moaxcp.verybinary.Expression.constant;
import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class SetBoolTypeTest {

  @Test
  void set() {
    var struct = struct()
        .bool()
        .build();

    assertThatThrownBy(() -> ((BoolType) struct.getType(0)).set(struct, TRUE))
        .isInstanceOf(UnsupportedOperationException.class)
        .hasMessage("set(Pointer, Boolean) not supported for BoolType. Use set(Pointer, boolean) instead.");
  }

  @Test
  void set_index() {
    var struct = struct()
        .boolArray(constant(5))
        .build();

    assertThatThrownBy(() -> ((BoolArrayType) struct.getType(0)).set(struct, 0, TRUE))
        .isInstanceOf(UnsupportedOperationException.class)
        .hasMessage("set(Pointer, long, Boolean) not supported for BoolArrayType. Use set(Pointer, long, boolean) instead.");
  }

  @Test
  void set_array() {
    var struct = struct()
        .boolArray(constant(5))
        .build();

    assertThatThrownBy(() -> ((BoolArrayType) struct.getType(0)).set(struct, new Boolean[] {TRUE, FALSE, TRUE, FALSE}))
        .isInstanceOf(UnsupportedOperationException.class)
        .hasMessage("set(Pointer, Boolean...) not supported for BoolArrayType. Use set(Pointer, boolean...) instead.");
  }

  @Test
  void set_array_index() {
    var struct = struct()
        .boolArray(constant(5))
        .build();

    assertThatThrownBy(() -> ((BoolArrayType) struct.getType(0)).set(struct, 1, new Boolean[] {TRUE, FALSE, TRUE, FALSE}))
      .isInstanceOf(UnsupportedOperationException.class)
      .hasMessage("set(Pointer, long, Boolean...) not supported for BoolArrayType. Use set(Pointer, long, boolean...) instead.");
  }

  @Test
  void setBool() {
    var struct = struct()
        .bool()
        .build();

    struct.setBool(0, true);

    assertThat(struct.getByteArray()).isEqualTo(ba().bool(true));
  }

  @Test
  void setBool_position_negative() {
    var struct = struct()
        .bool()
        .build();

    assertThatThrownBy(() -> struct.setBool(-1, true))
        .isInstanceOf(IndexOutOfBoundsException.class)
        .hasMessage("Index -1 out of bounds for length 1");
  }

  @Test
  void setBool_position_greater_than_length() {
    var struct = struct()
        .bool()
        .build();

    assertThatThrownBy(() -> struct.setBool(2, true))
        .isInstanceOf(IndexOutOfBoundsException.class)
        .hasMessage("Index 2 out of bounds for length 1");
  }

  @Test
  void setBool_not_allocated() {
    var struct = struct()
        .allocated()
        .bool()
        .build();

    assertThatThrownBy(() -> struct.setBool(0, true))
        .isInstanceOf(IndexOutOfBoundsException.class)
        .hasMessage("allocated: 0, index: 0, length: 1");
  }

  @Test
  void setBool_constant() {
    var struct = struct()
        .primitive().constant(true).bool()
        .build();

    assertThatThrownBy(() -> struct.setBool(0, false))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("BoolArrayType at position 0 is constant index: 0 value: false constant: true");
  }

  @Test
  void setBool_index() {
    var struct = struct()
        .int8()
        .boolArray(0)
        .bool()
        .fromBytes(new byte[] {1, 1, 0})
        .build();

    struct.setBool(1, 0, false);

    assertThat(struct.getInt8(0)).isEqualTo((byte) 1);
    assertThat(struct.getBool(1, 0)).isFalse();
    assertThat(struct.getByteArray()).isEqualTo(ba().int8(1).bool(false, false));
  }

  @Test
  void setBool_index_negative() {
    var struct = struct()
        .int8()
        .boolArray(0)
        .bool()
        .fromBytes(new byte[] {1, 1, 0})
        .build();

    assertThatThrownBy(() -> struct.setBool(1, -1, true))
        .isInstanceOf(IndexOutOfBoundsException.class)
        .hasMessage("BoolArrayType at position 1 index: -1 length: 1");
  }

  @Test
  void setBool_index_greater_than_length() {
    var struct = struct()
        .int8()
        .boolArray(0)
        .bool()
        .fromBytes(new byte[] {1, 1, 0})
        .build();

    assertThatThrownBy(() -> struct.setBool(1, 2, true))
        .isInstanceOf(ArrayIndexOutOfBoundsException.class)
        .hasMessage("BoolArrayType at position 1 index: 2 length: 1");

    assertThat(struct.getInt8(0)).isEqualTo((byte) 1);
    assertThat(struct.getBool(1, 0)).isTrue();
    assertThat(struct.getByteArray()).isEqualTo(ba().int8(1).bool(true, false));
  }

  @Test
  void setBool_index_not_allocated() {
    var struct = struct()
        .allocated()
        .int8()
        .boolArray(0)
        .build();

    assertThatThrownBy(() -> struct.setBool(1, 0, true))
        .isInstanceOf(IndexOutOfBoundsException.class)
        .hasMessage("allocated: 0, index: 0, length: 1");
  }

  @Test
  void setBool_index_0_not_array() {
    var struct = struct()
        .bool()
        .build();

    struct.setBool(0, 0, true);

    assertThat(struct.getByteArray()).isEqualTo(ba().bool(true));
  }

  @Test
  void setBool_index_1_not_array() {
    var struct = struct()
        .bool()
        .build();

    assertThatThrownBy(() -> struct.setBool(0, 1, true))
        .isInstanceOf(ArrayIndexOutOfBoundsException.class)
        .hasMessage("BoolArrayType at position 0 index: 1 length: 1");
  }

  @Test
  void setBool_index_constant_value() {
    var struct = struct()
        .int8()
        .primitive().constant(true).lengthField(0).bool()
        .fromBytes(ba().int8(2).bool(true, true))
        .build();

    struct.setBool(1, 1, true);

    assertThat(struct.getByteArray()).isEqualTo(ba().int8(2).bool(true, true));
  }

  @Test
  void setBool_index_constant_value_bad_value() {
    var struct = struct()
        .int8()
        .primitive().constant(true).lengthField(0).bool()
        .fromBytes(new byte[] {2, 1, 1})
        .build();

    assertThatThrownBy(() -> struct.setBool(1, 1, false))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("BoolArrayType at position 1 is constant index: 1 value: false constant: true");
  }

  @Test
  void setBool_index_constant_value_and_length() {
    var struct = struct()
        .primitive().constant(true).lengthExpression(constant(5)).bool()
        .build();

    assertThatThrownBy(() -> struct.setBool(0, 3, false))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("BoolArrayType at position 0 is constant index: 3 value: false constant: true");
  }

  @Test
  void setBool_array() {
    var struct = struct()
        .int8()
        .boolArray(0)
        .build();

    struct.setInt8(0, 5);
    struct.setBool(1, true, false, true, false, true);

    assertThat(struct.getByteArray()).isEqualTo(ba().int8(5).bool(true, false, true, false, true));
  }

  @Test
  void setBool_array_length_constant() {
    var struct = struct()
        .boolArray(constant(5))
        .build();
    struct.setBool(0, true, false, true, false, true);

    assertThat(struct.getByteArray()).isEqualTo(ba().bool(true, false, true, false, true));
  }

  @Test
  void setBool_array_length_field_constant() {
    var struct = struct()
        .primitive().constant((byte) 5).int8()
        .boolArray(0)
        .build();
    struct.setBool(1, true, false, true, false, true);

    assertThat(struct.getByteArray()).isEqualTo(ba().int8(5).bool(true, false, true, false, true));
  }

  @Test
  void setBool_index_array() {
    var struct = struct()
        .int8()
        .boolArray(0)
        .build();

    struct.setInt8(0, 5);
    struct.setBool(1, 2, true, true);

    assertThat(struct.getByteArray()).isEqualTo(ba().int8(5).bool(false, false, true, true, false));
  }

  @Test
  void setBool_index_array_length_constant() {
    var struct = struct()
        .boolArray(constant(5))
        .build();
    struct.setBool(0, 2, true, true);

    assertThat(struct.getByteArray()).isEqualTo(ba().bool(false, false, true, true, false));
  }

  @Test
  void setBool_index_array_length_field_constant() {
    var struct = struct()
        .primitive().constant((byte) 5).int8()
        .boolArray(0)
        .build();
    struct.setBool(1, 2, true, true);

    assertThat(struct.getByteArray()).isEqualTo(ba().int8(5).bool(false, false, true, true, false));
  }

  @Test
  void setBool_index_array_with_index_negative() {
    var struct = struct()
        .boolArray(constant(5))
        .build();

    assertThatThrownBy(() -> struct.setBool(0, -1, true, false))
        .isInstanceOf(IndexOutOfBoundsException.class)
        .hasMessage("BoolArrayType at position 0 length: 5 start: -1 end: 1");
  }

  @Test
  void setBool_index_array_with_index_greater_than_length() {
    var struct = struct()
        .boolArray(constant(5))
        .build();

    assertThatThrownBy(() -> struct.setBool(0, 5, true, false))
        .isInstanceOf(IndexOutOfBoundsException.class)
        .hasMessage("BoolArrayType at position 0 length: 5 start: 5 end: 7");
  }

  @Test
  void setBool_index_array_constant() {
    var struct = struct()
        .primitive().constant(true).lengthExpression(constant(5)).bool()
        .build();

    assertThatThrownBy(() -> struct.setBool(0, 1, false, false))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("BoolArrayType at position 0 is constant index: 1 value: false constant: true");
  }
}
