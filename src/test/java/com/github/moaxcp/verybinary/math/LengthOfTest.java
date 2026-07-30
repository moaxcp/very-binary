package com.github.moaxcp.verybinary.math;

import org.junit.jupiter.api.Test;

import static com.github.moaxcp.verybinary.Builders.struct;
import static com.github.moaxcp.verybinary.math.Constant.constant;
import static com.github.moaxcp.verybinary.math.LengthOf.lengthOf;
import static org.assertj.core.api.Assertions.assertThat;

public class LengthOfTest {

  @Test
  void equal() {
    var expression1 = lengthOf(0);
    var expression2 = lengthOf(0);
    assertThat(expression1).isEqualTo(expression2);
  }

  @Test
  void boolConstantTest() {
    var struct = struct()
        .boolList(constant(10))
        .build();

    var expression = lengthOf(0);

    assertThat(expression.isConstant(struct.getType())).isTrue();
    assertThat(expression.constantValue(struct.getType())).isEqualTo(10);
    assertThat(expression.defaultValue(struct.getType())).isEqualTo(10);
    assertThat(expression.evaluate(struct)).isEqualTo(10);
  }

  @Test
  void boolTest() {
    var struct = struct()
        .int8()
        .boolList(0)
        .build();
    struct.setInt8(0, 25);

    var expression = lengthOf(1);

    assertThat(expression.isConstant(struct.getType())).isFalse();
    assertThat(expression.constantValue(struct.getType())).isEqualTo(0);
    assertThat(expression.defaultValue(struct.getType())).isEqualTo(0);
    assertThat(expression.evaluate(struct)).isEqualTo(25);
  }

  @Test
  void int8ConstantTest() {
    var struct = struct()
        .int8List(constant(10))
        .build();

    var expression = lengthOf(0);

    assertThat(expression.isConstant(struct.getType())).isTrue();
    assertThat(expression.constantValue(struct.getType())).isEqualTo(10);
    assertThat(expression.defaultValue(struct.getType())).isEqualTo(10);
    assertThat(expression.evaluate(struct)).isEqualTo(10);
  }

  @Test
  void int8Test() {
    var struct = struct()
        .int8()
        .int8List(0)
        .build();
    struct.setInt8(0, 25);

    var expression = lengthOf(1);

    assertThat(expression.isConstant(struct.getType())).isFalse();
    assertThat(expression.constantValue(struct.getType())).isEqualTo(0);
    assertThat(expression.defaultValue(struct.getType())).isEqualTo(0);
    assertThat(expression.evaluate(struct)).isEqualTo(25);
  }

  @Test
  void int16ConstantTest() {
    var struct = struct()
        .int16List(constant(10))
        .build();

    var expression = lengthOf(0);

    assertThat(expression.isConstant(struct.getType())).isTrue();
    assertThat(expression.constantValue(struct.getType())).isEqualTo(10);
    assertThat(expression.defaultValue(struct.getType())).isEqualTo(10);
    assertThat(expression.evaluate(struct)).isEqualTo(10);
  }

  @Test
  void int16Test() {
    var struct = struct()
        .int8()
        .int16List(0)
        .build();
    struct.setInt8(0, 25);

    var expression = lengthOf(1);

    assertThat(expression.isConstant(struct.getType())).isFalse();
    assertThat(expression.constantValue(struct.getType())).isEqualTo(0);
    assertThat(expression.defaultValue(struct.getType())).isEqualTo(0);
    assertThat(expression.evaluate(struct)).isEqualTo(25);
  }

  @Test
  void int32ConstantTest() {
    var struct = struct()
        .int32List(constant(10))
        .build();

    var expression = lengthOf(0);

    assertThat(expression.isConstant(struct.getType())).isTrue();
    assertThat(expression.constantValue(struct.getType())).isEqualTo(10);
    assertThat(expression.defaultValue(struct.getType())).isEqualTo(10);
    assertThat(expression.evaluate(struct)).isEqualTo(10);
  }

  @Test
  void int32Test() {
    var struct = struct()
        .int8()
        .int32List(0)
        .build();
    struct.setInt8(0, 25);

    var expression = lengthOf(1);

    assertThat(expression.isConstant(struct.getType())).isFalse();
    assertThat(expression.constantValue(struct.getType())).isEqualTo(0);
    assertThat(expression.defaultValue(struct.getType())).isEqualTo(0);
    assertThat(expression.evaluate(struct)).isEqualTo(25);
  }

  @Test
  void int64ConstantTest() {
    var struct = struct()
        .int64List(constant(10))
        .build();

    var expression = lengthOf(0);

    assertThat(expression.isConstant(struct.getType())).isTrue();
    assertThat(expression.constantValue(struct.getType())).isEqualTo(10);
    assertThat(expression.defaultValue(struct.getType())).isEqualTo(10);
    assertThat(expression.evaluate(struct)).isEqualTo(10);
  }

  @Test
  void int64Test() {
    var struct = struct()
        .int8()
        .int64List(0)
        .build();
    struct.setInt8(0, 25);

    var expression = lengthOf(1);

    assertThat(expression.isConstant(struct.getType())).isFalse();
    assertThat(expression.constantValue(struct.getType())).isEqualTo(0);
    assertThat(expression.defaultValue(struct.getType())).isEqualTo(0);
    assertThat(expression.evaluate(struct)).isEqualTo(25);
  }

  @Test
  void uint8ConstantTest() {
    var struct = struct()
        .uint8List(constant(10))
        .build();

    var expression = lengthOf(0);

    assertThat(expression.isConstant(struct.getType())).isTrue();
    assertThat(expression.constantValue(struct.getType())).isEqualTo(10);
    assertThat(expression.defaultValue(struct.getType())).isEqualTo(10);
    assertThat(expression.evaluate(struct)).isEqualTo(10);
  }

  @Test
  void uint8Test() {
    var struct = struct()
        .int8()
        .uint8List(0)
        .build();
    struct.setInt8(0, 25);

    var expression = lengthOf(1);

    assertThat(expression.isConstant(struct.getType())).isFalse();
    assertThat(expression.constantValue(struct.getType())).isEqualTo(0);
    assertThat(expression.defaultValue(struct.getType())).isEqualTo(0);
    assertThat(expression.evaluate(struct)).isEqualTo(25);
  }

  @Test
  void uint16ConstantTest() {
    var struct = struct()
        .uint16List(constant(10))
        .build();

    var expression = lengthOf(0);

    assertThat(expression.isConstant(struct.getType())).isTrue();
    assertThat(expression.constantValue(struct.getType())).isEqualTo(10);
    assertThat(expression.defaultValue(struct.getType())).isEqualTo(10);
    assertThat(expression.evaluate(struct)).isEqualTo(10);
  }

  @Test
  void uint16Test() {
    var struct = struct()
        .int8()
        .uint16List(0)
        .build();
    struct.setInt8(0, 25);

    var expression = lengthOf(1);

    assertThat(expression.isConstant(struct.getType())).isFalse();
    assertThat(expression.constantValue(struct.getType())).isEqualTo(0);
    assertThat(expression.defaultValue(struct.getType())).isEqualTo(0);
    assertThat(expression.evaluate(struct)).isEqualTo(25);
  }

  @Test
  void uint32ConstantTest() {
    var struct = struct()
        .uint32List(constant(10))
        .build();

    var expression = lengthOf(0);

    assertThat(expression.isConstant(struct.getType())).isTrue();
    assertThat(expression.constantValue(struct.getType())).isEqualTo(10);
    assertThat(expression.defaultValue(struct.getType())).isEqualTo(10);
    assertThat(expression.evaluate(struct)).isEqualTo(10);
  }

  @Test
  void uint32Test() {
    var struct = struct()
        .int8()
        .uint32List(0)
        .build();
    struct.setInt8(0, 25);

    var expression = lengthOf(1);

    assertThat(expression.isConstant(struct.getType())).isFalse();
    assertThat(expression.constantValue(struct.getType())).isEqualTo(0);
    assertThat(expression.defaultValue(struct.getType())).isEqualTo(0);
    assertThat(expression.evaluate(struct)).isEqualTo(25);
  }

  @Test
  void uint64ConstantTest() {
    var struct = struct()
        .uint64List(constant(10))
        .build();

    var expression = lengthOf(0);

    assertThat(expression.isConstant(struct.getType())).isTrue();
    assertThat(expression.constantValue(struct.getType())).isEqualTo(10);
    assertThat(expression.defaultValue(struct.getType())).isEqualTo(10);
    assertThat(expression.evaluate(struct)).isEqualTo(10);
  }

  @Test
  void uint64Test() {
    var struct = struct()
        .int8()
        .uint64List(0)
        .build();
    struct.setInt8(0, 25);

    var expression = lengthOf(1);

    assertThat(expression.isConstant(struct.getType())).isFalse();
    assertThat(expression.constantValue(struct.getType())).isEqualTo(0);
    assertThat(expression.defaultValue(struct.getType())).isEqualTo(0);
    assertThat(expression.evaluate(struct)).isEqualTo(25);
  }

  @Test
  void float32ConstantTest() {
    var struct = struct()
        .float32List(constant(10))
        .build();

    var expression = lengthOf(0);

    assertThat(expression.isConstant(struct.getType())).isTrue();
    assertThat(expression.constantValue(struct.getType())).isEqualTo(10);
    assertThat(expression.defaultValue(struct.getType())).isEqualTo(10);
    assertThat(expression.evaluate(struct)).isEqualTo(10);
  }

  @Test
  void float32Test() {
    var struct = struct()
        .int8()
        .float32List(0)
        .build();
    struct.setInt8(0, 25);

    var expression = lengthOf(1);

    assertThat(expression.isConstant(struct.getType())).isFalse();
    assertThat(expression.constantValue(struct.getType())).isEqualTo(0);
    assertThat(expression.defaultValue(struct.getType())).isEqualTo(0);
    assertThat(expression.evaluate(struct)).isEqualTo(25);
  }

  @Test
  void float64ConstantTest() {
    var struct = struct()
        .float64List(constant(10))
        .build();

    var expression = lengthOf(0);

    assertThat(expression.isConstant(struct.getType())).isTrue();
    assertThat(expression.constantValue(struct.getType())).isEqualTo(10);
    assertThat(expression.defaultValue(struct.getType())).isEqualTo(10);
    assertThat(expression.evaluate(struct)).isEqualTo(10);
  }

  @Test
  void float64Test() {
    var struct = struct()
        .int8()
        .float64List(0)
        .build();
    struct.setInt8(0, 25);

    var expression = lengthOf(1);

    assertThat(expression.isConstant(struct.getType())).isFalse();
    assertThat(expression.constantValue(struct.getType())).isEqualTo(0);
    assertThat(expression.defaultValue(struct.getType())).isEqualTo(0);
    assertThat(expression.evaluate(struct)).isEqualTo(25);
  }
}
