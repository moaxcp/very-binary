package com.github.moaxcp.verybinary.math;

import org.junit.jupiter.api.Test;

import static com.github.moaxcp.verybinary.Builders.struct;
import static com.github.moaxcp.verybinary.math.Divide.divide;
import static com.github.moaxcp.verybinary.math.Int8Value.int8Value;
import static com.github.moaxcp.verybinary.math.Multiply.multiply;
import static com.github.moaxcp.verybinary.math.Subtract.subtract;
import static com.github.moaxcp.verybinary.math.Sum.sum;
import static com.github.moaxcp.verybinary.math.Variable.variable;
import static org.assertj.core.api.Assertions.assertThat;

public class ExpressionTest {
  @Test
  void constant_expression() {
    var expression = int8Value(5);
    var struct = struct()
        .build();
    assertThat(expression.evaluate(struct).toInt()).isEqualTo(5);
  }

  @Test
  void valueOf_expression() {
    var expression = variable(0);
    var struct = struct()
        .int8()
        .build();

    struct.setInt8(0, (byte) 2);

    assertThat(expression.evaluate(struct).toInt()).isEqualTo(2);
  }

  @Test
  void sum_expression() {
    var expression = sum(variable(0), variable(1), int8Value(1));
    var struct = struct()
        .int8()
        .int8()
        .build();

    struct.setInt8(0, (byte) 2);
    struct.setInt8(1, (byte) 3);

    assertThat(expression.evaluate(struct).toInt()).isEqualTo(6);
  }

  @Test
  void subtract_expression() {
    var expression = subtract(variable(0), variable(1), int8Value(1));
    var struct = struct()
        .int8()
        .int8()
        .build();

    struct.setInt8(0, (byte) 2);
    struct.setInt8(1, (byte) 3);

    assertThat(expression.evaluate(struct).toInt()).isEqualTo(-2);
  }

  @Test
  void multiply_expression() {
    var expression = multiply(variable(0), variable(1), int8Value(2));
    var struct = struct()
        .int8()
        .int8()
        .build();

    struct.setInt8(0, (byte) 8);
    struct.setInt8(1, (byte) 3);

    assertThat(expression.evaluate(struct).toInt()).isEqualTo(48);
  }

  @Test
  void divide_expression() {
    var expression = divide(variable(0), variable(1), int8Value(2));
    var struct = struct()
        .int8()
        .int8()
        .build();

    struct.setInt8(0, (byte) 18);
    struct.setInt8(1, (byte) 3);

    assertThat(expression.evaluate(struct).toInt()).isEqualTo(3);
  }

  @Test
  void combined_expression() {
    var expression = divide(multiply(sum(variable(0), variable(1), subtract(variable(0), variable(1))), variable(0), variable(1), int8Value(2)), int8Value(4));
    var struct = struct()
        .int8()
        .int8()
        .build();

    struct.setInt8(0, (byte) 8);
    struct.setInt8(1, (byte) 3);

    assertThat(expression.evaluate(struct).toInt()).isEqualTo(192);
  }
}
