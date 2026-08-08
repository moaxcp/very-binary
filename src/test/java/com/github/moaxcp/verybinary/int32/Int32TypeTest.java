package com.github.moaxcp.verybinary.int32;

import org.junit.jupiter.api.Test;

import static com.github.moaxcp.verybinary.BasicTypeInfo.INT32;
import static com.github.moaxcp.verybinary.Builders.struct;
import static com.github.moaxcp.verybinary.math.Int8Value.int8Value;
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
  void isFixedByteLength() {
    var struct = struct()
        .int32()
        .build();

    assertThat(struct.getType(0).isFixedByteLength()).isTrue();
    assertThat(struct.isFixedLength()).isTrue();
  }

  @Test
  void isFixedByteLengthArray() {
    var struct = struct()
        .int32List(int8Value(5))
        .build();

    assertThat(struct.getType(0).isFixedByteLength()).isTrue();
    assertThat(struct.isFixedLength()).isTrue();
  }
}
