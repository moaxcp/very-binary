package com.github.moaxcp.verybinary.int64;

import com.github.moaxcp.verybinary.Int64Type;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import static com.github.moaxcp.verybinary.Builders.struct;
import static com.github.moaxcp.verybinary.ByteArray.ba;
import static com.github.moaxcp.verybinary.ByteLengthListener.align;
import static com.github.moaxcp.verybinary.Expression.constant;
import static com.github.moaxcp.verybinary.Expression.valueOf;
import static com.github.moaxcp.verybinary.Int64Type.int64Type;
import static com.github.moaxcp.verybinary.Primitive.INT64;
import static com.github.moaxcp.verybinary.PrimitiveBuilder.primitive;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class Int64TypeTest {

  @Test
  void constructor() {
    var type = int64Type();
    assertThat(type.getPosition()).isEqualTo(-1);
  }

  @Test
  void constructorPosition() {
    var type = int64Type(15);
    assertThat(type.getPosition()).isEqualTo(15);
  }

  @Test
  void constructorEverything() {
    var struct = struct()
        .int64()
        .primitive().constant(5L).lengthExpression(valueOf(0)).int64()
        .align(2)
        .build();

    assertThat(struct.getByteLength()).isEqualTo(INT64.size() + 2);
    Assertions.assertThat(struct.<Int64Type>getType(1))
        .isEqualTo(primitive().position(1).byteLengthListener(align(2)).constant(5L).lengthExpression(valueOf(0)).int64());
  }

  @Test
  void copy() {
    var type = int64Type();
    var copy = type.copy(15);
    assertThat(copy.getPosition()).isEqualTo(15);
  }

  @Test
  void getByteLength() {
    var struct = struct()
        .int64()
        .build();

    assertThat(struct.getByteLength()).isEqualTo(INT64.size());
    assertThat(struct.getType(0).getByteLength(struct)).isEqualTo(INT64.size());
  }

  @Test
  void isFixedLength() {
    var struct = struct()
        .int64()
        .build();

    assertThat(struct.getType(0).isFixedLength(struct)).isTrue();
    assertThat(struct.isFixedLength()).isTrue();
  }

  @Test
  void isFixedLengthArray() {
    var struct = struct()
        .int64Array(constant(5))
        .build();

    assertThat(struct.getType(0).isFixedLength(struct)).isTrue();
    assertThat(struct.isFixedLength()).isTrue();
  }
}
