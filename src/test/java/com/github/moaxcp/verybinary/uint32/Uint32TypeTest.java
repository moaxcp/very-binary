package com.github.moaxcp.verybinary.uint32;

import com.github.moaxcp.verybinary.Uint32Type;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import static com.github.moaxcp.verybinary.Builders.struct;
import static com.github.moaxcp.verybinary.ByteLengthListener.align;
import static com.github.moaxcp.verybinary.Expression.constant;
import static com.github.moaxcp.verybinary.Expression.valueOf;
import static com.github.moaxcp.verybinary.Primitive.UINT32;
import static com.github.moaxcp.verybinary.PrimitiveBuilder.primitive;
import static org.assertj.core.api.Assertions.assertThat;

public class Uint32TypeTest {

  @Test
  void constructorEverything() {
    var struct = struct()
        .uint32()
        .primitive().constant(5L).lengthExpression(valueOf(0)).uint32()
        .align(2)
        .build();

    assertThat(struct.getByteLength()).isEqualTo(UINT32.size() + 2);
    Assertions.assertThat(struct.<Uint32Type>getType(1))
        .isEqualTo(primitive().position(1).byteLengthListener(align(2)).constant(5L).lengthExpression(valueOf(0)).uint32());
  }

  @Test
  void getByteLength() {
    var struct = struct()
        .uint32()
        .build();

    assertThat(struct.getByteLength()).isEqualTo(UINT32.size());
    assertThat(struct.getType(0).getByteLength(struct)).isEqualTo(UINT32.size());
  }

  @Test
  void isFixedLength() {
    var struct = struct()
        .uint32()
        .build();

    assertThat(struct.getType(0).isFixedLength(struct)).isTrue();
    assertThat(struct.isFixedLength()).isTrue();
  }

  @Test
  void isFixedLengthArray() {
    var struct = struct()
        .uint32Array(constant(5))
        .build();

    assertThat(struct.getType(0).isFixedLength(struct)).isTrue();
    assertThat(struct.isFixedLength()).isTrue();
  }
}
