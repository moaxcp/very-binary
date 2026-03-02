package com.github.moaxcp.verybinary.int32;

import org.junit.jupiter.api.Test;

import static com.github.moaxcp.verybinary.Builders.struct;
import static com.github.moaxcp.verybinary.ByteArray.ba;
import static org.assertj.core.api.Assertions.assertThat;

public class AllocateInt32TypeTest {
  @Test
  void allocate() {
    var struct = struct()
        .int32()
        .build();

    assertThat(struct.getByteArray()).isEqualTo(ba().int32(0));
  }

  @Test
  void allocate_with_constant() {
    var struct = struct()
        .primitive().constant(5).int32()
        .build();

    assertThat(struct.getByteArray()).isEqualTo(ba().int32(5));
  }

  @Test
  void allocate_empty_array() {
    var struct = struct()
        .int32()
        .int32Array(0)
        .build();

    assertThat(struct.getByteArray()).isEqualTo(ba().int32(0));
  }

  @Test
  void allocate_array_length_with_constant() {
    var struct = struct()
        .primitive().constant(5).int32()
        .int32Array(0)
        .build();

    assertThat(struct.getByteArray()).isEqualTo(ba().int32(5, 0, 0, 0, 0, 0));
  }

  @Test
  void allocate_array_length_and_array_with_constant() {
    var struct = struct()
        .primitive().constant(5).int32()
        .primitive().constant(new int[]{6, 6, 6, 6, 6}).int32()
        .build();

    assertThat(struct.getByteArray()).isEqualTo(ba().int32(5, 6, 6, 6, 6, 6));
  }
}
