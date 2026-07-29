package com.github.moaxcp.verybinary.math;

import org.junit.jupiter.api.Test;

import static com.github.moaxcp.verybinary.math.Constant.constant;
import static org.assertj.core.api.Assertions.assertThat;

public class ConstantTest {

  @Test
  public void testConstant() {
    Constant constant = constant(1);
    assertThat(constant.isConstant(null)).isTrue();
    assertThat(constant.constantValue(null)).isEqualTo(1);
    assertThat(constant.defaultValue(null)).isEqualTo(1);
    assertThat(constant.value()).isEqualTo(1);
    assertThat(constant.evaluate(null)).isEqualTo(1);
  }
}
