package com.github.moaxcp.verybinary;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static com.github.moaxcp.verybinary.ByteArray.ba;
import static com.github.moaxcp.verybinary.ShiftBytes.shiftBytes;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class ByteArrayUint8Test {

  @Test
  void uint8_overwrite() {
    var bytes = new ByteArray(new byte[] {5, 5, 5, 5, 5, 5, 5, 5, 5, 5});
    for (int i = 0; i < 10; i++) {
      bytes.setUint8(i, (byte) (255 - i));
    }
    assertThat(bytes.getBytes()).isEqualTo(new byte[] {-1, -2, -3, -4, -5, -6, -7, -8, -9, -10});
    for (int i = 0; i < 10; i++) {
      assertThat(bytes.getUint8(i)).isEqualTo((short) (255 - i));
    }
  }

  @Test
  void addUint8() {
    var bytes = new ByteArray(new byte[] {100});
    var events = new ArrayList<ShiftBytes>();
    bytes.addListener(events::add);
    for (int i = 0; i < 10; i++) {
      bytes.addUint8(0, (short) i);
    }
    assertThat(bytes.getBytes()).isEqualTo(new byte[] {9, 8, 7, 6, 5, 4, 3, 2, 1, 0, 100});
    assertThat(events).containsExactly(shiftBytes(0, 1), shiftBytes(0, 1), shiftBytes(0, 1), shiftBytes(0, 1), shiftBytes(0, 1), shiftBytes(0, 1), shiftBytes(0, 1), shiftBytes(0, 1), shiftBytes(0, 1), shiftBytes(0, 1));
  }

  @Test
  void removeUint8Events() {
    var bytes = new ByteArray(new byte[] {9, 8, 7, 6, 5, 4, 3, 2, 1, 0, 100});
    var events = new ArrayList<ShiftBytes>();
    bytes.addListener(events::add);
    for (int i = 0; i < 10; i++) {
      bytes.removeUint8(0);
    }
    assertThat(bytes).isEqualTo(ba().int8(100));
    assertThat(events).containsExactly(shiftBytes(0, -1), shiftBytes(0, -1), shiftBytes(0, -1), shiftBytes(0, -1), shiftBytes(0, -1), shiftBytes(0, -1), shiftBytes(0, -1), shiftBytes(0, -1), shiftBytes(0, -1), shiftBytes(0, -1));
  }

  @Test
  void uint8_accepts_boundaries_and_signed_byte_range() {
    ByteArray arr = new ByteArray(new byte[3]);
    arr.setUint8(0, (short) 0);
    arr.setUint8(1, (short) 127);
    arr.setUint8(2, (short) 255);

    assertThat(arr.getUint8(0)).isEqualTo((short) 0);
    assertThat(arr.getUint8(1)).isEqualTo((short) 127);
    assertThat(arr.getUint8(2)).isEqualTo((short) 255);
  }

  @Test
  void uint8_rejects_below_minus128_and_above_255() {
    ByteArray arr = new ByteArray(new byte[2]);
    assertThatThrownBy(() -> arr.setUint8(0, (short) -1))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("uint8 out of range: -1");
    assertThatThrownBy(() -> arr.setUint8(0, (short) 256))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("uint8 out of range: 256");
  }

  @Test
  void uint8_array_element_validation() {
    ByteArray arr = new ByteArray(new byte[2]);
    assertThatThrownBy(() -> arr.setUint8(0, new short[]{0, (short) 256}))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("uint8 out of range: 256");
    assertThatThrownBy(() -> arr.setUint8(0, new short[]{0, (short) -1}))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("uint8 out of range: -1");
  }
}
