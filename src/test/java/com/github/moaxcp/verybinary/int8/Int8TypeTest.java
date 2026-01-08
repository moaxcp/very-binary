package com.github.moaxcp.verybinary.int8;

import com.github.moaxcp.verybinary.Int8Type;
import org.junit.jupiter.api.Test;

import static com.github.moaxcp.verybinary.Builders.struct;
import static com.github.moaxcp.verybinary.ByteArray.ba;
import static com.github.moaxcp.verybinary.ByteLengthListener.align;
import static com.github.moaxcp.verybinary.Expression.constant;
import static com.github.moaxcp.verybinary.Expression.valueOf;
import static com.github.moaxcp.verybinary.Int8Type.int8;
import static com.github.moaxcp.verybinary.Primitive.INT8;
import static com.github.moaxcp.verybinary.PrimitiveBuilder.primitive;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class Int8TypeTest {

  @Test
  void constructor() {
    var type = Int8Type.int8();
    assertThat(type.getPosition()).isEqualTo(-1);
  }

  @Test
  void constructorPosition() {
    var type = int8(15);
    assertThat(type.getPosition()).isEqualTo(15);
  }

  @Test
  void constructorEverything() {
    var struct = struct()
        .int8()
        .primitive().constant((byte) 5).lengthExpression(valueOf(0)).int8()
        .align(2)
        .build();

    assertThat(struct.getByteLength()).isEqualTo(INT8.size() + 2);
    assertThat(struct.<Int8Type>getType(1))
        .isEqualTo(primitive().position(1).byteLengthListener(align(2)).constant((byte) 5).lengthExpression(valueOf(0)).int8());
  }

  @Test
  void copy() {
    var type = Int8Type.int8();
    var copy = type.copy(15);
    assertThat(copy.getPosition()).isEqualTo(15);
  }

  @Test
  void getByteLength() {
    var struct = struct()
        .int8()
        .build();

    assertThat(struct.getByteLength()).isEqualTo(INT8.size());
    assertThat(struct.getType(0).getByteLength(struct)).isEqualTo(INT8.size());
  }

  @Test
  void isFixedLength() {
    var struct = struct()
        .int8()
        .build();

    assertThat(struct.getType(0).isFixedLength(struct)).isTrue();
    assertThat(struct.isFixedLength()).isTrue();
  }

  @Test
  void isFixedLengthArray() {
    var struct = struct()
        .int8Array(constant(5))
        .build();

    assertThat(struct.getType(0).isFixedLength(struct)).isTrue();
    assertThat(struct.isFixedLength()).isTrue();
  }
}
