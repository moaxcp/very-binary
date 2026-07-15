package com.github.moaxcp.verybinary.int32;

import org.junit.jupiter.api.Test;

import static com.github.moaxcp.verybinary.Builders.struct;
import static com.github.moaxcp.verybinary.math.Expression.constant;
import static com.github.moaxcp.verybinary.BasicTypeInfo.INT32;
import static org.assertj.core.api.Assertions.assertThat;

public class Int32TypeTest {

  @Test
  void getByteLength() {
    var struct = struct()
        .int32()
        .build();

    assertThat(struct.getByteLength()).isEqualTo(INT32.size());
    assertThat(struct.getType(0).getByteLength(struct)).isEqualTo(INT32.size());
  }

  @Test
  void isFixedLength() {
    var struct = struct()
        .int32()
        .build();

    assertThat(struct.getType(0).isFixedLength()).isTrue();
    assertThat(struct.isFixedLength()).isTrue();
  }

  @Test
  void isFixedLengthArray() {
    var struct = struct()
        .int32Array(constant(5))
        .build();

    assertThat(struct.getType(0).isFixedLength()).isTrue();
    assertThat(struct.isFixedLength()).isTrue();
  }
}
