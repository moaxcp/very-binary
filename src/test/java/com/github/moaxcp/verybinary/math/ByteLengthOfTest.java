package com.github.moaxcp.verybinary.math;

import org.junit.jupiter.api.Test;

import static com.github.moaxcp.verybinary.Builders.struct;
import static com.github.moaxcp.verybinary.math.ByteLengthOf.byteLengthOf;
import static com.github.moaxcp.verybinary.math.Int8Value.int8Value;
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
        .boolList(int8Value(10))
        .build();

    var expression = byteLengthOf(0);

    assertThat(expression.isConstant(struct.getType())).isTrue();
    assertThat(expression.constantValue(struct.getType()).toInt()).isEqualTo(10);
    assertThat(expression.defaultValue(struct.getType()).toInt()).isEqualTo(10);
    assertThat(expression.evaluate(struct).toInt()).isEqualTo(10);
  }

  @Test
  void boolTest() {
    var struct = struct()
        .int8()
        .boolList(0)
        .build();
    struct.setInt8(0, 25);

    var expression = byteLengthOf(1);

    assertThat(expression.isConstant(struct.getType())).isFalse();
    assertThat(expression.constantValue(struct.getType()).toInt()).isEqualTo(0);
    assertThat(expression.defaultValue(struct.getType()).toInt()).isEqualTo(0);
    assertThat(expression.evaluate(struct).toInt()).isEqualTo(25);
  }

  @Test
  void int8ConstantTest() {
    var struct = struct()
        .int8List(int8Value(10))
        .build();

    var expression = byteLengthOf(0);

    assertThat(expression.isConstant(struct.getType())).isTrue();
    assertThat(expression.constantValue(struct.getType()).toInt()).isEqualTo(10);
    assertThat(expression.defaultValue(struct.getType()).toInt()).isEqualTo(10);
    assertThat(expression.evaluate(struct).toInt()).isEqualTo(10);
  }

  @Test
  void int8Test() {
    var struct = struct()
        .int8()
        .int8List(0)
        .build();
    struct.setInt8(0, 25);

    var expression = byteLengthOf(1);

    assertThat(expression.isConstant(struct.getType())).isFalse();
    assertThat(expression.constantValue(struct.getType()).toInt()).isEqualTo(0);
    assertThat(expression.defaultValue(struct.getType()).toInt()).isEqualTo(0);
    assertThat(expression.evaluate(struct).toInt()).isEqualTo(25);
  }

  @Test
  void int16ConstantTest() {
    var struct = struct()
        .int16List(int8Value(10))
        .build();

    var expression = byteLengthOf(0);

    assertThat(expression.isConstant(struct.getType())).isTrue();
    assertThat(expression.constantValue(struct.getType()).toInt()).isEqualTo(20);
    assertThat(expression.defaultValue(struct.getType()).toInt()).isEqualTo(20);
    assertThat(expression.evaluate(struct).toInt()).isEqualTo(20);
  }

  @Test
  void int16Test() {
    var struct = struct()
        .int8()
        .int16List(0)
        .build();
    struct.setInt8(0, 25);

    var expression = byteLengthOf(1);

    assertThat(expression.isConstant(struct.getType())).isFalse();
    assertThat(expression.constantValue(struct.getType()).toInt()).isEqualTo(0);
    assertThat(expression.defaultValue(struct.getType()).toInt()).isEqualTo(0);
    assertThat(expression.evaluate(struct).toInt()).isEqualTo(50);
  }

  @Test
  void int32ConstantTest() {
    var struct = struct()
        .int32List(int8Value(10))
        .build();

    var expression = byteLengthOf(0);

    assertThat(expression.isConstant(struct.getType())).isTrue();
    assertThat(expression.constantValue(struct.getType()).toInt()).isEqualTo(40);
    assertThat(expression.defaultValue(struct.getType()).toInt()).isEqualTo(40);
    assertThat(expression.evaluate(struct).toInt()).isEqualTo(40);
  }

  @Test
  void int32Test() {
    var struct = struct()
        .int8()
        .int32List(0)
        .build();
    struct.setInt8(0, 25);

    var expression = byteLengthOf(1);

    assertThat(expression.isConstant(struct.getType())).isFalse();
    assertThat(expression.constantValue(struct.getType()).toInt()).isEqualTo(0);
    assertThat(expression.defaultValue(struct.getType()).toInt()).isEqualTo(0);
    assertThat(expression.evaluate(struct).toInt()).isEqualTo(100);
  }

  @Test
  void int64ConstantTest() {
    var struct = struct()
        .int64List(int8Value(10))
        .build();

    var expression = byteLengthOf(0);

    assertThat(expression.isConstant(struct.getType())).isTrue();
    assertThat(expression.constantValue(struct.getType()).toInt()).isEqualTo(80);
    assertThat(expression.defaultValue(struct.getType()).toInt()).isEqualTo(80);
    assertThat(expression.evaluate(struct).toInt()).isEqualTo(80);
  }

  @Test
  void int64Test() {
    var struct = struct()
        .int8()
        .int64List(0)
        .build();
    struct.setInt8(0, 25);

    var expression = byteLengthOf(1);

    assertThat(expression.isConstant(struct.getType())).isFalse();
    assertThat(expression.constantValue(struct.getType()).toInt()).isEqualTo(0);
    assertThat(expression.defaultValue(struct.getType()).toInt()).isEqualTo(0);
    assertThat(expression.evaluate(struct).toInt()).isEqualTo(200);
  }

  @Test
  void uint8ConstantTest() {
    var struct = struct()
        .uint8List(int8Value(10))
        .build();

    var expression = byteLengthOf(0);

    assertThat(expression.isConstant(struct.getType())).isTrue();
    assertThat(expression.constantValue(struct.getType()).toInt()).isEqualTo(10);
    assertThat(expression.defaultValue(struct.getType()).toInt()).isEqualTo(10);
    assertThat(expression.evaluate(struct).toInt()).isEqualTo(10);
  }

  @Test
  void uint8Test() {
    var struct = struct()
        .int8()
        .uint8List(0)
        .build();
    struct.setInt8(0, 25);

    var expression = byteLengthOf(1);

    assertThat(expression.isConstant(struct.getType())).isFalse();
    assertThat(expression.constantValue(struct.getType()).toInt()).isEqualTo(0);
    assertThat(expression.defaultValue(struct.getType()).toInt()).isEqualTo(0);
    assertThat(expression.evaluate(struct).toInt()).isEqualTo(25);
  }

  @Test
  void uint16ConstantTest() {
    var struct = struct()
        .uint16List(int8Value(10))
        .build();

    var expression = byteLengthOf(0);

    assertThat(expression.isConstant(struct.getType())).isTrue();
    assertThat(expression.constantValue(struct.getType()).toInt()).isEqualTo(20);
    assertThat(expression.defaultValue(struct.getType()).toInt()).isEqualTo(20);
    assertThat(expression.evaluate(struct).toInt()).isEqualTo(20);
  }

  @Test
  void uint16Test() {
    var struct = struct()
        .int8()
        .uint16List(0)
        .build();
    struct.setInt8(0, 25);

    var expression = byteLengthOf(1);

    assertThat(expression.isConstant(struct.getType())).isFalse();
    assertThat(expression.constantValue(struct.getType()).toInt()).isEqualTo(0);
    assertThat(expression.defaultValue(struct.getType()).toInt()).isEqualTo(0);
    assertThat(expression.evaluate(struct).toInt()).isEqualTo(50);
  }

  @Test
  void uint32ConstantTest() {
    var struct = struct()
        .uint32List(int8Value(10))
        .build();

    var expression = byteLengthOf(0);

    assertThat(expression.isConstant(struct.getType())).isTrue();
    assertThat(expression.constantValue(struct.getType()).toInt()).isEqualTo(40);
    assertThat(expression.defaultValue(struct.getType()).toInt()).isEqualTo(40);
    assertThat(expression.evaluate(struct).toInt()).isEqualTo(40);
  }

  @Test
  void uint32Test() {
    var struct = struct()
        .int8()
        .uint32List(0)
        .build();
    struct.setInt8(0, 25);

    var expression = byteLengthOf(1);

    assertThat(expression.isConstant(struct.getType())).isFalse();
    assertThat(expression.constantValue(struct.getType()).toInt()).isEqualTo(0);
    assertThat(expression.defaultValue(struct.getType()).toInt()).isEqualTo(0);
    assertThat(expression.evaluate(struct).toInt()).isEqualTo(100);
  }

  @Test
  void uint64ConstantTest() {
    var struct = struct()
        .uint64List(int8Value(10))
        .build();

    var expression = byteLengthOf(0);

    assertThat(expression.isConstant(struct.getType())).isTrue();
    assertThat(expression.constantValue(struct.getType()).toInt()).isEqualTo(80);
    assertThat(expression.defaultValue(struct.getType()).toInt()).isEqualTo(80);
    assertThat(expression.evaluate(struct).toInt()).isEqualTo(80);
  }

  @Test
  void uint64Test() {
    var struct = struct()
        .int8()
        .uint64List(0)
        .build();
    struct.setInt8(0, 25);

    var expression = byteLengthOf(1);

    assertThat(expression.isConstant(struct.getType())).isFalse();
    assertThat(expression.constantValue(struct.getType()).toInt()).isEqualTo(0);
    assertThat(expression.defaultValue(struct.getType()).toInt()).isEqualTo(0);
    assertThat(expression.evaluate(struct).toInt()).isEqualTo(200);
  }

  @Test
  void float32ConstantTest() {
    var struct = struct()
        .float32List(int8Value(10))
        .build();

    var expression = byteLengthOf(0);

    assertThat(expression.isConstant(struct.getType())).isTrue();
    assertThat(expression.constantValue(struct.getType()).toInt()).isEqualTo(40);
    assertThat(expression.defaultValue(struct.getType()).toInt()).isEqualTo(40);
    assertThat(expression.evaluate(struct).toInt()).isEqualTo(40);
  }

  @Test
  void float32Test() {
    var struct = struct()
        .int8()
        .float32List(0)
        .build();
    struct.setInt8(0, 25);

    var expression = byteLengthOf(1);

    assertThat(expression.isConstant(struct.getType())).isFalse();
    assertThat(expression.constantValue(struct.getType()).toInt()).isEqualTo(0);
    assertThat(expression.defaultValue(struct.getType()).toInt()).isEqualTo(0);
    assertThat(expression.evaluate(struct).toInt()).isEqualTo(100);
  }

  @Test
  void float64ConstantTest() {
    var struct = struct()
        .float64List(int8Value(10))
        .build();

    var expression = byteLengthOf(0);

    assertThat(expression.isConstant(struct.getType())).isTrue();
    assertThat(expression.constantValue(struct.getType()).toInt()).isEqualTo(80);
    assertThat(expression.defaultValue(struct.getType()).toInt()).isEqualTo(80);
    assertThat(expression.evaluate(struct).toInt()).isEqualTo(80);
  }

  @Test
  void float64Test() {
    var struct = struct()
        .int8()
        .float64List(0)
        .build();
    struct.setInt8(0, 25);

    var expression = byteLengthOf(1);

    assertThat(expression.isConstant(struct.getType())).isFalse();
    assertThat(expression.constantValue(struct.getType()).toInt()).isEqualTo(0);
    assertThat(expression.defaultValue(struct.getType()).toInt()).isEqualTo(0);
    assertThat(expression.evaluate(struct).toInt()).isEqualTo(200);
  }
}
