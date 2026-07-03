package com.github.moaxcp.verybinary.int8;

import org.junit.jupiter.api.Test;

import static com.github.moaxcp.verybinary.Builders.struct;
import static com.github.moaxcp.verybinary.Expression.constant;
import static com.github.moaxcp.verybinary.BasicTypeInfo.INT8;
import static org.assertj.core.api.Assertions.assertThat;

public class Int8TypeTest {

  @Test
  void getByteLength() {
    var struct = struct()
        .int8()
        .build();

    assertThat(struct.getByteLength()).isEqualTo(INT8.size());
    assertThat(struct.getType(0).getByteLength(struct)).isEqualTo(INT8.size());
  }

  @Test
  void isFixedLength() {
    var struct = struct()
        .int8()
        .build();

    assertThat(struct.getType(0).isFixedLength(struct)).isTrue();
    assertThat(struct.isFixedLength()).isTrue();
  }

  @Test
  void isFixedLengthArray() {
    var struct = struct()
        .int8Array(constant(5))
        .build();

    assertThat(struct.getType(0).isFixedLength(struct)).isTrue();
    assertThat(struct.isFixedLength()).isTrue();
  }
}
