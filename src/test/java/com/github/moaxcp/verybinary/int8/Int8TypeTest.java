package com.github.moaxcp.verybinary.int8;

import org.junit.jupiter.api.Test;

import static com.github.moaxcp.verybinary.Builders.struct;
import static com.github.moaxcp.verybinary.math.Constant.constant;
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
  void isFixedByteLength() {
    var struct = struct()
        .int8()
        .build();

    assertThat(struct.getType(0).isFixedByteLength()).isTrue();
    assertThat(struct.isFixedLength()).isTrue();
  }

  @Test
  void isFixedByteLengthArray() {
    var struct = struct()
        .int8List(constant(5))
        .build();

    assertThat(struct.getType(0).isFixedByteLength()).isTrue();
    assertThat(struct.isFixedLength()).isTrue();
  }
}
