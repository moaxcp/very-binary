package com.github.moaxcp.verybinary.math;

import com.github.moaxcp.verybinary.Builders;
import org.junit.jupiter.api.Test;

import static com.github.moaxcp.verybinary.math.Constant.constant;
import static com.github.moaxcp.verybinary.math.Multiply.distribute;
import static com.github.moaxcp.verybinary.math.Multiply.multiply;
import static com.github.moaxcp.verybinary.math.Subtract.subtract;
import static com.github.moaxcp.verybinary.math.Sum.sum;
import static org.assertj.core.api.Assertions.assertThat;

public class MultiplyDistributeTest {

  @Test
  void distribute_sum_into_multiply() {
    // 2 * (3 + 4) -> (2 * 3) + (2 * 4)
    var multiply = multiply(constant(2), sum(constant(3), constant(4)));
    Expression result = distribute(multiply);
    assertThat(result).isEqualTo(sum(multiply(constant(2), constant(3)), multiply(constant(2), constant(4))));
  }

  @Test
  void distribute_subtract_into_multiply() {
    // 2 * (5 - 3) -> (2 * 5) - (2 * 3)
    var multiply = multiply(constant(2), subtract(constant(5), constant(3)));
    Expression result = distribute(multiply);
    assertThat(result).isEqualTo(subtract(multiply(constant(2), constant(5)), multiply(constant(2), constant(3))));
  }

  @Test
  void distribute_nested_multiply_flattens() {
    // 2 * (3 * 4) -> 2 * 3 * 4
    var multiply = multiply(constant(2), multiply(constant(3), constant(4)));
    Expression result = distribute(multiply);
    assertThat(result).isEqualTo(multiply(constant(2), constant(3), constant(4)));
  }

  @Test
  void distribute_sum_evaluates_correctly() {
    // 2 * (3 + 4) distributed -> (2*3) + (2*4) = 6 + 8 = 14
    var distributed = distribute(multiply(constant(2), sum(constant(3), constant(4))));
    var struct = Builders.struct().build();
    assertThat(distributed.evaluate(struct)).isEqualTo(14);
  }

  @Test
  void distribute_subtract_evaluates_correctly() {
    // 2 * (5 - 3) distributed -> (2*5) - (2*3) = 10 - 6 = 4
    var distributed = distribute(multiply(constant(2), subtract(constant(5), constant(3))));
    var struct = Builders.struct().build();
    assertThat(distributed.evaluate(struct)).isEqualTo(4);
  }

  @Test
  void distribute_nested_multiply_evaluates_correctly() {
    // 2 * (3 * 4) distributed -> 2 * 3 * 4 = 24
    var distributed = distribute(multiply(constant(2), multiply(constant(3), constant(4))));
    var struct = Builders.struct().build();
    assertThat(distributed.evaluate(struct)).isEqualTo(24);
  }
}
