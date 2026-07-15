package com.github.moaxcp.verybinary.struct;

import org.junit.jupiter.api.Test;

import static com.github.moaxcp.verybinary.Builders.struct;
import static com.github.moaxcp.verybinary.Builders.structType;
import static com.github.moaxcp.verybinary.ByteArray.ba;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class GetStructTypeTest {
  @Test
  void get() {
    var struct = struct()
        .int8()
        .struct()
          .int8()
          .int8Array(0)
          .end()
        .fromBytes(ba().int8(100, 2, 3, 3))
        .build();

    assertThat(struct.getStruct(1))
        .isEqualTo(struct()
            .int8()
            .int8Array(0)
            .fromBytes(ba().int8(2, 3, 3))
            .build());
  }

  @Test
  void get_position_negative() {
    var struct = struct()
        .int8()
        .struct()
          .int8()
          .int8Array(0)
          .end()
        .fromBytes(ba().int8(100, 2, 3, 3))
        .build();

    assertThatThrownBy(() -> struct.getStruct(-1))
        .isInstanceOf(IndexOutOfBoundsException.class)
        .hasMessage("Index -1 out of bounds for length 2");
  }

  @Test
  void get_position_greater_than_length() {
    var struct = struct()
        .int8()
        .struct()
        .int8()
        .int8Array(0)
        .end()
        .fromBytes(ba().int8(100, 2, 3, 3))
        .build();

    assertThatThrownBy(() -> struct.getStruct(2))
        .isInstanceOf(IndexOutOfBoundsException.class)
        .hasMessage("Index 2 out of bounds for length 2");
  }

  @Test
  void get_allocated() {
    var inner = structType()
        .int8()
        .int8Array(0)
        .build();

    var struct = struct()
        .int8()
        .struct(inner)
        .build();

    assertThat(struct.getStruct(1))
        .isEqualTo(struct(inner).build());
  }

  @Test
  void get_not_allocated() {
    var inner = structType()
        .int8()
        .int8Array(0)
        .build();

    var struct = struct()
        .allocated()
        .int8()
        .struct(inner)
        .build();

    assertThatThrownBy(() -> struct.getStruct(1))
        .isInstanceOf(IndexOutOfBoundsException.class)
        .hasMessage("allocated: 0, index: 1, length: 0");
  }

  @Test
  void get_index() {
    var inner = structType()
        .primitive().int16()
        .primitive().lengthField(0).bool()
        .build();

    var struct = struct()
        .int8()
        .structArray(0, inner)
        .fromBytes(ba()
            .int8(2)
            .int16(3).bool(true, false, true)
            .int16(4).bool(true, false, true, false))
        .build();

    assertThat(struct.getStruct(1, 0))
        .isEqualTo(struct(inner).fromBytes(ba().int16(3).bool(true, false, true)).build());
    assertThat(struct.getStruct(1, 1))
        .isEqualTo(struct(inner).fromBytes(ba().int16(4).bool(true, false, true, false)).build());
  }
}
