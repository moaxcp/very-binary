package com.github.moaxcp.verybinary.uint16;

import com.github.moaxcp.verybinary.Uint16Type;
import org.junit.jupiter.api.Test;

import static com.github.moaxcp.verybinary.Builders.struct;
import static com.github.moaxcp.verybinary.ByteArray.ba;
import static com.github.moaxcp.verybinary.Expression.constant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class GetUint16TypeTest {
  @Test
  void getWrapper() {
    var struct = struct()
        .uint16()
        .build();

    assertThatThrownBy(() -> ((Uint16Type) struct.getType(0)).get(struct))
        .isInstanceOf(UnsupportedOperationException.class)
        .hasMessage("get(Pointer) not supported for Uint16Type. Use getUint16(Pointer) instead.");
  }

  @Test
  void getUint16() {
    var struct = struct()
        .uint16()
        .build();

    struct.setUint16(0, 2);

    assertThat(struct.getUint16(0)).isEqualTo(2);
  }

  @Test
  void getUint16_position_negative() {
    var struct = struct()
        .uint16()
        .build();

    assertThatThrownBy(() -> struct.getUint16(-1))
        .isInstanceOf(IndexOutOfBoundsException.class)
        .hasMessage("Index -1 out of bounds for length 1");
  }

  @Test
  void getUint16_position_greater_than_length() {
    var struct = struct()
        .uint16()
        .build();

    assertThatThrownBy(() -> struct.getUint16(2))
        .isInstanceOf(IndexOutOfBoundsException.class)
        .hasMessage("Index 2 out of bounds for length 1");
  }

  @Test
  void getUint16Allocated() {
    var struct = struct()
        .uint16()
        .build();

    assertThat(struct.getUint16(0)).isEqualTo(0);
    assertThat(struct.getByteArray()).isEqualTo(ba().uint16(0));
  }

  @Test
  void getUint16NotAllocated() {
    var struct = struct()
        .allocated()
        .uint16()
        .build();

    assertThatThrownBy(() -> struct.getUint16(0))
        .isInstanceOf(IndexOutOfBoundsException.class)
        .hasMessage("allocated: 0, index: 0, length: 2");
  }

  @Test
  void getUint16_constant() {
    var struct = struct()
        .primitive().constant(5).uint16()
        .build();

    assertThat(struct.getUint16(0)).isEqualTo(5);
  }

  @Test
  void getUint16_index_0_not_array() {
    var struct = struct()
        .uint16()
        .fromBytes(ba().uint16(1))
        .build();

    assertThat(struct.getUint16(0, 0)).isEqualTo(1);
  }

  @Test
  void getUint16_index_1_not_array() {
    var struct = struct()
        .uint16()
        .fromBytes(ba().uint16(1))
        .build();

    assertThatThrownBy(() -> struct.getUint16(0, 1))
        .isInstanceOf(ArrayIndexOutOfBoundsException.class)
        .hasMessage("Uint16Type at position 0 index: 1 length: 1");
  }

  @Test
  void getArrayWrapper() {
    var struct = struct()
        .uint16()
        .uint16Array(0)
        .build();

    assertThatThrownBy(() -> ((Uint16Type) struct.getType(1)).get(struct, 0))
        .isInstanceOf(UnsupportedOperationException.class)
        .hasMessage("get(Pointer, long) not supported for Uint16Type. Use getUint16(Pointer, long) instead.");
  }

  @Test
  void getUint16Array() {
    var struct = struct()
        .uint16()
        .uint16Array(0)
        .fromBytes(ba().uint16(2, 1, 2))
        .build();

    assertThat(struct.getUint16(1, 0)).isEqualTo(1);
    assertThat(struct.getUint16(1, 1)).isEqualTo(2);
  }

  @Test
  void getUint16Array_negative() {
    var struct = struct()
        .uint16()
        .uint16Array(0)
        .build();

    assertThatThrownBy(() -> struct.getUint16(1, -1))
        .isInstanceOf(ArrayIndexOutOfBoundsException.class)
        .hasMessage("Uint16Type at position 1 index: -1 length: 0");
  }

  @Test
  void getUint16Array_greater_than_length() {
    var struct = struct()
        .uint16()
        .uint16Array(0)
        .fromBytes(ba().uint16(2, 1, 2))
        .build();

    assertThatThrownBy(() -> struct.getUint16(1, 2))
        .isInstanceOf(ArrayIndexOutOfBoundsException.class)
        .hasMessage("Uint16Type at position 1 index: 2 length: 2");
  }

  @Test
  void getUint16Array_not_allocated() {
    var struct = struct()
        .allocated()
        .uint16()
        .uint16Array(0)
        .build();

    assertThatThrownBy(() -> struct.getUint16(1, 0))
        .isInstanceOf(IndexOutOfBoundsException.class)
        .hasMessage("allocated: 0, index: 0, length: 2");
  }

  @Test
  void getUint16Array_constant() {
    var struct = struct()
        .primitive().constant(5).lengthExpression(constant(5)).uint16()
        .build();

    assertThat(struct.getUint16(0, 3)).isEqualTo(5);
  }
}
