package com.github.moaxcp.verybinary.math;

import com.github.moaxcp.verybinary.Builders;
import org.junit.jupiter.api.Test;

import static com.github.moaxcp.verybinary.math.Constant.constant;
import static com.github.moaxcp.verybinary.math.Divide.distribute;
import static com.github.moaxcp.verybinary.math.Divide.divide;
import static com.github.moaxcp.verybinary.math.Subtract.subtract;
import static com.github.moaxcp.verybinary.math.Sum.sum;
import static org.assertj.core.api.Assertions.assertThat;

public class DivideDistributeTest {

  @Test
  void distribute_sum_numerator() {
    // (3 + 4) / 2 -> (3/2) + (4/2)
    var divide = divide(sum(constant(3), constant(4)), constant(2));
    Expression result = distribute(divide);
    assertThat(result).isEqualTo(sum(divide(constant(3), constant(2)), divide(constant(4), constant(2))));
  }

  @Test
  void distribute_subtract_numerator() {
    // (6 - 4) / 2 -> (6/2) - (4/2)
    var divide = divide(subtract(constant(6), constant(4)), constant(2));
    Expression result = distribute(divide);
    assertThat(result).isEqualTo(subtract(divide(constant(6), constant(2)), divide(constant(4), constant(2))));
  }

  @Test
  void distribute_nested_divide_numerator_flattens() {
    // (12/3) / 2 -> 12/3/2
    var divide = divide(divide(constant(12), constant(3)), constant(2));
    Expression result = distribute(divide);
    assertThat(result).isEqualTo(divide(constant(12), constant(3), constant(2)));
  }

  @Test
  void distribute_sum_numerator_evaluates_correctly() {
    // (3 + 4) / 2 distributed -> (3/2) + (4/2) = 1 + 2 = 3 (integer division)
    var distributed = distribute(divide(sum(constant(3), constant(4)), constant(2)));
    var struct = Builders.struct().build();
    assertThat(distributed.evaluate(struct)).isEqualTo(3);
  }

  @Test
  void distribute_subtract_numerator_evaluates_correctly() {
    // (6 - 4) / 2 distributed -> (6/2) - (4/2) = 3 - 2 = 1
    var distributed = distribute(divide(subtract(constant(6), constant(4)), constant(2)));
    var struct = Builders.struct().build();
    assertThat(distributed.evaluate(struct)).isEqualTo(1);
  }

  @Test
  void distribute_nested_divide_numerator_evaluates_correctly() {
    // (12/3) / 2 distributed -> 12/3/2 = 4/2 = 2
    var distributed = distribute(divide(divide(constant(12), constant(3)), constant(2)));
    var struct = Builders.struct().build();
    assertThat(distributed.evaluate(struct)).isEqualTo(2);
  }
}
