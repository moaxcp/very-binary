package com.github.moaxcp.verybinary.uint8;

import org.junit.jupiter.api.Test;

import static com.github.moaxcp.verybinary.Builders.struct;
import static com.github.moaxcp.verybinary.ByteArray.ba;
import static org.assertj.core.api.Assertions.assertThat;

public class AllocateUint8TypeTest {

  @Test
  void allocate() {
    var struct = struct()
        .uint8()
        .build();

    assertThat(struct.getByteArray()).isEqualTo(ba().uint8(0));
  }

  @Test
  void allocate_with_constant() {
    var struct = struct()
        .primitive().constant((short) 5).uint8()
        .build();

    assertThat(struct.getByteArray()).isEqualTo(ba().uint8(5));
  }

  @Test
  void allocate_empty_array() {
    var struct = struct()
        .uint8()
        .uint8Array(0)
        .build();

    assertThat(struct.getByteArray()).isEqualTo(ba().uint8(0));
  }

  @Test
  void allocate_array_length_with_constant() {
    var struct = struct()
        .primitive().constant((short) 5).uint8()
        .uint8Array(0)
        .build();

    assertThat(struct.getByteArray()).isEqualTo(ba().uint8(5, 0, 0, 0, 0, 0));
  }
}
