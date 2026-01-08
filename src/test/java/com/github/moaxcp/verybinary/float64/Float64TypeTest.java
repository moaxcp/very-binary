package com.github.moaxcp.verybinary.float64;

import com.github.moaxcp.verybinary.Float64Type;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import static com.github.moaxcp.verybinary.Builders.struct;
import static com.github.moaxcp.verybinary.ByteLengthListener.align;
import static com.github.moaxcp.verybinary.Expression.constant;
import static com.github.moaxcp.verybinary.Expression.valueOf;
import static com.github.moaxcp.verybinary.Float64Type.float64Type;
import static com.github.moaxcp.verybinary.Primitive.FLOAT64;
import static com.github.moaxcp.verybinary.PrimitiveBuilder.primitive;
import static org.assertj.core.api.Assertions.assertThat;

public class Float64TypeTest {

  @Test
  void constructor() {
    var type = float64Type();
    assertThat(type.getPosition()).isEqualTo(-1);
  }

  @Test
  void constructorPosition() {
    var type = float64Type(15);
    assertThat(type.getPosition()).isEqualTo(15);
  }

  @Test
  void constructorEverything() {
    var struct = struct()
        .float64()
        .primitive().constant(3.0d).lengthExpression(valueOf(0)).float64()
        .align(2)
        .build();

    assertThat(struct.getByteLength()).isEqualTo(FLOAT64.size() + 2);
    Assertions.assertThat(struct.<Float64Type>getType(1))
        .isEqualTo(primitive().position(1).byteLengthListener(align(2)).constant(3.0d).lengthExpression(valueOf(0)).float64());
  }

  @Test
  void copy() {
    var type = float64Type();
    var copy = type.copy(15);
    assertThat(copy.getPosition()).isEqualTo(15);
  }

  @Test
  void getByteLength() {
    var struct = struct()
        .float64()
        .build();

    assertThat(struct.getByteLength()).isEqualTo(FLOAT64.size());
    assertThat(struct.getType(0).getByteLength(struct)).isEqualTo(FLOAT64.size());
  }

  @Test
  void isFixedLength() {
    var struct = struct()
        .float64()
        .build();

    assertThat(struct.getType(0).isFixedLength(struct)).isTrue();
    assertThat(struct.isFixedLength()).isTrue();
  }

  @Test
  void isFixedLengthArray() {
    var struct = struct()
        .float64Array(constant(3))
        .build();

    assertThat(struct.getType(0).isFixedLength(struct)).isTrue();
    assertThat(struct.isFixedLength()).isTrue();
  }
}
