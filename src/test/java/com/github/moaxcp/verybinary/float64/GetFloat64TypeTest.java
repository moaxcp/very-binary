package com.github.moaxcp.verybinary.float64;

import com.github.moaxcp.verybinary.Float64ArrayType;
import com.github.moaxcp.verybinary.Float64Type;
import org.junit.jupiter.api.Test;

import static com.github.moaxcp.verybinary.Builders.struct;
import static com.github.moaxcp.verybinary.ByteArray.ba;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class GetFloat64TypeTest {
  @Test
  void getWrapper() {
    var struct = struct()
        .float64()
        .build();

    assertThatThrownBy(() -> ((Float64Type) struct.getType(0)).get(struct))
        .isInstanceOf(UnsupportedOperationException.class)
        .hasMessage("get(Pointer) not supported for Float64Type. Use getFloat64(Pointer) instead.");
  }

  @Test
  void getFloat64() {
    var struct = struct()
        .float64()
        .build();

    struct.setFloat64(0, 2.0d);

    assertThat(struct.getFloat64(0)).isEqualTo(2.0d);
  }

  @Test
  void getFloat64_position_negative() {
    var struct = struct()
        .float64()
        .build();

    assertThatThrownBy(() -> struct.getFloat64(-1))
        .isInstanceOf(IndexOutOfBoundsException.class)
        .hasMessage("Index -1 out of bounds for length 1");
  }

  @Test
  void getFloat64_position_greater_than_length() {
    var struct = struct()
        .float64()
        .build();

    assertThatThrownBy(() -> struct.getFloat64(2))
        .isInstanceOf(IndexOutOfBoundsException.class)
        .hasMessage("Index 2 out of bounds for length 1");
  }

  @Test
  void getFloat64Allocated() {
    var struct = struct()
        .float64()
        .build();

    assertThat(struct.getFloat64(0)).isEqualTo(0.0d);
    assertThat(struct.getByteArray()).isEqualTo(ba().float64(0));
  }

  @Test
  void getFloat64NotAllocated() {
    var struct = struct()
        .allocated()
        .float64()
        .build();

    assertThatThrownBy(() -> struct.getFloat64(0))
        .isInstanceOf(IndexOutOfBoundsException.class)
        .hasMessage("allocated: 0, index: 0, length: 8");
  }

  @Test
  void getFloat64_constant() {
    var struct = struct()
        .primitive().constant(3.0d).float64()
        .build();

    assertThat(struct.getFloat64(0)).isEqualTo(3.0d);
  }

  @Test
  void getFloat64_index_0_not_array() {
    var struct = struct()
        .float64()
        .fromBytes(ba().float64(0))
        .build();

    assertThatThrownBy(() -> struct.getFloat64(0, 0))
        .isInstanceOf(ClassCastException.class);
  }

  @Test
  void getArrayWrapper() {
    var struct = struct()
        .float64()
        .float64Array(0)
        .build();

    assertThatThrownBy(() -> ((Float64ArrayType) struct.getType(1)).get(struct, 0))
        .isInstanceOf(UnsupportedOperationException.class)
        .hasMessage("get(Pointer, long) not supported for Float64ArrayType. Use getFloat64(Pointer, long) instead.");
  }

  @Test
  void getFloat64Array() {
    var struct = struct()
        .float64()
        .float64Array(0)
        .fromBytes(ba().float64(2, 3, 2))
        .build();

    assertThat(struct.getFloat64(1, 0)).isEqualTo(3.0d);
    assertThat(struct.getFloat64(1, 1)).isEqualTo(2.0d);
  }

  @Test
  void getFloat64Array_negative() {
    var struct = struct()
        .float64()
        .float64Array(0)
        .build();

    assertThatThrownBy(() -> struct.getFloat64(1, -1))
        .isInstanceOf(ArrayIndexOutOfBoundsException.class)
        .hasMessage("Float64ArrayType at position 1 index: -1 length: 0");
  }

  @Test
  void getFloat64Array_greater_than_length() {
    var struct = struct()
        .float64()
        .float64Array(0)
        .fromBytes(ba().float64(2, 3, 2))
        .build();

    assertThatThrownBy(() -> struct.getFloat64(1, 2))
        .isInstanceOf(ArrayIndexOutOfBoundsException.class)
        .hasMessage("Float64ArrayType at position 1 index: 2 length: 2");
  }

  @Test
  void getFloat64Array_not_allocated() {
    var struct = struct()
        .allocated()
        .float64()
        .float64Array(0)
        .build();

    assertThatThrownBy(() -> struct.getFloat64(1, 0))
        .isInstanceOf(IndexOutOfBoundsException.class)
        .hasMessage("allocated: 0, index: 0, length: 8");
  }

  @Test
  void getFloat64Array_constant() {
    var struct = struct()
        .primitive().constant(new double[]{3, 3, 3}).float64()
        .build();

    assertThat(struct.getFloat64(0, 2)).isEqualTo(3.0d);
  }
}
