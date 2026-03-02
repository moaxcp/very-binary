package com.github.moaxcp.verybinary.bool;

import org.junit.jupiter.api.Test;

import static com.github.moaxcp.verybinary.Builders.struct;
import static com.github.moaxcp.verybinary.ByteArray.ba;
import static org.assertj.core.api.Assertions.assertThat;

public class AllocateBoolTypeTest {

  @Test
  void allocate() {
    var struct = struct()
        .bool()
        .build();

    assertThat(struct.getByteArray()).isEqualTo(ba().bool(false));
  }

  @Test
  void allocate_with_constant_true() {
    var struct = struct()
        .primitive().constant(true).bool()
        .build();

    assertThat(struct.getByteArray()).isEqualTo(ba().bool(true));
  }

  @Test
  void allocate_with_constant_false() {
    var struct = struct()
        .primitive().constant(false).bool()
        .build();

    assertThat(struct.getByteArray()).isEqualTo(ba().bool(false));
  }

  @Test
  void allocate_empty_array() {
    var struct = struct()
        .int8()
        .boolArray(0)
        .build();

    assertThat(struct.getByteArray()).isEqualTo(ba().int8(0));
  }

  @Test
  void allocate_array_length_with_constant() {
    var struct = struct()
        .primitive().constant(5).int8()
        .boolArray(0)
        .build();

    assertThat(struct.getByteArray()).isEqualTo(ba().int8(5).bool(false, false, false, false, false));
  }

  @Test
  void allocate_array_length_field_constant() {
    var struct = struct()
        .primitive().constant(5).int8()
        .boolArray(0)
        .build();

    assertThat(struct.getByteArray()).isEqualTo(ba().int8(5).bool(false, false, false, false, false));
  }

  @Test
  void allocate_byte_length_field_constant() {
    var struct = struct()
        .primitive().constant(5).int8()
        .primitive().byteLengthField(0).bool()
        .build();

    assertThat(struct.getByteArray()).isEqualTo(ba().int8(5).bool(false, false, false, false, false));
  }

  @Test
  void allocate_array_constant() {
    var struct = struct()
        .primitive().constant(new boolean[]{true, true, true, true, true}).bool()
        .build();

    assertThat(struct.getByteArray()).isEqualTo(ba().bool(true, true, true, true, true));
  }
}
