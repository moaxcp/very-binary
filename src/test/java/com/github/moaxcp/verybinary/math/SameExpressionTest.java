package com.github.moaxcp.verybinary.math;

import org.junit.jupiter.api.Test;

import java.util.List;

import static com.github.moaxcp.verybinary.Builders.struct;
import static com.github.moaxcp.verybinary.math.Int8Value.int8Value;
import static org.assertj.core.api.Assertions.assertThat;

public class SameExpressionTest {
  @Test
  void testSingleExpression() {
    var expression = new SameExpression(List.of(int8Value(5)));
    var struct = struct().build();
    assertThat(expression.evaluate(struct)).isEqualTo(BoolValue.TRUE);
  }

  @Test
  void testTwoExpression() {
    var expression = new SameExpression(List.of(int8Value(5), int8Value(5)));
    var struct = struct().build();
    assertThat(expression.evaluate(struct)).isEqualTo(BoolValue.TRUE);
  }

  @Test
  void testThreeExpression() {
    var expression = new SameExpression(List.of(int8Value(5), int8Value(5), int8Value(5)));
    var struct = struct().build();
    assertThat(expression.evaluate(struct)).isEqualTo(BoolValue.TRUE);
  }

  @Test
  void testTwoExpressionNotEqual() {
    var expression = new SameExpression(List.of(int8Value(6), int8Value(5)));
    var struct = struct().build();
    assertThat(expression.evaluate(struct)).isEqualTo(BoolValue.FALSE);
  }

  @Test
  void testThreeExpressionNotEqual() {
    var expression = new SameExpression(List.of(int8Value(5), int8Value(5), int8Value(6)));
    var struct = struct().build();
    assertThat(expression.evaluate(struct)).isEqualTo(BoolValue.FALSE);
  }
}
