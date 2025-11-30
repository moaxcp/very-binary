package com.github.moaxcp.verybinary;

import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.util.Arrays;

import static com.github.moaxcp.verybinary.BigEndianSerializer.bigEndianSerializer;
import static com.github.moaxcp.verybinary.LittleEndianSerializer.littleEndianSerializer;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Cross-endian symmetry tests: verify that values written in one endianness,
 * when byte-reversed and read in the opposite endianness, round-trip to the
 * same numeric values. Includes unsigned bounds and NaN payload preservation.
 */
public class CrossEndianSerializerTest {
  private final BigEndianSerializer be = bigEndianSerializer();
  private final LittleEndianSerializer le = littleEndianSerializer();

  private static byte[] reverse(byte[] a) {
    byte[] b = Arrays.copyOf(a, a.length);
    for (int i = 0, j = b.length - 1; i < j; i++, j--) {
      byte t = b[i];
      b[i] = b[j];
      b[j] = t;
    }
    return b;
  }

  @Test
  void int16_cross_endian_symmetry() {
    short v = (short) 0xA1B2; // negative value to catch sign handling
    byte[] beBytes = new byte[2];
    be.writeInt16(beBytes, 0, v);
    assertThat(le.readInt16(reverse(beBytes), 0)).isEqualTo(v);

    byte[] leBytes = new byte[2];
    le.writeInt16(leBytes, 0, v);
    assertThat(be.readInt16(reverse(leBytes), 0)).isEqualTo(v);
  }

  @Test
  void int32_cross_endian_symmetry() {
    int v = 0x89ABCDEF; // sign bit set
    byte[] beBytes = new byte[4];
    be.writeInt32(beBytes, 0, v);
    assertThat(le.readInt32(reverse(beBytes), 0)).isEqualTo(v);

    byte[] leBytes = new byte[4];
    le.writeInt32(leBytes, 0, v);
    assertThat(be.readInt32(reverse(leBytes), 0)).isEqualTo(v);
  }

  @Test
  void int64_cross_endian_symmetry() {
    long v = 0x0123_4567_89ABL;
    v = (v << 32) | 0xCDEF_0123L; // 0x0123456789ABCDEF0123 pattern
    byte[] beBytes = new byte[8];
    be.writeInt64(beBytes, 0, v);
    assertThat(le.readInt64(reverse(beBytes), 0)).isEqualTo(v);

    byte[] leBytes = new byte[8];
    le.writeInt64(leBytes, 0, v);
    assertThat(be.readInt64(reverse(leBytes), 0)).isEqualTo(v);
  }

  @Test
  void uint16_cross_endian_bounds() {
    int[] vals = {0x0000, 0x00FF, 0xFFFF};
    for (int v : vals) {
      byte[] beBytes = new byte[2];
      be.writeUint16(beBytes, 0, v);
      assertThat(le.readUint16(reverse(beBytes), 0)).isEqualTo(v);

      byte[] leBytes = new byte[2];
      le.writeUint16(leBytes, 0, v);
      assertThat(be.readUint16(reverse(leBytes), 0)).isEqualTo(v);
    }
  }

  @Test
  void uint32_cross_endian_bounds() {
    long[] vals = {0x0000_0000L, 0x7FFF_FFFFL, 0xFFFF_FFFFL};
    for (long v : vals) {
      byte[] beBytes = new byte[4];
      be.writeUint32(beBytes, 0, v);
      assertThat(le.readUint32(reverse(beBytes), 0)).isEqualTo(v);

      byte[] leBytes = new byte[4];
      le.writeUint32(leBytes, 0, v);
      assertThat(be.readUint32(reverse(leBytes), 0)).isEqualTo(v);
    }
  }

  @Test
  void uint64_cross_endian_bounds() {
    BigInteger[] vals = {
        BigInteger.ZERO,
        new BigInteger("FFFFFFFFFFFFFFFF", 16)
    };
    for (BigInteger v : vals) {
      byte[] beBytes = new byte[8];
      be.writeUint64(beBytes, 0, v);
      assertThat(le.readUint64(reverse(beBytes), 0)).isEqualTo(v);

      byte[] leBytes = new byte[8];
      le.writeUint64(leBytes, 0, v);
      assertThat(be.readUint64(reverse(leBytes), 0)).isEqualTo(v);
    }
  }

  @Test
  void float32_nan_payload_preserved_cross_endian() {
    // Create a NaN with a specific payload (quiet NaN with payload bits)
    int nanBits = 0x7FC0_00A5; // qNaN with payload 0xA5
    float nan = Float.intBitsToFloat(nanBits);

    byte[] beBytes = new byte[4];
    be.writeFloat32(beBytes, 0, nan);
    float leRead = le.readFloat32(reverse(beBytes), 0);
    assertThat(Float.floatToRawIntBits(leRead)).isEqualTo(nanBits);

    byte[] leBytes = new byte[4];
    le.writeFloat32(leBytes, 0, nan);
    float beRead = be.readFloat32(reverse(leBytes), 0);
    assertThat(Float.floatToRawIntBits(beRead)).isEqualTo(nanBits);
  }

  @Test
  void float64_nan_payload_preserved_cross_endian() {
    long nanBits = 0x7FF8_0000_0000_00A5L; // qNaN with small payload
    double nan = Double.longBitsToDouble(nanBits);

    byte[] beBytes = new byte[8];
    be.writeFloat64(beBytes, 0, nan);
    double leRead = le.readFloat64(reverse(beBytes), 0);
    assertThat(Double.doubleToRawLongBits(leRead)).isEqualTo(nanBits);

    byte[] leBytes = new byte[8];
    le.writeFloat64(leBytes, 0, nan);
    double beRead = be.readFloat64(reverse(leBytes), 0);
    assertThat(Double.doubleToRawLongBits(beRead)).isEqualTo(nanBits);
  }

  @Test
  void float32_cross_endian_values() {
    int[] patterns = new int[] {
        0x0000_0000,                 // +0.0
        0x8000_0000,                 // -0.0
        0x3F80_0000,                 // 1.0
        0xC020_0000,                 // -2.5
        0x7F80_0000,                 // +Infinity
        0xFF80_0000,                 // -Infinity
        0x0080_0000,                 // MIN_NORMAL
        0x0000_0001,                 // smallest subnormal +
        0x8000_0001                  // smallest subnormal -
    };

    for (int bits : patterns) {
      float v = Float.intBitsToFloat(bits);

      byte[] beBytes = new byte[4];
      be.writeFloat32(beBytes, 0, v);
      float leRead = le.readFloat32(reverse(beBytes), 0);
      assertThat(Float.floatToRawIntBits(leRead)).isEqualTo(bits);

      byte[] leBytes = new byte[4];
      le.writeFloat32(leBytes, 0, v);
      float beRead = be.readFloat32(reverse(leBytes), 0);
      assertThat(Float.floatToRawIntBits(beRead)).isEqualTo(bits);
    }
  }

  @Test
  void float64_cross_endian_values() {
    long[] patterns = new long[] {
        0x0000_0000_0000_0000L,      // +0.0
        0x8000_0000_0000_0000L,      // -0.0
        0x3FF0_0000_0000_0000L,      // 1.0
        0xC004_0000_0000_0000L,      // -2.5
        0x7FF0_0000_0000_0000L,      // +Infinity
        0xFFF0_0000_0000_0000L,      // -Infinity
        0x0010_0000_0000_0000L,      // MIN_NORMAL
        0x0000_0000_0000_0001L,      // smallest subnormal +
        0x8000_0000_0000_0001L       // smallest subnormal -
    };

    for (long bits : patterns) {
      double v = Double.longBitsToDouble(bits);

      byte[] beBytes = new byte[8];
      be.writeFloat64(beBytes, 0, v);
      double leRead = le.readFloat64(reverse(beBytes), 0);
      assertThat(Double.doubleToRawLongBits(leRead)).isEqualTo(bits);

      byte[] leBytes = new byte[8];
      le.writeFloat64(leBytes, 0, v);
      double beRead = be.readFloat64(reverse(leBytes), 0);
      assertThat(Double.doubleToRawLongBits(beRead)).isEqualTo(bits);
    }
  }
}
