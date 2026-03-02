package com.github.moaxcp.verybinary.int64;

import org.junit.jupiter.api.Test;

import static com.github.moaxcp.verybinary.Builders.struct;
import static com.github.moaxcp.verybinary.Expression.constant;
import static com.github.moaxcp.verybinary.Primitive.INT64;
import static org.assertj.core.api.Assertions.assertThat;

public class Int64TypeTest {

  @Test
  void getByteLength() {
    var struct = struct()
        .int64()
        .build();

    assertThat(struct.getByteLength()).isEqualTo(INT64.size());
    assertThat(struct.getType(0).getByteLength(struct)).isEqualTo(INT64.size());
  }

  @Test
  void isFixedLength() {
    var struct = struct()
        .int64()
        .build();

    assertThat(struct.getType(0).isFixedLength(struct)).isTrue();
    assertThat(struct.isFixedLength()).isTrue();
  }

  @Test
  void isFixedLengthArray() {
    var struct = struct()
        .int64Array(constant(5))
        .build();

    assertThat(struct.getType(0).isFixedLength(struct)).isTrue();
    assertThat(struct.isFixedLength()).isTrue();
  }
}
