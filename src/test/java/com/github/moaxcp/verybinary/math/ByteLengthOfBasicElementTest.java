package com.github.moaxcp.verybinary.math;

import org.junit.jupiter.api.Test;
import static com.github.moaxcp.verybinary.Builders.struct;
import static com.github.moaxcp.verybinary.math.ByteLengthOfBasicElement.lengthOfBasicElement;
import static com.github.moaxcp.verybinary.math.Constant.constant;
import static org.assertj.core.api.Assertions.assertThat;

public class ByteLengthOfBasicElementTest {
  @Test
  void boolConstantTest() {
    var struct = struct()
        .boolList(constant(10))
        .build();

    var expression = lengthOfBasicElement(0);

    assertThat(expression.isConstant(struct.getType())).isTrue();
    assertThat(expression.constantValue(struct.getType())).isEqualTo(1);
    assertThat(expression.defaultValue(struct.getType())).isEqualTo(1);
    assertThat(expression.evaluate(struct)).isEqualTo(1);
  }

  @Test
  void boolTest() {
    var struct = struct()
        .int8()
        .boolList(0)
        .build();
    struct.setInt8(0, 25);

    var expression = lengthOfBasicElement(1);

    assertThat(expression.isConstant(struct.getType())).isTrue();
    assertThat(expression.constantValue(struct.getType())).isEqualTo(1);
    assertThat(expression.defaultValue(struct.getType())).isEqualTo(1);
    assertThat(expression.evaluate(struct)).isEqualTo(1);
  }

  @Test
  void int8ConstantTest() {
    var struct = struct()
        .int8List(constant(10))
        .build();

    var expression = lengthOfBasicElement(0);

    assertThat(expression.isConstant(struct.getType())).isTrue();
    assertThat(expression.constantValue(struct.getType())).isEqualTo(1);
    assertThat(expression.defaultValue(struct.getType())).isEqualTo(1);
    assertThat(expression.evaluate(struct)).isEqualTo(1);
  }

  @Test
  void int8Test() {
    var struct = struct()
        .int8()
        .int8List(0)
        .build();
    struct.setInt8(0, 25);

    var expression = lengthOfBasicElement(1);

    assertThat(expression.isConstant(struct.getType())).isTrue();
    assertThat(expression.constantValue(struct.getType())).isEqualTo(1);
    assertThat(expression.defaultValue(struct.getType())).isEqualTo(1);
    assertThat(expression.evaluate(struct)).isEqualTo(1);
  }

  @Test
  void int16ConstantTest() {
    var struct = struct()
        .int16List(constant(10))
        .build();

    var expression = lengthOfBasicElement(0);

    assertThat(expression.isConstant(struct.getType())).isTrue();
    assertThat(expression.constantValue(struct.getType())).isEqualTo(2);
    assertThat(expression.defaultValue(struct.getType())).isEqualTo(2);
    assertThat(expression.evaluate(struct)).isEqualTo(2);
  }

  @Test
  void int16Test() {
    var struct = struct()
        .int8()
        .int16List(0)
        .build();
    struct.setInt8(0, 25);

    var expression = lengthOfBasicElement(1);

    assertThat(expression.isConstant(struct.getType())).isTrue();
    assertThat(expression.constantValue(struct.getType())).isEqualTo(2);
    assertThat(expression.defaultValue(struct.getType())).isEqualTo(2);
    assertThat(expression.evaluate(struct)).isEqualTo(2);
  }

  @Test
  void int32ConstantTest() {
    var struct = struct()
        .int32List(constant(10))
        .build();

    var expression = lengthOfBasicElement(0);

    assertThat(expression.isConstant(struct.getType())).isTrue();
    assertThat(expression.constantValue(struct.getType())).isEqualTo(4);
    assertThat(expression.defaultValue(struct.getType())).isEqualTo(4);
    assertThat(expression.evaluate(struct)).isEqualTo(4);
  }

  @Test
  void int32Test() {
    var struct = struct()
        .int8()
        .int32List(0)
        .build();
    struct.setInt8(0, 25);

    var expression = lengthOfBasicElement(1);

    assertThat(expression.isConstant(struct.getType())).isTrue();
    assertThat(expression.constantValue(struct.getType())).isEqualTo(4);
    assertThat(expression.defaultValue(struct.getType())).isEqualTo(4);
    assertThat(expression.evaluate(struct)).isEqualTo(4);
  }

  @Test
  void int64ConstantTest() {
    var struct = struct()
        .int64List(constant(10))
        .build();

    var expression = lengthOfBasicElement(0);

    assertThat(expression.isConstant(struct.getType())).isTrue();
    assertThat(expression.constantValue(struct.getType())).isEqualTo(8);
    assertThat(expression.defaultValue(struct.getType())).isEqualTo(8);
    assertThat(expression.evaluate(struct)).isEqualTo(8);
  }

  @Test
  void int64Test() {
    var struct = struct()
        .int8()
        .int64List(0)
        .build();
    struct.setInt8(0, 25);

    var expression = lengthOfBasicElement(1);

    assertThat(expression.isConstant(struct.getType())).isTrue();
    assertThat(expression.constantValue(struct.getType())).isEqualTo(8);
    assertThat(expression.defaultValue(struct.getType())).isEqualTo(8);
    assertThat(expression.evaluate(struct)).isEqualTo(8);
  }

  @Test
  void uint8ConstantTest() {
    var struct = struct()
        .uint8List(constant(10))
        .build();

    var expression = lengthOfBasicElement(0);

    assertThat(expression.isConstant(struct.getType())).isTrue();
    assertThat(expression.constantValue(struct.getType())).isEqualTo(1);
    assertThat(expression.defaultValue(struct.getType())).isEqualTo(1);
    assertThat(expression.evaluate(struct)).isEqualTo(1);
  }

  @Test
  void uint8Test() {
    var struct = struct()
        .int8()
        .uint8List(0)
        .build();
    struct.setInt8(0, 25);

    var expression = lengthOfBasicElement(1);

    assertThat(expression.isConstant(struct.getType())).isTrue();
    assertThat(expression.constantValue(struct.getType())).isEqualTo(1);
    assertThat(expression.defaultValue(struct.getType())).isEqualTo(1);
    assertThat(expression.evaluate(struct)).isEqualTo(1);
  }

  @Test
  void uint16ConstantTest() {
    var struct = struct()
        .uint16List(constant(10))
        .build();

    var expression = lengthOfBasicElement(0);

    assertThat(expression.isConstant(struct.getType())).isTrue();
    assertThat(expression.constantValue(struct.getType())).isEqualTo(2);
    assertThat(expression.defaultValue(struct.getType())).isEqualTo(2);
    assertThat(expression.evaluate(struct)).isEqualTo(2);
  }

  @Test
  void uint16Test() {
    var struct = struct()
        .int8()
        .uint16List(0)
        .build();
    struct.setInt8(0, 25);

    var expression = lengthOfBasicElement(1);

    assertThat(expression.isConstant(struct.getType())).isTrue();
    assertThat(expression.constantValue(struct.getType())).isEqualTo(2);
    assertThat(expression.defaultValue(struct.getType())).isEqualTo(2);
    assertThat(expression.evaluate(struct)).isEqualTo(2);
  }

  @Test
  void uint32ConstantTest() {
    var struct = struct()
        .uint32List(constant(10))
        .build();

    var expression = lengthOfBasicElement(0);

    assertThat(expression.isConstant(struct.getType())).isTrue();
    assertThat(expression.constantValue(struct.getType())).isEqualTo(4);
    assertThat(expression.defaultValue(struct.getType())).isEqualTo(4);
    assertThat(expression.evaluate(struct)).isEqualTo(4);
  }

  @Test
  void uint32Test() {
    var struct = struct()
        .int8()
        .uint32List(0)
        .build();
    struct.setInt8(0, 25);

    var expression = lengthOfBasicElement(1);

    assertThat(expression.isConstant(struct.getType())).isTrue();
    assertThat(expression.constantValue(struct.getType())).isEqualTo(4);
    assertThat(expression.defaultValue(struct.getType())).isEqualTo(4);
    assertThat(expression.evaluate(struct)).isEqualTo(4);
  }

  @Test
  void uint64ConstantTest() {
    var struct = struct()
        .uint64List(constant(10))
        .build();

    var expression = lengthOfBasicElement(0);

    assertThat(expression.isConstant(struct.getType())).isTrue();
    assertThat(expression.constantValue(struct.getType())).isEqualTo(8);
    assertThat(expression.defaultValue(struct.getType())).isEqualTo(8);
    assertThat(expression.evaluate(struct)).isEqualTo(8);
  }

  @Test
  void uint64Test() {
    var struct = struct()
        .int8()
        .uint64List(0)
        .build();
    struct.setInt8(0, 25);

    var expression = lengthOfBasicElement(1);

    assertThat(expression.isConstant(struct.getType())).isTrue();
    assertThat(expression.constantValue(struct.getType())).isEqualTo(8);
    assertThat(expression.defaultValue(struct.getType())).isEqualTo(8);
    assertThat(expression.evaluate(struct)).isEqualTo(8);
  }

  @Test
  void float32ConstantTest() {
    var struct = struct()
        .float32List(constant(10))
        .build();

    var expression = lengthOfBasicElement(0);

    assertThat(expression.isConstant(struct.getType())).isTrue();
    assertThat(expression.constantValue(struct.getType())).isEqualTo(4);
    assertThat(expression.defaultValue(struct.getType())).isEqualTo(4);
    assertThat(expression.evaluate(struct)).isEqualTo(4);
  }

  @Test
  void float32Test() {
    var struct = struct()
        .int8()
        .float32List(0)
        .build();
    struct.setInt8(0, 25);

    var expression = lengthOfBasicElement(1);

    assertThat(expression.isConstant(struct.getType())).isTrue();
    assertThat(expression.constantValue(struct.getType())).isEqualTo(4);
    assertThat(expression.defaultValue(struct.getType())).isEqualTo(4);
    assertThat(expression.evaluate(struct)).isEqualTo(4);
  }

  @Test
  void float64ConstantTest() {
    var struct = struct()
        .float64List(constant(10))
        .build();

    var expression = lengthOfBasicElement(0);

    assertThat(expression.isConstant(struct.getType())).isTrue();
    assertThat(expression.constantValue(struct.getType())).isEqualTo(8);
    assertThat(expression.defaultValue(struct.getType())).isEqualTo(8);
    assertThat(expression.evaluate(struct)).isEqualTo(8);
  }

  @Test
  void float64Test() {
    var struct = struct()
        .int8()
        .float64List(0)
        .build();
    struct.setInt8(0, 25);

    var expression = lengthOfBasicElement(1);

    assertThat(expression.isConstant(struct.getType())).isTrue();
    assertThat(expression.constantValue(struct.getType())).isEqualTo(8);
    assertThat(expression.defaultValue(struct.getType())).isEqualTo(8);
    assertThat(expression.evaluate(struct)).isEqualTo(8);
  }
}
