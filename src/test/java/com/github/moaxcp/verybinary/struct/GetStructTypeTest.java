package com.github.moaxcp.verybinary.struct;

import org.junit.jupiter.api.Test;

import static com.github.moaxcp.verybinary.Builders.struct;
import static com.github.moaxcp.verybinary.ByteArray.ba;
import static org.assertj.core.api.Assertions.assertThat;

public class GetStructTypeTest {
  @Test
  void get() {
    var struct = struct()
        .int8()
        .struct()
          .int8()
          .int8Array(0)
          .end()
        .fromBytes(ba().int8(100, 2, 3, 3))
        .build();

    assertThat(struct.getStruct(1))
        .isEqualTo(struct()
            .int8()
            .int8Array(0)
            .fromBytes(ba().int8(2, 3, 3))
            .build());
  }
}
