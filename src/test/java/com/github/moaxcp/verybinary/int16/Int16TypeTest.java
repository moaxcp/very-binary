package com.github.moaxcp.verybinary.int16;

import com.github.moaxcp.verybinary.Int16Type;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import static com.github.moaxcp.verybinary.Builders.struct;
import static com.github.moaxcp.verybinary.ByteLengthListener.align;
import static com.github.moaxcp.verybinary.Expression.constant;
import static com.github.moaxcp.verybinary.Expression.valueOf;
import static com.github.moaxcp.verybinary.Int16Type.int16;
import static com.github.moaxcp.verybinary.Primitive.INT16;
import static com.github.moaxcp.verybinary.PrimitiveBuilder.primitive;
import static org.assertj.core.api.Assertions.assertThat;

public class Int16TypeTest {

  @Test
  void constructor() {
    var type = int16();
    assertThat(type.getPosition()).isEqualTo(-1);
  }

  @Test
  void constructorPosition() {
    var type = int16(15);
    assertThat(type.getPosition()).isEqualTo(15);
  }

  @Test
  void constructorEverything() {
    var struct = struct()
        .int16()
        .primitive().constant((short) 5).lengthExpression(valueOf(0)).int16()
        .align(2)
        .build();

    assertThat(struct.getByteLength()).isEqualTo(INT16.size() + 2);
    Assertions.assertThat(struct.<Int16Type>getType(1))
        .isEqualTo(primitive().position(1).byteLengthListener(align(2)).constant((short) 5).lengthExpression(valueOf(0)).int16());
  }

  @Test
  void copy() {
    var type = int16();
    var copy = type.copy(15);
    assertThat(copy.getPosition()).isEqualTo(15);
  }

  @Test
  void getByteLength() {
    var struct = struct()
        .int16()
        .build();

    assertThat(struct.getType(0).getByteLength(struct)).isEqualTo(INT16.size());
  }

  @Test
  void isFixedLength() {
    var struct = struct()
        .int16()
        .build();

    assertThat(struct.getType(0).isFixedLength(struct)).isTrue();
    assertThat(struct.isFixedLength()).isTrue();
  }

  @Test
  void isFixedLengthArray() {
    var struct = struct()
        .int16Array(constant(5))
        .build();

    assertThat(struct.getType(0).isFixedLength(struct)).isTrue();
    assertThat(struct.isFixedLength()).isTrue();
  }
}
