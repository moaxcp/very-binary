package com.github.moaxcp.verybinary.float32;

import com.github.moaxcp.verybinary.Float32Type;
import org.junit.jupiter.api.Test;

import static com.github.moaxcp.verybinary.Builders.struct;
import static com.github.moaxcp.verybinary.Builders.structType;
import static com.github.moaxcp.verybinary.ByteArray.ba;
import static com.github.moaxcp.verybinary.ByteLengthListener.align;
import static com.github.moaxcp.verybinary.Expression.constant;
import static com.github.moaxcp.verybinary.Expression.valueOf;
import static com.github.moaxcp.verybinary.Float32Type.float32Type;
import static com.github.moaxcp.verybinary.Primitive.FLOAT32;
import static com.github.moaxcp.verybinary.PrimitiveBuilder.primitive;
import static org.assertj.core.api.Assertions.assertThat;

public class Float32TypeTest {

  @Test
  void constructor() {
    var type = float32Type();
    assertThat(type.getPosition()).isEqualTo(-1);
  }

  @Test
  void constructorPosition() {
    var type = float32Type(15);
    assertThat(type.getPosition()).isEqualTo(15);
  }

  @Test
  void constructorEverything() {
    var struct = struct()
        .float32()
        .primitive().constant(3.0f).lengthExpression(valueOf(0)).float32()
        .align(2)
        .build();

    assertThat(struct.getByteLength()).isEqualTo(FLOAT32.size() + 2);
    assertThat(struct.<Float32Type>getType(1)).isEqualTo(primitive().position(1).byteLengthListener(align(2)).constant(3.0f).lengthExpression(valueOf(0)).float32());
  }

  @Test
  void copy() {
    var type = float32Type();
    var copy = type.copy(-1);
    assertThat(copy).isEqualTo(type);
  }

  @Test
  void getUnitSize() {
    var struct = struct()
        .float32()
        .build();

    assertThat(((Float32Type) struct.getType(0)).getUnitSize()).isEqualTo(FLOAT32);
  }

  @Test
  void getOffset() {
    var struct = struct()
        .float32()
        .float32()
        .build();

    assertThat(struct.getType(0).getOffset(struct)).isEqualTo(0);
    assertThat(struct.getType(1).getOffset(struct)).isEqualTo(4);
  }

  @Test
  void getAllocationLength() {
    var type = structType()
        .float32()
        .build();

    assertThat(type.getType(0).getAllocationLength()).isEqualTo(4);
  }

  @Test
  void getAllocationLength_array() {
    var type = structType()
        .int8()
        .float32Array(0)
        .build();
    assertThat(type.getType(1).getAllocationLength(type)).isEqualTo(0);
  }

  @Test
  void getAllocationLength_array_with_constant_length() {
    var type = structType()
        .float32Array(constant(5))
        .build();
    assertThat(type.getType(0).getAllocationLength(type)).isEqualTo(20);
    assertThat(type.getAllocationLength()).isEqualTo(20);
  }

  @Test
  void getAllocationLength_array_with_constant_length_field() {
    var type = structType()
        .primitive().constant((byte) 5).int8()
        .float32Array(0)
        .build();
    assertThat(type.getType(1).getAllocationLength(type)).isEqualTo(20);
    assertThat(type.getAllocationLength()).isEqualTo(21);
  }

  @Test
  void getByteLength() {
    var struct = struct()
        .float32()
        .build();

    assertThat(struct.getByteLength()).isEqualTo(FLOAT32.size());
    assertThat(struct.getType(0).getByteLength(struct)).isEqualTo(FLOAT32.size());
  }

  @Test
  void getByteLength_array_constant_length() {
    var struct = struct()
        .float32Array(constant(5))
        .build();

    assertThat(struct.getByteLength(0)).isEqualTo(FLOAT32.size() * 5);
    assertThat(struct.getType(0).getByteLength(struct)).isEqualTo(FLOAT32.size() * 5);
  }

  @Test
  void getByteLength_array_with_length_field() {
    var struct = struct()
        .primitive().constant((short) 5).int8()
        .float32Array(0)
        .build();

    assertThat(struct.getByteLength(1)).isEqualTo(FLOAT32.size() * 5);
    assertThat(struct.getType(1).getByteLength(struct)).isEqualTo(FLOAT32.size() * 5);
  }

  @Test
  void getByteLength_array_with_index() {
    var struct = struct()
        .float32Array(constant(5))
        .build();

    assertThat(struct.getByteLength(0, 2)).isEqualTo(4);
  }

  @Test
  void getByteLength_array_with_index_length() {
    var struct = struct()
        .float32Array(constant(5))
        .build();

    assertThat(struct.getByteLength(0, 2, 2)).isEqualTo(8);
  }

  @Test
  void isFixedLength() {
    var struct = struct()
        .float32()
        .build();

    assertThat(struct.getType(0).isFixedLength(struct)).isTrue();
    assertThat(struct.isFixedLength()).isTrue();
  }

  @Test
  void isFixedLengthArray_constant_length() {
    var struct = struct()
        .float32Array(constant(5))
        .build();

    assertThat(struct.getType(0).isFixedLength(struct)).isTrue();
    assertThat(struct.isFixedLength()).isTrue();
  }

  @Test
  void isFixedLengthArray_variable_length() {
    var struct = struct()
        .int8()
        .float32Array(0)
        .build();

    assertThat(struct.getType(1).isFixedLength(struct)).isFalse();
    assertThat(struct.isFixedLength()).isFalse();
  }

  @Test
  void isFixedLengthArray_constant_length_field() {
    var struct = struct()
        .primitive().constant((byte) 5).int8()
        .float32Array(0)
        .build();

    assertThat(struct.getType(1).isFixedLength(struct)).isTrue();
    assertThat(struct.isFixedLength()).isTrue();
  }

  @Test
  void setting_length_field_extends_array() {
    var struct = struct()
        .int8()
        .float32Array(0)
        .build();

    struct.setInt8(0, 5);
    assertThat(struct.getByteArray()).isEqualTo(ba().int8(5).float32(0, 0, 0, 0, 0));
  }

  @Test
  void setting_length_field_extends_array_with_constant_values() {
    var struct = struct()
        .int8()
        .primitive().lengthField(0).constant(5f).float32()
        .build();

    struct.setInt8(0, 5);
    assertThat(struct.getByteArray()).isEqualTo(ba().int8(5).float32(5, 5, 5, 5, 5));
  }

  @Test
  void setting_byte_length_field_extends_array() {
    var struct = struct()
        .int8()
        .primitive().byteLengthField(0).float32()
        .build();

    struct.setInt8(0, 5);

    assertThat(struct.getByteArray()).isEqualTo(ba().int8(5).float32(0, 0, 0, 0, 0));
  }

  @Test
  void setting_byte_length_field_extends_array_with_constant_values() {
    var struct = struct()
        .int8()
        .primitive().byteLengthField(0).constant(5f).float32()
        .build();

    struct.setInt8(0, 5);

    assertThat(struct.getByteArray()).isEqualTo(ba().int8(5).float32(5, 5, 5, 5, 5));
  }
}
