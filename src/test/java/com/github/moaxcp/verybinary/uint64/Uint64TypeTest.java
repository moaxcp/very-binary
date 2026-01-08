package com.github.moaxcp.verybinary.uint64;

import com.github.moaxcp.verybinary.Uint64Type;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;

import static com.github.moaxcp.verybinary.Builders.struct;
import static com.github.moaxcp.verybinary.ByteLengthListener.align;
import static com.github.moaxcp.verybinary.Expression.constant;
import static com.github.moaxcp.verybinary.Expression.valueOf;
import static com.github.moaxcp.verybinary.Primitive.UINT64;
import static com.github.moaxcp.verybinary.PrimitiveBuilder.primitive;
import static com.github.moaxcp.verybinary.Uint64Type.uint64Type;
import static org.assertj.core.api.Assertions.assertThat;

public class Uint64TypeTest {

  @Test
  void constructor() {
    var type = uint64Type();
    assertThat(type.getPosition()).isEqualTo(-1);
  }

  @Test
  void constructorPosition() {
    var type = uint64Type(15);
    assertThat(type.getPosition()).isEqualTo(15);
  }

  @Test
  void constructorEverything() {
    var struct = struct()
        .uint64()
        .primitive().constant(BigInteger.valueOf(5)).lengthExpression(valueOf(0)).uint64()
        .align(2)
        .build();

    assertThat(struct.getByteLength()).isEqualTo(UINT64.size() + 2);
    Assertions.assertThat(struct.<Uint64Type>getType(1))
        .isEqualTo(primitive().position(1).byteLengthListener(align(2)).constant(BigInteger.valueOf(5)).lengthExpression(valueOf(0)).uint64());
  }

  @Test
  void copy() {
    var type = uint64Type();
    var copy = type.copy(15);
    assertThat(copy.getPosition()).isEqualTo(15);
  }

  @Test
  void getByteLength() {
    var struct = struct()
        .uint64()
        .build();

    assertThat(struct.getByteLength()).isEqualTo(UINT64.size());
    assertThat(struct.getType(0).getByteLength(struct)).isEqualTo(UINT64.size());
  }

  @Test
  void isFixedLength() {
    var struct = struct()
        .uint64()
        .build();

    assertThat(struct.getType(0).isFixedLength(struct)).isTrue();
    assertThat(struct.isFixedLength()).isTrue();
  }

  @Test
  void isFixedLengthArray() {
    var struct = struct()
        .uint64Array(constant(5))
        .build();

    assertThat(struct.getType(0).isFixedLength(struct)).isTrue();
    assertThat(struct.isFixedLength()).isTrue();
  }
}
