package com.github.moaxcp.verybinary.uint64;

import com.github.moaxcp.verybinary.ByteArray;
import com.github.moaxcp.verybinary.ShiftBytes;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.util.ArrayList;

import static com.github.moaxcp.verybinary.ByteArray.ba;
import static com.github.moaxcp.verybinary.ShiftBytes.shiftBytes;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class ByteArrayUint64Test {

  @Test
  void uint64_overwrite() {
    var bytes = new ByteArray(new byte[] {5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5});
    for (int i = 0; i < 10; i++) {
      bytes.setUint64(i * 8, new BigInteger("18446744073709551615").subtract(BigInteger.valueOf(i)));
    }
    assertThat(bytes).isEqualTo(ba(new byte[] {
        -1, -1, -1, -1, -1, -1, -1, -1,
        -1, -1, -1, -1, -1, -1, -1, -2,
        -1, -1, -1, -1, -1, -1, -1, -3,
        -1, -1, -1, -1, -1, -1, -1, -4,
        -1, -1, -1, -1, -1, -1, -1, -5,
        -1, -1, -1, -1, -1, -1, -1, -6,
        -1, -1, -1, -1, -1, -1, -1, -7,
        -1, -1, -1, -1, -1, -1, -1, -8,
        -1, -1, -1, -1, -1, -1, -1, -9,
        -1, -1, -1, -1, -1, -1, -1, -10
    }));
    for (int i = 0; i < 10; i++) {
      assertThat(bytes.getUint64(i * 8)).isEqualTo(new BigInteger("18446744073709551615").subtract(BigInteger.valueOf(i)));
    }
  }

  @Test
  void addInt32() {
    var bytes = new ByteArray(new byte[] {100});
    var events = new ArrayList<ShiftBytes>();
    bytes.addListener(events::add);
    for (int i = 0; i < 10; i++) {
      bytes.addUint64(0, BigInteger.valueOf(i));
    }
    assertThat(bytes).isEqualTo(ba(new byte[] {
        0, 0, 0, 0, 0, 0, 0, 9,
        0, 0, 0, 0, 0, 0, 0, 8,
        0, 0, 0, 0, 0, 0, 0, 7,
        0, 0, 0, 0, 0, 0, 0, 6,
        0, 0, 0, 0, 0, 0, 0, 5,
        0, 0, 0, 0, 0, 0, 0, 4,
        0, 0, 0, 0, 0, 0, 0, 3,
        0, 0, 0, 0, 0, 0, 0, 2,
        0, 0, 0, 0, 0, 0, 0, 1,
        0, 0, 0, 0, 0, 0, 0, 0,
        100
    }));
    assertThat(events).containsExactly(shiftBytes(0, 8), shiftBytes(0, 8), shiftBytes(0, 8), shiftBytes(0, 8), shiftBytes(0, 8), shiftBytes(0, 8), shiftBytes(0, 8), shiftBytes(0, 8), shiftBytes(0, 8), shiftBytes(0, 8));
  }

  @Test
  void removeInt32Events() {
    var bytes = new ByteArray(new byte[] {
        0, 0, 0, 0, 0, 0, 0, 9,
        0, 0, 0, 0, 0, 0, 0, 8,
        0, 0, 0, 0, 0, 0, 0, 7,
        0, 0, 0, 0, 0, 0, 0, 6,
        0, 0, 0, 0, 0, 0, 0, 5,
        0, 0, 0, 0, 0, 0, 0, 4,
        0, 0, 0, 0, 0, 0, 0, 3,
        0, 0, 0, 0, 0, 0, 0, 2,
        0, 0, 0, 0, 0, 0, 0, 1,
        0, 0, 0, 0, 0, 0, 0, 0,
        100
    });
    var events = new ArrayList<ShiftBytes>();
    bytes.addListener(events::add);
    for (int i = 0; i < 10; i++) {
      bytes.removeUint64(0);
    }
    assertThat(bytes).isEqualTo(ba().int8(100));
    assertThat(events).containsExactly(shiftBytes(0, -8), shiftBytes(0, -8), shiftBytes(0, -8), shiftBytes(0, -8), shiftBytes(0, -8), shiftBytes(0, -8), shiftBytes(0, -8), shiftBytes(0, -8), shiftBytes(0, -8), shiftBytes(0, -8));
  }

  @Test
  void uint64_accepts_boundaries_and_rejects_out_of_range() {
    ByteArray arr = new ByteArray(new byte[16]);
    BigInteger max = BigInteger.ONE.shiftLeft(64).subtract(BigInteger.ONE);
    arr.setUint64(0, BigInteger.ZERO);
    arr.setUint64(8, max);
    assertThat(arr.getUint64(0)).isEqualTo(BigInteger.ZERO);
    assertThat(arr.getUint64(8)).isEqualTo(max);

    assertThatThrownBy(() -> arr.setUint64(0, BigInteger.valueOf(-1)))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("uint64 out of range: -1");
    assertThatThrownBy(() -> arr.setUint64(0, BigInteger.ONE.shiftLeft(64)))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("uint64 out of range: 18446744073709551616");
  }

  @Test
  void uint64_array_element_validation() {
    ByteArray arr = new ByteArray(new byte[16]);
    BigInteger tooLarge = BigInteger.ONE.shiftLeft(64);
    assertThatThrownBy(() -> arr.setUint64(0, new BigInteger[]{BigInteger.ZERO, BigInteger.valueOf(-1)}))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("uint64 out of range: -1");
    assertThatThrownBy(() -> arr.setUint64(0, new BigInteger[]{BigInteger.ZERO, tooLarge}))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("uint64 out of range: 18446744073709551616");
  }
}
