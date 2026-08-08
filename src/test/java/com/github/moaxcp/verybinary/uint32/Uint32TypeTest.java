package com.github.moaxcp.verybinary.uint32;

import org.junit.jupiter.api.Test;

import static com.github.moaxcp.verybinary.BasicTypeInfo.UINT32;
import static com.github.moaxcp.verybinary.Builders.struct;
import static com.github.moaxcp.verybinary.math.Int8Value.int8Value;
import static org.assertj.core.api.Assertions.assertThat;

public class Uint32TypeTest {

  @Test
  void getByteLength() {
    var struct = struct()
        .uint32()
        .build();

    assertThat(struct.getByteLength()).isEqualTo(UINT32.size());
    assertThat(struct.getType(0).getByteLength(struct)).isEqualTo(UINT32.size());
  }

  @Test
  void isFixedByteLength() {
    var struct = struct()
        .uint32()
        .build();

    assertThat(struct.getType(0).isFixedByteLength()).isTrue();
    assertThat(struct.isFixedLength()).isTrue();
  }

  @Test
  void isFixedByteLengthArray() {
    var struct = struct()
        .uint32List(int8Value(5))
        .build();

    assertThat(struct.getType(0).isFixedByteLength()).isTrue();
    assertThat(struct.isFixedLength()).isTrue();
  }
}
