package com.github.moaxcp.verybinary.math;

import org.junit.jupiter.api.Test;

import static com.github.moaxcp.verybinary.Builders.struct;
import static com.github.moaxcp.verybinary.math.Constant.constant;
import static com.github.moaxcp.verybinary.math.ByteLengthOf.byteLengthOf;
import static org.assertj.core.api.Assertions.assertThat;

public class ByteLengthOfTest {

  @Test
  void equal() {
    var expression1 = byteLengthOf(0);
    var expression2 = byteLengthOf(0);
    assertThat(expression1).isEqualTo(expression2);
  }

  @Test
  void boolConstantTest() {
    var struct = struct()
        .boolArray(constant(10))
        .build();

    var expression = byteLengthOf(0);

    assertThat(expression.isConstant(struct.getType())).isTrue();
    assertThat(expression.constantValue(struct.getType())).isEqualTo(10);
    assertThat(expression.defaultValue(struct.getType())).isEqualTo(10);
    assertThat(expression.evaluate(struct)).isEqualTo(10);
  }

  @Test
  void boolTest() {
    var struct = struct()
        .int8()
        .boolArray(0)
        .build();
    struct.setInt8(0, 25);

    var expression = byteLengthOf(1);

    assertThat(expression.isConstant(struct.getType())).isFalse();
    assertThat(expression.constantValue(struct.getType())).isEqualTo(0);
    assertThat(expression.defaultValue(struct.getType())).isEqualTo(0);
    assertThat(expression.evaluate(struct)).isEqualTo(25);
  }

  @Test
  void int8ConstantTest() {
    var struct = struct()
        .int8Array(constant(10))
        .build();

    var expression = byteLengthOf(0);

    assertThat(expression.isConstant(struct.getType())).isTrue();
    assertThat(expression.constantValue(struct.getType())).isEqualTo(10);
    assertThat(expression.defaultValue(struct.getType())).isEqualTo(10);
    assertThat(expression.evaluate(struct)).isEqualTo(10);
  }

  @Test
  void int8Test() {
    var struct = struct()
        .int8()
        .int8Array(0)
        .build();
    struct.setInt8(0, 25);

    var expression = byteLengthOf(1);

    assertThat(expression.isConstant(struct.getType())).isFalse();
    assertThat(expression.constantValue(struct.getType())).isEqualTo(0);
    assertThat(expression.defaultValue(struct.getType())).isEqualTo(0);
    assertThat(expression.evaluate(struct)).isEqualTo(25);
  }

  @Test
  void int16ConstantTest() {
    var struct = struct()
        .int16Array(constant(10))
        .build();

    var expression = byteLengthOf(0);

    assertThat(expression.isConstant(struct.getType())).isTrue();
    assertThat(expression.constantValue(struct.getType())).isEqualTo(20);
    assertThat(expression.defaultValue(struct.getType())).isEqualTo(20);
    assertThat(expression.evaluate(struct)).isEqualTo(20);
  }

  @Test
  void int16Test() {
    var struct = struct()
        .int8()
        .int16Array(0)
        .build();
    struct.setInt8(0, 25);

    var expression = byteLengthOf(1);

    assertThat(expression.isConstant(struct.getType())).isFalse();
    assertThat(expression.constantValue(struct.getType())).isEqualTo(0);
    assertThat(expression.defaultValue(struct.getType())).isEqualTo(0);
    assertThat(expression.evaluate(struct)).isEqualTo(50);
  }

  @Test
  void int32ConstantTest() {
    var struct = struct()
        .int32Array(constant(10))
        .build();

    var expression = byteLengthOf(0);

    assertThat(expression.isConstant(struct.getType())).isTrue();
    assertThat(expression.constantValue(struct.getType())).isEqualTo(40);
    assertThat(expression.defaultValue(struct.getType())).isEqualTo(40);
    assertThat(expression.evaluate(struct)).isEqualTo(40);
  }

  @Test
  void int32Test() {
    var struct = struct()
        .int8()
        .int32Array(0)
        .build();
    struct.setInt8(0, 25);

    var expression = byteLengthOf(1);

    assertThat(expression.isConstant(struct.getType())).isFalse();
    assertThat(expression.constantValue(struct.getType())).isEqualTo(0);
    assertThat(expression.defaultValue(struct.getType())).isEqualTo(0);
    assertThat(expression.evaluate(struct)).isEqualTo(100);
  }

  @Test
  void int64ConstantTest() {
    var struct = struct()
        .int64Array(constant(10))
        .build();

    var expression = byteLengthOf(0);

    assertThat(expression.isConstant(struct.getType())).isTrue();
    assertThat(expression.constantValue(struct.getType())).isEqualTo(80);
    assertThat(expression.defaultValue(struct.getType())).isEqualTo(80);
    assertThat(expression.evaluate(struct)).isEqualTo(80);
  }

  @Test
  void int64Test() {
    var struct = struct()
        .int8()
        .int64Array(0)
        .build();
    struct.setInt8(0, 25);

    var expression = byteLengthOf(1);

    assertThat(expression.isConstant(struct.getType())).isFalse();
    assertThat(expression.constantValue(struct.getType())).isEqualTo(0);
    assertThat(expression.defaultValue(struct.getType())).isEqualTo(0);
    assertThat(expression.evaluate(struct)).isEqualTo(200);
  }

  @Test
  void uint8ConstantTest() {
    var struct = struct()
        .uint8Array(constant(10))
        .build();

    var expression = byteLengthOf(0);

    assertThat(expression.isConstant(struct.getType())).isTrue();
    assertThat(expression.constantValue(struct.getType())).isEqualTo(10);
    assertThat(expression.defaultValue(struct.getType())).isEqualTo(10);
    assertThat(expression.evaluate(struct)).isEqualTo(10);
  }

  @Test
  void uint8Test() {
    var struct = struct()
        .int8()
        .uint8Array(0)
        .build();
    struct.setInt8(0, 25);

    var expression = byteLengthOf(1);

    assertThat(expression.isConstant(struct.getType())).isFalse();
    assertThat(expression.constantValue(struct.getType())).isEqualTo(0);
    assertThat(expression.defaultValue(struct.getType())).isEqualTo(0);
    assertThat(expression.evaluate(struct)).isEqualTo(25);
  }

  @Test
  void uint16ConstantTest() {
    var struct = struct()
        .uint16Array(constant(10))
        .build();

    var expression = byteLengthOf(0);

    assertThat(expression.isConstant(struct.getType())).isTrue();
    assertThat(expression.constantValue(struct.getType())).isEqualTo(20);
    assertThat(expression.defaultValue(struct.getType())).isEqualTo(20);
    assertThat(expression.evaluate(struct)).isEqualTo(20);
  }

  @Test
  void uint16Test() {
    var struct = struct()
        .int8()
        .uint16Array(0)
        .build();
    struct.setInt8(0, 25);

    var expression = byteLengthOf(1);

    assertThat(expression.isConstant(struct.getType())).isFalse();
    assertThat(expression.constantValue(struct.getType())).isEqualTo(0);
    assertThat(expression.defaultValue(struct.getType())).isEqualTo(0);
    assertThat(expression.evaluate(struct)).isEqualTo(50);
  }

  @Test
  void uint32ConstantTest() {
    var struct = struct()
        .uint32Array(constant(10))
        .build();

    var expression = byteLengthOf(0);

    assertThat(expression.isConstant(struct.getType())).isTrue();
    assertThat(expression.constantValue(struct.getType())).isEqualTo(40);
    assertThat(expression.defaultValue(struct.getType())).isEqualTo(40);
    assertThat(expression.evaluate(struct)).isEqualTo(40);
  }

  @Test
  void uint32Test() {
    var struct = struct()
        .int8()
        .uint32Array(0)
        .build();
    struct.setInt8(0, 25);

    var expression = byteLengthOf(1);

    assertThat(expression.isConstant(struct.getType())).isFalse();
    assertThat(expression.constantValue(struct.getType())).isEqualTo(0);
    assertThat(expression.defaultValue(struct.getType())).isEqualTo(0);
    assertThat(expression.evaluate(struct)).isEqualTo(100);
  }

  @Test
  void uint64ConstantTest() {
    var struct = struct()
        .uint64Array(constant(10))
        .build();

    var expression = byteLengthOf(0);

    assertThat(expression.isConstant(struct.getType())).isTrue();
    assertThat(expression.constantValue(struct.getType())).isEqualTo(80);
    assertThat(expression.defaultValue(struct.getType())).isEqualTo(80);
    assertThat(expression.evaluate(struct)).isEqualTo(80);
  }

  @Test
  void uint64Test() {
    var struct = struct()
        .int8()
        .uint64Array(0)
        .build();
    struct.setInt8(0, 25);

    var expression = byteLengthOf(1);

    assertThat(expression.isConstant(struct.getType())).isFalse();
    assertThat(expression.constantValue(struct.getType())).isEqualTo(0);
    assertThat(expression.defaultValue(struct.getType())).isEqualTo(0);
    assertThat(expression.evaluate(struct)).isEqualTo(200);
  }

  @Test
  void float32ConstantTest() {
    var struct = struct()
        .float32Array(constant(10))
        .build();

    var expression = byteLengthOf(0);

    assertThat(expression.isConstant(struct.getType())).isTrue();
    assertThat(expression.constantValue(struct.getType())).isEqualTo(40);
    assertThat(expression.defaultValue(struct.getType())).isEqualTo(40);
    assertThat(expression.evaluate(struct)).isEqualTo(40);
  }

  @Test
  void float32Test() {
    var struct = struct()
        .int8()
        .float32Array(0)
        .build();
    struct.setInt8(0, 25);

    var expression = byteLengthOf(1);

    assertThat(expression.isConstant(struct.getType())).isFalse();
    assertThat(expression.constantValue(struct.getType())).isEqualTo(0);
    assertThat(expression.defaultValue(struct.getType())).isEqualTo(0);
    assertThat(expression.evaluate(struct)).isEqualTo(100);
  }

  @Test
  void float64ConstantTest() {
    var struct = struct()
        .float64Array(constant(10))
        .build();

    var expression = byteLengthOf(0);

    assertThat(expression.isConstant(struct.getType())).isTrue();
    assertThat(expression.constantValue(struct.getType())).isEqualTo(80);
    assertThat(expression.defaultValue(struct.getType())).isEqualTo(80);
    assertThat(expression.evaluate(struct)).isEqualTo(80);
  }

  @Test
  void float64Test() {
    var struct = struct()
        .int8()
        .float64Array(0)
        .build();
    struct.setInt8(0, 25);

    var expression = byteLengthOf(1);

    assertThat(expression.isConstant(struct.getType())).isFalse();
    assertThat(expression.constantValue(struct.getType())).isEqualTo(0);
    assertThat(expression.defaultValue(struct.getType())).isEqualTo(0);
    assertThat(expression.evaluate(struct)).isEqualTo(200);
  }
}
