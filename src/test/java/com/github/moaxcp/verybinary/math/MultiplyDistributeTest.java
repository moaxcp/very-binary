package com.github.moaxcp.verybinary.math;

import com.github.moaxcp.verybinary.Builders;
import org.junit.jupiter.api.Test;

import static com.github.moaxcp.verybinary.math.Int8Value.int8Value;
import static com.github.moaxcp.verybinary.math.Multiply.distribute;
import static com.github.moaxcp.verybinary.math.Multiply.multiply;
import static com.github.moaxcp.verybinary.math.Subtract.subtract;
import static com.github.moaxcp.verybinary.math.Sum.sum;
import static org.assertj.core.api.Assertions.assertThat;

public class MultiplyDistributeTest {

  @Test
  void distribute_sum_into_multiply() {
    // 2 * (3 + 4) -> (2 * 3) + (2 * 4)
    var multiply = multiply(int8Value(2), sum(int8Value(3), int8Value(4)));
    var result = distribute(multiply);
    assertThat(result).isEqualTo(sum(multiply(int8Value(2), int8Value(3)), multiply(int8Value(2), int8Value(4))));
  }

  @Test
  void distribute_subtract_into_multiply() {
    // 2 * (5 - 3) -> (2 * 5) - (2 * 3)
    var multiply = multiply(int8Value(2), subtract(int8Value(5), int8Value(3)));
    var result = distribute(multiply);
    assertThat(result).isEqualTo(subtract(multiply(int8Value(2), int8Value(5)), multiply(int8Value(2), int8Value(3))));
  }

  @Test
  void distribute_nested_multiply_flattens() {
    // 2 * (3 * 4) -> 2 * 3 * 4
    var multiply = multiply(int8Value(2), multiply(int8Value(3), int8Value(4)));
    var result = distribute(multiply);
    assertThat(result).isEqualTo(multiply(int8Value(2), int8Value(3), int8Value(4)));
  }

  @Test
  void distribute_sum_evaluates_correctly() {
    // 2 * (3 + 4) distributed -> (2*3) + (2*4) = 6 + 8 = 14
    var distributed = distribute(multiply(int8Value(2), sum(int8Value(3), int8Value(4))));
    var struct = Builders.struct().build();
    assertThat(distributed.evaluate(struct).toInt()).isEqualTo(14);
  }

  @Test
  void distribute_subtract_evaluates_correctly() {
    // 2 * (5 - 3) distributed -> (2*5) - (2*3) = 10 - 6 = 4
    var distributed = distribute(multiply(int8Value(2), subtract(int8Value(5), int8Value(3))));
    var struct = Builders.struct().build();
    assertThat(distributed.evaluate(struct).toInt()).isEqualTo(4);
  }

  @Test
  void distribute_nested_multiply_evaluates_correctly() {
    // 2 * (3 * 4) distributed -> 2 * 3 * 4 = 24
    var distributed = distribute(multiply(int8Value(2), multiply(int8Value(3), int8Value(4))));
    var struct = Builders.struct().build();
    assertThat(distributed.evaluate(struct).toInt()).isEqualTo(24);
  }
}
