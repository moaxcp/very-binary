package com.github.moaxcp.verybinary.float64;

import com.github.moaxcp.verybinary.Float64Type;
import org.junit.jupiter.api.Test;

import static com.github.moaxcp.verybinary.Builders.struct;
import static com.github.moaxcp.verybinary.ByteArray.ba;
import static com.github.moaxcp.verybinary.Expression.constant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class AddFloat64TypeTest {
  @Test
  void addFloat64Wrapper() {
    var struct = struct()
        .float64()
        .float64Array(0)
        .build();

    assertThatThrownBy(() -> ((Float64Type) struct.getType(1)).add(struct, Double.valueOf(1.0d)))
        .isInstanceOf(UnsupportedOperationException.class)
        .hasMessage("add(Pointer, Double) not supported for Float64Type. Use add(Pointer, double) instead.");
  }

  @Test
  void addFloat64Wrapper_with_index() {
    var struct = struct()
        .float64()
        .float64Array(0)
        .build();

    assertThatThrownBy(() -> ((Float64Type) struct.getType(1)).add(struct, 0, Double.valueOf(1.0d)))
        .isInstanceOf(UnsupportedOperationException.class)
        .hasMessage("add(Pointer, long, Double) not supported for Float64Type. Use add(Pointer, long, double) instead.");
  }

  @Test
  void addFloat64() {
    var struct = struct()
        .float64()
        .float64Array(0)
        .fromBytes(ba().float64(2).float64(2).float64(3))
        .build();

    struct.addFloat64(1, 3.0d);
    struct.addFloat64(1, 2.0d);

    assertThat(struct.getByteArray()).isEqualTo(ba().float64(4).float64(2).float64(3).float64(3).float64(2));
  }

  @Test
  void addFloat64_position_negative() {
    var struct = struct()
        .float64()
        .float64Array(0)
        .fromBytes(ba().float64(2, 3, 2))
        .build();

    assertThatThrownBy(() -> struct.addFloat64(-1, 3.0d))
        .isInstanceOf(IndexOutOfBoundsException.class)
        .hasMessage("Index -1 out of bounds for length 2");
  }

  @Test
  void addFloat64_position_greater_than_length() {
    var struct = struct()
        .float64()
        .float64Array(0)
        .fromBytes(ba().float64(2, 3, 2))
        .build();

    assertThatThrownBy(() -> struct.addFloat64(3, 3.0d))
        .isInstanceOf(IndexOutOfBoundsException.class)
        .hasMessage("Index 3 out of bounds for length 2");
  }

  @Test
  void addFloat64_not_allocated() {
    var struct = struct()
        .allocated()
        .float64()
        .float64Array(0)
        .build();
    assertThatThrownBy(() -> struct.addFloat64(1, 3.0d))
        .isInstanceOf(IndexOutOfBoundsException.class)
        .hasMessage("allocated: 0, index: 0, length: 8");
  }

  @Test
  void addFloat64Array_constant() {
    var struct = struct()
        .primitive().constant(3.0d).lengthExpression(constant(3)).float64()
        .build();

    assertThatThrownBy(() -> struct.addFloat64(0, 4.0d))
        .isInstanceOf(UnsupportedOperationException.class)
        .hasMessage("Float64Type at position 0 is constant index: 3 value: 4.0 constant: 3.0");
  }

  @Test
  void addFloat64_not_array() {
    var struct = struct()
        .float64()
        .fromBytes(ba().float64(2))
        .build();
    assertThatThrownBy(() -> struct.addFloat64(0, 3.0d))
        .isInstanceOf(ArrayIndexOutOfBoundsException.class)
        .hasMessage("Float64Type cannot add to non-array type at position 0 index: 1 length: 1");
  }

  @Test
  void addFloat64_with_index() {
    var struct = struct()
        .float64()
        .float64Array(0)
        .fromBytes(ba().float64(2).float64(2).float64(3))
        .build();

    struct.addFloat64(1, 0, 3.0d);
    struct.addFloat64(1, 1, 2.0d);

    assertThat(struct.getByteArray()).isEqualTo(ba().float64(4).float64(3).float64(2).float64(2).float64(3));
  }

  @Test
  void addFloat64_with_index_negative() {
    var struct = struct()
        .float64()
        .float64Array(0)
        .fromBytes(ba().float64(2, 3, 2))
        .build();

    assertThatThrownBy(() -> struct.addFloat64(1, -1, 3.0d))
        .isInstanceOf(ArrayIndexOutOfBoundsException.class)
        .hasMessage("Float64Type at position 1 index: -1 new length: 3");
  }

  @Test
  void addFloat64_with_index_greater_than_length() {
    var struct = struct()
        .float64()
        .float64Array(0)
        .fromBytes(ba().float64(2, 3, 2))
        .build();

    assertThatThrownBy(() -> struct.addFloat64(1, 3, 3.0d))
        .isInstanceOf(ArrayIndexOutOfBoundsException.class)
        .hasMessage("Float64Type at position 1 index: 3 new length: 3");
  }

  @Test
  void addFloat64_with_index_not_allocated() {
    var struct = struct()
        .allocated()
        .float64()
        .float64Array(0)
        .build();

    assertThatThrownBy(() -> struct.addFloat64(1, 0, 3.0d))
        .isInstanceOf(IndexOutOfBoundsException.class)
        .hasMessage("allocated: 0, index: 0, length: 8");
  }

  @Test
  void addFloat64_with_index_0_not_array() {
    var struct = struct()
        .float64()
        .fromBytes(ba().float64(2))
        .build();

    assertThatThrownBy(() -> struct.addFloat64(0, 0, 3.0d))
        .isInstanceOf(ArrayIndexOutOfBoundsException.class)
        .hasMessage("Float64Type cannot add to non-array type at position 0 index: 0 length: 1");
  }

  @Test
  void addFloat64_with_index_1_not_array() {
    var struct = struct()
        .float64()
        .fromBytes(ba().float64(2))
        .build();

    assertThatThrownBy(() -> struct.addFloat64(0, 1, 3.0d))
        .isInstanceOf(ArrayIndexOutOfBoundsException.class)
        .hasMessage("Float64Type cannot add to non-array type at position 0 index: 1 length: 1");
  }

  @Test
  void addFloat64Array_with_index_constant() {
    var struct = struct()
        .primitive().constant(3.0d).lengthExpression(constant(3)).float64()
        .build();

    assertThatThrownBy(() -> struct.addFloat64(0, 2, 4.0d))
        .isInstanceOf(UnsupportedOperationException.class)
        .hasMessage("Float64Type at position 0 is constant index: 2 value: 4.0 constant: 3.0");
  }
}
