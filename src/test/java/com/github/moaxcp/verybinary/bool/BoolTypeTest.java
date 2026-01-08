package com.github.moaxcp.verybinary.bool;

import com.github.moaxcp.verybinary.BoolType;
import org.junit.jupiter.api.Test;

import static com.github.moaxcp.verybinary.Builders.struct;
import static com.github.moaxcp.verybinary.Builders.structType;
import static com.github.moaxcp.verybinary.ByteArray.ba;
import static com.github.moaxcp.verybinary.ByteLengthListener.align;
import static com.github.moaxcp.verybinary.Expression.constant;
import static com.github.moaxcp.verybinary.Expression.valueOf;
import static com.github.moaxcp.verybinary.Primitive.BOOL;
import static com.github.moaxcp.verybinary.PrimitiveBuilder.primitive;
import static org.assertj.core.api.Assertions.assertThat;

public class BoolTypeTest {

  @Test
  void constructor() {
    var type = BoolType.bool();
    assertThat(type).isEqualTo(new BoolType(-1));
  }

  @Test
  void constructorPosition() {
    var type = BoolType.bool(15);
    assertThat(type).isEqualTo(new BoolType(15));
  }

  @Test
  void constructorEverything() {
    var struct = struct()
        .int8()
        .primitive().constant(true).lengthExpression(valueOf(0)).bool()
        .align(2)
        .build();

    assertThat(struct.getByteLength()).isEqualTo(BOOL.size() + 2);
    assertThat(struct.<BoolType>getType(1))
        .isEqualTo(primitive().position(1).byteLengthListener(align(2)).constant(true).lengthExpression(valueOf(0)).bool());
  }

  @Test
  void copy() {
    var type = BoolType.bool();
    var copy = type.copy(-1);
    assertThat(copy).isEqualTo(type);
  }

  @Test
  void getUnitSize() {
    var struct = struct()
        .bool()
        .build();

    assertThat(((BoolType) struct.getType(0)).getUnitSize()).isEqualTo(BOOL);
  }

  @Test
  void getOffset() {
    var struct = struct()
        .bool()
        .bool()
        .build();

    assertThat(struct.getType(0).getOffset(struct)).isEqualTo(0);
    assertThat(struct.getType(1).getOffset(struct)).isEqualTo(1);
  }

  @Test
  void getAllocationLength() {
    var type = structType()
        .bool()
        .build();

    assertThat(type.getType(0).getAllocationLength()).isEqualTo(1);
  }

  @Test
  void getAllocationLength_array() {
    var type = structType()
        .int8()
        .boolArray(0)
        .build();
    assertThat(type.getType(0).getAllocationLength()).isEqualTo(1);
  }

  @Test
  void getAllocationLength_array_with_constant_length() {
    var type = structType()
        .boolArray(constant(5))
        .build();
    assertThat(type.getType(0).getAllocationLength()).isEqualTo(5);
    assertThat(type.getAllocationLength()).isEqualTo(5);
  }

  @Test
  void getAllocationLength_array_with_constant_length_field() {
    var type = structType()
        .primitive().constant((byte) 5).int8()
        .boolArray(0)
        .build();
    assertThat(type.getType(1).getAllocationLength(type)).isEqualTo(5);
    assertThat(type.getAllocationLength()).isEqualTo(6);
  }

  @Test
  void getByteLength() {
    var struct = struct()
        .bool()
        .build();

    assertThat(struct.getByteLength(0)).isEqualTo(BOOL.size());
    assertThat(struct.getType(0).getByteLength(struct)).isEqualTo(BOOL.size());
  }

  @Test
  void getByteLength_array_constant_length() {
    var struct = struct()
        .boolArray(constant(5))
        .build();

    assertThat(struct.getByteLength(0)).isEqualTo(BOOL.size() * 5);
    assertThat(struct.getType(0).getByteLength(struct)).isEqualTo(BOOL.size() * 5);
  }

  @Test
  void getByteLength_array_with_length_field() {
    var struct = struct()
        .primitive().constant((short) 5).int8()
        .boolArray(0)
        .build();

    assertThat(struct.getByteLength(1)).isEqualTo(BOOL.size() * 5);
    assertThat(struct.getType(1).getByteLength(struct)).isEqualTo(BOOL.size() * 5);
  }

  @Test
  void getByteLength_array_with_index() {
    var struct = struct()
        .boolArray(constant(5))
        .build();

    assertThat(struct.getByteLength(0, 2)).isEqualTo(1);
  }

  @Test
  void getByteLength_array_with_index_length() {
    var struct = struct()
        .boolArray(constant(5))
        .build();

    assertThat(struct.getByteLength(0, 2, 2)).isEqualTo(2);
  }

  @Test
  void isFixedLength() {
    var struct = struct()
        .bool()
        .build();

    assertThat(struct.getType(0).isFixedLength(struct)).isTrue();
    assertThat(struct.isFixedLength()).isTrue();
  }

  @Test
  void isFixedLengthArray_constant_length() {
    var struct = struct()
        .boolArray(constant(5))
        .build();

    assertThat(struct.getType(0).isFixedLength(struct)).isTrue();
    assertThat(struct.isFixedLength()).isTrue();
  }

  @Test
  void isFixedLengthArray_variable_length() {
    var struct = struct()
        .int8()
        .boolArray(0)
        .build();

    assertThat(struct.getType(1).isFixedLength(struct)).isFalse();
    assertThat(struct.isFixedLength()).isFalse();
  }

  @Test
  void isFixedLengthArray_constant_length_field() {
    var struct = struct()
        .primitive().constant((byte) 5).int8()
        .boolArray(0)
        .build();

    assertThat(struct.getType(1).isFixedLength(struct)).isTrue();
    assertThat(struct.isFixedLength()).isTrue();
  }

  @Test
  void setting_length_field_extends_array() {
    var struct = struct()
        .int8()
        .boolArray(0)
        .build();

    struct.setInt8(0, 5);
    assertThat(struct.getByteArray()).isEqualTo(ba().int8(5).bool(false, false, false, false, false));
  }

  @Test
  void setting_length_field_extends_array_with_constant_values() {
    var struct = struct()
        .int8()
        .primitive().lengthField(0).constant(true).bool()
        .build();

    struct.setInt8(0, 5);
    assertThat(struct.getByteArray()).isEqualTo(ba().int8(5).bool(true, true, true, true, true));
  }

  @Test
  void setting_byte_length_field_extends_array() {
    var struct = struct()
        .int8()
        .primitive().byteLengthField(0).bool()
        .build();

    struct.setInt8(0, 5);

    assertThat(struct.getByteArray()).isEqualTo(ba().int8(5).bool(false, false, false, false, false));
  }

  @Test
  void setting_byte_length_field_extends_array_with_constant_values() {
    var struct = struct()
        .int8()
        .primitive().byteLengthField(0).constant(true).bool()
        .build();

    struct.setInt8(0, 5);

    assertThat(struct.getByteArray()).isEqualTo(ba().int8(5).bool(true, true, true, true, true));
  }
}
