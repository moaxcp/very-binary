package com.github.moaxcp.verybinary.float32;

import org.junit.jupiter.api.Test;

import static com.github.moaxcp.verybinary.Builders.struct;
import static com.github.moaxcp.verybinary.ByteArray.ba;
import static org.assertj.core.api.Assertions.assertThat;

public class AllocateFloat32TypeTest {
  @Test
  void allocate() {
    var struct = struct()
        .float32()
        .build();

    assertThat(struct.getByteArray()).isEqualTo(ba().float32(0));
  }

  @Test
  void allocate_with_constant() {
    var struct = struct()
        .primitive().constant(3.0f).float32()
        .build();

    // 3.0f -> 0x40400000 -> {64,64,0,0}
    assertThat(struct.getByteArray()).isEqualTo(ba().float32(3));
  }

  @Test
  void allocate_empty_array() {
    var struct = struct()
        .float32()
        .float32Array(0)
        .build();

    assertThat(struct.getByteArray()).isEqualTo(ba().float32(0));
  }

  @Test
  void allocate_array_length_with_constant() {
    var struct = struct()
        .primitive().constant(3.0f).float32()
        .float32Array(0)
        .build();

    assertThat(struct.getByteArray()).isEqualTo(ba().float32(3, 0, 0, 0));
  }

  @Test
  void allocate_array_length_and_array_with_constant() {
    var struct = struct()
        .primitive().constant(3.0f).float32()
        .primitive().constant(new float[]{2, 2, 2}).float32()
        .build();

    assertThat(struct.getByteArray()).isEqualTo(ba().float32(3, 2, 2, 2));
  }
}
