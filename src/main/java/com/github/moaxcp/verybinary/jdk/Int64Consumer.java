package com.github.moaxcp.verybinary.jdk;

import java.util.Objects;
import java.util.function.LongConsumer;

public interface Int64Consumer extends LongConsumer {
  void accept(long value);

  default Int64Consumer andThen(Int64Consumer after) {
    Objects.requireNonNull(after);
    return (long t) -> { accept(t); after.accept(t); };
  }
}
