package com.github.moaxcp.verybinary.jdk;

import java.util.Objects;

public interface Int16Consumer {
  void accept(short value);

  default Int16Consumer andThen(Int16Consumer after) {
    Objects.requireNonNull(after);
    return (short t) -> { accept(t); after.accept(t); };
  }
}
