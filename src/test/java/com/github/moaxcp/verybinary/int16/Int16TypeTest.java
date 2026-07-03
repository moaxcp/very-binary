package com.github.moaxcp.verybinary.int16;

import org.junit.jupiter.api.Test;

import static com.github.moaxcp.verybinary.Builders.struct;
import static com.github.moaxcp.verybinary.Expression.constant;
import static com.github.moaxcp.verybinary.BasicTypeInfo.INT16;
import static org.assertj.core.api.Assertions.assertThat;

public class Int16TypeTest {

  @Test
  void getByteLength() {
    var struct = struct()
        .int16()
        .build();

    assertThat(struct.getType(0).getByteLength(struct)).isEqualTo(INT16.size());
  }

  @Test
  void isFixedLength() {
    var struct = struct()
        .int16()
        .build();

    assertThat(struct.getType(0).isFixedLength(struct)).isTrue();
    assertThat(struct.isFixedLength()).isTrue();
  }

  @Test
  void isFixedLengthArray() {
    var struct = struct()
        .int16Array(constant(5))
        .build();

    assertThat(struct.getType(0).isFixedLength(struct)).isTrue();
    assertThat(struct.isFixedLength()).isTrue();
  }
}
