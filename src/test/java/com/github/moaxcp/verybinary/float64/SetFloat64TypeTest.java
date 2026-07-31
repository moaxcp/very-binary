package com.github.moaxcp.verybinary.float64;

import com.github.moaxcp.verybinary.Float64ListType;
import com.github.moaxcp.verybinary.Float64Type;
import org.junit.jupiter.api.Test;

import static com.github.moaxcp.verybinary.Builders.struct;
import static com.github.moaxcp.verybinary.ByteArray.ba;
import static com.github.moaxcp.verybinary.list.Float64List.toFloat64List;
import static com.github.moaxcp.verybinary.math.Constant.constant;
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
        .hasMessage("setUnchecked(Pointer, Double) not supported for Float64Type. Use setUnchecked(Pointer, double) instead.");
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
        .basic().constant(3.0d).float64()
        .build();

    assertThatThrownBy(() -> struct.setFloat64(0, 2.0d))
        .isInstanceOf(IllegalStateException.class)
        .hasMessage("Float64Type at position 0 is constant value");
  }

  @Test
  void setArrayWrapper() {
    var struct = struct()
        .float64List(constant(1))
        .build();

    assertThatThrownBy(() -> ((Float64ListType) struct.getType(0)).set(struct, 0, Double.valueOf(2.0d)))
        .isInstanceOf(UnsupportedOperationException.class)
        .hasMessage("set(Pointer, long, T) not supported for Float64ListType. Use setFloat64(Pointer, long, double) instead.");
  }

  @Test
  void setFloat64Array() {
    var struct = struct()
        .float64()
        .float64List(0)
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
        .float64List(0)
        .float64()
        .fromBytes(ba().float64(2).float64(2).float64(3).float64(4))
        .build();

    assertThatThrownBy(() -> struct.setFloat64(1, -1, 2.0d))
        .isInstanceOf(IndexOutOfBoundsException.class)
        .hasMessage("Float64ListType at position 1 index: -1 length: 2");
  }

  @Test
  void setFloat64Array_greater_than_length() {
    var struct = struct()
        .float64()
        .float64List(0)
        .float64()
        .fromBytes(ba().float64(2).float64(2).float64(3).float64(4))
        .build();

    assertThatThrownBy(() -> struct.setFloat64(1, 2, 2.0d))
        .isInstanceOf(ArrayIndexOutOfBoundsException.class)
        .hasMessage("Float64ListType at position 1 index: 2 length: 2");

    assertThat(struct.getByteArray()).isEqualTo(ba().float64(2).float64(2).float64(3).float64(4));
  }

  @Test
  void setFloat64Array_not_allocated() {
    var struct = struct()
        .allocated()
        .float64()
        .float64List(0)
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

    assertThatThrownBy(() -> struct.setFloat64(0, 0, 2.0d))
        .isInstanceOf(ClassCastException.class);
  }

  @Test
  void setFloat64Array_constant_value_and_length() {
    var struct = struct()
        .basic().constant(toFloat64List(3, 3, 3)).float64()
        .build();

    assertThatThrownBy(() -> struct.setFloat64(0, 2, 2.0d))
        .isInstanceOf(IllegalStateException.class)
        .hasMessage("Float64ListType at position 0 is constant value");
  }

  @Test
  void setFloat64Array_constant_value() {
    var struct = struct()
        .float64()
        .basic().constant(toFloat64List(3, 3, 3)).float64()
        .fromBytes(ba().float64(2, 3, 3))
        .build();

    assertThatThrownBy(() -> struct.setFloat64(1, 1, 2.0d))
        .isInstanceOf(IllegalStateException.class)
        .hasMessage("Float64ListType at position 1 is constant value");
  }

  @Test
  void setFloat64Array_constant_value_same() {
    var struct = struct()
        .float64()
        .basic().constant(toFloat64List(new double[]{3, 3, 3})).float64()
        .fromBytes(ba().float64(2, 3, 3))
        .build();

    struct.setFloat64(1, 1, 3.0d);

    assertThat(struct.getByteArray()).isEqualTo(ba().float64(2, 3, 3));
  }

  @Test
  void setFloat64Array_set_length_field_without_adding_to_array() {
    var struct = struct()
        .float64()
        .float64List(0)
        .build();

    struct.setFloat64(0, 2.0d);

    assertThat(struct.getByteArray()).isEqualTo(ba().float64(2).float64(0, 0));
  }
}
