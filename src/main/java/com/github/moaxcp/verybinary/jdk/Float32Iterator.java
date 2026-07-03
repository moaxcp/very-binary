package com.github.moaxcp.verybinary.jdk;

import java.util.Objects;
import java.util.PrimitiveIterator;
import java.util.function.Consumer;

public interface Float32Iterator extends PrimitiveIterator<Float, Float32Consumer> {
  float nextFloat();

  default void forEachRemaining(Float32Consumer action) {
    Objects.requireNonNull(action);
    while (hasNext())
      action.accept(nextFloat());
  }

  @Override
  default Float next() {
    return nextFloat();
  }

  @Override
  default void forEachRemaining(Consumer<? super Float> action) {
    if (action instanceof Float32Consumer) {
      forEachRemaining((Float32Consumer) action);
    }
    else {
      // The method reference action::accept is never null
      Objects.requireNonNull(action);
      forEachRemaining((Float32Consumer) action::accept);
    }
  }
}
