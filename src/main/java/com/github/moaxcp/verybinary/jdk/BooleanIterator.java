package com.github.moaxcp.verybinary.jdk;

import java.util.Objects;
import java.util.PrimitiveIterator;
import java.util.function.Consumer;

public interface BooleanIterator extends PrimitiveIterator<Boolean, BooleanConsumer> {
  boolean nextBoolean();

  default void forEachRemaining(BooleanConsumer action) {
    Objects.requireNonNull(action);
    while (hasNext())
      action.accept(nextBoolean());
  }

  @Override
  default Boolean next() {
    return nextBoolean();
  }

  @Override
  default void forEachRemaining(Consumer<? super Boolean> action) {
    if (action instanceof BooleanConsumer) {
      forEachRemaining((BooleanConsumer) action);
    }
    else {
      // The method reference action::accept is never null
      Objects.requireNonNull(action);
      forEachRemaining((BooleanConsumer) action::accept);
    }
  }
}
