package com.github.moaxcp.verybinary.uint16;

import org.junit.jupiter.api.Test;

import static com.github.moaxcp.verybinary.Builders.struct;
import static com.github.moaxcp.verybinary.math.Expression.constant;
import static com.github.moaxcp.verybinary.BasicTypeInfo.UINT16;
import static org.assertj.core.api.Assertions.assertThat;

public class Uint16TypeTest {

  @Test
  void getByteLength() {
    var struct = struct()
        .uint16()
        .build();

    assertThat(struct.getByteLength()).isEqualTo(UINT16.size());
    assertThat(struct.getType(0).getByteLength(struct)).isEqualTo(UINT16.size());
  }

  @Test
  void isFixedLength() {
    var struct = struct()
        .uint16()
        .build();

    assertThat(struct.getType(0).isFixedLength()).isTrue();
    assertThat(struct.isFixedLength()).isTrue();
  }

  @Test
  void isFixedLengthArray() {
    var struct = struct()
        .primitive().lengthExpression(constant(5)).uint16()
        .uint16Array(constant(5))
        .build();

    assertThat(struct.getType(0).isFixedLength()).isTrue();
    assertThat(struct.isFixedLength()).isTrue();
  }
}
