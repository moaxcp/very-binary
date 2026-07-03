package com.github.moaxcp.verybinary.jdk;

import java.util.Objects;
import java.util.PrimitiveIterator;
import java.util.function.Consumer;

public interface Int8Iterator extends PrimitiveIterator<Byte, Int8Consumer> {
  byte nextInt8();

  default void forEachRemaining(Int8Consumer action) {
    Objects.requireNonNull(action);
    while (hasNext())
      action.accept(nextInt8());
  }

  @Override
  default Byte next() {
    return nextInt8();
  }

  @Override
  default void forEachRemaining(Consumer<? super Byte> action) {
    if (action instanceof Int8Consumer) {
      forEachRemaining((Int8Consumer) action);
    }
    else {
      Objects.requireNonNull(action);
      forEachRemaining((Int8Consumer) action::accept);
    }
  }
}
