package com.github.moaxcp.verybinary.math;

import org.junit.jupiter.api.Test;

import static com.github.moaxcp.verybinary.math.Int8Value.int8Value;
import static org.assertj.core.api.Assertions.assertThat;

public class ConstantTest {

  @Test
  public void testConstant() {
    Int8Value constant = int8Value(1);
    assertThat(constant.isConstant(null)).isTrue();
    assertThat(constant.constantValue(null).toInt()).isEqualTo(1);
    assertThat(constant.defaultValue(null).toInt()).isEqualTo(1);
    assertThat(constant.toByte()).isEqualTo((byte) 1);
    assertThat(constant.evaluate(null).toInt()).isEqualTo(1);
  }
}
