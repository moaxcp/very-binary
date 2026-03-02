package com.github.moaxcp.verybinary.int16;

import org.junit.jupiter.api.Test;

import static com.github.moaxcp.verybinary.Builders.struct;
import static com.github.moaxcp.verybinary.ByteArray.ba;
import static org.assertj.core.api.Assertions.assertThat;

public class AllocateInt16TypeTest {
  @Test
  void allocate() {
    var struct = struct()
        .int16()
        .build();

    assertThat(struct.getByteArray()).isEqualTo(ba().int16(0));
  }

  @Test
  void allocate_with_constant() {
    var struct = struct()
        .primitive().constant((short) 5).int16()
        .build();

    assertThat(struct.getByteArray()).isEqualTo(ba().int16(5));
  }

  @Test
  void allocate_empty_array() {
    var struct = struct()
        .int16()
        .int16Array(0)
        .build();

    assertThat(struct.getByteArray()).isEqualTo(ba().int16(0));
  }

  @Test
  void allocate_array_length_with_constant() {
    var struct = struct()
        .primitive().constant((short) 5).int16()
        .int16Array(0)
        .build();

    assertThat(struct.getByteArray()).isEqualTo(ba().int16(5, 0, 0, 0, 0, 0));
  }

  @Test
  void allocate_array_length_and_array_with_constant() {
    var struct = struct()
        .primitive().constant((short) 5).int16()
        .primitive().constant(new short[]{6, 6, 6, 6, 6}).int16()
        .build();

    assertThat(struct.getByteArray()).isEqualTo(ba().int16(5, 6, 6, 6, 6, 6));
  }
}
