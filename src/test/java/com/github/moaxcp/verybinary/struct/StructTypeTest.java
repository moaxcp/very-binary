package com.github.moaxcp.verybinary.struct;

import com.github.moaxcp.verybinary.Type;
import com.github.moaxcp.verybinary.Int16Type;
import com.github.moaxcp.verybinary.Int8Type;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.util.List;

import static com.github.moaxcp.verybinary.Builders.*;
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
          .primitive().lengthExpression(constant(5)).int16()
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
          .primitive().lengthExpression(constant(5)).int16()
          .end()
        .build();

    assertThat(struct.getAllocationLength()).isEqualTo(56);
  }

  @Test
  void getAllocationLength_array_with_constant_length_field() {
    var struct = structType()
        .primitive().constant((byte) 5).int8()
        .struct()
          .lengthField(0)
          .int8()
          .primitive().lengthExpression(constant(5)).int16()
          .end()
        .build();

    assertThat(struct.getAllocationLength()).isEqualTo(56);
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
        .struct(type)
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
          .primitive().constant(5).int16()
          .boolArray(0)
          .end()
        .build();

    assertThat(struct.getByteLength(1)).isEqualTo(0);
    assertThat(struct.getByteLength()).isEqualTo(1);
  }

  @Test
  void getByteLength_array_constant_length() {
    var inner = structType()
        .int16()
        .boolArray(0)
        .build();

    var struct = struct()
        .int8()
        .structArray(constant(2), inner)
        .fromBytes(ba()
            .int8(2)
            .int16(3).bool(true, false, true)
            .int16(5).bool(true, false, true, false, true))
        .build();

    assertThat(struct.getByteLength(1)).isEqualTo(12);
    assertThat(struct.getByteLength()).isEqualTo(13);
  }

  @Test
  void getByteLength_array_constant_length_field() {
    var inner = structType()
        .int16()
        .boolArray(0)
        .build();

    var struct = struct()
        .primitive().constant(2).int8()
        .structArray(0, inner)
        .fromBytes(ba()
            .int8(2)
            .int16(3).bool(true, false, true)
            .int16(5).bool(true, false, true, false, true))
        .build();

    assertThat(struct.getByteLength(1)).isEqualTo(12);
    assertThat(struct.getByteLength()).isEqualTo(13);
  }

  @Test
  void getByteLength_array_index() {
    var inner = structType()
        .int16()
        .boolArray(0)
        .build();

    var struct = struct()
        .primitive().constant(2).int8()
        .structArray(0, inner)
        .fromBytes(ba()
            .int8(2)
            .int16(3).bool(true, false, true)
            .int16(5).bool(true, false, true, false, true))
        .build();

    assertThat(struct.getByteLength(1, 0)).isEqualTo(5);
    assertThat(struct.getByteLength(1, 1)).isEqualTo(7);
  }

  @Test
  void getByteLength_array_index_length() {
    var inner = structType()
        .int16()
        .boolArray(0)
        .build();

    var struct = struct()
        .primitive().constant(4).int8()
        .structArray(0, inner)
        .fromBytes(ba()
            .int8(4)
            .int16(3).bool(true, false, true)
            .int16(5).bool(true, false, true, false, true)
            .int16(4).bool(true, false, true, false)
            .int16(2).bool(true, false))
        .build();

    assertThat(struct.getByteLength(1, 0, 4)).isEqualTo(22);
    assertThat(struct.getByteLength(1, 1, 2)).isEqualTo(13);
  }

  @Test
  void isFixedLength_constant_length() {
    var inner = structType()
        .primitive().constant(3).int16()
        .primitive().constant(true).lengthField(0).bool()
        .build();

    var struct = struct()
        .int8()
        .structArray(constant(2), inner)
        .fromBytes(ba()
            .int8(2)
            .int16(3).bool(true, true, true)
            .int16(3).bool(true, true, true))
        .build();

    assertThat(struct.isFixedLength()).isTrue();
  }

  @Test
  void isFixedLength_variable_length() {
    var inner = structType()
        .int16()
        .boolArray(0)
        .build();

    var struct = struct()
        .primitive().constant(2).int8()
        .structArray(0, inner)
        .fromBytes(ba()
            .int8(2)
            .int16(3).bool(true, false, true)
            .int16(5).bool(true, false, true, false, true))
        .build();

    assertThat(struct.isFixedLength()).isFalse();
  }

  @Test
  void isFixedLength_constant_length_field() {
    var inner = structType()
        .primitive().constant(3).int16()
        .primitive().constant(new boolean[]{true, true, true}).bool()
        .build();

    var struct = struct()
        .primitive().constant(2).int8()
        .structArray(0, inner)
        .fromBytes(ba()
            .int8(2)
            .int16(3).bool(true, true, true)
            .int16(3).bool(true, true, true))
        .build();

    assertThat(struct.isFixedLength()).isTrue();
  }

  @Test
  void setting_length_field_extends_array() {
    var inner = structType()
        .primitive().int16()
        .primitive().lengthField(0).bool()
        .build();

    var struct = struct()
        .int8()
        .structArray(0, inner)
        .fromBytes(ba()
            .int8(2)
            .int16(3).bool(true, true, true)
            .int16(3).bool(true, true, true))
        .build();

    struct.setInt8(0, 5);

    assertThat(struct.getByteArray()).isEqualTo(ba().int8(5)
        .int16(3).bool(true, true, true)
        .int16(3).bool(true, true, true)
        .int16(0)
        .int16(0)
        .int16(0));
  }

  @Test
  void setting_length_field_extends_array_with_constant_values() {
    var inner = structType()
        .constant(ba().int16(3).bool(true, true, true))
        .primitive().int16()
        .primitive().lengthField(0).bool()
        .build();

    var struct = struct()
        .int8()
        .structArray(0, inner)
        .fromBytes(ba()
            .int8(2)
            .int16(3).bool(true, true, true)
            .int16(3).bool(true, true, true))
        .build();

    struct.setInt8(0, 5);

    assertThat(struct.getByteArray()).isEqualTo(ba().int8(5)
        .int16(3).bool(true, true, true)
        .int16(3).bool(true, true, true)
        .int16(3).bool(true, true, true)
        .int16(3).bool(true, true, true)
        .int16(3).bool(true, true, true));
  }

  @Test
  void toString_empty() {
    var struct = struct().build();
    assertThat(struct.toString()).isEqualTo("{}");
  }

  @Test
  void toString_notEmpty() {
    var inner = structType().int8().build();
    var innerArray = structListType().int8().lengthExpression(constant(2)).build();
    var struct = struct()
        .int8()
        .int8Array(constant(2))
        .uint8()
        .uint8Array(constant(2))
        .int16()
        .int16Array(constant(2))
        .uint16()
        .uint16Array(constant(2))
        .int32()
        .int32Array(constant(2))
        .uint32()
        .uint32Array(constant(2))
        .int64()
        .int64Array(constant(2))
        .uint64()
        .uint64Array(constant(2))
        .float32()
        .float32Array(constant(2))
        .float64()
        .float64Array(constant(2))
        .struct(inner)
        .structArray(innerArray)
        .build()
        .setInt8(0, -127)
        .setInt8(1, new int[] {-127, 128})
        .setUint8(2, 255)
        .setUint8(3, new int[] {0, 255})
        .setInt16(4, -32768)
        .setInt16(5, new int[] {-32768, 32767})
        .setUint16(6, 65535)
        .setUint16(7, new int[] {0, 65535})
        .setInt32(8, -2147483648)
        .setInt32(9, new int[] {-2147483648, 2147483647})
        .setUint32(10, 4294967295L)
        .setUint32(11, new long[] {0, 4294967295L})
        .setInt64(12, -9223372036854775808L)
        .setInt64(13, new long[] {-9223372036854775808L, 9223372036854775807L})
        .setUint64(14, new BigInteger("18446744073709551615"))
        .setUint64(15, List.of(BigInteger.ZERO, new BigInteger("18446744073709551615")))
        .setFloat32(16, Float.MIN_VALUE)
        .setFloat32(17, Float.MIN_VALUE, Float.MAX_VALUE)
        .setFloat64(18, Double.MIN_VALUE)
        .setFloat64(19, Double.MIN_VALUE, Double.MAX_VALUE)
        .setStruct(20, struct(inner).build().setInt8(0, -127))
        .setStruct(21, 0, struct(inner).build().setInt8(0, -127))
        .setStruct(21, 1, struct(inner).build().setInt8(0, -127));
    assertThat(struct.toString()).isEqualTo("{int8=-127, int8=[-127, -128], uint8=255, uint8=[0, 255], int16=-32768, int16=[-32768, 32767], uint16=65535, uint16=[0, 65535], int32=-2147483648, int32=[-2147483648, 2147483647], uint32=4294967295, uint32=[0, 4294967295], int64=-9223372036854775808, int64=[-9223372036854775808, 9223372036854775807], uint64=18446744073709551615, uint64=[0, 18446744073709551615], float32=1.4E-45, float32=[1.4E-45, 3.4028235E38], float64=4.9E-324, float64=[4.9E-324, 1.7976931348623157E308], Struct={int8=-127}, Struct=[{int8=-127}, {int8=-127}]}");
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
