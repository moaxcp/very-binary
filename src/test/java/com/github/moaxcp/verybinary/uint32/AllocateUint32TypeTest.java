package com.github.moaxcp.verybinary.uint32;

import org.junit.jupiter.api.Test;

import static com.github.moaxcp.verybinary.Builders.struct;
import static com.github.moaxcp.verybinary.ByteArray.ba;
import static org.assertj.core.api.Assertions.assertThat;

public class AllocateUint32TypeTest {

  @Test
  void allocate() {
    var struct = struct()
        .uint32()
        .build();

    assertThat(struct.getByteArray()).isEqualTo(ba().uint32(0));
  }

  @Test
  void allocate_with_constant() {
    var struct = struct()
        .primitive().constant(5L).uint32()
        .build();

    assertThat(struct.getByteArray()).isEqualTo(ba().uint32(5));
  }

  @Test
  void allocate_empty_array() {
    var struct = struct()
        .uint32()
        .uint32Array(0)
        .build();

    assertThat(struct.getByteArray()).isEqualTo(ba().uint32(0));
  }

  @Test
  void allocate_array_length_with_constant() {
    var struct = struct()
        .primitive().constant(5L).uint32()
        .uint32Array(0)
        .build();

    assertThat(struct.getByteArray()).isEqualTo(ba().uint32(5, 0, 0, 0, 0, 0));
  }
}
