package com.github.moaxcp.verybinary.int8;

import com.github.moaxcp.verybinary.Struct;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.github.moaxcp.verybinary.Builders.struct;
import static com.github.moaxcp.verybinary.Expression.constant;
import static org.assertj.core.api.Assertions.assertThat;

public class Int8ListTypeTest {
  @Test
  void testInt8List() {
    Struct struct = struct()
        .int8Array(constant(3))
        .build();

    struct.setInt8(0, (byte) 1, (byte) 2, (byte) 3);

    com.github.moaxcp.verybinary.list.Int8List list = struct.get(0);
    assertThat(list).containsExactly((byte) 1, (byte) 2, (byte) 3);

    list.set(1, (byte) 10);
    assertThat(struct.getByteArray().getInt8(1)).isEqualTo((byte) 10);
    assertThat(list.get(1)).isEqualTo((byte) 10);
  }

  @Test
  void testAddInt8() {
    Struct struct = struct()
        .int8Array() // length 0
        .build();

    struct.addInt8(0, (byte) 5);
    struct.addInt8(0, (byte) 6);

    com.github.moaxcp.verybinary.list.Int8List list = struct.get(0);
    assertThat(list).containsExactly((byte) 5, (byte) 6);
  }

  @Test
  void testRawList() {
    Struct struct = struct()
        .int8Array(constant(2))
        .build();

    struct.setInt8(0, List.of((byte) 100, (byte) 101));

    List<Byte> raw = struct.getStandardList(0);
    assertThat(raw).containsExactly((byte) 100, (byte) 101);
  }
}
