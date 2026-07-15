package com.github.moaxcp.verybinary;

import org.junit.jupiter.api.Test;

import static com.github.moaxcp.verybinary.math.Expression.*;
import static com.github.moaxcp.verybinary.Builders.struct;
import static org.assertj.core.api.Assertions.assertThat;

public class ExpressionTest {
  @Test
  void constant_expression() {
    var expression = constant(5);
    var struct = struct()
        .build();
    assertThat(expression.evaluate(struct)).isEqualTo(5);
  }

  @Test
  void valueOf_expression() {
    var expression = variable(0);
    var struct = struct()
        .int8()
        .build();

    struct.setInt8(0, (byte) 2);

    assertThat(expression.evaluate(struct)).isEqualTo(2);
  }

  @Test
  void sum_expression() {
    var expression = sum(variable(0), variable(1), constant(1));
    var struct = struct()
        .int8()
        .int8()
        .build();

    struct.setInt8(0, (byte) 2);
    struct.setInt8(1, (byte) 3);

    assertThat(expression.evaluate(struct)).isEqualTo(6);
  }

  @Test
  void subtract_expression() {
    var expression = subtract(variable(0), variable(1), constant(1));
    var struct = struct()
        .int8()
        .int8()
        .build();

    struct.setInt8(0, (byte) 2);
    struct.setInt8(1, (byte) 3);

    assertThat(expression.evaluate(struct)).isEqualTo(-2);
  }

  @Test
  void multiply_expression() {
    var expression = multiply(variable(0), variable(1), constant(2));
    var struct = struct()
        .int8()
        .int8()
        .build();

    struct.setInt8(0, (byte) 8);
    struct.setInt8(1, (byte) 3);

    assertThat(expression.evaluate(struct)).isEqualTo(48);
  }

  @Test
  void divide_expression() {
    var expression = divide(variable(0), variable(1), constant(2));
    var struct = struct()
        .int8()
        .int8()
        .build();

    struct.setInt8(0, (byte) 18);
    struct.setInt8(1, (byte) 3);

    assertThat(expression.evaluate(struct)).isEqualTo(3);
  }

  @Test
  void combined_expression() {
    var expression = divide(multiply(sum(variable(0), variable(1), subtract(variable(0), variable(1))), variable(0), variable(1), constant(2)), constant(4));
    var struct = struct()
        .int8()
        .int8()
        .build();

    struct.setInt8(0, (byte) 8);
    struct.setInt8(1, (byte) 3);

    assertThat(expression.evaluate(struct)).isEqualTo(192);
  }
}
