package com.github.moaxcp.verybinary.jdk;

import java.util.Objects;
import java.util.function.IntConsumer;

public interface Int32Consumer extends IntConsumer {
  void accept(int value);

  default Int32Consumer andThen(Int32Consumer after) {
    Objects.requireNonNull(after);
    return (int t) -> { accept(t); after.accept(t); };
  }
}
