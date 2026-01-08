package com.github.moaxcp.verybinary.int8;

import org.junit.jupiter.api.Test;

import static com.github.moaxcp.verybinary.Builders.struct;
import static com.github.moaxcp.verybinary.ByteArray.ba;
import static org.assertj.core.api.Assertions.assertThat;

public class AllocateInt8TypeTest {
  @Test
  void allocate() {
    var struct = struct()
        .int8()
        .build();

    assertThat(struct.getByteArray()).isEqualTo(ba().int8(0));
  }

  @Test
  void allocate_with_constant() {
    var struct = struct()
        .primitive().constant((byte) 5).int8()
        .build();

    assertThat(struct.getByteArray()).isEqualTo(ba().int8(5));
  }

  @Test
  void allocate_empty_array() {
    var struct = struct()
        .int8()
        .int8Array(0)
        .build();

    assertThat(struct.getByteArray()).isEqualTo(ba().int8(0));
  }

  @Test
  void allocate_array_length_with_constant() {
    var struct = struct()
        .primitive().constant((byte) 5).int8()
        .int8Array(0)
        .build();

    assertThat(struct.getByteArray()).isEqualTo(ba().int8(5, 0, 0, 0, 0, 0));
  }

  @Test
  void allocate_array_length_and_array_with_constant() {
    var struct = struct()
        .primitive().constant((byte) 5).int8()
        .primitive().constant((byte) 6).lengthField(0).int8()
        .build();

    assertThat(struct.getByteArray()).isEqualTo(ba().int8(5, 6, 6, 6, 6, 6));
  }
}
