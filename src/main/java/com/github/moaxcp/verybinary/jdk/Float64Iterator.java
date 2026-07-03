package com.github.moaxcp.verybinary.jdk;

import java.util.Objects;
import java.util.PrimitiveIterator;
import java.util.function.DoubleConsumer;

public interface Float64Iterator extends PrimitiveIterator<Double, DoubleConsumer> {
  double nextFloat64();

  default void forEachRemaining(DoubleConsumer action) {
    Objects.requireNonNull(action);
    while (hasNext())
      action.accept(nextFloat64());
  }

  @Override
  default Double next() {
    return nextFloat64();
  }
}
