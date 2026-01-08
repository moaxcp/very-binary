package com.github.moaxcp.verybinary.int32;

import com.github.moaxcp.verybinary.Int32Type;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import static com.github.moaxcp.verybinary.Builders.struct;
import static com.github.moaxcp.verybinary.ByteArray.ba;
import static com.github.moaxcp.verybinary.ByteLengthListener.align;
import static com.github.moaxcp.verybinary.Expression.constant;
import static com.github.moaxcp.verybinary.Expression.valueOf;
import static com.github.moaxcp.verybinary.Int32Type.int32Type;
import static com.github.moaxcp.verybinary.Primitive.INT32;
import static com.github.moaxcp.verybinary.PrimitiveBuilder.primitive;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class Int32TypeTest {

  @Test
  void constructor() {
    var type = int32Type();
    assertThat(type.getPosition()).isEqualTo(-1);
  }

  @Test
  void constructorPosition() {
    var type = int32Type(15);
    assertThat(type.getPosition()).isEqualTo(15);
  }

  @Test
  void constructorEverything() {
    var struct = struct()
        .int32()
        .primitive().constant(5).lengthExpression(valueOf(0)).int32()
        .align(2)
        .build();

    assertThat(struct.getByteLength()).isEqualTo(INT32.size() + 2);
    Assertions.assertThat(struct.<Int32Type>getType(1))
        .isEqualTo(primitive().position(1).byteLengthListener(align(2)).constant(5).lengthExpression(valueOf(0)).int32());
  }

  @Test
  void copy() {
    var type = int32Type();
    var copy = type.copy(15);
    assertThat(copy.getPosition()).isEqualTo(15);
  }

  @Test
  void getByteLength() {
    var struct = struct()
        .int32()
        .build();

    assertThat(struct.getByteLength()).isEqualTo(INT32.size());
    assertThat(struct.getType(0).getByteLength(struct)).isEqualTo(INT32.size());
  }

  @Test
  void isFixedLength() {
    var struct = struct()
        .int32()
        .build();

    assertThat(struct.getType(0).isFixedLength(struct)).isTrue();
    assertThat(struct.isFixedLength()).isTrue();
  }

  @Test
  void isFixedLengthArray() {
    var struct = struct()
        .int32Array(constant(5))
        .build();

    assertThat(struct.getType(0).isFixedLength(struct)).isTrue();
    assertThat(struct.isFixedLength()).isTrue();
  }
}
