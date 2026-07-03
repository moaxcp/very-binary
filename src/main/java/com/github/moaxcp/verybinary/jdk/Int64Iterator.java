package com.github.moaxcp.verybinary.jdk;

import java.util.Objects;
import java.util.PrimitiveIterator;
import java.util.function.Consumer;

public interface Int64Iterator extends PrimitiveIterator<Long, Int64Consumer> {
  long nextInt64();

  default void forEachRemaining(Int64Consumer action) {
    Objects.requireNonNull(action);
    while (hasNext())
      action.accept(nextInt64());
  }

  @Override
  default Long next() {
    return nextInt64();
  }

  @Override
  default void forEachRemaining(Consumer<? super Long> action) {
    if (action instanceof Int64Consumer) {
      forEachRemaining((Int64Consumer) action);
    }
    else {
      Objects.requireNonNull(action);
      forEachRemaining((Int64Consumer) action::accept);
    }
  }
}
