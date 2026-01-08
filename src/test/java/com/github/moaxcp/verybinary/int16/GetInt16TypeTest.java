package com.github.moaxcp.verybinary.int16;

import com.github.moaxcp.verybinary.Int16Type;
import org.junit.jupiter.api.Test;

import static com.github.moaxcp.verybinary.Builders.struct;
import static com.github.moaxcp.verybinary.ByteArray.ba;
import static com.github.moaxcp.verybinary.Expression.constant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class GetInt16TypeTest {
  @Test
  void getWrapper() {
    var struct = struct()
        .int16()
        .build();

    assertThatThrownBy(() -> ((Int16Type) struct.getType(0)).get(struct))
        .isInstanceOf(UnsupportedOperationException.class)
        .hasMessage("get(Pointer) not supported for Int16Type. Use getInt16(Pointer) instead.");
  }

  @Test
  void getInt16() {
    var struct = struct()
        .int16()
        .build();

    struct.setInt16(0, (short) 2);

    assertThat(struct.getInt16(0)).isEqualTo((short) 2);
  }

  @Test
  void getInt16_position_negative() {
    var struct = struct()
        .int16()
        .build();

    assertThatThrownBy(() -> struct.getInt16(-1))
        .isInstanceOf(IndexOutOfBoundsException.class)
        .hasMessage("Index -1 out of bounds for length 1");
  }

  @Test
  void getInt16_position_greater_than_length() {
    var struct = struct()
        .int16()
        .build();

    assertThatThrownBy(() -> struct.getInt16(2))
        .isInstanceOf(IndexOutOfBoundsException.class)
        .hasMessage("Index 2 out of bounds for length 1");
  }

  @Test
  void getInt16Allocated() {
    var struct = struct()
        .int16()
        .build();

    assertThat(struct.getInt16(0)).isEqualTo((short) 0);
    assertThat(struct.getByteArray()).isEqualTo(ba().int16(0));
  }

  @Test
  void getInt16NotAllocated() {
    var struct = struct()
        .allocated()
        .int16()
        .build();

    assertThatThrownBy(() -> struct.getInt16(0))
        .isInstanceOf(IndexOutOfBoundsException.class)
        .hasMessage("allocated: 0, index: 0, length: 2");
  }

  @Test
  void getInt16_constant() {
    var struct = struct()
        .primitive().constant((short) 5).int16()
        .build();

    assertThat(struct.getInt16(0)).isEqualTo((short) 5);
  }

  @Test
  void getInt16_index_0_not_array() {
    var struct = struct()
        .int16()
        .fromBytes(ba().int16(1))
        .build();

    assertThat(struct.getInt16(0, 0)).isEqualTo((short) 1);
  }

  @Test
  void getInt16_index_1_not_array() {
    var struct = struct()
        .int16()
        .fromBytes(ba().int16(1))
        .build();

    assertThatThrownBy(() -> struct.getInt16(0, 1))
        .isInstanceOf(ArrayIndexOutOfBoundsException.class)
        .hasMessage("Int16Type at position 0 index: 1 length: 1");
  }

  @Test
  void getArrayWrapper() {
    var struct = struct()
        .int16()
        .int16Array(0)
        .build();

    assertThatThrownBy(() -> ((Int16Type) struct.getType(1)).get(struct, 0))
        .isInstanceOf(UnsupportedOperationException.class)
        .hasMessage("get(Pointer, long) not supported for Int16Type. Use getInt16(Pointer, long) instead.");
  }

  @Test
  void getInt16Array() {
    var struct = struct()
        .int16()
        .int16Array(0)
        .fromBytes(ba().int16(2, 1, 2))
        .build();

    assertThat(struct.getInt16(1, 0)).isEqualTo((short) 1);
    assertThat(struct.getInt16(1, 1)).isEqualTo((short) 2);
  }

  @Test
  void getInt16Array_negative() {
    var struct = struct()
        .int16()
        .int16Array(0)
        .build();

    assertThatThrownBy(() -> struct.getInt16(1, -1))
        .isInstanceOf(ArrayIndexOutOfBoundsException.class)
        .hasMessage("Int16Type at position 1 index: -1 length: 0");
  }

  @Test
  void getInt16Array_greater_than_length() {
    var struct = struct()
        .int16()
        .int16Array(0)
        .fromBytes(ba().int16(2, 1, 2))
        .build();

    assertThatThrownBy(() -> struct.getInt16(1, 2))
        .isInstanceOf(ArrayIndexOutOfBoundsException.class)
        .hasMessage("Int16Type at position 1 index: 2 length: 2");
  }

  @Test
  void getInt16Array_not_allocated() {
    var struct = struct()
        .allocated()
        .int16()
        .int16Array(0)
        .build();

    assertThatThrownBy(() -> struct.getInt16(1, 0))
        .isInstanceOf(IndexOutOfBoundsException.class)
        .hasMessage("allocated: 0, index: 0, length: 2");
  }

  @Test
  void getInt16Array_constant() {
    var struct = struct()
        .primitive().constant((short) 5).lengthExpression(constant(5)).int16()
        .build();

    assertThat(struct.getInt16(0, 3)).isEqualTo((short) 5);
  }
}
