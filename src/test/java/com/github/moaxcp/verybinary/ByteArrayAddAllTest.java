package com.github.moaxcp.verybinary;

import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;

public class ByteArrayAddAllTest {

  @Test
  void addAll_append_and_read_back() {
    ByteArray bytes = new ByteArray();
    List<Object> values = List.of(
      Boolean.TRUE,           // bool
      (byte) -5,              // int8
      (short) 255,            // uint8 (range-based dispatch)
      (short) 0x1234,         // int16
      0xABCD,                 // uint16 (range-based dispatch)
      0x89ABCDEF,             // int32
      0xFFFF_FFFFL,           // uint32 (range-based dispatch)
      0x0123456789ABCDEFL,    // int64
      new BigInteger("18446744073709551615"), // uint64 max
      3.14f,                  // float32
      Math.E                  // float64
    );

    bytes.addAll(values);

    long idx = 0;
    assertThat(bytes.getBool(idx)).isTrue();
    idx += BasicTypeInfo.BOOL.size();

    assertThat(bytes.getInt8(idx)).isEqualTo((byte) -5);
    idx += BasicTypeInfo.INT8.size();

    assertThat(bytes.getUint8(idx)).isEqualTo((short) 255);
    idx += BasicTypeInfo.UINT8.size();

    assertThat(bytes.getInt16(idx)).isEqualTo((short) 0x1234);
    idx += BasicTypeInfo.INT16.size();

    assertThat(bytes.getUint16(idx)).isEqualTo(0xABCD);
    idx += BasicTypeInfo.UINT16.size();

    assertThat(bytes.getInt32(idx)).isEqualTo(0x89ABCDEF);
    idx += BasicTypeInfo.INT32.size();

    assertThat(bytes.getUint32(idx)).isEqualTo(0xFFFF_FFFFL);
    idx += BasicTypeInfo.UINT32.size();

    assertThat(bytes.getInt64(idx)).isEqualTo(0x0123456789ABCDEFL);
    idx += BasicTypeInfo.INT64.size();

    assertThat(bytes.getUint64(idx)).isEqualTo(new BigInteger("18446744073709551615"));
    idx += BasicTypeInfo.UINT64.size();

    assertThat(bytes.getFloat32(idx)).isCloseTo(3.14f, within(1e-6f));
    idx += BasicTypeInfo.FLOAT32.size();

    assertThat(bytes.getFloat64(idx)).isCloseTo(Math.E, within(1e-12));
  }

  @Test
  void factory_byteArray_builds_from_list() {
    List<Object> values = List.of(
      true,
      (byte) 7,
      (short) 255,          // uint8
      (short) -2,           // int16 (not uint8)
      0x1234,               // uint16
      0x7FFF_FFFE,          // int32
      0xFFFF_FFFFL,         // uint32
      1_234_567_890_123_456_789L, // int64
      new BigInteger("42"),
      1.5f,
      2.5
    );

    ByteArray bytes = ByteArray.byteArray(values);

    long idx = 0;
    assertThat(bytes.getBool(idx)).isTrue();
    idx += BasicTypeInfo.BOOL.size();

    assertThat(bytes.getInt8(idx)).isEqualTo((byte) 7);
    idx += BasicTypeInfo.INT8.size();

    assertThat(bytes.getUint8(idx)).isEqualTo((short) 255);
    idx += BasicTypeInfo.UINT8.size();

    assertThat(bytes.getInt16(idx)).isEqualTo((short) -2);
    idx += BasicTypeInfo.INT16.size();

    assertThat(bytes.getUint16(idx)).isEqualTo(0x1234);
    idx += BasicTypeInfo.UINT16.size();

    assertThat(bytes.getInt32(idx)).isEqualTo(0x7FFF_FFFE);
    idx += BasicTypeInfo.INT32.size();

    assertThat(bytes.getUint32(idx)).isEqualTo(0xFFFF_FFFFL);
    idx += BasicTypeInfo.UINT32.size();

    assertThat(bytes.getInt64(idx)).isEqualTo(1_234_567_890_123_456_789L);
    idx += BasicTypeInfo.INT64.size();

    assertThat(bytes.getUint64(idx)).isEqualTo(new BigInteger("42"));
    idx += BasicTypeInfo.UINT64.size();

    assertThat(bytes.getFloat32(idx)).isCloseTo(1.5f, within(1e-6f));
    idx += BasicTypeInfo.FLOAT32.size();

    assertThat(bytes.getFloat64(idx)).isCloseTo(2.5, within(1e-12));
  }
}
