package com.github.moaxcp.verybinary.math;

import org.junit.jupiter.api.Test;

import static com.github.moaxcp.verybinary.Builders.struct;
import static com.github.moaxcp.verybinary.math.ValueOf.variable;
import static org.assertj.core.api.Assertions.assertThat;

public class ValueOfTest {
  @Test
  public void testVariableInt8() {
    var struct = struct()
        .int8()
        .build();
    struct.setInt8(0, 10);

    var expression = variable(0);

    assertThat(expression.isConstant(struct.getType())).isFalse();
    assertThat(expression.constantValue(struct.getType()).toInt()).isEqualTo(0);
    assertThat(expression.defaultValue(struct.getType()).toInt()).isEqualTo(0);
    assertThat(expression.evaluate(struct).toInt()).isEqualTo(10);
  }

  @Test
  public void testVariableInt8_constant() {
    var struct = struct()
        .basic().constant(25).int8()
        .build();

    var expression = variable(0);

    assertThat(expression.isConstant(struct.getType())).isTrue();
    assertThat(expression.constantValue(struct.getType()).toInt()).isEqualTo(25);
    assertThat(expression.defaultValue(struct.getType()).toInt()).isEqualTo(25);
    assertThat(expression.evaluate(struct).toInt()).isEqualTo(25);
  }
}
