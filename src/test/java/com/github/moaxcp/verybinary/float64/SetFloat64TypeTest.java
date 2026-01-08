package com.github.moaxcp.verybinary.float64;

import com.github.moaxcp.verybinary.Float64Type;
import org.junit.jupiter.api.Test;

import static com.github.moaxcp.verybinary.Builders.struct;
import static com.github.moaxcp.verybinary.ByteArray.ba;
import static com.github.moaxcp.verybinary.Expression.constant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class SetFloat64TypeTest {
  @Test
  void setWrapper() {
    var struct = struct()
        .float64()
        .build();

    assertThatThrownBy(() -> ((Float64Type) struct.getType(0)).set(struct, Double.valueOf(2.0d)))
        .isInstanceOf(UnsupportedOperationException.class)
        .hasMessage("set(Pointer, Double) not supported for Float64Type. Use set(Pointer, double) instead.");
  }

  @Test
  void setFloat64() {
    var struct = struct()
        .float64()
        .build();

    struct.setFloat64(0, 2.0d);

    assertThat(struct.getByteArray()).isEqualTo(ba().float64(2));
  }

  @Test
  void setFloat64_position_negative() {
    var struct = struct()
        .float64()
        .build();

    assertThatThrownBy(() -> struct.setFloat64(-1, 2.0d))
        .isInstanceOf(IndexOutOfBoundsException.class)
        .hasMessage("Index -1 out of bounds for length 1");
  }

  @Test
  void setFloat64_position_greater_than_length() {
    var struct = struct()
        .float64()
        .build();

    assertThatThrownBy(() -> struct.setFloat64(2, 2.0d))
        .isInstanceOf(IndexOutOfBoundsException.class)
        .hasMessage("Index 2 out of bounds for length 1");
  }

  @Test
  void setFloat64_not_allocated() {
    var struct = struct()
        .allocated()
        .float64()
        .build();

    assertThatThrownBy(() -> struct.setFloat64(0, 2.0d))
        .isInstanceOf(ArrayIndexOutOfBoundsException.class)
        .hasMessage("Index 0 out of bounds for length 0");
  }

  @Test
  void setFloat64_constant() {
    var struct = struct()
        .primitive().constant(3.0d).float64()
        .build();

    assertThatThrownBy(() -> struct.setFloat64(0, 2.0d))
        .isInstanceOf(UnsupportedOperationException.class)
        .hasMessage("Float64Type at position 0 is constant index: 0 value: 2.0 constant: 3.0");
  }

  @Test
  void setArrayWrapper() {
    var struct = struct()
        .float64()
        .float64Array(0)
        .build();

    assertThatThrownBy(() -> ((Float64Type) struct.getType(1)).set(struct, 0, Double.valueOf(2.0d)))
        .isInstanceOf(UnsupportedOperationException.class)
        .hasMessage("set(Pointer, long, Double) not supported for Float64Type. Use set(Pointer, long, double) instead.");
  }

  @Test
  void setFloat64Array() {
    var struct = struct()
        .float64()
        .float64Array(0)
        .float64()
        .fromBytes(ba().float64(2).float64(5).float64(3).float64(4))
        .build();

    struct.setFloat64(1, 0, 2.0d);

    assertThat(struct.getFloat64(0)).isEqualTo(2.0);
    assertThat(struct.getFloat64(1, 0)).isEqualTo(2.0d);
    assertThat(struct.getByteArray()).isEqualTo(ba().float64(2).float64(2).float64(3).float64(4));
  }

  @Test
  void setFloat64Array_negative() {
    var struct = struct()
        .float64()
        .float64Array(0)
        .float64()
        .fromBytes(ba().float64(2).float64(2).float64(3).float64(4))
        .build();

    assertThatThrownBy(() -> struct.setFloat64(1, -1, 2.0d))
        .isInstanceOf(IndexOutOfBoundsException.class)
        .hasMessage("Float64Type at position 1 index: -1 length: 2");
  }

  @Test
  void setFloat64Array_greater_than_length() {
    var struct = struct()
        .float64()
        .float64Array(0)
        .float64()
        .fromBytes(ba().float64(2).float64(2).float64(3).float64(4))
        .build();

    assertThatThrownBy(() -> struct.setFloat64(1, 2, 2.0d))
        .isInstanceOf(ArrayIndexOutOfBoundsException.class)
        .hasMessage("Float64Type at position 1 index: 2 length: 2");

    assertThat(struct.getByteArray()).isEqualTo(ba().float64(2).float64(2).float64(3).float64(4));
  }

  @Test
  void setFloat64Array_not_allocated() {
    var struct = struct()
        .allocated()
        .float64()
        .float64Array(0)
        .build();

    assertThatThrownBy(() -> struct.setFloat64(1, 0, 2.0d))
        .isInstanceOf(IndexOutOfBoundsException.class)
        .hasMessage("allocated: 0, index: 0, length: 8");
  }

  @Test
  void setFloat64Array_index_0_not_array() {
    var struct = struct()
        .float64()
        .build();

    struct.setFloat64(0, 0, 2.0d);

    assertThat(struct.getByteArray()).isEqualTo(ba().float64(2));
  }

  @Test
  void setFloat64Array_index_1_not_array() {
    var struct = struct()
        .float64()
        .build();

    assertThatThrownBy(() -> struct.setFloat64(0, 1, 2.0d))
        .isInstanceOf(ArrayIndexOutOfBoundsException.class)
        .hasMessage("Float64Type at position 0 index: 1 length: 1");
  }

  @Test
  void setFloat64Array_constant_value_and_length() {
    var struct = struct()
        .primitive().constant(3.0d).lengthExpression(constant(3)).float64()
        .build();

    assertThatThrownBy(() -> struct.setFloat64(0, 2, 2.0d))
        .isInstanceOf(UnsupportedOperationException.class)
        .hasMessage("Float64Type at position 0 is constant index: 2 value: 2.0 constant: 3.0");
  }

  @Test
  void setFloat64Array_constant_value() {
    var struct = struct()
        .float64()
        .primitive().constant(3.0d).lengthField(0).float64()
        .fromBytes(ba().float64(2, 3, 3))
        .build();

    struct.setFloat64(1, 1, 2.0d);
    assertThat(struct.getByteArray()).isEqualTo(ba().float64(2, 3, 2));
  }

  @Test
  void setFloat64Array_constant_value_same() {
    var struct = struct()
        .float64()
        .primitive().constant(3.0d).lengthField(0).float64()
        .fromBytes(ba().float64(2, 3, 3))
        .build();

    struct.setFloat64(1, 1, 3.0d);

    assertThat(struct.getByteArray()).isEqualTo(ba().float64(2, 3, 3));
  }

  @Test
  void setFloat64Array_set_length_field_without_adding_to_array() {
    var struct = struct()
        .float64()
        .float64Array(0)
        .build();

    struct.setFloat64(0, 2.0d);

    assertThat(struct.getByteArray()).isEqualTo(ba().float64(2).float64(0, 0));
  }
}
