package com.github.moaxcp.verybinary;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static com.github.moaxcp.verybinary.ByteArray.ba;
import static com.github.moaxcp.verybinary.ShiftBytes.shiftBytes;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class ByteArrayUInt16Test {

  @Test
  void uint16_overwrite() {
    var bytes = new ByteArray(new byte[] {5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5});
    for (int i = 0; i < 10; i++) {
      bytes.setUint16(i * 2, 65535 - i);
    }
    assertThat(bytes.getBytes()).isEqualTo(new byte[] {-1, -1, -1, -2, -1, -3, -1, -4, -1, -5, -1, -6, -1, -7, -1, -8, -1, -9, -1, -10});
    for (int i = 0; i < 10; i++) {
      assertThat(bytes.getUint16(i * 2)).isEqualTo(65535 - i);
    }
  }

  @Test
  void addUint16() {
    var bytes = new ByteArray(new byte[] {100});
    var events = new ArrayList<ShiftBytes>();
    bytes.addListener(events::add);
    for (int i = 0; i < 10; i++) {
      bytes.addUint16(0, i);
    }
    assertThat(bytes.getBytes()).isEqualTo(new byte[] {0, 9, 0, 8, 0, 7, 0, 6, 0, 5, 0, 4, 0, 3, 0, 2, 0, 1, 0, 0, 100});
    assertThat(events).containsExactly(shiftBytes(0, 2), shiftBytes(0, 2), shiftBytes(0, 2), shiftBytes(0, 2), shiftBytes(0, 2), shiftBytes(0, 2), shiftBytes(0, 2), shiftBytes(0, 2), shiftBytes(0, 2), shiftBytes(0, 2));
  }

  @Test
  void removeUint16Events() {
    var bytes = new ByteArray(new byte[] {0, 9, 0, 8, 0, 7, 0, 6, 0, 5, 0, 4, 0, 3, 0, 2, 0, 1, 0, 0, 100});
    var events = new ArrayList<ShiftBytes>();
    bytes.addListener(events::add);
    for (int i = 0; i < 10; i++) {
      bytes.removeUint16(0);
    }
    assertThat(bytes).isEqualTo(ba().int8(100));
    assertThat(events).containsExactly(shiftBytes(0, -2), shiftBytes(0, -2), shiftBytes(0, -2), shiftBytes(0, -2), shiftBytes(0, -2), shiftBytes(0, -2), shiftBytes(0, -2), shiftBytes(0, -2), shiftBytes(0, -2), shiftBytes(0, -2));
  }

  @Test
  void uint16_accepts_boundaries_and_rejects_out_of_range() {
    ByteArray arr = new ByteArray(new byte[4]);
    arr.setUint16(0, 0);
    arr.setUint16(2, 0xFFFF);
    assertThat(arr.getUint16(0)).isEqualTo(0);
    assertThat(arr.getUint16(2)).isEqualTo(0xFFFF);

    assertThatThrownBy(() -> arr.setUint16(0, -1))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("uint16 out of range: -1");
    assertThatThrownBy(() -> arr.setUint16(0, 0x1_0000))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("uint16 out of range: 65536");
  }

  @Test
  void uint16_array_element_validation() {
    ByteArray arr = new ByteArray(new byte[4]);
    assertThatThrownBy(() -> arr.setUint16(0, new int[]{1, -1}))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("uint16 out of range: -1");
    assertThatThrownBy(() -> arr.setUint16(0, new int[]{1, 0x1_0000}))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("uint16 out of range: 65536");
  }
}
