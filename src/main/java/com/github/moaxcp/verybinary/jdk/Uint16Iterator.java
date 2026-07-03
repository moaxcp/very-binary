package com.github.moaxcp.verybinary.jdk;

import java.util.Objects;
import java.util.PrimitiveIterator;
import java.util.function.Consumer;

public interface Uint16Iterator extends PrimitiveIterator<Integer, Uint16Consumer> {
  int nextUint16();

  default void forEachRemaining(Uint16Consumer action) {
    Objects.requireNonNull(action);
    while (hasNext())
      action.accept(nextUint16());
  }

  @Override
  default Integer next() {
    return nextUint16();
  }

  @Override
  default void forEachRemaining(Consumer<? super Integer> action) {
    if (action instanceof Uint16Consumer) {
      forEachRemaining((Uint16Consumer) action);
    }
    else {
      Objects.requireNonNull(action);
      forEachRemaining((Uint16Consumer) action::accept);
    }
  }
}
