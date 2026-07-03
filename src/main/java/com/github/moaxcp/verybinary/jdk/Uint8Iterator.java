package com.github.moaxcp.verybinary.jdk;

import java.util.Objects;
import java.util.PrimitiveIterator;
import java.util.function.Consumer;

public interface Uint8Iterator extends PrimitiveIterator<Short, Uint8Consumer> {
  short nextUint8();

  default void forEachRemaining(Uint8Consumer action) {
    Objects.requireNonNull(action);
    while (hasNext())
      action.accept(nextUint8());
  }

  @Override
  default Short next() {
    return nextUint8();
  }

  @Override
  default void forEachRemaining(Consumer<? super Short> action) {
    if (action instanceof Uint8Consumer) {
      forEachRemaining((Uint8Consumer) action);
    }
    else {
      Objects.requireNonNull(action);
      forEachRemaining((Uint8Consumer) action::accept);
    }
  }
}
