package com.github.moaxcp.verybinary.uint8;

import org.junit.jupiter.api.Test;

import static com.github.moaxcp.verybinary.BasicTypeInfo.UINT8;
import static com.github.moaxcp.verybinary.Builders.struct;
import static com.github.moaxcp.verybinary.math.Int8Value.int8Value;
import static org.assertj.core.api.Assertions.assertThat;

public class Uint8TypeTest {

  @Test
  void getByteLength() {
    var struct = struct()
        .uint8()
        .build();

    assertThat(struct.getByteLength()).isEqualTo(UINT8.size());
    assertThat(struct.getType(0).getByteLength(struct)).isEqualTo(UINT8.size());
  }

  @Test
  void isFixedByteLength() {
    var struct = struct()
        .uint8()
        .build();

    assertThat(struct.getType(0).isFixedByteLength()).isTrue();
    assertThat(struct.isFixedLength()).isTrue();
  }

  @Test
  void isFixedByteLengthArray() {
    var struct = struct()
        .uint8List(int8Value(5))
        .build();

    assertThat(struct.getType(0).isFixedByteLength()).isTrue();
    assertThat(struct.isFixedLength()).isTrue();
  }
}
