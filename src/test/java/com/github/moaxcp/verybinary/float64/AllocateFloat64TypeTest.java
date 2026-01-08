package com.github.moaxcp.verybinary.float64;

import org.junit.jupiter.api.Test;

import static com.github.moaxcp.verybinary.Builders.struct;
import static com.github.moaxcp.verybinary.ByteArray.ba;
import static org.assertj.core.api.Assertions.assertThat;

public class AllocateFloat64TypeTest {
  @Test
  void allocate() {
    var struct = struct()
        .float64()
        .build();

    assertThat(struct.getByteArray()).isEqualTo(ba().float64(0));
  }

  @Test
  void allocate_with_constant() {
    var struct = struct()
        .primitive().constant(3.0d).float64()
        .build();

    assertThat(struct.getByteArray()).isEqualTo(ba().float64(3));
  }

  @Test
  void allocate_empty_array() {
    var struct = struct()
        .float64()
        .float64Array(0)
        .build();

    assertThat(struct.getByteArray()).isEqualTo(ba().float64(0));
  }

  @Test
  void allocate_array_length_with_constant() {
    var struct = struct()
        .primitive().constant(3.0d).float64()
        .float64Array(0)
        .build();

    // 3.0d then three zeros
    assertThat(struct.getByteArray()).isEqualTo(ba().float64(3, 0, 0, 0));
  }

  @Test
  void allocate_array_length_and_array_with_constant() {
    var struct = struct()
        .primitive().constant(3.0d).float64()
        .primitive().constant(2.0d).lengthField(0).float64()
        .build();

    // 3.0d then 3 elements of 2.0d
    assertThat(struct.getByteArray()).isEqualTo(ba().float64(3, 2, 2, 2));
  }
}
