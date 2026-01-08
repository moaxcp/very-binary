package com.github.moaxcp.verybinary.int64;

import org.junit.jupiter.api.Test;

import static com.github.moaxcp.verybinary.Builders.struct;
import static com.github.moaxcp.verybinary.ByteArray.ba;
import static org.assertj.core.api.Assertions.assertThat;

public class AllocateInt64TypeTest {
  @Test
  void allocate() {
    var struct = struct()
        .int64()
        .build();

    assertThat(struct.getByteArray()).isEqualTo(ba().int64(0));
  }

  @Test
  void allocate_with_constant() {
    var struct = struct()
        .primitive().constant(5L).int64()
        .build();

    assertThat(struct.getByteArray()).isEqualTo(ba().int64(5));
  }

  @Test
  void allocate_empty_array() {
    var struct = struct()
        .int64()
        .int64Array(0)
        .build();

    assertThat(struct.getByteArray()).isEqualTo(ba().int64(0));
  }

  @Test
  void allocate_array_length_with_constant() {
    var struct = struct()
        .primitive().constant(5L).int64()
        .int64Array(0)
        .build();

    assertThat(struct.getByteArray()).isEqualTo(ba().int64(5, 0, 0, 0, 0, 0));
  }

  @Test
  void allocate_array_length_and_array_with_constant() {
    var struct = struct()
        .primitive().constant(5L).int64()
        .primitive().constant(6L).lengthField(0).int64()
        .build();

    assertThat(struct.getByteArray()).isEqualTo(ba().int64(5, 6, 6, 6, 6, 6));
  }
}
