package com.github.moaxcp.verybinary.uint16;

import org.junit.jupiter.api.Test;

import static com.github.moaxcp.verybinary.Builders.struct;
import static com.github.moaxcp.verybinary.ByteArray.ba;
import static org.assertj.core.api.Assertions.assertThat;

public class AllocateUint16TypeTest {
  @Test
  void allocate() {
    var struct = struct()
        .uint16()
        .build();

    assertThat(struct.getByteArray()).isEqualTo(ba().uint16(0));
  }

  @Test
  void allocate_with_constant() {
    var struct = struct()
        .primitive().constant(5).uint16()
        .build();

    assertThat(struct.getByteArray()).isEqualTo(ba().uint16(5));
  }

  @Test
  void allocate_empty_array() {
    var struct = struct()
        .uint16()
        .uint16Array(0)
        .build();

    assertThat(struct.getByteArray()).isEqualTo(ba().uint16(0));
  }

  @Test
  void allocate_array_length_with_constant() {
    var struct = struct()
        .primitive().constant(5).uint16()
        .uint16Array(0)
        .build();

    assertThat(struct.getByteArray()).isEqualTo(ba().uint16(5, 0, 0, 0, 0, 0));
  }
}
