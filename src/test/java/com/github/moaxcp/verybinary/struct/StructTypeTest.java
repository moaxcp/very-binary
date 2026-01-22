package com.github.moaxcp.verybinary.struct;

import com.github.moaxcp.verybinary.Int16Type;
import com.github.moaxcp.verybinary.Int8Type;
import com.github.moaxcp.verybinary.Type;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;

import static com.github.moaxcp.verybinary.Builders.struct;
import static com.github.moaxcp.verybinary.Builders.structType;
import static com.github.moaxcp.verybinary.ByteArray.ba;
import static com.github.moaxcp.verybinary.Expression.constant;
import static org.assertj.core.api.Assertions.assertThat;

public class StructTypeTest {

  @Test
  void constructor() {
    var type = structType().build();
    assertThat(type.getPosition()).isEqualTo(-1);
  }

  @Test
  void getType() {
    var struct = struct()
        .int8()
        .int16()
        .build();

    assertThat(struct.<Type<?>>getType(0)).isInstanceOf(Int8Type.class);
    assertThat(struct.<Type<?>>getType(1)).isInstanceOf(Int16Type.class);
  }

  @Test
  void getOffset() {
    var struct = struct()
        .int8()
        .struct()
          .int8()
          .int16()
          .end()
        .build();

    assertThat(struct.getType().getOffset(struct)).isEqualTo(0);
    assertThat(struct.getType(0).getOffset(struct)).isEqualTo(0);
    assertThat(struct.getType(1).getOffset(struct)).isEqualTo(1);
  }

  @Test
  void getAllocationLength() {
    var struct = structType()
        .int8()
        .struct()
          .int8()
          .int16()
          .end()
        .build();

    assertThat(struct.getAllocationLength()).isEqualTo(4);
  }

  @Test
  void getAllocationLength_array() {
    var struct = structType()
        .int8()
        .struct()
          .lengthField(0)
          .int8()
          .int16()
          .end()
        .build();

    assertThat(struct.getAllocationLength()).isEqualTo(1);
  }

  @Test
  void getAllocationLength_array_with_constant_length() {
    var struct = structType()
        .int8()
        .struct()
          .lengthExpression(constant(5))
          .int8()
          .int16()
          .end()
        .build();

    assertThat(struct.getAllocationLength()).isEqualTo(16);
  }

  @Test
  void getAllocationLength_array_with_constant_length_field() {
    var struct = structType()
        .primitive().constant((byte) 5).int8()
        .struct()
          .lengthField(0)
          .int8()
          .int16()
          .end()
        .build();

    assertThat(struct.getAllocationLength()).isEqualTo(16);
  }

  @Test
  void getByteLength() {
    var type = structType()
        .int16()
        .int16Array(0)
        .build();

    var struct = struct()
        .int8()
        .int8Array(0)
        .int8()
        .type(type)
        .fromBytes(ba().int8(3).int8(1, 2, 3).int8(4)
            .int16(3).int16(3, 2, 4))
        .build();

    assertThat(struct.getByteLength()).isEqualTo(13);
  }

  @Test
  void getByteLength_array_with_length_field() {
    var struct = struct()
        .int8()
        .structArray(0)
          .int16()
          .boolArray(0)
          .end()
        .build();

    assertThat(struct.getByteLength()).isEqualTo(1);
  }

  @Test
  void isFixedLength_true() {
    var struct = struct()
        .int8()
        .int16()
        .build();

    assertThat(struct.isFixedLength()).isTrue();
  }

  @Test
  void isFixedLength_false() {
    var struct = struct()
        .int8()
        .int8Array(0)
        .build();

    assertThat(struct.isFixedLength()).isFalse();
  }

  @Test
  void AllTypes() {
    var struct = struct()
        .int8()
        .int8Array(0)
        .uint8()
        .uint8Array(2)
        .int16()
        .int16Array(4)
        .uint16()
        .uint16Array(6)
        .int32()
        .int32Array(8)
        .uint32()
        .uint32Array(10)
        .int64()
        .int64Array(12)
        .uint64()
        .uint64Array(14)
        .float32()
        .float32Array(16)
        .float64()
        .float64Array(18)
        .fromBytes(new byte[] {2, -20, -21,
            2, 20, 21,
            0, 2, -1, -20, -1, -21,
            0, 2, 0, 20, 0, 21,
            0, 0, 0, 2, -1, -1, -1, -20, -1, -1, -1, -21,
            0, 0, 0, 2, 0, 0, 0, 20, 0, 0, 0, 21,
            0, 0, 0, 0, 0, 0, 0, 2, -1, -1, -1 ,-1, -1, -1, -1, -20, -1, -1, -1, -1, -1, -1, -1, -21,
            0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0, 0, 0, 0, 0, 20, 0, 0, 0, 0, 0, 0, 0, 21,
            64, 0, 0, 0, 64, 64, 0, 0, 64, -128, 0, 0,
            64, 0, 0, 0, 0, 0, 0, 0, 64, 8, 0, 0, 0, 0, 0, 0, 64, 16, 0, 0, 0, 0, 0, 0
        })
        .build();

    assertThat(struct.getInt8(0)).isEqualTo((byte) 2);
    assertThat(struct.getInt8(1, 0)).isEqualTo((byte) -20);
    assertThat(struct.getInt8(1, 1)).isEqualTo((byte) -21);
    assertThat(struct.getUint8(2)).isEqualTo((byte) 2);
    assertThat(struct.getUint8(3, 0)).isEqualTo((byte) 20);
    assertThat(struct.getUint8(3, 1)).isEqualTo((byte) 21);
    assertThat(struct.getInt16(4)).isEqualTo((short) 2);
    assertThat(struct.getInt16(5, 0)).isEqualTo((short) -20);
    assertThat(struct.getInt16(5, 1)).isEqualTo((short) -21);
    assertThat(struct.getUint16(6)).isEqualTo(2);
    assertThat(struct.getUint16(7, 0)).isEqualTo(20);
    assertThat(struct.getUint16(7, 1)).isEqualTo(21);
    assertThat(struct.getInt32(8)).isEqualTo(2);
    assertThat(struct.getInt32(9, 0)).isEqualTo(-20);
    assertThat(struct.getInt32(9, 1)).isEqualTo(-21);
    assertThat(struct.getUint32(10)).isEqualTo(2);
    assertThat(struct.getUint32(11, 0)).isEqualTo(20);
    assertThat(struct.getUint32(11, 1)).isEqualTo(21);
    assertThat(struct.getInt64(12)).isEqualTo(2);
    assertThat(struct.getInt64(13, 0)).isEqualTo(-20);
    assertThat(struct.getInt64(13, 1)).isEqualTo(-21);
    assertThat(struct.getUint64(14)).isEqualTo(BigInteger.valueOf(2));
    assertThat(struct.getUint64(15, 0)).isEqualTo(BigInteger.valueOf(20));
    assertThat(struct.getUint64(15, 1)).isEqualTo(BigInteger.valueOf(21));
    assertThat(struct.getFloat32(16)).isEqualTo(2);
    assertThat(struct.getFloat32(17, 0)).isEqualTo(3);
    assertThat(struct.getFloat32(17, 1)).isEqualTo(4);
    assertThat(struct.getFloat64(18)).isEqualTo(2);
    assertThat(struct.getFloat64(19, 0)).isEqualTo(3);
    assertThat(struct.getFloat64(19, 1)).isEqualTo(4);
  }

  @Test
  void withStruct() {
    var struct = struct()
        .int8()
        .int16()
        .int8Array(1)
        .struct()
            .int16()
            .int16Array(0)
            .end()
        .fromBytes(new byte[] {1, 0, 2, 1, 2, 0, 3, 0, 1, 0, 2, 0, 3})
        .build();

    assertThat(struct.getInt8(0)).isEqualTo((byte) 1);
    assertThat(struct.getInt16(1)).isEqualTo((short) 2);
    assertThat(struct.getInt8(2, 0)).isEqualTo((byte) 1);
    assertThat(struct.getInt8(2, 1)).isEqualTo((byte) 2);
    var inner = struct.getStruct(3);
    assertThat(inner.getInt16(0)).isEqualTo((short) 3);
    assertThat(inner.getInt16(1, 0)).isEqualTo((short) 1);
    assertThat(inner.getInt16(1, 1)).isEqualTo((short) 2);
    assertThat(inner.getInt16(1, 2)).isEqualTo((short) 3);
  }
}
