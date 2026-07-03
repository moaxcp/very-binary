package com.github.moaxcp.verybinary.jdk;

import java.util.Objects;
import java.util.PrimitiveIterator;
import java.util.function.Consumer;

public interface Int32Iterator extends PrimitiveIterator<Integer, Int32Consumer> {
  int nextInt32();

  default void forEachRemaining(Int32Consumer action) {
    Objects.requireNonNull(action);
    while (hasNext())
      action.accept(nextInt32());
  }

  @Override
  default Integer next() {
    return nextInt32();
  }

  @Override
  default void forEachRemaining(Consumer<? super Integer> action) {
    if (action instanceof Int32Consumer) {
      forEachRemaining((Int32Consumer) action);
    }
    else {
      Objects.requireNonNull(action);
      forEachRemaining((Int32Consumer) action::accept);
    }
  }
}
