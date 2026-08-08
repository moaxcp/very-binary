package com.github.moaxcp.verybinary.int16;

import org.junit.jupiter.api.Test;

import static com.github.moaxcp.verybinary.BasicTypeInfo.INT16;
import static com.github.moaxcp.verybinary.Builders.struct;
import static com.github.moaxcp.verybinary.math.Int8Value.int8Value;
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
  void isFixedByteLength() {
    var struct = struct()
        .int16()
        .build();

    assertThat(struct.getType(0).isFixedByteLength()).isTrue();
    assertThat(struct.isFixedLength()).isTrue();
  }

  @Test
  void isFixedByteLengthArray() {
    var struct = struct()
        .int16List(int8Value(5))
        .build();

    assertThat(struct.getType(0).isFixedByteLength()).isTrue();
    assertThat(struct.isFixedLength()).isTrue();
  }
}
