package com.github.moaxcp.verybinary.bool;

import com.github.moaxcp.verybinary.BoolArrayType;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.github.moaxcp.verybinary.Builders.struct;
import static com.github.moaxcp.verybinary.ByteArray.ba;
import static com.github.moaxcp.verybinary.Expression.constant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class AddBoolTypeTest {

  @Test
  void add() {
    var struct = struct()
        .int8()
        .boolArray(0)
        .build();

    assertThatThrownBy(() -> ((BoolArrayType) struct.getType(1)).add(struct, Boolean.TRUE))
        .isInstanceOf(UnsupportedOperationException.class)
        .hasMessage("add(Pointer, Boolean) not supported for BoolArrayType. Use add(Pointer, boolean) instead.");
  }

  @Test
  void add_index() {
    var struct = struct()
        .int8()
        .boolArray(0)
        .build();

    assertThatThrownBy(() -> ((BoolArrayType) struct.getType(1)).add(struct, 0, Boolean.TRUE))
        .isInstanceOf(UnsupportedOperationException.class)
        .hasMessage("add(Pointer, long, Boolean) not supported for BoolArrayType. Use add(Pointer, long, boolean) instead.");
  }

  @Test
  void add_array() {
    var struct = struct()
        .int8()
        .boolArray(0)
        .build();

    assertThatThrownBy(() -> ((BoolArrayType) struct.getType(1)).add(struct, new Boolean[]{true, false}))
        .isInstanceOf(UnsupportedOperationException.class)
        .hasMessage("add(Pointer, Boolean...) not supported for BoolArrayType. Use add(Pointer, boolean...) instead.");
  }

  @Test
  void add_index_array() {
    var struct = struct()
        .int8()
        .boolArray(0)
        .build();

    assertThatThrownBy(() -> ((BoolArrayType) struct.getType(1)).add(struct, 0, new Boolean[]{true, false}))
        .isInstanceOf(UnsupportedOperationException.class)
        .hasMessage("add(Pointer, long, Boolean...) not supported for BoolArrayType. Use add(Pointer, long, boolean...) instead.");
  }

  @Test
  void addBoolArray_with_list() {
    var struct = struct()
        .int8()
        .boolArray(0)
        .build();

    struct.addBool(1, List.of(true, false));

    assertThat(struct.getByteArray()).isEqualTo(ba().int8(2).bool(true, false));
  }

  @Test
  void addBool() {
    var struct = struct()
        .int8()
        .boolArray(0)
        .fromBytes(new byte[] {2, 1, 0})
        .build();

    struct.addBool(1, true);
    struct.addBool(1, false);

    assertThat(struct.getByteArray()).isEqualTo(ba().int8(4).bool(true, false, true, false));
  }

  @Test
  void addBool_position_negative() {
    var struct = struct()
        .bool()
        .boolArray(0)
        .fromBytes(new byte[] {2, 1, 0})
        .build();

    assertThatThrownBy(() -> struct.addBool(-1, true))
        .isInstanceOf(IndexOutOfBoundsException.class)
        .hasMessage("Index -1 out of bounds for length 2");
  }

  @Test
  void addBool_position_greater_than_length() {
    var struct = struct()
        .bool()
        .boolArray(0)
        .fromBytes(new byte[] {2, 1, 0})
        .build();

    assertThatThrownBy(() -> struct.addBool(3, true))
        .isInstanceOf(IndexOutOfBoundsException.class)
        .hasMessage("Index 3 out of bounds for length 2");
  }

  @Test
  void addBool_not_allocated() {
    var struct = struct()
        .allocated()
        .int8()
        .boolArray(0)
        .build();

    assertThatThrownBy(() -> struct.addBool(1, true))
        .isInstanceOf(IndexOutOfBoundsException.class)
        .hasMessage("allocated: 0, index: 0, length: 1");
  }

  @Test
  void addBool_constant_value() {
    var struct = struct()
        .int8()
        .primitive().constant(new boolean[]{true, true, true, true, true}).bool()
        .build();

    assertThatThrownBy(() -> struct.addBool(1, true))
        .isInstanceOf(IllegalStateException.class)
        .hasMessage("BoolArrayType at position 1 is constant length: 5 index: 5");
  }

  @Test
  void addBool_constant_value_bad_value() {
    var struct = struct()
        .int8()
        .primitive().constant(new boolean[]{true, true, true, true, true}).bool()
        .build();

    assertThatThrownBy(() -> struct.addBool(1, false))
        .isInstanceOf(IllegalStateException.class)
        .hasMessage("BoolArrayType at position 1 is constant length: 5 index: 5");
  }

  @Test
  void addBool_constant_value_and_length() {
    var struct = struct()
        .primitive().constant(new boolean[]{true, true, true, true, true}).bool()
        .build();

    assertThatThrownBy(() -> struct.addBool(0, false))
        .isInstanceOf(IllegalStateException.class)
        .hasMessage("BoolArrayType at position 0 is constant length: 5 index: 5");
  }

  @Test
  void addBool_not_array() {
    var struct = struct()
        .bool()
        .fromBytes(new byte[] {1})
        .build();
    assertThatThrownBy(() -> struct.addBool(0, true))
        .isInstanceOf(ClassCastException.class);
  }

  @Test
  void addBool_index() {
    var struct = struct()
        .int8()
        .boolArray(0)
        .fromBytes(new byte[] {2, 1, 0})
        .build();

    struct.addBool(1, 0, true);
    struct.addBool(1, 1, false);

    assertThat(struct.getByteArray()).isEqualTo(ba().int8(4).bool(true, false, true, false));
  }

  @Test
  void addBool_index_negative() {
    var struct = struct()
        .int8()
        .boolArray(0)
        .fromBytes(new byte[] {2, 1, 0})
        .build();

    assertThatThrownBy(() -> struct.addBool(1, -1, true))
        .isInstanceOf(ArrayIndexOutOfBoundsException.class)
        .hasMessage("BoolArrayType at position 1 index: -1 new length: 3");
  }

  @Test
  void addBool_index_greater_than_length() {
    var struct = struct()
        .int8()
        .boolArray(0)
        .fromBytes(new byte[] {2, 1, 0})
        .build();

    assertThatThrownBy(() -> struct.addBool(1, 3, true))
        .isInstanceOf(ArrayIndexOutOfBoundsException.class)
        .hasMessage("BoolArrayType at position 1 index: 3 new length: 3");
  }

  @Test
  void addBool_index_not_allocated() {
    var struct = struct()
        .allocated()
        .int8()
        .boolArray(0)
        .build();

    assertThatThrownBy(() -> struct.addBool(1, 0, true))
        .isInstanceOf(IndexOutOfBoundsException.class)
        .hasMessage("allocated: 0, index: 0, length: 1");
  }

  @Test
  void addBool_index_0_not_array() {
    var struct = struct()
        .bool()
        .fromBytes(new byte[] {1})
        .build();

    assertThatThrownBy(() -> struct.addBool(0, 0, true))
        .isInstanceOf(ClassCastException.class);
  }

  @Test
  void addBool_index_1_not_array() {
    var struct = struct()
        .bool()
        .fromBytes(new byte[] {1})
        .build();

    assertThatThrownBy(() -> struct.addBool(0, 1, true))
        .isInstanceOf(ClassCastException.class);
  }

  @Test
  void addBool_index_length_constant() {
    var struct = struct()
        .primitive().lengthExpression(constant(5)).bool()
        .build();

    assertThatThrownBy(() -> struct.addBool(0, 3, false))
        .isInstanceOf(IllegalStateException.class)
        .hasMessage("BoolArrayType at position 0 is constant length: 5 index: 3");
  }

  @Test
  void addBool_index_constant() {
    var struct = struct()
        .primitive().constant(new boolean[]{true, true, true, true, true}).bool()
        .build();

    assertThatThrownBy(() -> struct.addBool(0, 3, false))
        .isInstanceOf(IllegalStateException.class)
        .hasMessage("BoolArrayType at position 0 is constant length: 5 index: 3");
  }

  @Test
  void addBool_array() {
    var struct = struct()
        .int8()
        .boolArray(0)
        .build();

    struct.addBool(1, true, false, true, false, true);

    assertThat(struct.getByteArray()).isEqualTo(ba().int8(5).bool(true, false, true, false, true));
  }

  @Test
  void addBool_array_constant() {
    var struct = struct()
        .int8()
        .primitive().constant(new boolean[]{true, true, true, true, true}).bool()
        .build();

    assertThatThrownBy(() -> struct.addBool(1, true, true, true, true, true))
        .isInstanceOf(IllegalStateException.class)
        .hasMessage("Cannot add elements to fixed length array BoolArrayType at position 1 index: 5");
  }

  @Test
  void addBool_array_constant_bad_values() {
    var struct = struct()
        .int8()
        .primitive().constant(new boolean[]{true}).bool()
        .build();

    assertThatThrownBy(() -> struct.addBool(1, false, false, false, false, false))
        .isInstanceOf(IllegalStateException.class)
        .hasMessage("Cannot add elements to fixed length array BoolArrayType at position 1 index: 1");
  }

  @Test
  void addBool_array_constant_length() {
    var struct = struct()
        .primitive().constant(new boolean[]{true, true, true, true, true}).bool()
        .build();

    assertThatThrownBy(() -> struct.addBool(0, true, false, true, false, true))
        .isInstanceOf(IllegalStateException.class)
        .hasMessage("Cannot add elements to fixed length array BoolArrayType at position 0 index: 5");
  }

  @Test
  void addBool_array_index() {
    var struct = struct()
        .int8()
        .boolArray(0)
        .build();

    struct.addBool(1, true, true, true);
    struct.addBool(1, 2, true, false, true);

    assertThat(struct.getByteArray()).isEqualTo(ba().int8(6).bool(true, true, true, false, true, true));
  }
}
