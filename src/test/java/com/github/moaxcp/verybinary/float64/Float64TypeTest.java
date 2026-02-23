package com.github.moaxcp.verybinary.float64;

import org.junit.jupiter.api.Test;

import static com.github.moaxcp.verybinary.Builders.struct;
import static com.github.moaxcp.verybinary.Expression.constant;
import static com.github.moaxcp.verybinary.Primitive.FLOAT64;
import static org.assertj.core.api.Assertions.assertThat;

public class Float64TypeTest {

  @Test
  void getByteLength() {
    var struct = struct()
        .float64()
        .build();

    assertThat(struct.getByteLength()).isEqualTo(FLOAT64.size());
    assertThat(struct.getType(0).getByteLength(struct)).isEqualTo(FLOAT64.size());
  }

  @Test
  void isFixedLength() {
    var struct = struct()
        .float64()
        .build();

    assertThat(struct.getType(0).isFixedLength(struct)).isTrue();
    assertThat(struct.isFixedLength()).isTrue();
  }

  @Test
  void isFixedLengthArray() {
    var struct = struct()
        .float64Array(constant(3))
        .build();

    assertThat(struct.getType(0).isFixedLength(struct)).isTrue();
    assertThat(struct.isFixedLength()).isTrue();
  }
}
