package com.github.moaxcp.verybinary.uint8;

import org.junit.jupiter.api.Test;

import static com.github.moaxcp.verybinary.Builders.struct;
import static com.github.moaxcp.verybinary.Expression.constant;
import static com.github.moaxcp.verybinary.Primitive.UINT8;
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
  void isFixedLength() {
    var struct = struct()
        .uint8()
        .build();

    assertThat(struct.getType(0).isFixedLength(struct)).isTrue();
    assertThat(struct.isFixedLength()).isTrue();
  }

  @Test
  void isFixedLengthArray() {
    var struct = struct()
        .uint8Array(constant(5))
        .build();

    assertThat(struct.getType(0).isFixedLength(struct)).isTrue();
    assertThat(struct.isFixedLength()).isTrue();
  }
}
