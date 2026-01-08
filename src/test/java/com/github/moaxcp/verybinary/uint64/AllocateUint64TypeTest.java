package com.github.moaxcp.verybinary.uint64;

import org.junit.jupiter.api.Test;

import java.math.BigInteger;

import static com.github.moaxcp.verybinary.Builders.struct;
import static com.github.moaxcp.verybinary.ByteArray.ba;
import static org.assertj.core.api.Assertions.assertThat;

public class AllocateUint64TypeTest {
  @Test
  void allocate() {
    var struct = struct()
        .uint64()
        .build();

    assertThat(struct.getByteArray()).isEqualTo(ba().uint64(0));
  }

  @Test
  void allocate_with_constant() {
    var struct = struct()
        .primitive().constant(BigInteger.valueOf(5)).uint64()
        .build();

    assertThat(struct.getByteArray()).isEqualTo(ba().uint64(5));
  }

  @Test
  void allocate_empty_array() {
    var struct = struct()
        .uint64()
        .uint64Array(0)
        .build();

    assertThat(struct.getByteArray()).isEqualTo(ba().uint64(0));
  }

  @Test
  void allocate_array_length_with_constant() {
    var struct = struct()
        .primitive().constant(BigInteger.valueOf(5)).uint64()
        .uint64Array(0)
        .build();

    assertThat(struct.getByteArray()).isEqualTo(ba().uint64(5, 0, 0, 0, 0, 0));
  }

  @Test
  void allocate_array_length_and_array_with_constant() {
    var struct = struct()
        .primitive().constant(BigInteger.valueOf(5)).uint64()
        .primitive().constant(BigInteger.valueOf(6)).lengthField(0).uint64()
        .build();

    assertThat(struct.getByteArray()).isEqualTo(ba().uint64(5, 6, 6, 6, 6, 6));
  }
}
