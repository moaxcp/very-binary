package com.github.moaxcp.verybinary.bool;

import com.github.moaxcp.verybinary.BoolType;
import org.junit.jupiter.api.Test;

import static com.github.moaxcp.verybinary.Builders.struct;
import static com.github.moaxcp.verybinary.ByteArray.ba;
import static com.github.moaxcp.verybinary.Expression.constant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class GetBoolTypeTest {

  @Test
  void get() {
    var struct = struct()
        .bool()
        .build();

    assertThatThrownBy(() -> ((BoolType) struct.getType(0)).get(struct))
        .isInstanceOf(UnsupportedOperationException.class)
        .hasMessage("get(Pointer) not supported for BoolType. Use getBool(Pointer) instead.");
  }

  @Test
  void get_index() {
    var struct = struct()
        .boolArray(constant(5))
        .build();

    assertThatThrownBy(() -> ((BoolType) struct.getType(0)).get(struct, 0))
        .isInstanceOf(UnsupportedOperationException.class)
        .hasMessage("get(Pointer, long) not supported for BoolType. Use getBool(Pointer, long) instead.");
  }

  @Test
  void getArray() {
    var struct = struct()
        .boolArray(constant(5))
        .build();
    assertThatThrownBy(() -> ((BoolType) struct.getType(0)).getArray(struct))
        .isInstanceOf(UnsupportedOperationException.class)
        .hasMessage("getArray(Pointer) not supported for BoolType. Use getBoolArray(Pointer) instead.");
  }

  @Test
  void getArray_index_length() {
    var struct = struct()
        .boolArray(constant(5))
        .build();

    assertThatThrownBy(() -> ((BoolType) struct.getType(0)).getArray(struct, 2, 2))
        .isInstanceOf(UnsupportedOperationException.class)
        .hasMessage("getArray(Pointer, long, long) not supported for BoolType. Use getBoolArray(Pointer, long, long) instead.");
  }

  @Test
  void getList() {
    var struct = struct()
        .boolArray(constant(5))
        .build();

    assertThatThrownBy(() -> ((BoolType) struct.getType(0)).getList(struct))
        .isInstanceOf(UnsupportedOperationException.class)
        .hasMessage("getList(Pointer) not supported for BoolType. Use getBoolList(Pointer) instead.");
  }

  @Test
  void getList_index_length() {
    var struct = struct()
        .boolArray(constant(5))
        .build();

    assertThatThrownBy(() -> ((BoolType) struct.getType(0)).getList(struct, 2, 2))
        .isInstanceOf(UnsupportedOperationException.class)
        .hasMessage("getList(Pointer, long, long) not supported for BoolType. Use getBoolList(Pointer, long, long) instead.");
  }

  @Test
  void getBool() {
    var struct = struct()
        .bool()
        .build();

    struct.setBool(0, true);

    assertThat(struct.getBool(0)).isTrue();
  }

  @Test
  void getBool_position_negative() {
    var struct = struct()
        .bool()
        .build();

    assertThatThrownBy(() -> struct.getBool(-1))
        .isInstanceOf(IndexOutOfBoundsException.class)
        .hasMessage("Index -1 out of bounds for length 1");
  }

  @Test
  void getBool_position_greater_than_length() {
    var struct = struct()
        .bool()
        .build();

    assertThatThrownBy(() -> struct.getBool(2))
        .isInstanceOf(IndexOutOfBoundsException.class)
        .hasMessage("Index 2 out of bounds for length 1");
  }

  @Test
  void getBoolAllocated() {
    var struct = struct()
        .bool()
        .build();

    assertThat(struct.getBool(0)).isFalse();
    assertThat(struct.getByteArray()).isEqualTo(ba().bool(false));
  }

  @Test
  void getBoolNotAllocated() {
    var struct = struct()
        .allocated()
        .bool()
        .build();

    assertThatThrownBy(() -> struct.getBool(0))
        .isInstanceOf(IndexOutOfBoundsException.class)
        .hasMessage("allocated: 0, index: 0, length: 1");
  }

  @Test
  void getBool_constant() {
    var struct = struct()
        .primitive().constant(true).bool()
        .build();

    assertThat(struct.getBool(0)).isTrue();
  }

  @Test
  void getBool_index_0_not_array() {
    var struct = struct()
        .bool()
        .fromBytes(new byte[] {1})
        .build();

    assertThat(struct.getBool(0, 0)).isTrue();
  }

  @Test
  void getBool_index_1_not_array() {
    var struct = struct()
        .bool()
        .fromBytes(new byte[] {1})
        .build();

    assertThatThrownBy(() -> struct.getBool(0, 1))
        .isInstanceOf(ArrayIndexOutOfBoundsException.class)
        .hasMessage("BoolType at position 0 index: 1 length: 1");
  }

  @Test
  void getBool_index() {
    var struct = struct()
        .int8()
        .boolArray(0)
        .fromBytes(new byte[] {2, 1, 0})
        .build();

    assertThat(struct.getBool(1, 0)).isTrue();
    assertThat(struct.getBool(1, 1)).isFalse();
  }

  @Test
  void getBool_index_negative() {
    var struct = struct()
        .int8()
        .boolArray(0)
        .build();

    assertThatThrownBy(() -> struct.getBool(1, -1))
        .isInstanceOf(ArrayIndexOutOfBoundsException.class)
        .hasMessage("BoolType at position 1 index: -1 length: 0");
  }

  @Test
  void getBool_index_greater_than_length() {
    var struct = struct()
        .int8()
        .boolArray(0)
        .fromBytes(new byte[] {2, 1, 0})
        .build();

    assertThatThrownBy(() -> struct.getBool(1, 2))
        .isInstanceOf(ArrayIndexOutOfBoundsException.class)
        .hasMessage("BoolType at position 1 index: 2 length: 2");
  }

  @Test
  void getBool_index_not_allocated() {
    var struct = struct()
        .allocated()
        .int8()
        .boolArray(0)
        .build();

    assertThatThrownBy(() -> struct.getBool(1, 0))
        .isInstanceOf(IndexOutOfBoundsException.class)
        .hasMessage("allocated: 0, index: 0, length: 1");
  }

  @Test
  void getBool_index_constant() {
    var struct = struct()
        .primitive().constant(true).lengthExpression(constant(5)).bool()
        .build();

    assertThat(struct.getBool(0, 3)).isTrue();
  }

  @Test
  void getBoolArray() {
    var struct = struct()
        .boolArray(constant(5))
        .fromBytes(ba().bool(true, false, true, false, true))
        .build();

    assertThat(struct.getBoolArray(0)).containsExactly(true, false, true, false, true);
  }

  @Test
  void getBoolArray_sub() {
    var struct = struct()
        .boolArray(constant(5))
        .fromBytes(ba().bool(true, false, true, false, true))
        .build();

    assertThat(struct.getBoolArray(0, 2, 2)).containsExactly(true, false);
  }

  @Test
  void getBoolArray_sub_index_negative() {
    var struct = struct()
        .boolArray(constant(5))
        .fromBytes(ba().bool(true, false, true, false, true))
        .build();

    assertThatThrownBy(() -> struct.getBoolArray(0, -2, 2))
        .isInstanceOf(ArrayIndexOutOfBoundsException.class)
        .hasMessage("BoolType at position 0 length: 5 start: -2 end: 0");
  }

  @Test
  void getBoolArray_sub_index_over() {
    var struct = struct()
        .boolArray(constant(5))
        .fromBytes(ba().bool(true, false, true, false, true))
        .build();

    assertThatThrownBy(() -> struct.getBoolArray(0, 5, 2))
        .isInstanceOf(ArrayIndexOutOfBoundsException.class)
        .hasMessage("BoolType at position 0 length: 5 start: 5 end: 7");
  }

  @Test
  void getBoolList() {
    var struct = struct()
        .boolArray(constant(5))
        .fromBytes(ba().bool(true, false, true, false, true))
        .build();

    assertThat(struct.getBoolList(0)).containsExactly(true, false, true, false, true);
  }

  @Test
  void getBoolList_sub() {
    var struct = struct()
        .boolArray(constant(5))
        .fromBytes(ba().bool(true, false, true, false, true))
        .build();

    assertThat(struct.getBoolList(0, 2, 2)).containsExactly(true, false);
  }

  @Test
  void getBoolList_sub_index_negative() {
    var struct = struct()
        .boolArray(constant(5))
        .fromBytes(ba().bool(true, false, true, false, true))
        .build();

    assertThatThrownBy(() -> struct.getBoolList(0, -2, 2))
        .isInstanceOf(ArrayIndexOutOfBoundsException.class)
        .hasMessage("BoolType at position 0 length: 5 start: -2 end: 0");
  }

  @Test
  void getBoolList_sub_index_over() {
    var struct = struct()
        .boolArray(constant(5))
        .fromBytes(ba().bool(true, false, true, false, true))
        .build();

    assertThatThrownBy(() -> struct.getBoolList(0, 5, 2))
        .isInstanceOf(ArrayIndexOutOfBoundsException.class)
        .hasMessage("BoolType at position 0 length: 5 start: 5 end: 7");
  }
}
