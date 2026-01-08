package com.github.moaxcp.verybinary.float64;

import com.github.moaxcp.verybinary.Float64Type;
import org.junit.jupiter.api.Test;

import static com.github.moaxcp.verybinary.Builders.struct;
import static com.github.moaxcp.verybinary.ByteArray.ba;
import static com.github.moaxcp.verybinary.Expression.constant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class RemoveFloat64TypeTest {
  @Test
  void removeFloat64() {
    var struct = struct()
        .float64()
        .float64Array(0)
        .fromBytes(ba().float64(2, 3, 2))
        .build();

    struct.removeAll(1);

    assertThat(struct.getByteArray()).isEqualTo(ba().float64(0));
  }

  @Test
  void removeFloat64_position_negative() {
    var struct = struct()
        .float64()
        .float64Array(0)
        .build();

    assertThatThrownBy(() -> struct.removeAll(-1))
        .isInstanceOf(IndexOutOfBoundsException.class)
        .hasMessage("Index -1 out of bounds for length 2");
  }

  @Test
  void removeFloat64_position_greater_than_length() {
    var struct = struct()
        .float64()
        .float64Array(0)
        .build();

    assertThatThrownBy(() -> struct.removeAll(2))
        .isInstanceOf(IndexOutOfBoundsException.class)
        .hasMessage("Index 2 out of bounds for length 2");
  }

  @Test
  void removeFloat64_not_allocated() {
    var struct = struct()
        .allocated()
        .float64()
        .float64Array(0)
        .build();

    assertThatThrownBy(() -> struct.removeAll(1))
        .isInstanceOf(IndexOutOfBoundsException.class)
        .hasMessage("allocated: 0, index: 0, length: 8");
  }

  @Test
  void removeFloat64_not_array() {
    var struct = struct()
        .float64()
        .build();

    assertThatThrownBy(() -> struct.removeAll(0))
        .isInstanceOf(ArrayIndexOutOfBoundsException.class)
        .hasMessage("Float64Type cannot remove from non-array type at position 0");
  }

  @Test
  void removeFloat64_with_index_0() {
    var struct = struct()
        .float64()
        .float64Array(0)
        .build();

    struct.addFloat64(1, 3.0d);
    struct.addFloat64(1, 2.0d);
    struct.remove(1, 0);

    assertThat(((Float64Type) struct.getType(1)).getFloat64(struct, 0)).isEqualTo(2.0d);

    assertThat(struct.getByteArray()).isEqualTo(ba().float64(1).float64(2));
  }

  @Test
  void removeFloat64_with_index_1() {
    var struct = struct()
        .float64()
        .float64Array(0)
        .build();
    struct.addFloat64(1, 3.0d);
    struct.addFloat64(1, 2.0d);

    struct.remove(1, 1);

    assertThat(struct.getByteArray()).isEqualTo(ba().float64(1).float64(3));
  }

  @Test
  void removeFloat64_with_index_negative() {
    var struct = struct()
        .float64()
        .float64Array(0)
        .fromBytes(ba().float64(2, 3, 2))
        .build();

    assertThatThrownBy(() -> struct.remove(1, -1))
        .isInstanceOf(ArrayIndexOutOfBoundsException.class)
        .hasMessage("Float64Type at position 1 index: -1 length: 2");
  }

  @Test
  void removeFloat64_with_index_greater_than_length() {
    var struct = struct()
        .float64()
        .float64Array(0)
        .fromBytes(ba().float64(2, 3, 2))
        .build();

    assertThatThrownBy(() -> struct.remove(1, 2))
        .isInstanceOf(ArrayIndexOutOfBoundsException.class)
        .hasMessage("Float64Type at position 1 index: 2 length: 2");
  }

  @Test
  void removeFloat64_with_index_not_array() {
    var struct = struct()
        .float64()
        .fromBytes(ba().float64(2))
        .build();

    assertThatThrownBy(() -> struct.remove(0, 0))
        .isInstanceOf(ArrayIndexOutOfBoundsException.class)
        .hasMessage("Float64Type cannot remove from non-array type at position 0");
  }

  @Test
  void removeFloat64Array_fixed_length() {
    var struct = struct()
        .primitive().constant(3.0d).lengthExpression(constant(3)).float64()
        .build();

    assertThatThrownBy(() -> struct.removeAll(0))
        .isInstanceOf(UnsupportedOperationException.class)
        .hasMessage("Cannot remove fixed length array Float64Type at position 0");
  }

  @Test
  void removeFloat64Array_fixed_length_with_index() {
    var struct = struct()
        .primitive().constant(3.0d).lengthExpression(constant(3)).float64()
        .build();

    assertThatThrownBy(() -> struct.remove(0, 2))
        .isInstanceOf(UnsupportedOperationException.class)
        .hasMessage("Cannot remove element from fixed length array Float64Type at position 0 index: 2");
  }
}
