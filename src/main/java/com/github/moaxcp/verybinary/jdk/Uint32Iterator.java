package com.github.moaxcp.verybinary.jdk;

import java.util.Objects;
import java.util.PrimitiveIterator;
import java.util.function.Consumer;

public interface Uint32Iterator extends PrimitiveIterator<Long, Uint32Consumer> {
  long nextUint32();

  default void forEachRemaining(Uint32Consumer action) {
    Objects.requireNonNull(action);
    while (hasNext())
      action.accept(nextUint32());
  }

  @Override
  default Long next() {
    return nextUint32();
  }

  @Override
  default void forEachRemaining(Consumer<? super Long> action) {
    if (action instanceof Uint32Consumer) {
      forEachRemaining((Uint32Consumer) action);
    }
    else {
      Objects.requireNonNull(action);
      forEachRemaining((Uint32Consumer) action::accept);
    }
  }
}
