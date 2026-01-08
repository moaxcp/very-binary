package com.github.moaxcp.verybinary.float32;

import com.github.moaxcp.verybinary.Float32Type;
import org.junit.jupiter.api.Test;

import static com.github.moaxcp.verybinary.Builders.struct;
import static com.github.moaxcp.verybinary.ByteArray.ba;
import static com.github.moaxcp.verybinary.ByteLengthListener.align;
import static com.github.moaxcp.verybinary.Expression.constant;
import static com.github.moaxcp.verybinary.Expression.valueOf;
import static com.github.moaxcp.verybinary.Float32Type.float32Type;
import static com.github.moaxcp.verybinary.Primitive.FLOAT32;
import static com.github.moaxcp.verybinary.PrimitiveBuilder.primitive;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class Float32TypeTest {

  @Test
  void constructor() {
    var type = float32Type();
    assertThat(type.getPosition()).isEqualTo(-1);
  }

  @Test
  void constructorPosition() {
    var type = float32Type(15);
    assertThat(type.getPosition()).isEqualTo(15);
  }

  @Test
  void constructorEverything() {
    var struct = struct()
        .float32()
        .primitive().constant(3.0f).lengthExpression(valueOf(0)).float32()
        .align(2)
        .build();

    assertThat(struct.getByteLength()).isEqualTo(FLOAT32.size() + 2);
    assertThat(struct.<Float32Type>getType(1)).isEqualTo(primitive().position(1).byteLengthListener(align(2)).constant(3.0f).lengthExpression(valueOf(0)).float32());
  }

  @Test
  void copy() {
    var type = float32Type();
    var copy = type.copy(15);
    assertThat(copy.getPosition()).isEqualTo(15);
  }

  @Test
  void getByteLength() {
    var struct = struct()
        .float32()
        .build();

    assertThat(struct.getByteLength()).isEqualTo(FLOAT32.size());
    assertThat(struct.getType(0).getByteLength(struct)).isEqualTo(FLOAT32.size());
  }

  @Test
  void isFixedLength() {
    var struct = struct()
        .float32()
        .build();

    assertThat(struct.getType(0).isFixedLength(struct)).isTrue();
    assertThat(struct.isFixedLength()).isTrue();
  }

  @Test
  void isFixedLengthArray() {
    var struct = struct()
        .float32Array(constant(3))
        .build();

    assertThat(struct.getType(0).isFixedLength(struct)).isTrue();
    assertThat(struct.isFixedLength()).isTrue();
  }
}
