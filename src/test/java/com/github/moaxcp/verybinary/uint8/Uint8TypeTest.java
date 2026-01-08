package com.github.moaxcp.verybinary.uint8;

import com.github.moaxcp.verybinary.Uint8Type;
import org.junit.jupiter.api.Test;

import static com.github.moaxcp.verybinary.Builders.struct;
import static com.github.moaxcp.verybinary.ByteArray.ba;
import static com.github.moaxcp.verybinary.ByteLengthListener.align;
import static com.github.moaxcp.verybinary.Expression.constant;
import static com.github.moaxcp.verybinary.Expression.valueOf;
import static com.github.moaxcp.verybinary.Primitive.UINT8;
import static com.github.moaxcp.verybinary.PrimitiveBuilder.primitive;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class Uint8TypeTest {

  @Test
  void constructor() {
    var type = new Uint8Type(-1);
    assertThat(type.getPosition()).isEqualTo(-1);
  }

  @Test
  void constructorPosition() {
    var type = new Uint8Type(15);
    assertThat(type.getPosition()).isEqualTo(15);
  }

  @Test
  void constructorEverything() {
    var struct = struct()
        .uint8()
        .primitive().constant((short) 5).lengthExpression(valueOf(0)).uint8()
        .align(2)
        .build();

    assertThat(struct.getByteLength()).isEqualTo(UINT8.size() + 2);
    assertThat(struct.<Uint8Type>getType(1))
        .isEqualTo(primitive().position(1).byteLengthListener(align(2)).constant((short) 5).lengthExpression(valueOf(0)).uint8());
  }

  @Test
  void copy() {
    var type = new Uint8Type(-1);
    var copy = type.copy(15);
    assertThat(copy.getPosition()).isEqualTo(15);
  }

  @Test
  void getByteLength() {
    var struct = struct()
        .uint8()
        .build();

    assertThat(struct.getByteLength()).isEqualTo(UINT8.size());
    assertThat(struct.getType(0).getByteLength(struct)).isEqualTo(UINT8.size());
  }

  @Test
  void isFixedLength() {
    var struct = struct()
        .uint8()
        .build();

    assertThat(struct.getType(0).isFixedLength(struct)).isTrue();
    assertThat(struct.isFixedLength()).isTrue();
  }

  @Test
  void isFixedLengthArray() {
    var struct = struct()
        .uint8Array(constant(5))
        .build();

    assertThat(struct.getType(0).isFixedLength(struct)).isTrue();
    assertThat(struct.isFixedLength()).isTrue();
  }
}
