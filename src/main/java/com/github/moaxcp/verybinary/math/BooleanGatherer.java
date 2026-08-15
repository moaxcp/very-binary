package com.github.moaxcp.verybinary.math;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Supplier;
import java.util.stream.Gatherer;
import java.util.stream.Gatherer.Downstream;
import java.util.stream.Gatherer.Integrator;

public class BooleanGatherer {

  private static class State {
    private final BiFunction<ArithmeticValue, ArithmeticValue, BoolValue> function;
    ArithmeticValue previous;
    BoolValue result = null;

    State(BiFunction<ArithmeticValue, ArithmeticValue, BoolValue> function) {
      this.function = function;
    }

    boolean accumulate(ArithmeticValue value) {
      if (previous == null) {
        previous = value;
        result = BoolValue.TRUE;
      } else {
        result = function.apply(previous, value);
      }
      return result.toBool();
    }

    BoolValue getResult() {
      return result;
    }
  }

  public static Gatherer<ArithmeticValue, ?, BoolValue> booleanGatherer(BiFunction<ArithmeticValue, ArithmeticValue, BoolValue> function) {
    Supplier<State> initializer = () -> new State(function);
    Integrator<State, ArithmeticValue, BoolValue> integrator = (state, element, downstream) -> state.accumulate(element);
    BiConsumer<State, Downstream<? super BoolValue>> finisher = (state, downstream) -> downstream.push(state.getResult());
    return Gatherer.ofSequential(initializer, integrator, finisher);
  }

  public static Gatherer<ArithmeticValue, ?, BoolValue> equality() {
    return booleanGatherer(ArithmeticValue::same);
  }

  public static Gatherer<ArithmeticValue, ?, BoolValue> notSame() {
    return booleanGatherer(ArithmeticValue::notSame);
  }

  public static Gatherer<ArithmeticValue, ?, BoolValue> greaterThan() {
    return booleanGatherer(ArithmeticValue::greaterThan);
  }

  public static Gatherer<ArithmeticValue, ?, BoolValue> greaterThanOrEqual() {
    return booleanGatherer(ArithmeticValue::greaterThanOrEqual);
  }

  public static Gatherer<ArithmeticValue, ?, BoolValue> lessThan() {
    return booleanGatherer(ArithmeticValue::lessThan);
  }

  public static Gatherer<ArithmeticValue, ?, BoolValue> lessThanOrEqual() {
    return booleanGatherer(ArithmeticValue::lessThanOrEqual);
  }
}
