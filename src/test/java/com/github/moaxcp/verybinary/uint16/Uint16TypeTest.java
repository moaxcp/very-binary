package com.github.moaxcp.verybinary.uint16;

import com.github.moaxcp.verybinary.Uint16Type;
import org.junit.jupiter.api.Test;

import static com.github.moaxcp.verybinary.Builders.struct;
import static com.github.moaxcp.verybinary.ByteLengthListener.align;
import static com.github.moaxcp.verybinary.Expression.constant;
import static com.github.moaxcp.verybinary.Expression.valueOf;
import static com.github.moaxcp.verybinary.Primitive.UINT16;
import static com.github.moaxcp.verybinary.PrimitiveBuilder.primitive;
import static org.assertj.core.api.Assertions.assertThat;

public class Uint16TypeTest {

  @Test
  void constructorEverything() {
    var struct = struct()
        .uint16()
        .primitive().constant(5).lengthExpression(valueOf(0)).uint16()
        .align(2)
        .build();

    assertThat(struct.getByteLength()).isEqualTo(UINT16.size() + 2);
    assertThat(struct.<Uint16Type>getType(1))
        .isEqualTo(primitive().position(1).byteLengthListener(align(2)).constant(5).lengthExpression(valueOf(0)).uint16());
  }

  @Test
  void getByteLength() {
    var struct = struct()
        .uint16()
        .build();

    assertThat(struct.getByteLength()).isEqualTo(UINT16.size());
    assertThat(struct.getType(0).getByteLength(struct)).isEqualTo(UINT16.size());
  }

  @Test
  void isFixedLength() {
    var struct = struct()
        .uint16()
        .build();

    assertThat(struct.getType(0).isFixedLength(struct)).isTrue();
    assertThat(struct.isFixedLength()).isTrue();
  }

  @Test
  void isFixedLengthArray() {
    var struct = struct()
        .primitive().lengthExpression(constant(5)).uint16()
        .uint16Array(constant(5))
        .build();

    assertThat(struct.getType(0).isFixedLength(struct)).isTrue();
    assertThat(struct.isFixedLength()).isTrue();
  }
}
