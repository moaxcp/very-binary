package com.github.moaxcp.verybinary.jdk;

import java.util.Objects;
import java.util.PrimitiveIterator;
import java.util.function.Consumer;

public interface Int16Iterator extends PrimitiveIterator<Short, Int16Consumer> {
  short nextInt16();

  default void forEachRemaining(Int16Consumer action) {
    Objects.requireNonNull(action);
    while (hasNext())
      action.accept(nextInt16());
  }

  @Override
  default Short next() {
    return nextInt16();
  }

  @Override
  default void forEachRemaining(Consumer<? super Short> action) {
    if (action instanceof Int16Consumer) {
      forEachRemaining((Int16Consumer) action);
    }
    else {
      Objects.requireNonNull(action);
      forEachRemaining((Int16Consumer) action::accept);
    }
  }
}
